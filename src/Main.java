
/**
 * Created by cj on 16/09/16.
 */

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

public class Main {

    private static final String CLIENT = "client";
    private static final String SERVER = "server";

    public static void main(String[] args) throws IOException {

        if (args[0].toLowerCase().equals(CLIENT)) {
            System.out.println("Starting Client...");
            try {

                String url = "rmi://" + args[1] + "/" + args[2];
                ServerRMI server = (ServerRMI) Naming.lookup(url);
                User user = new User(server);
                server.registrateClient(user);
                user.start();

            } catch (NotBoundException e) {
                e.printStackTrace();
            }

        }
        if (args[0].toLowerCase().equals(SERVER)) {
            System.out.println("Starting Server...");
            Server server = new Server();
            Naming.rebind(args[1], server);
        }
    }
}
