package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Manager {
    private static void showMenuIndex(){
        System.out.println("-------------------------------------------------");
        System.out.println("0: Exit");
        System.out.println("1: Find by slang word");
        System.out.println("2: Find by definition");
        System.out.println("3: Show history finding");
        System.out.println("4: Add new slang word");
        System.out.println("5: Edit slang word");
        System.out.println("6: Delete 1 slang word");
        System.out.println("7: Reset all slang word");
        System.out.println("8: Random 1 slang word");
        System.out.println("9: Quiz: show 1 random slang word with 4 answers");
        System.out.println("10: Quiz: show 1 definition with 4 answers");
        System.out.println("------------------------------------------------");
    }

    public static void main(String[] args) throws IOException {
        Dictionary wordDict = new Dictionary();
        Scanner sc = new Scanner(System.in);
        int option;

        do {
            showMenuIndex();
            System.out.print("Input your option(0->10): ");
            option = sc.nextInt();  sc.nextLine();
            switch (option){
                case 0:
                    break;
                case 1:
                    /** Find by slang word */
                    System.out.print("Enter slangword: ");
                    String findword = sc.nextLine();
                    wordDict.find(findword);
                    break;
                case 2:
                    /** Find by definition */
                    System.out.print("Enter definition: ");
                    String findbydef = sc.nextLine();
                    wordDict.findByDef(findbydef);
                    break;
                case 3:
                    /** Show history */
                    wordDict.showHistory();
                    break;
                case 4:
                    /** Add slang word */
                    wordDict.add();
                    break;
                case 5:
                    /** Edit slang word */
                    wordDict.edit();
                    break;
                case 6:
                    /** Delete slang word */
                    wordDict.delete();
                    break;
                case 7:
                    /** Add slang word */
                    wordDict.reset();
                    break;
                case 8:
                    /** Random slang word */
                    System.out.println("Slang word on this day: ");
                    HashMap<String, String> randomWord = wordDict.randomWord(1, true);
                    for (String key : randomWord.keySet()){
                        System.out.println(key + " : " + randomWord.get(key));
                    }
                    break;
                case 9:
                    /** Quiz, show random slang word */
                    wordDict.generateQuiz("Quiz: What is definition of: ", true);
                    break;
                case 10:
                    /** Quiz, show random definition */
                    wordDict.generateQuiz("Quiz: What is slang word of: ", false);
                    break;
                default:
                    break;
            }
        } while (option != 0);

        wordDict.save();
    }
}
