package serverapplication;

import clientapplication.ClientRMI;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by o_0 on 2016-09-20.
 */
public class Server implements ServerRMI, ServerLogic, ServerActions { // no need for thread
    private static final String DELIMITERS = "/ ";
    //ServerSocket serverSocket;
    //private Object clientLock = new Object();
    private ArrayList<ClientRMI> clients;
    //private ConcurrentHashMap<SocketAddress, Client> clientLookup;
    //private BlockingQueue<MsgContainer> messageToBroadcast = new LinkedBlockingQueue<MsgContainer>();
    //private ExecutorService threadPool;
    private ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<String, Command>();
    //private AtomicBoolean running;

    @Override
    public void postChatMsg(ClientRMI client, String msg) throws RemoteException {
        if (msg != null) {
            if (msg.length() > 0 && msg.charAt(0) == '/') {
                // evaluate command
                evaluateCommand(msg, client);
            } else {
                broadcastMsg(client.getNickName() + ": " + msg, client);
            }
        }
    }

//    class MsgContainer {
//        public String msg;
//        public Client client;
//
//        public MsgContainer(String msg, Client client) {
//            this.msg = msg;
//            this.client = client;
//        }
//    }

    public Server(){
//        this.running = new AtomicBoolean(true);
//        this.serverSocket = new ServerSocket(port);
//        this.clientLookup = new ConcurrentHashMap<SocketAddress, Client>();
//        this.threadPool = Executors.newCachedThreadPool();
        this.clients = new ArrayList<>();
        registrateAllCommands();
//        System.out.println(InetAddress.getLocalHost());
    }
//
//    class UserContainer {
//        public String nickName;
//        public ClientRMI client;
//
//        @Override
//        public String toString() {
//            return "UserContainer{" +
//                    "nickName='" + nickName + '\'' +
//                    ", client=" + client +
//                    '}';
//        }
//    }

    private void registrateAllCommands() {
        commandList.put("quit", new CmdQuit(this));
        commandList.put("who", new CmdWho(this));
        commandList.put("nick", new CmdChangeNick(this));
        commandList.put("help", new CmdHelp(this));
    }

    // loops thru and sends msg instead
//    private void sendBroadcastMessage(MsgContainer msg) {
//        sendBroadcastMessage(msg.msg, msg.client);
//    }

//    private void sendBroadcastMessage(String msg, Client from) {
//        SocketAddress inetAddress = (from != null) ? from.getSocketAddress() : null;
//        for (Map.Entry<SocketAddress, Client> entry : clientLookup.entrySet()) {
//            System.out.println(entry);
//            Client c = entry.getValue();
//            if (!c.getSocketAddress().equals(inetAddress)) {
//                try {
//                    c.sendMsgToclient(msg);
//                }catch (IOException ex) {
//                    System.out.println(" c.sendMsgToclient(msg);");
//                    disconnectClient(c);
//                    c.terminateClient();
//                }
//
//            }
//        }
//    }

//    public void run() {
//        while (running.get()) {
//            try {
//                MsgContainer msg = messageToBroadcast.take();
//
//                sendBroadcastMessage(msg);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    public void start() {
//
//        try {
//            threadPool.execute(this);
//            while (running.get()) {
//                System.out.println("Waiting for connection...");
//                Socket clientSocket = null;
//                clientSocket = serverSocket.accept();
//                System.out.println("Client connected!");
//                SocketAddress inetAddress = clientSocket.getRemoteSocketAddress();//.getSocketAddress();
//                Client client = new Client(clientSocket, this);
//                Client oldClient = clientLookup.put(inetAddress, client);
//                if (oldClient != null) {
//                    oldClient.terminateClient();
//                }
//                System.out.println("Client Added!");
//                threadPool.execute(client);
//                broadcastMsg("Client " + client.getNickName() + " connected", null);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            shutdownServer();
//        }
//    }

//    public void shutdownServer() {
//        for (Map.Entry<SocketAddress, Client> entry : clientLookup.entrySet()) {
//            //System.out.println(entry);
//            Client c = entry.getValue();
//            disconnectClient(c);
//        }
//
//        try {
//            if(serverSocket != null)
//                serverSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        running.set(false);
//        threadPool.shutdown();
//    }


//    @Override
//    public void removeClient(Client client) {
//        SocketAddress addr = client.getSocketAddress();
//        Client remove = this.clientLookup.remove(addr);
//        broadcastMsg("Client: " + remove.getNickName() + " Disconected", remove);
//    }

