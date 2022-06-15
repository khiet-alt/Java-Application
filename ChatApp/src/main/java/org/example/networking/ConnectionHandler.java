package org.example.networking;

import org.example.entity.Message;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    public ObjectOutputStream objectOutputStream;
    public ObjectInputStream objectInputStream;

    String username = null;

    public ConnectionHandler(Socket serverSocket){
        socket = serverSocket;

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e){}
    }

    public void run(){
        startReading();
    }

    public void startReading() {
        Runnable r1 = () -> {
            try {
                while (!socket.isClosed()) {
                    Message msg = null;
                    try {
                        msg = (Message) objectInputStream.readObject();
                    } catch (Exception e){
                        Server.removeFromQueue(username);
                    }

                    if (msg.getContent().equalsIgnoreCase("inforaboutusername")){
                        // Catch first message, which is "username"
                        username = msg.getSender();
                        Server.addToQueue(username);
                        System.out.println(username + " has connected");
                    } else {
                        // This is normal message from clients
                        Server.addMessageToStore(msg);
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed from error read");
                e.printStackTrace();
            } finally {
                try {
                    close();
                    System.out.println("Close all 3 streams");
                    Server.removeFromQueue(username);

                } catch (Exception e) {}
            }
        };

        new Thread(r1).start();
    }

    public void close() {
        try {
            if (objectInputStream != null){
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (!socket.isClosed()) {
                socket.isClosed();
            }
        } catch (Exception e) {
            System.out.println("There are errors when closing 3 stream");
        }
    }
}
