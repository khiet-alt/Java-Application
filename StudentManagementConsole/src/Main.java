import java.io.IOException;

public class Main {
    public static final String rootDir = System.getProperty("user.dir") + "\\src\\data\\studentList.bin";

    public static void main(String[] args) throws IOException {
        Manager manager = new Manager(rootDir);
        manager.menuDriver();
    }
}