    @Override
    synchronized public void registrateClient(ClientRMI clientRMI) throws RemoteException {
        clients.add(clientRMI);
    }

    @Override
    synchronized public void deregistrateClient(ClientRMI clientRMI) throws RemoteException {
        clients.remove(clientRMI);
    }

    /**
     * Disconect the client and remove it from active chatters
     *
     * @param client
     */
    @Override
    synchronized public void disconnectClient(ClientRMI client) {
        broadcastMsg("client disconected: " , client);
        clients.remove(client);
    }

    @Override
    synchronized public ClientRMI changeNick(String nickName, ClientRMI requestSender) {
        boolean flag = false;
        if (clients.contains(requestSender) == false) {
            System.out.println("changeNick null");
            return null;
        }
        for (ClientRMI c : clients) {
            try {
                if (c.getNickName().equals(nickName)) {
                    return null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        try {
            requestSender.setNickName(nickName);
        } catch (RemoteException e) {

            e.printStackTrace();
        }
        return requestSender;
    }

    // de registrate client
//    @Override
//    public void disconnectClient(ClientRMI client) {
//        SocketAddress addr = client.getSocketAddress();
//        Client remove = this.clientLookup.remove(addr);
//        if (remove == null) {
//            //System.out.println("Client already disconnect and been removed");
//            return;
//        }
//        remove.terminateClient();
//        broadcastMsg("Client: " + remove.getNickName() + " Disconected", remove);
//    }

    @Override
    synchronized public String listNicknames() {
        StringBuilder sb = new StringBuilder();
        ArrayList<ClientRMI> removeList = new ArrayList<>();
        String nickName;
        for (ClientRMI user : clients ) {
            System.out.println(user);
            try {
                nickName = user.getNickName();
            } catch (RemoteException e) {
                removeList.add(user);
                continue;
            }
            sb.append(nickName + "\n");
        }
        for (ClientRMI c : removeList ) {
            clients.remove(c);
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

    /**
     * Brodcast a msg to all active chat clients
     * @param msg the message
     * @param from who sent the msg
     * @return
     */
    @Override
    synchronized public boolean broadcastMsg(String msg, ClientRMI from) {
        boolean msgSent = true;
        ArrayList<ClientRMI> removeList = new ArrayList<>();

        String nickName = null;
        try {
            nickName = from.getNickName();
        } catch (RemoteException e) {
            clients.remove(from);
            e.printStackTrace();
            return false;
        }
        for (ClientRMI user : clients ) {
                if (user == from) {

                    continue;
                }
                try {
                    user.sendMsgToclient("from " + nickName + " msg: " + msg);
                } catch (RemoteException e) {
                    removeList.add(user);
                }
            }

        for (ClientRMI c : removeList ) {
            clients.remove(c);
        }
        return msgSent;
    }

    /**
     * evaluate a Command and preformes it
     * @param msg the command string
     * @param client who invoked the command
     */
    public void evaluateCommand(String msg, ClientRMI client) throws RemoteException {
        StringTokenizer tokenizer = new StringTokenizer(msg.substring(1), DELIMITERS);
        String cmd = null;
        if (tokenizer.hasMoreTokens()) {
            cmd = tokenizer.nextToken();
        }

        if (cmd == null) {
            client.sendMsgToclient("Unkown command");
            return;

        }
        Command command = commandList.get(cmd);
        if (command != null) {
            command.processCommand(msg, client);
        } else {
            client.sendMsgToclient("Unkown command");
        }
    }
}
