package pl.agh.edu.pl.kompilatory.izanat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Izochora on 2017-05-15.
 */
public class SpecificStudentAllConstraints {
    private List<StudentConstraint> constraints = new ArrayList<>();


    public SpecificStudentAllConstraints() {
    }

    public List<StudentConstraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<StudentConstraint> constraints) {
        this.constraints = constraints;
    }

    public void addConstraintToStudent(StudentConstraint c){
        constraints.add(c);

    }
}
