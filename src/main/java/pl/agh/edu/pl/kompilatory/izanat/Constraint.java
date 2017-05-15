package pl.agh.edu.pl.kompilatory.izanat;

/**
 * Created by Izochora on 2017-05-15.
 */
public class Constraint {
    private String name;
    private int start;
    private int end;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Constraint(String name, int start, int end, int id) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.id = id;
    }
}
