package pl.agh.edu.pl.kompilatory.izanat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Izochora on 2017-05-15.
 */
public class Constraint {
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Constraint(String name, LocalDateTime start, LocalDateTime end, int id) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.id = id;
    }

}

