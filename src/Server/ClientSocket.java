/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
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

    private final Socket socket;
    private BufferedReader inputFromClient;
    private DataOutputStream outputToServer;

    public ClientSocket(Socket socket) {
        this.socket = socket;
        try {
            inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputToServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        System.out.println("Socket is running");
        String message;
        while (true) {
            try {
                
                message = inputFromClient.readLine();
                if(!message.isEmpty()){
                   outputToServer.writeChars(message);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
