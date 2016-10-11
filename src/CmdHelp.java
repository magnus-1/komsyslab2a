import java.rmi.RemoteException;

/**
 * Created by cj on 20/09/16.
 */
public class CmdHelp implements Command {
    private ServerActions serverDelegate;
    public CmdHelp(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }

    @Override
    public void processCommand(String msg, ClientRMI sender) {
        try {
            sender.sendMsgToclient(serverDelegate.listCommands());
        } catch (RemoteException e) {
            e.printStackTrace();
            serverDelegate.disconnectClient(sender);
        }
    }
}
