package pl.agh.edu.pl.kompilatory.izanat;

import java.time.LocalDateTime;

/**
 * Created by Izochora on 2017-06-07.
 */
public class StudentConstraint extends Constraint {
    private String instructorName;

    public StudentConstraint(String name, LocalDateTime start, LocalDateTime end, int id, String instructorName) {
        super(name, start, end, id);
        this.instructorName = instructorName;
    }

    public StudentConstraint(String name, LocalDateTime start, LocalDateTime end, int id) {
        super(name, start, end, id);
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
}
