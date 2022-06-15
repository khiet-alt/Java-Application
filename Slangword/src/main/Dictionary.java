package main;

import java.io.IOException;
import java.util.*;

public class Dictionary {
    private HashMap<String, String> dict;
    private LinkedList<String> history;

    private static final String historyFile = "src/main/data/history.txt";
    private static final String slangFile = "src/main/data/slang.txt";


    public Dictionary() throws IOException {
        dict = FileOperation.readFileIntoMap(slangFile);
        history = FileOperation.readHistory(historyFile);
    }

    public void showHistory() {
        System.out.println("History: ");
        for (String line : history){
            System.out.println(line);
        }
    }

    public void save() throws IOException {
        FileOperation.writeHashmapToFile(slangFile, dict);
        FileOperation.writeHistory(historyFile, history);
    }

    public void find(String keyword){
        String definition = dict.get(keyword.toLowerCase(Locale.ROOT));
        if (definition != null){
            System.out.println("Definition of " + keyword + ": " + definition);
        }
        else {
            System.out.println("Can't found " + keyword);
        }
        history.add(keyword);
    }

    public void findByDef(String definition){
        LinkedList<String> keyList = new LinkedList<>();
        for (String key : dict.keySet()){
//            if (dict.get(key).contains(definition))
            if (FileOperation.containsIgnoreCase(dict.get(key), definition))
                keyList.add(key);
        }
        for (String key : keyList){
            System.out.println(key + " : " + dict.get(key));
        }
        history.add(definition);
    }

    public HashMap<String, String> randomWord(int number, boolean flag){
        HashMap<String, String> randomList = new HashMap<>();
        Random generator = new Random();
        Object[] values = dict.keySet().toArray();

        for (int i = 0; i < number; i++){
            String randomValue = (String) values[generator.nextInt(values.length)];
            if (flag == true)
                randomList.put(randomValue, dict.get(randomValue));
            else
                randomList.put(dict.get(randomValue), randomValue);
        }
        return randomList;
    }

    public void generateQuiz(String ques, boolean flag){
        Scanner sc = new Scanner(System.in);
        HashMap<String, String> randomQuiz = randomWord(4, flag);
        // Pick random question in 4 choice
        Random generator = new Random();
        Object[] values = randomQuiz.keySet().toArray();
        String question = (String) values[generator.nextInt(values.length)];
        String answer = randomQuiz.get(question);

        System.out.print(ques + question + "\n");
        int index = 1, correct = 0;
        for (String key : randomQuiz.keySet()){
            System.out.println(index + ") " + randomQuiz.get(key));
            if (randomQuiz.get(key) == answer)
                correct = index;
            index += 1;
        }
        System.out.println("|||||||||||||");
        System.out.print("Choose your answer (1-->4): ");
        int choose = sc.nextInt();  sc.nextLine();
        if (choose == correct)
            System.out.println("Correct answer !!!");
        else {
            System.out.println("Oops! Wrong answer !!!");
            if (flag == true)
                System.out.println("Correct result is: " + question + " --> " + answer);
            else
                System.out.println("Correct result is: " + answer + " --> " + question);
        }
    }

    public void add(){
        Scanner sc = new Scanner(System.in);
        String key, value;

        // Get input from user
        System.out.print("Enter key: ");
        key = sc.nextLine();
        System.out.print("Enter definition: ");
        value = sc.nextLine();

        // Add to slang-word dictionary
        if (dict.get(key.toLowerCase(Locale.ROOT)) == null){
            dict.put(key, value);
            System.out.println("Add successfully: " + key + " --> " + value);
        } else {
            System.out.println("This keyword has appeared");
        }
    }

    public void delete(){
        Scanner sc = new Scanner(System.in);
        String key;

        // Get input from user
        System.out.print("Enter key need to be deleted: ");
        key = sc.nextLine();

        if (dict.get(key.toLowerCase(Locale.ROOT)) != null){
            System.out.print("Confirm your command (1: yes, 0: no): ");
            int confirm = sc.nextInt(); sc.nextLine();
            if (confirm == 1) {
                System.out.println("Delete successfully: " + key + " --> " + dict.get(key));
                dict.remove(key.toLowerCase(Locale.ROOT));
            } else
                System.out.println("Delete failed");
        } else
            System.out.println("This key hasn't existing, delete failed");
    }

    public void edit(){
        Scanner sc = new Scanner(System.in);
        String key, value;

        // Get input from user
        System.out.print("Enter key need to be edited: ");
        key = sc.nextLine();

        // Add to slang-word dictionary
        if (dict.get(key.toLowerCase(Locale.ROOT)) != null){
            System.out.print("Edit definition: ");
            value = sc.nextLine();
            dict.put(key.toLowerCase(Locale.ROOT), value);
            System.out.println("Edit successfully: " + key + " --> " + dict.get(key));
        } else {
            System.out.println("This keyword hasn't existing, edit failed");
        }
    }

    public void reset(){
        dict.clear();
        System.out.println("Clear all slang words");
    }

}
