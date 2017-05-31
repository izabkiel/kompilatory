package pl.agh.edu.pl.kompilatory.izanat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Izochora on 2017-05-15.
 */
public class SpecificStudentAllConstraints {
    private List<Constraint> constraints = new ArrayList<Constraint>();
    private String instructorName;


    public SpecificStudentAllConstraints() {
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String constructorName) {
        this.instructorName = constructorName;
    }
    public void addConstraintToStudent(Constraint c){
        constraints.add(c);

    }
}
