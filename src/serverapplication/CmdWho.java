package serverapplication;

import clientapplication.ClientRMI;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by cj on 20/09/16.
 */
public class CmdWho implements Command {
    private ServerActions serverDelegate;
    public CmdWho(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }

    @Override
    public void processCommand(String msg, ClientRMI sender) {
        try {
            sender.sendMsgToclient(serverDelegate.listNicknames());
        } catch (RemoteException e) {
            e.printStackTrace();
            serverDelegate.disconnectClient(sender);
        }
    }
}
