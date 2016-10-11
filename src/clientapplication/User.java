package clientapplication;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.Buffer;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cj on 20/09/16.
 */
public class User implements Runnable { // should not implement runable, but instead som notify thingy

    private Socket clientSocket;
    private AtomicBoolean running;
    private BufferedReader input;
    private PrintWriter output;

    public User(String ip, int port) throws IOException {
        this.clientSocket = new Socket(ip, port);
        this.running = new AtomicBoolean(true);
        try {
            this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            closeAllConnections();
        }
    }

    //no need for threads
    public void start() {
        Thread th = new Thread(this);
        th.start();
        Scanner scanner = new Scanner(System.in);
        try {

            while (running.get()) {
                String msg = scanner.nextLine();
                try {
                    postMessage(msg);
                    if (msg.equals("/quit")) {
                        running.set(false);
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                    running.set(false);
                }
            }
        } finally {
            closeAllConnections();
        }

    }



    // deregesrate from server
    private void closeAllConnections() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.output != null) {
            this.output.close();
        }
        if (this.input != null) {
            try {
                this.input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void postMessage(String msg) throws IOException {
        this.output.println(msg);
        this.output.flush();
    }

    public void terminateUser() {
        running.set(false);
    }

    // replace with rmi notifinble version ...


    public void sendMsgToclient(String msg)
    {
        System.out.println("Incoming msg: " + msg);
    }
    @Override
    public void run() {
        BufferedReader buffer = null;
        try {
            while (running.get()) {
                if (input == null) {
                    break;
                }
                String msg = input.readLine();
                if (msg == null) {
                    running.set(false);
                    return;
                }
                System.out.println(msg);


            }
        } catch (SocketException e) {

        } catch (IOException e) {

        } finally {
            closeAllConnections();
            running.set(false);
        }
    }
}
