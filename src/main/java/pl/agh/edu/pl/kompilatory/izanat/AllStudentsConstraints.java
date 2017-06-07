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
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        Collections.sort(s.getConstraints(), new Comparator<StudentConstraint>() {
            @Override
            public int compare(StudentConstraint o1, StudentConstraint o2) {

                if (o1.getStart().isBefore(o2.getStart()))
                    return -1;
                else if (o1.getStart().isAfter(o2.getStart()))
                    return 1;
                else
                    return o1.getInstructorName().compareTo(o2.getInstructorName());
            }
        });
        for (int i = 0; i < s.getConstraints().size() - 1; i++) {
            LocalDate first = s.getConstraints().get(i).getStart().toLocalDate();
            LocalDate second = s.getConstraints().get(i + 1).getStart().toLocalDate();
            studentCNF.append(s.getConstraints().get(i).getId() + " ");
            if (!first.equals(second)) {
                studentCNF.append("0 ");
                numberOfClouses++;
            }

        }
        studentCNF.append(s.getConstraints().get(s.getConstraints().size() - 1).getId() + " 0 ");
/*        for (Constraint constraint : s.getConstraints()) {
            studentCNF.append(constraint.getId() + " ");
        }
        studentCNF.append("0 ");
        System.out.println("porownanie mistrza " + studentCNF + " " + string);*/
    }

    public void addContraintToStudent(String name, LocalDateTime start, LocalDateTime end, int id, String instructorName) {
        if (checkIfStudentExists(name)) {
            getSpecificStudentAllContraints(name).add(new StudentConstraint(name, start, end, id, instructorName));
        } else {
            SpecificStudentAllConstraints s = new SpecificStudentAllConstraints();
            s.addConstraintToStudent(new StudentConstraint(name, start, end, id,instructorName));
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

    public List<StudentConstraint> getSpecificStudentAllContraints(String name) {
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
