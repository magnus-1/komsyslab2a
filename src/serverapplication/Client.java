package serverapplication;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by magnus on 2016-09-20.
 */
public class Client {
//    private ServerLogic serverDelegate;
//    private Socket clientSocket;
//    private AtomicBoolean runClient;
//    private BufferedReader input;
//    private PrintWriter output;
//    private String nickName;
//    private Object writeLock = new Object();
//    public Client(Socket clientSocket, ServerLogic serverDelegate) {
//        this.serverDelegate = serverDelegate;
//        this.clientSocket = clientSocket;
//        try {
//            clientSocket.setSoTimeout(200);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        this.runClient = new AtomicBoolean(true);
//        String addr = clientSocket.getInetAddress().toString();
//        this.nickName = "player" +addr.substring(addr.length() - 3) + clientSocket.getPort();
//        try {
//            this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            this.output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public SocketAddress getSocketAddress() {
//        //System.out.println("sock add:" + clientSocket.getRemoteSocketAddress().toString());
//        return clientSocket.getRemoteSocketAddress();
//    }
//
//
//    public String getNickName() {
//        return nickName;
//    }
//
//    public void setNickName(String nickName) {
//        this.nickName = nickName;
//    }
//
//    @Override
//    public void run() {
//
//
//        try {
//            sendMsgToclient("Welcome chatter!");
//            while (runClient.get()) {
//                //clientSocket.isClosed()
//
//                try {
//                    String msg = input.readLine();
//                    if (msg != null) {
//                        if (msg.length() > 0 && msg.charAt(0) == '/') {
//                            // evaluate command
//                            serverDelegate.evaluateCommand(msg, this);
//                        } else {
//                            serverDelegate.broadcastMsg(this.nickName + ": " + msg, this);
//                        }
//                    }else {
//                        runClient.set(false);
//                    }
//                }catch (SocketTimeoutException ex) {
//                    //System.out.println("time");
//                }
//            }
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        } finally {
//
//            serverDelegate.removeClient(this);
//            terminateClient();
//            closeAllConnections();
//        }
//    }
//
//    public boolean isRunning() {
//        return this.runClient.get();
//    }
//
//    private void closeAllConnections() {
//        if (clientSocket != null) {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (this.output != null) {
//            this.output.close();
//        }
//        if (this.input != null) {
//            try {
//                this.input.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void terminateClient() {
//        runClient.set(false);
//        try {
//            input.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // from the notifble interface
//    public void sendMsgToclient(String msg) throws IOException {
//        System.out.println("Sending msg: " + msg + " to: " + clientSocket.getRemoteSocketAddress());
//        synchronized (writeLock) {  //PrintWriter is not threadsafe
//            this.output.println(msg);
//            if (this.output.checkError()) {
//                runClient.set(false);
//                System.out.println("checkerror thingy");
//                throw new IOException("Client socket closed");
//            }
//            this.output.flush();
//        }
//
//    }
}
