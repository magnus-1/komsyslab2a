package serverapplication;

/**
 * Created by o_0 on 2016-09-20.
 */
public class CmdQuit implements Command {
    private ServerActions serverDelegate;
    public CmdQuit(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }


    @Override
    public void processCommand(String msg, Client sender) {
        serverDelegate.disconnectClient(sender);
    }
}
