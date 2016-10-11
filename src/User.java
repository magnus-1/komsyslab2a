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

    private ServerRMI serverRMI;
    public User(ServerRMI serverRMI) throws RemoteException  {
        super();
        this.serverRMI = serverRMI;
        this.nickName = "name" + (new Random()).nextInt();

    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try {

            while (true) {
                String msg = scanner.nextLine();
                try {
                    serverRMI.postChatMsg(this,msg);
                    if (msg.equals("/quit")) {
                        System.out.println("Time to quit...");
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
        System.exit(0);

    }

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

}
