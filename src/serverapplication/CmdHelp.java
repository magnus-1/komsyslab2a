package serverapplication;

import java.io.IOException;

/**
 * Created by cj on 20/09/16.
 */
public class CmdHelp implements Command {
    private ServerActions serverDelegate;
    public CmdHelp(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }

    @Override
    public void processCommand(String msg, Client sender) {
        try {
            sender.sendMsgToclient(serverDelegate.listCommands());
        } catch (IOException e) {
            sender.terminateClient();
        }
    }
}
