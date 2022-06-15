import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

public class Student {
    public int id;
    public String name;
    public double score;
    public String image;
    public String address;
    public String note;

    public Student(int id, String name, double score, String image, String address, String note) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.image = image;
        this.address = address;
        this.note = note;
    }

    public String joinString() {
        return id + "-" + name + "-" + score + "-" + image + "-" + address + "-" + note + "\n";
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

}

class Sortbyid implements Comparator<Student>{
    public int compare(Student a, Student b){
        return a.id - b.id;
    }
}

class Sortbyscore implements Comparator<Student>{
    public int compare(Student a, Student b){
        return Double.compare(a.score, b.score);
    }
}