package serverapplication;

import java.io.IOException;

/**
 * Created by cj on 20/09/16.
 */
public class CmdWho implements Command {
    private ServerActions serverDelegate;
    public CmdWho(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }

    @Override
    public void processCommand(String msg, Client sender) {
        try {
            sender.sendMsgToclient(serverDelegate.listNicknames());
        } catch (IOException e) {
            sender.terminateClient();
        }
    }
}
