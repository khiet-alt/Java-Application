package org.example.networking;

import org.example.entity.Message;
import org.example.utilities.FileChooserOperation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Client {

    Socket socket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    JPanel currentOnlUser;
    JLabel targetNameLabel;
    JLabel nameLabel;
    JPanel msgDisplayArea;
    JTextArea inputArea;
    JButton sendFileButton;
    public String username;
    public String targetName = null;
    boolean isOpenMsg;

    ArrayList<Message> waitMessage = new ArrayList<>();
    ConcurrentHashMap<String, String> listSentFile = new ConcurrentHashMap<>();

    public void setTopFrame(Frame topFrame) {
        topFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                close();
                System.exit(0);
            }
        });
    }

    public Client(String u) {
        username = u;
        isOpenMsg = false;

        try {
            socket = new Socket("localhost", 3500);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());


            // First, send your name to server
            objectOutputStream.writeObject(new Message("", username, "receiver", "inforaboutusername"));
            //

            // Start 2 threads run background concurrently to send and receive message.
            startReading();
//            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentOnlUserPanel(JPanel current) {
        currentOnlUser = current;
        currentOnlUser.setLayout(new BoxLayout(current, BoxLayout.Y_AXIS));
    }

    public void transferComponents(JLabel target, JLabel nl, JPanel ma, JTextArea ta, JButton fileButton) {
        targetNameLabel = target;
        msgDisplayArea = ma;
        inputArea = ta;
        nameLabel = nl;
        inputArea.setEditable(false);
        sendFileButton = fileButton;
        sendFileButton.setEnabled(false);
        ma.setLayout(new BoxLayout(ma, BoxLayout.Y_AXIS));
    }

    public void updateOnlineUser(List<String> activeUsers) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentOnlUser.removeAll();
                currentOnlUser.revalidate();
                currentOnlUser.repaint();

                for (String user: activeUsers) {
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
                    JButton button = new JButton(user);
                    buttonPanel.add(button);
                    currentOnlUser.add(buttonPanel);

                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    targetName = button.getText();
                                    inputArea.setEditable(true);
                                    sendFileButton.setEnabled(true);

                                    // reset all message
                                    msgDisplayArea.removeAll();
                                    msgDisplayArea.revalidate();
                                    msgDisplayArea.repaint();
                                    //

                                    retrievePrevMessage();

                                    targetNameLabel.setText("You are chatting with ");
                                    nameLabel.setText(targetName);
                                    nameLabel.setFont(new Font("Calibri", Font.BOLD, 25));
                                    nameLabel.setForeground(Color.BLUE);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void startReading() {
        Runnable r1 = () -> {
            try {
                while (true) {
                    Message msg = (Message) objectInputStream.readObject();

                    if (msg.getReceiver().equalsIgnoreCase("allcurrentuser")) {
                        // Update online user in client view (Retrieve online user from Server)
                        // This is special message from server, sending all infor about current online user to Client
                        int numberUser = Integer.parseInt(msg.getContent());
                        List<String> onlUsers = new ArrayList<>();

                        for (int i = 0; i < numberUser; i++){
                            String messageUser = (String) objectInputStream.readObject();
                            onlUsers.add(messageUser);
                            System.out.println("Current user: " + messageUser);
                        }
                        onlUsers.remove(username);
                        updateOnlineUser(onlUsers);
                    } else if (msg.getId().equalsIgnoreCase("specialsendfile")) {
                        // Receiver file from server
                        waitMessage.add(msg);
                        String[] parts = msg.getContent().split("###");
                        String fileName = parts[0];
                        String fullFilePath = parts[1];
                        listSentFile.put(fileName, fullFilePath);

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                JButton msgButton = null;

                                if (targetName == null) {

                                } else {
                                    if (msg.getSender().equalsIgnoreCase(username) && msg.getReceiver().equalsIgnoreCase(targetName)) {
                                        // display sender side
                                        msgButton = new JButton(msg.getSender() + ": " + fileName + " (Click to download)");
                                        msgButton.setForeground(Color.GRAY);
                                    } else if (msg.getReceiver().equalsIgnoreCase(username) && msg.getSender().equalsIgnoreCase(targetName)) {
                                        // display receiver side
                                        msgButton = new JButton(msg.getSender() + ": " + fileName + " (Click to download)");
                                        msgButton.setForeground(Color.GRAY);
                                    }
                                    msgButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                FileChooserOperation.writeToFile(fileName, listSentFile.get(fileName));
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                                System.out.println("Error from calling write file to file from client side");
                                            }
                                        }
                                    });

                                    msgDisplayArea.add(msgButton);
                                    msgDisplayArea.revalidate();
                                    msgDisplayArea.repaint();
                                }
                            }
                        });
                    } else {
                        // Processing normal message between clients in here
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                waitMessage.add(msg);

                                if (targetName == null) {

                                } else {

                                    if (msg.getSender().equalsIgnoreCase(username) && msg.getReceiver().equalsIgnoreCase(targetName)) {
                                        // display sender side
                                        JLabel msgButton = new JLabel(msg.getSender() + ": " + msg.getContent(), SwingConstants.RIGHT);
                                        msgButton.setHorizontalAlignment(SwingConstants.RIGHT);
                                        msgButton.setForeground(Color.black);
                                        msgDisplayArea.add(msgButton);
                                    } else if (msg.getReceiver().equalsIgnoreCase(username) && msg.getSender().equalsIgnoreCase(targetName)) {
                                        // display receiver side
                                        JLabel msgButton = new JLabel(msg.getSender() + ": " + msg.getContent(), SwingConstants.RIGHT);
                                        msgButton.setHorizontalAlignment(SwingConstants.RIGHT);
                                        msgButton.setForeground(Color.blue);
                                        msgDisplayArea.add(msgButton);
                                    }
                                    msgDisplayArea.revalidate();
                                    msgDisplayArea.repaint();
                                }
                            }
                        });
                    }
                    System.out.println("Received + " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection closed from startReading()");
                e.printStackTrace();
            } finally {
//                close();
            }
        };

        new Thread(r1).start();
    }

    public void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String typedInput = inputArea.getText();
                Message messageInput = new Message("", username, targetName, typedInput);

                try {

                    objectOutputStream.writeObject(messageInput);
                    objectOutputStream.flush();
                } catch (Exception e) {}
                finally {
                    inputArea.setText("");
                }
            }
        }).start();
    }

    public void sendFile() {
        /*
        Simply sending sender and receiver with file name and absolute file name into server.
        */
        new Thread(new Runnable() {
            @Override
            public void run() {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fc.showSaveDialog(null);
                        File pickedFile = fc.getSelectedFile();

                        try {
                            // Signaling
                            objectOutputStream.writeObject(new Message("specialsendfile", username, targetName, String.join("###", pickedFile.getName(), pickedFile.getAbsolutePath())));
                            objectOutputStream.flush();
                            //

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error when sending file from client");
                        }
                    }
                });
            }
        }).start();
    }

    public void retrievePrevMessage(){
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JLabel msg = null;
                    for (Message message: waitMessage) {
                        if (message.getId().equalsIgnoreCase("specialsendfile")){
                            if ((message.getSender().equalsIgnoreCase(username) && message.getReceiver().equalsIgnoreCase(targetName)) && (message.getReceiver().equalsIgnoreCase(username) && message.getSender().equalsIgnoreCase(targetName))) {

                            } else {
                                String[] parts = message.getContent().split("###");
                                String fileName = parts[0];
                                JButton msgButton = new JButton(message.getSender() + ": " + fileName + " (Click to download)");
                                msgButton.setForeground(Color.GRAY);
                                msgButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            FileChooserOperation.writeToFile(fileName, listSentFile.get(fileName));
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                            System.out.println("Error from calling write file to file from client side");
                                        }
                                    }
                                });
                                msgDisplayArea.add(msgButton);
                            }

                        } else {
                            if (message.getSender().equalsIgnoreCase(username) && message.getReceiver().equalsIgnoreCase(targetName)) {
                                // sender
                                msg = new JLabel(message.getSender() + ": " + message.getContent());
                                msg.setForeground(Color.BLACK);
                            }
                            if (message.getReceiver().equalsIgnoreCase(username) && message.getSender().equalsIgnoreCase(targetName)) {
                                // Receiver
                                msg = new JLabel(message.getSender() + ": " + message.getContent());
                                msg.setForeground(Color.blue);
                            }
                            msgDisplayArea.add(msg);
                        }
                    }
                    msgDisplayArea.revalidate();
                    msgDisplayArea.repaint();
                }
            });

        } catch (Exception e) {}

    }

    public void close() {
        try {
            if (objectInputStream != null){
                System.out.println("Close objectInputStream from Client");
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                System.out.println("Close objectOutputStream from Client");
                objectOutputStream.close();
            }
            if (!socket.isClosed()) {
                System.out.println("Close socket from Client");
                socket.isClosed();
            }
        } catch (Exception e) {
            System.out.println("There are errors when closing 3 stream");
        }
    }

    public static void main(String[] args) {
        new Client("mapden");
    }
}
