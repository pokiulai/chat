package New;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    String hostName;
    int portNumber;
    Socket socket;
    boolean uniqueName;

    DataOutputStream outToServer;
    BufferedReader inFromServer;
    Scanner scan;

    public Client() throws IOException {
        hostName = "localhost";
        portNumber = 6677;
        scan = new Scanner(System.in);
        socket = new Socket(hostName, portNumber);
        outToServer = new DataOutputStream(socket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        uniqueName = false;
    }

    public void startClient() {

        //Receives messages
        new Thread() {
            @Override
            public void run() {
                String serverMessage;
                System.out.println("Thread created for listening to server messages");
                while (true) {
                    try {
                        serverMessage = inFromServer.readLine();
                        whatProtocol(serverMessage);
//                        System.out.println(serverMessage);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();

        //Joining chat
        System.out.println("Type your user name");
        while (true) {
            try {
                joinServer();
                Thread.sleep(500);
                if (uniqueName) {
                    break;
                } else {
                    System.out.println("Name is taken");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //heart pump mechanism
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    heartBeat();
                }
            }
        };

        String userInput;
        while (true) {
            userInput = scan.nextLine();
            sendMessage(userInput);
        }

    }

    private void sendMessage(String userInput) {
        try {
            if (userInput.equals("QUIT")) {
                outToServer.writeBytes("QUIT" + '\n');
            } else {
                outToServer.writeBytes("DATA " + userInput + '\n');
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void whatProtocol(String message) throws IOException {

        String protocol = (message.substring(0, 4));
//        String[] protocol = message.split("\\s+");
//        System.out.println(protocol[0]);
        switch (protocol) {
            case "DATA":
                receiveMessage(message);
                break;
            case "LIST":
                retrieveUserList(message);
                break;
            case "J_OK":
                uniqueName = true;
                break;
            case "J_ER":
                uniqueName = false;
                break;
        }
    }

    private void receiveMessage(String message) {
        System.out.println(message);
    }

    private void retrieveUserList(String listOfNames) {
        String[] names = listOfNames.split("\\s+");
        System.out.println("List of users:");
        for (int i = 1; i < names.length; i++) {
            if (i == 1) {
                System.out.println(names[i].substring(1));
            } else if (i == (names.length - 1)) {
//                System.out.println(names[i].substring(0, i - 2));
                return;
            } else {
                System.out.println(names[i]);
            }
        }
    }

    private void joinServer() {
        try {
            String userName;
            userName = scan.nextLine();
            outToServer.writeBytes("JOIN {" + userName + "}" + '\n');
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void heartBeat() {
        try {
            String heartbeat = "ALVE";
            outToServer.writeBytes(heartbeat + '\n');
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startClient();
    }
}
