package pl.agh.edu.pl.kompilatory.izanat;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Izochora on 2017-05-23.
 */
public class AllInstructorsConstraints {
    List<InstructorConstraints> instructorConstraints = new LinkedList<InstructorConstraints>();
    private int numberOfClouses = 0;
    private int variables = 0;
    StringBuilder c = new StringBuilder("");

    public int getNumberOfClouses() {
        return numberOfClouses;
    }

    public void setNumberOfClouses(int numberOfClouses) {
        this.numberOfClouses = numberOfClouses;
    }

    public int getVariables() {
        return variables;
    }

    public void setVariables(int variables) {
        this.variables = variables;
    }

    public List<InstructorConstraints> getInstructorConstraints() {
        return instructorConstraints;
    }

    public void setInstructorConstraints(List<InstructorConstraints> instructorConstraints) {
        this.instructorConstraints = instructorConstraints;
    }

    public void addContraintToInstructor(String name, int start, int end, int id){
        if (checkIfInstructorExists(name)) {
            getSpecificInstructorAllContraints(name).add(new Constraint(name, start, end, id));
        } else {
            InstructorConstraints s = new InstructorConstraints();
            s.addConstraitToInstructor(new Constraint(name, start, end, id));
            instructorConstraints.add(s);
        }
        variables++;
    }

    private boolean checkIfInstructorExists(String name) {
        for (InstructorConstraints s : instructorConstraints) {
            if (s.getConstraints().get(0).getName().equals(name))
                return true;
        }
        return false;
    }

    public List<Constraint> getSpecificInstructorAllContraints(String name) {
        for (InstructorConstraints s : instructorConstraints) {
            if (s.getConstraints().get(0).getName().equals(name)) {
                return s.getConstraints();
            }
        }
        return null;
    }
}
