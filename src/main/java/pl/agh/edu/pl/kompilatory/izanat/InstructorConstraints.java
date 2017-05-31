package pl.agh.edu.pl.kompilatory.izanat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Izochora on 2017-05-23.
 */
public class InstructorConstraints {
    private List<Constraint> constraints = new ArrayList<Constraint>();

    public InstructorConstraints() {
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public void addConstraitToInstructor(Constraint c){
        constraints.add(c);
    }

}
