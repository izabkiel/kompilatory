package pl.agh.edu.pl.kompilatory.izanat;

import java.util.Date;

/**
 * Created by Izochora on 2017-05-15.
 */
public class Constraint {
    private String name;
    private Date start;
    private Date end;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Constraint(String name, Date start, Date end, int id) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.id = id;
    }

}

