package serverapplication;

import clientapplication.ClientRMI;

/**
 * Created by o_0 on 2016-09-20.
 */
public interface Command {
    /**
     * Process a command
     * @param msg the command and its arguments
     * @param sender the client that sent the command
     */
    void processCommand(String msg, ClientRMI sender);
}
