import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.*;

/**
 * Created by o_0 on 2016-09-20.
 */
public class Server extends UnicastRemoteObject implements ServerRMI, ServerLogic, ServerActions { // no need for thread
    private static final String DELIMITERS = "/ ";
    private ArrayList<ClientRMI> clients;
    private ConcurrentHashMap<String, Command> commandList = new ConcurrentHashMap<String, Command>();

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

    public Server() throws RemoteException {
        super();
        this.clients = new ArrayList<>();
        registrateAllCommands();
    }

    private void registrateAllCommands() {
        commandList.put("quit", new CmdQuit(this));
        commandList.put("who", new CmdWho(this));
        commandList.put("nick", new CmdChangeNick(this));
        commandList.put("help", new CmdHelp(this));
    }


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
