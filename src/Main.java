
/**
 * Created by cj on 16/09/16.
 */
import clientapplication.User;
import serverapplication.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {

    private static final String CLIENT = "client";
    private static final String SERVER = "server";

    public static void main(String[] args) throws IOException {

        if  (args[0].toLowerCase().equals(CLIENT)){
            System.out.println("Starting Client...");
            //InetSocketAddress addr = new InetSocketAddress(args[1],Integer.parseInt(args[2]));
            User user = new User(args[1], Integer.parseInt(args[2]));
            user.start();
        }
        if (args[0].toLowerCase().equals(SERVER)){
            System.out.println("Starting Server...");
            Server server = new Server(Integer.parseInt(args[1]));
            server.start();
        }
    }
}
