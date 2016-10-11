package serverapplication;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by o_0 on 2016-09-20.
 */
public class Server implements Runnable, ServerLogic, ServerActions { // no need for thread
    private static final String DELIMITERS = "/ ";
    ServerSocket serverSocket;
    private ConcurrentHashMap<SocketAddress, Client> clientLookup;
    private BlockingQueue<MsgContainer> messageToBroadcast = new LinkedBlockingQueue<MsgContainer>();
    private ExecutorService threadPool;
    private ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<String, Command>();
    private AtomicBoolean running;

    class MsgContainer {
        public String msg;
        public Client client;

        public MsgContainer(String msg, Client client) {
            this.msg = msg;
            this.client = client;
        }
    }

    public Server(int port) throws IOException {
        this.running = new AtomicBoolean(true);
        this.serverSocket = new ServerSocket(port);
        this.clientLookup = new ConcurrentHashMap<SocketAddress, Client>();
        this.threadPool = Executors.newCachedThreadPool();
        registrateAllCommands();
        System.out.println(InetAddress.getLocalHost());
    }

    private void registrateAllCommands() {
        commandList.put("quit", new CmdQuit(this));
        commandList.put("who", new CmdWho(this));
        commandList.put("nick", new CmdChangeNick(this));
        commandList.put("help", new CmdHelp(this));
    }

    // loops thru and sends msg instead
    private void sendBroadcastMessage(MsgContainer msg) {
        sendBroadcastMessage(msg.msg, msg.client);
    }

    private void sendBroadcastMessage(String msg, Client from) {
        SocketAddress inetAddress = (from != null) ? from.getSocketAddress() : null;
        for (Map.Entry<SocketAddress, Client> entry : clientLookup.entrySet()) {
            System.out.println(entry);
            Client c = entry.getValue();
            if (!c.getSocketAddress().equals(inetAddress)) {
                try {
                    c.sendMsgToclient(msg);
                }catch (IOException ex) {
                    System.out.println(" c.sendMsgToclient(msg);");
                    disconnectClient(c);
                    c.terminateClient();
                }

            }
        }
    }

    public void run() {
        while (running.get()) {
            try {
                MsgContainer msg = messageToBroadcast.take();

                sendBroadcastMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void start() {

        try {
            threadPool.execute(this);
            while (running.get()) {
                System.out.println("Waiting for connection...");
                Socket clientSocket = null;
                clientSocket = serverSocket.accept();
                System.out.println("Client connected!");
                SocketAddress inetAddress = clientSocket.getRemoteSocketAddress();//.getSocketAddress();
                Client client = new Client(clientSocket, this);
                Client oldClient = clientLookup.put(inetAddress, client);
                if (oldClient != null) {
                    oldClient.terminateClient();
                }
                System.out.println("Client Added!");
                threadPool.execute(client);
                broadcastMsg("Client " + client.getNickName() + " connected", null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            shutdownServer();
        }
    }

    public void shutdownServer() {
        for (Map.Entry<SocketAddress, Client> entry : clientLookup.entrySet()) {
            //System.out.println(entry);
            Client c = entry.getValue();
            disconnectClient(c);
        }

        try {
            if(serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running.set(false);
        threadPool.shutdown();
    }


    @Override
    public void removeClient(Client client) {
        SocketAddress addr = client.getSocketAddress();
        Client remove = this.clientLookup.remove(addr);
        broadcastMsg("Client: " + remove.getNickName() + " Disconected", remove);
    }

    @Override
    public boolean nickExist(String nickName) {
        boolean flag = false;
        for (Map.Entry<SocketAddress, Client> entry : clientLookup.entrySet()) {
            System.out.println(entry);
            Client c = entry.getValue();
            if(nickName.equals(c.getNickName())) {
                return true;
            }
        }
        return false;
    }

    // de registrate client
    @Override
    public void disconnectClient(Client client) {
        SocketAddress addr = client.getSocketAddress();
        Client remove = this.clientLookup.remove(addr);
        if (remove == null) {
            //System.out.println("Client already disconnect and been removed");
            return;
        }
        remove.terminateClient();
        broadcastMsg("Client: " + remove.getNickName() + " Disconected", remove);
    }

    @Override
    public String listNicknames() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<SocketAddress, Client> entry : clientLookup.entrySet()) {
            System.out.println(entry);
            Client c = entry.getValue();
            if (c.isRunning() == false) {
                removeClient(c);
            }else {
                sb.append(c.getNickName() + "\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String listCommands() {
        return "Commands: \n"
                + "/quit = logout and exit \n"
                + "/who = list of all connected users \n"
                + "/nick <newNickName> = change nickname \n"
                + "/help = list all available commands";
    }

    @Override
    public boolean broadcastMsg(String msg, Client from) {
        return messageToBroadcast.offer(new MsgContainer(msg, from));
    }

    // same
    @Override
    public void evaluateCommand(String msg, Client client) {
        StringTokenizer tokenizer = new StringTokenizer(msg.substring(1), DELIMITERS);
        String cmd = null;
        if (tokenizer.hasMoreTokens()) {
            cmd = tokenizer.nextToken();
        }

        if (cmd == null) {
            try {
                client.sendMsgToclient("Unkown command");
            } catch (IOException e) {
                client.terminateClient();
            }
            return;
        }
        Command command = commandList.get(cmd);
        if (command != null) {
            command.processCommand(msg, client);
        } else {
            try {
                client.sendMsgToclient("Unkown command");
            } catch (IOException e) {
                client.terminateClient();
            }
        }
    }
}
