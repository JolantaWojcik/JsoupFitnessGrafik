package pl.jolantawojcik.grafikfitnessteam;

/**
 * Created by Jola on 25/08/2015.
 */
public class Record {

    private int id;
    private String classes, instructor, date, time;

    public Record(String classes, String instructor, String date, String time) {
        this.classes = classes;
        this.instructor = instructor;
        this.date = date;
        this.time = time;
    }

    public Record() { }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
