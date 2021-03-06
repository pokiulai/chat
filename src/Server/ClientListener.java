/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Po
 */
public class ClientListener {

    public static void main(String[] args) throws IOException {

        int port = 6677;
        ServerSocket serverSocket;
        Socket socket;
        ClientSocket clientSock;
        DataInputStream input;
        DataOutputStream output;
        HashMap<String, ClientSocket> clientMap;

        serverSocket = new ServerSocket(port);

        while (true) {
            System.out.println("Listening for clients");
            socket = serverSocket.accept();
            System.out.println("Found a client");
            System.out.println("Thread socket created");
            clientSock = new ClientSocket(socket);
            new Thread(clientSock).start();

        }

    }

}
