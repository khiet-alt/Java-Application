package org.example.networking;

import org.example.entity.Message;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private ServerSocket serverSocket;
    static private JTextPane showUserPane;
    static Lock lock = new ReentrantLock();

    static public Queue<String> currentUserOnl = new ConcurrentLinkedQueue<>();
    static public ConcurrentHashMap<Socket, ConnectionHandler> socketLists = new ConcurrentHashMap<>();
    static public CopyOnWriteArrayList<Message> messageStore = new CopyOnWriteArrayList<>();

    public Server(JTextPane userPanel) {
        showUserPane = userPanel;
        showUserPane.setEditable(false);
        showUserPane.setContentType("text/html");
        // Align jtextpane center
        StyledDocument doc = showUserPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        //

        try {
            serverSocket = new ServerSocket(3500);
            System.out.println("Waiting for client...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Socket newSocket = serverSocket.accept();
                            ConnectionHandler newConnectionHandler = new ConnectionHandler(newSocket);
                            Thread newConnection = new Thread(newConnectionHandler);
                            socketLists.put(newSocket, newConnectionHandler);
                            newConnection.start();
                        }

                    } catch (Exception e) {
                    }
                }
            }).start();
        } catch (Exception e) {
        }
    }

    static public void addToQueue(String name) {
        try {
            lock.lock();
            currentUserOnl.add(name);
            showCurrentUser();
            broadcastCurrentUserToClient();
        } catch (Exception e){}
        finally {
            lock.unlock();
        }
    }

    static public void removeFromQueue(String name) {
        try {
            lock.lock();
            currentUserOnl.remove(name);
            showCurrentUser();
            broadcastCurrentUserToClient();
        } catch (Exception e) {}
        finally {
            lock.unlock();
        }
    }

    static public void addMessageToStore(Message msg) {
        messageStore.add(msg);
        updateMessageToClient(msg);
        System.out.println("Received from client: " + msg);
    }

    static public void updateMessageToClient(Message recentMessage){
        String sender = recentMessage.getSender();
        String receiver = recentMessage.getReceiver();

        int countCheck = 0;

        for (Socket socket : socketLists.keySet()) {
            ConnectionHandler connection = socketLists.get(socket);

            if (countCheck >= 2){
                break;
            }

            try {
                if (connection.username.equalsIgnoreCase(sender) || connection.username.equalsIgnoreCase(receiver)) {
                    // Need to route this message to those client
                    connection.objectOutputStream.writeObject(recentMessage);
                    connection.objectOutputStream.flush();

                    countCheck += 1;
                }
            } catch (Exception e) {}
        }
    }

    static public void showCurrentUser() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                String textResult = "<html><center>";
                for (String user : currentUserOnl) {
                    textResult +=
                            "       <b ><font size=15 color=rgb(1,1,1)>" +
                                    user +
                                    "</font></b> <br>";
                }
                textResult += "</center></html>";
                showUserPane.setText(textResult);
                lock.unlock();
            }
        });
    }

    static public void broadcastCurrentUserToClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Socket clientHost : socketLists.keySet()) {
                        ObjectOutputStream outputForSocket = socketLists.get(clientHost).objectOutputStream;
                        outputForSocket.writeObject(new Message("1", "server", "allcurrentuser", Integer.toString(currentUserOnl.size())));
                        outputForSocket.flush();

                        for (String user: currentUserOnl) {
                            outputForSocket.writeObject(user);
                            outputForSocket.flush();
                        }

                    }
                } catch (Exception e) {}
            }
        }).start();
    }
}
