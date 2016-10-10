package serverapplication;

import java.io.IOException;

/**
 * Created by o_0 on 2016-09-20.
 */
public class CmdChangeNick implements Command{
    private ServerActions serverDelegate;
    public CmdChangeNick(ServerActions serverDelegate) {
        this.serverDelegate = serverDelegate;
    }
    @Override
    public void processCommand(String msg, Client sender) {
        int idx = msg.indexOf(" ");
        if (idx == -1) {
            try {
                sender.sendMsgToclient("Invalid nickname...");
            } catch (IOException e) {
                sender.terminateClient();
            }
            return;
        }
        String oldName = sender.getNickName();
        String newName = msg.substring(idx);
        if (newName.length() > 0 && serverDelegate.nickExist(newName) == false) {
            sender.setNickName(newName);
            serverDelegate.broadcastMsg(oldName + " changed nickname to " + newName,sender);
        }else {
            try {
                sender.sendMsgToclient("Nick already exist...");
            } catch (IOException e) {
                sender.terminateClient();
            }
        }
    }
}
