import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Manager {
    List<Student> studentList;
    public static final String rootDir = System.getProperty("user.dir") + "\\src\\data\\studentList.bin";

    public Manager(String filename) throws IOException {
        /** This function will read data of each student from studentList.bin file and store in studentList variable insdie this object */
        studentList = new ArrayList<Student>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String row;

            while(bufferedReader.ready()){
                row = bufferedReader.readLine();
                String[] arrOfStr = row.split("-");
                studentList.add(new Student(Integer.parseInt(arrOfStr[0]), arrOfStr[1], Double.parseDouble(arrOfStr[2]), arrOfStr[3], arrOfStr[4], arrOfStr[5]));
            }
        } catch (FileNotFoundException ex){
            return;
        }
    }

    public void listAllStudent(){
        System.out.println("-------------------");
        if (studentList.size() == 0)
            System.out.println("Empty student list !!!");
        int index = 0;
        for (Student stu: studentList){
            System.out.print(index + ": ");
            System.out.println(stu.toString());
            index += 1;
        }
        System.out.println("-------------------");
    }

    public void update(int flag, boolean remove){
        /** If flag=-1 --> add new student, if flag != 1, we will update student at index flag (if remove==true, we will delete student at index flag) */
        /** Add one student into list */
        if (flag >= studentList.size())
            flag = -1;
        if (remove == true){
            studentList.remove(flag);
            writeToFile();
            return;
        }
        Scanner sc = new Scanner(System.in);
        int id; double score; String name, image, address, note;

        /** Enter information of student from user input*/
        System.out.print("Enter id: ");
        id = sc.nextInt();  sc.nextLine();
        System.out.print("Enter name: ");
        name = sc.nextLine();
        System.out.print("Enter score: ");
        score = sc.nextDouble();    sc.nextLine();
        System.out.print("Enter image: ");
        image = sc.nextLine();
        System.out.print("Enter address: ");
        address = sc.nextLine();
        System.out.print("Enter note: ");
        note = sc.nextLine();

        Student newStudent = new Student(id, name, score, image, address, note);
        if (flag == -1)
            studentList.add(newStudent);
        else {
            studentList.set(flag, newStudent);
        }

        writeToFile();
    }

    public void writeToFile(){
        /** Write all information of each student into binary file */
        try (
                FileOutputStream outputStream = new FileOutputStream(rootDir);
        ) {
            for (Student stu: studentList){
                byte b[] = stu.joinString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(b);
            }
            System.out.println("Update student to file successfully");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void showMenuIndex(){
        System.out.println("Press 0 to exit");
        System.out.println("Press 1 to add new student");
        System.out.println("Press 2 to update existed student (at given index)");
        System.out.println("Press 3 to remove student (at given index)");
        System.out.println("Press 4 to view list of student");
        System.out.println("Press 5 to export to text file");
    }

    public void menuDriver() throws IOException {
        Scanner sc = new Scanner(System.in);
        int option;

        do {
            showMenuIndex();
            System.out.print("Input your option(0->5): ");
            option = sc.nextInt();
            switch (option){
                case 0:
                    break;
                case 1:
                    /** Add new student */
                    update(-1, false);
                    break;
                case 2:
                    /** Update student */
                    listAllStudent();
                    System.out.print("Enter index of student that need to updated: ");
                    int index = sc.nextInt();
                    update(index, false);
                    break;
                case 3:
                    /** Remove student */
                    listAllStudent();
                    System.out.print("Enter index of student that need to removed: ");
                    int index_remove = sc.nextInt();
                    update(index_remove, true);
                    break;
                case 4:
                    /** Display list of student */
                    System.out.print("Press 1: sort by ma hoc sinh, Press 2: sort by Diem: ");
                    int sortBy = sc.nextInt();
                    System.out.print("Press 1: Ascending order, Press 2: Descending order: ");
                    int orderBy = sc.nextInt();

                    if (sortBy == 1)
                        Collections.sort(studentList, new Sortbyid());
                    else
                        Collections.sort(studentList, new Sortbyscore());
                    if (orderBy == 2)
                        Collections.reverse(studentList);

                    listAllStudent();
                    break;
                case 5:
                    /** Export student list into text file */
                    CsvWriterSimple csvWriter = new CsvWriterSimple(studentList);
                    csvWriter.convert(studentList);
                    System.out.println("Write to csv file successfully");
                    break;
                default:
                    break;
            }
        } while (option != 0);
    }
}
