package serverapplication;

/**
 * Created by o_0 on 2016-09-20.
 */
public interface ServerActions {
    /**
     * Disconect the client and remove it from active chatters
     * @param client
     */
    void disconnectClient(Client client);

    boolean nickExist(String nickName);

    /**
     * list Nicknames
     * @return a list of nick names
     */
    String listNicknames();

    /**
     * list of Commands
     * @return list of commands
     */
    String listCommands();

    /**
     * Brodcast a msg to all active chat clients
     * @param msg the message
     * @param from who sent the msg
     * @return
     */
    boolean broadcastMsg(String msg,Client from);
}
