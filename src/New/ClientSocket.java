/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package New;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Po
 */
public class ClientSocket implements Runnable {

    private Socket socket;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private String clientSocketUserName;

    private boolean isAlive;

    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outToClient = new DataOutputStream(socket.getOutputStream());
        isAlive = true;
    }

    @Override
    public void run() {

        new Thread() {
            @Override
            public void run() {
                String messageFromClient;
                while (true) {
                    try {
                        messageFromClient = inFromClient.readLine();
//                        outToClient.writeBytes("SERVER ECHO: " + messageFromClient + '\n');
                        whatProtocol(messageFromClient);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }.start();

    }

    private void whatProtocol(String message) throws IOException {

        String protocol = (message.substring(0, 4)).toUpperCase();
//        System.out.println("inside chatprotocol" + protocol);
//        outToClient.writeBytes("Inside whatprotocol + " + message + '\n');
//        message = message.substring(5);

        switch (protocol) {
            case "ALVE":
                heartBeat();
                break;
            case "DATA":
                sendMessage(message);
                break;
            case "JOIN":
                joinChat(message);
                break;
            case "QUIT":
                quitChat();
                break;

        }
    }

    private void sendMessage(String msg) {
        String message = (msg.substring(4, msg.length()));
        Server.messageAll(clientSocketUserName + message);
    }

    private void heartBeat() {
        isAlive = true;
        try {
            outToClient.writeBytes("This is your heart speaking" + '\n');
        } catch (IOException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void quitChat() {
        try {
            Server.clientList.remove(clientSocketUserName);
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void joinChat(String message) throws IOException {
        String userName = message.substring(message.indexOf("{") + 1, message.indexOf("}"));

        if (Server.clientList.isEmpty()) {
            clientSocketUserName = userName;
            Server.clientList.put(userName, this);
            outToClient.writeBytes("J_OK" + '\n');
        } else if (!Server.clientList.containsKey(userName)) {
            clientSocketUserName = userName;
            Server.clientList.put(userName, this);
            outToClient.writeBytes("J_OK" + '\n');
        } else {
            outToClient.writeBytes("J_ERR" + '\n');
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

}
