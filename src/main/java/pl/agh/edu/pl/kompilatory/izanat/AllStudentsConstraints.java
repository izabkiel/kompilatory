package pl.agh.edu.pl.kompilatory.izanat;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Izochora on 2017-05-25.
 */
public class AllStudentsConstraints {
    List<SpecificStudentAllConstraints> constraints = new ArrayList<SpecificStudentAllConstraints>();
    private int numberOfClouses = 0;
    private int variables = 0;
    StringBuilder studentCNF;

    public AllStudentsConstraints() {
        studentCNF = new StringBuilder("");
    }

    public StringBuilder getStudentCNF() {
        return studentCNF;
    }

    public void setStudentCNF(StringBuilder studentCNF) {
        this.studentCNF = studentCNF;
    }

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

    public List<SpecificStudentAllConstraints> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<SpecificStudentAllConstraints> constraints) {
        this.constraints = constraints;
    }

    public void makeConditionForStudents() {
        variables = 0;
        numberOfClouses = constraints.size();
        for (SpecificStudentAllConstraints s : constraints) {
            variables += s.getConstraints().size();
        }
        for (SpecificStudentAllConstraints s : constraints) {
            specificStudentCondition(s);
        }

/*        for (int i = 0; i < constraints.size(); i++) {
            for (int j = i + 1; j < constraints.size(); j++)
                studentCNF.append(checkOverlappingTime(constraints.get(i).getConstraints(), constraints.get(j).getConstraints()));
        }*/
    }


    private void specificStudentCondition(SpecificStudentAllConstraints s) {
        for (Constraint constraint : s.getConstraints()) {
            studentCNF.append(constraint.getId() + " ");
        }
        studentCNF.append("0 ");
    }

    public void addContraintToStudent(String name, Date start, Date end, int id) {
        if (checkIfStudentExists(name)) {
            getSpecificStudentAllContraints(name).add(new Constraint(name, start, end, id));
        } else {
            SpecificStudentAllConstraints s = new SpecificStudentAllConstraints();
            s.addConstraintToStudent(new Constraint(name, start, end, id));
            constraints.add(s);
        }
    }

    private boolean checkIfStudentExists(String name) {
        for (SpecificStudentAllConstraints s : constraints) {
            if (s.getConstraints().get(0).getName().equals(name))
                return true;
        }
        return false;
    }

    public List<Constraint> getSpecificStudentAllContraints(String name) {
        for (SpecificStudentAllConstraints s : constraints) {
            if (s.getConstraints().get(0).getName().equals(name)) {
                return s.getConstraints();
            }
        }
        return null;
    }

    public SpecificStudentAllConstraints getSpecificStudentByName(String name){
        for (SpecificStudentAllConstraints s : constraints) {
            if (s.getConstraints().get(0).getName().equals(name)) {
                return s;
            }
        }
        return null;
    }
}
