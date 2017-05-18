package pl.agh.edu.pl.kompilatory.izanat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Izochora on 2017-05-15.
 */
public class SpecificSubjectAllConstraints {
    private List<Constraint> constraints = new ArrayList<Constraint>();

    public SpecificSubjectAllConstraints() {
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public void addConstrait(Constraint c){
        constraints.add(c);
    }

    public Constraint getConstraintById(int id){
        for(Constraint c:constraints){
            if(c.getId()==id){
                return c;
            }
        }
        return null;
    }
}
