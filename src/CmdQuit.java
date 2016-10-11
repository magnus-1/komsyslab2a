/**
 * Created by o_0 on 2016-09-20.
 */
public class CmdQuit implements Command {
    private ServerActions serverDelegate;
    public CmdQuit(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }


    @Override
    public void processCommand(String msg, ClientRMI sender) {
        serverDelegate.disconnectClient(sender);
    }
}
