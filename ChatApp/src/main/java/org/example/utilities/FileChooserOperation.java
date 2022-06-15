package org.example.utilities;

import javax.swing.*;
import java.io.*;

public class FileChooserOperation {

    static public File getFilePath(){
        JFileChooser fc = new JFileChooser();
        File file = null;
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
        return file;
    }

    static public void writeToFile(String fileName, String absolutePath) throws IOException {
        File pickedFile = new File(absolutePath);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pickedFile));

        byte[] bytes = new byte[(int)pickedFile.length()];
        bis.read(bytes, 0, bytes.length);

        // Folder chooser
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);

        String FILE_TO_RECEIVED = f.getSelectedFile() + "\\" + fileName;
        System.out.println("Write into " + FILE_TO_RECEIVED);

        //receive file
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(FILE_TO_RECEIVED));
        bufferedOutputStream.write(bytes, 0, bytes.length);
        bufferedOutputStream.flush();

        JOptionPane.showMessageDialog(null, "Download successfully");

        bis.close();
        bufferedOutputStream.close();
    }
}