package serverapplication;

import clientapplication.ClientRMI;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by o_0 on 2016-09-20.
 */
public class CmdChangeNick implements Command{
    private ServerActions serverDelegate;
    public CmdChangeNick(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }
    @Override
    public void processCommand(String msg, ClientRMI sender) {
        int idx = msg.indexOf(" ");
        if (idx == -1) {
            try {
                sender.sendMsgToclient("Invalid nickname...");
            } catch (IOException e) {
                serverDelegate.disconnectClient(sender);
            }
            return;

        }
        String newName = msg.substring(idx);
        String oldName = null;
        if (newName.length() > 0 ) {
            try {
                oldName = sender.getNickName();
                ClientRMI client = serverDelegate.changeNick(newName,sender );
                if (client == null) {
                    sender.sendMsgToclient("Nick already exist...");
                    return;

                }
                serverDelegate.broadcastMsg(oldName + " changed nickname to " + newName,client);
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }

        }
    }
}
