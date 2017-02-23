/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Po
 */
public class ServerProtocol {

    public void sendProtocol(String message) {

        String protocol = message.substring(0, 3);
        switch (protocol) {
            case "ALVE":
                heartBeat();
                break;
            case "DATA":
                sendMessage(message);
                break;
            case "QUIT":
                quitChat(message);
                break;
            case "JOIN":
                joinChat(message);
                break;

        }

    }

    private void heartBeat() {

    }

    private void sendMessage(String message) {

    }

    private void joinChat(String message) {
        String userName;
        String ip;
        int port;

    }

    private void quitChat(String message) {

    }

}
