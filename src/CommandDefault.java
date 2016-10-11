/**
 * Created by o_0 on 2016-09-20.
 */
public class CommandDefault implements Command {
    private String name;
    private ServerActions serverDelegate;
    public CommandDefault(String name) {
        this.name = name;
    }

    @Override
    public void processCommand(String msg, ClientRMI sender) {
        System.out.println("this is the command default: " + name);
    }
}
