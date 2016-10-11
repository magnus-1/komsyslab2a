import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by cj on 20/09/16.
 */
public class User extends UnicastRemoteObject implements ClientRMI { // should not implement runable, but instead som notify thingy

    String nickName = "";
//    private Socket clientSocket;
//    private AtomicBoolean running;
//    private BufferedReader input;
//    private PrintWriter output;

    private ServerRMI serverRMI;
    public User(ServerRMI serverRMI) throws RemoteException  {
        super();
        this.serverRMI = serverRMI;
        this.nickName = "name" + (new Random()).nextInt();
//        this.clientSocket = new Socket(ip, port);
//        this.running = new AtomicBoolean(true);
//        try {
//            this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            this.output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//        } catch (IOException e) {
//            e.printStackTrace();
//            closeAllConnections();
//        }
    }

    //no need for threads
    public void start() {
        //Thread th = new Thread(this);
        //th.start();
        Scanner scanner = new Scanner(System.in);
        try {

            while (true) {
                String msg = scanner.nextLine();
                try {
                    serverRMI.postChatMsg(this,msg);
                    //postMessage(msg);
                    if (msg.equals("/quit")) {
                        break;
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                    break;
                }
            }
        } finally {
            //closeAllConnections();
            try {
                serverRMI.deregistrateClient(this);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }



//    // deregesrate from server
//    private void closeAllConnections() {
//        if (clientSocket != null) {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (this.output != null) {
//            this.output.close();
//        }
//        if (this.input != null) {
//            try {
//                this.input.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void postMessage(String msg) throws IOException {
//        this.output.println(msg);
//        this.output.flush();
//    }

//    public void terminateUser() {
//        running.set(false);
//    }

    // replace with rmi notifinble version ...


    public void sendMsgToclient(String msg)
    {
        System.out.println("Incoming msg: " + msg);
    }

    @Override
    synchronized public String getNickName() throws RemoteException {
        return this.nickName;
    }

    @Override
    synchronized public void setNickName(String nickName) throws RemoteException {
        this.nickName = nickName;
    }

//    @Override
//    public void run() {
//        BufferedReader buffer = null;
//        try {
//            while (running.get()) {
//                if (input == null) {
//                    break;
//                }
//                String msg = input.readLine();
//                if (msg == null) {
//                    running.set(false);
//                    return;
//                }
//                System.out.println(msg);
//
//
//            }
//        } catch (SocketException e) {
//
//        } catch (IOException e) {
//
//        } finally {
//            closeAllConnections();
//            running.set(false);
//        }
//    }
}
