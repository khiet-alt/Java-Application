package main;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

public class FileOperation {

    public static HashMap<String, String> readFileIntoMap(String filename) throws IOException {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = bufferedReader.readLine()) != null){
                String parts[] = line.split("`", 2);
                if (parts.length >= 2){
                    hashMap.put(parts[0].toLowerCase(Locale.ROOT), parts[1]);
                } else {
//                    System.out.println("ignoring line: " + line);
                }
            }
//            System.out.println("--------------------------------");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return hashMap;
    }

    public static void writeHashmapToFile(String filename, HashMap<String, String> dict) throws IOException {
        try (FileWriter writer = new FileWriter(filename);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)){

            for (String key : dict.keySet()){
                String line = String.join("`", key, dict.get(key));
                line += "\n";
                bufferedWriter.write(line);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static LinkedList<String> readHistory(String filename) throws IOException {
        LinkedList<String> history = new LinkedList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));){
            String line;

            while ((line = bufferedReader.readLine()) != null){
                history.add(line);
            }
            System.out.println("--------------------------------");
        } catch (FileNotFoundException ex) {
            return history;
        }
        return history;
    }

    public static void writeHistory(String filename, LinkedList<String> history){
        try (FileWriter writer = new FileWriter(filename);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)){

            for (String word : history){
                bufferedWriter.write(word + "\n");
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}
