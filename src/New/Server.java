package New;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    
    public static HashMap<String, ClientSocket> clientList = new HashMap<>();
//    private ArrayList<ClientSocket> clientSocketList;
    private final int portNumber;
    private ServerSocket serverSocket;
    private Socket socket;
    
    public Server() throws IOException {
        portNumber = 6677;
        serverSocket = new ServerSocket(portNumber);
//        clientSocketList = new ArrayList<>();
    }
    
    public void startServer() {
        System.out.println("Server started");
        //Thread for listening for sock requests
        new Thread() {
            @Override
            public void run() {
                listenForClients();
            }
        }.start();

        //Thread for sending currente user list
        new Thread() {
            public void run() {
                System.out.println("Listen for change is running");
                listenForListChange();
            }
        }.start();

        //Thread removes dead users
        new Thread() {
            public void run() {
                checkSocketIsAlive();
            }
        }.start();
    }
    
    private void checkSocketIsAlive() {
        while (true) {
            try {
                Thread.sleep(6100);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    private void listenForListChange() {
//        int listSize = Server.clientList.size();
        int listSize = 0;
        DataOutputStream outToClient = null;
        String message = "";
        while (true) {
            System.out.println("listsize is " + listSize + " list is " + Server.clientList.size());
            if (listSize != Server.clientList.size()) {
                try {
                    for (ClientSocket cs : Server.clientList.values()) {
                        outToClient = new DataOutputStream(cs.getSocket().getOutputStream());
                        for (String name : Server.clientList.keySet()) {
                            message += name + " ";
                        }
                        outToClient.writeBytes("LIST {" + message + "}" + '\n');
                        message = "";
                    }
                    listSize = Server.clientList.size();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void listenForClients() {
        try {
            System.out.println("Thread running to listen for clients");
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Found a client");
                
                new Thread(new ClientSocket(socket)).start();
                System.out.println("Created a thread for client");
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static synchronized void messageAll(String message) {
        DataOutputStream outToAll;
        for (ClientSocket cs : Server.clientList.values()) {
            try {
                outToAll = new DataOutputStream(cs.getSocket().getOutputStream());
                outToAll.writeBytes("DATA " + message + '\n');
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
        
    }
}
