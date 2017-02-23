/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Po
 */
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket;
        String message;
        DataInputStream input;
        DataOutputStream output;

        int port = 6677;
        String host = "localhost";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Connecting to server...");
        socket = new Socket(host, port);
        System.out.println("Connection estabilished");
        System.out.println("Creating streams");
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        System.out.println("Streams created");

        while (true) {
            message = scanner.nextLine();

        }
    }

}
