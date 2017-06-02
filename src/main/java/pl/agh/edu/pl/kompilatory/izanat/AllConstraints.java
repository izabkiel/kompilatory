package pl.agh.edu.pl.kompilatory.izanat;

import java.io.*;
import java.util.*;

/**
 * Created by Izochora on 2017-05-15.
 */
public class AllConstraints {
    private AllInstructorsConstraints allInstructorsConstraints = new AllInstructorsConstraints();
    private AllStudentsConstraints allStudentsConstraints = new AllStudentsConstraints();
    private List<Constraint> allConstraints = new LinkedList<Constraint>();
    private int variables = 0;
    private int numberOfClouses = 0;


    public void printAllConstraint() {
        System.out.println("------------");
        for (Constraint c : allConstraints) {
                System.out.println(c.getName() + " " + c.getStart() + " " + c.getEnd() + " " + c.getId());

        }
    }
    public AllInstructorsConstraints getAllInstructorsConstraints() {
        return allInstructorsConstraints;
    }

    public void setAllInstructorsConstraints(AllInstructorsConstraints allInstructorsConstraints) {
        this.allInstructorsConstraints = allInstructorsConstraints;
    }

    public AllStudentsConstraints getAllStudentsConstraints() {
        return allStudentsConstraints;
    }

    public void setAllStudentsConstraints(AllStudentsConstraints allStudentsConstraints) {
        this.allStudentsConstraints = allStudentsConstraints;
    }

    public List<Constraint> getAllConstraints() {
        return allConstraints;
    }

    public void setAllConstraints(List<Constraint> allConstraints) {
        this.allConstraints = allConstraints;
    }

    public void setAllConstraints(AllStudentsConstraints allStudentsConstraintsConstraints,AllInstructorsConstraints allInstructorsConstraints) {
        for(InstructorConstraints cc : allInstructorsConstraints.getInstructorConstraints()){
            for(Constraint c : cc.getConstraints()){
                allConstraints.add(c);
            }
        }

        for(SpecificStudentAllConstraints cc : allStudentsConstraints.getConstraints()){
            for(Constraint c : cc.getConstraints()){
                allConstraints.add(c);
            }
        }
    }

    public Constraint getConstraintByID(int id){
        for(Constraint c: allConstraints){
            if(c.getId()==id){
                return c;
            }
        }
        return null;
    }

    public void generateSAT(){
        System.out.println("Instructor constrains ");
        for(InstructorConstraints i : allInstructorsConstraints.getInstructorConstraints()){
            for(Constraint c : i.getConstraints()){
                System.out.println(c.getName()+ " "+c.getStart()+" "+c.getEnd()+" "+c.getId());
            }
        }
        allStudentsConstraints.makeConditionForStudents();
        variables = allStudentsConstraints.getVariables() + allInstructorsConstraints.getVariables();
        StringBuilder c = generateConditionForInstructors();
        numberOfClouses += allStudentsConstraints.getNumberOfClouses() + allInstructorsConstraints.getNumberOfClouses();
        writeToFile("p cnf " + variables + " " + numberOfClouses + "\n" + allStudentsConstraints.studentCNF +allInstructorsConstraints.c + c);
        allStudentsConstraints.setStudentCNF(new StringBuilder(""));
        numberOfClouses = 0;
    }

    private void writeToFile(String c) {
        try {
            PrintWriter writer = new PrintWriter("elo.cnf");
            writer.print(c);
            writer.close();
        } catch (IOException e) {
            System.out.print("nie zapisano do pliku");
        }
    }

    private StringBuilder generateConditionForInstructors(){
        StringBuilder condition = new StringBuilder("");
        for(InstructorConstraints c: allInstructorsConstraints.getInstructorConstraints()){
            condition.append(genarateConditionForInstructorsStudent(c.getConstraints().get(0).getName()));
        }
        return condition;
    }

    private StringBuilder genarateConditionForInstructorsStudent(String instructorName){
        StringBuilder condition = new StringBuilder("");
        List<Constraint> instructorConstraints = allInstructorsConstraints.getSpecificInstructorAllContraints(instructorName);
        List<SpecificStudentAllConstraints> instructorStudentsContraints = new LinkedList<SpecificStudentAllConstraints>();
        for(SpecificStudentAllConstraints s: allStudentsConstraints.getConstraints()){
            if(s.getInstructorName().equals(instructorName)){
                instructorStudentsContraints.add(s);
            }
        }

        for (int i = 0; i < instructorStudentsContraints.size(); i++) {
            for (int j = i + 1; j < instructorStudentsContraints.size(); j++)
                condition.append(checkOverlappingTime(instructorStudentsContraints.get(i).getConstraints(), instructorStudentsContraints.get(j).getConstraints()));
        }

        for(SpecificStudentAllConstraints s: instructorStudentsContraints){
             condition.append(checkIncludingTime(s.getConstraints(),instructorConstraints));
        }
        return condition;

    }

    private String checkIncludingTime(List<Constraint> studentContraints, List<Constraint> instructorConstraints) {
        String c = "";
        for (Constraint s : studentContraints) {
            boolean add = true;
            for (Constraint i : instructorConstraints) {
                if (!(s.getStart().before(i.getStart()) || s.getEnd().after(i.getEnd()))) {
                   add = false;
                }
            }
            if(add){
                c = c + "-" + s.getId() + " 0 ";
                numberOfClouses++;
            }
        }
        return c;
    }

    private String checkOverlappingTime(List<Constraint> a, List<Constraint> b) {
        String c = "";
        for (Constraint g : a) {
            for (Constraint m : b) {
                if (!(g.getEnd().before(m.getStart()) || g.getEnd().equals(m.getStart()) || m.getEnd().equals(g.getStart()) || m.getEnd().before(g.getStart()))) {
                    c = c + "-" + g.getId() + " -" + m.getId() + " 0 ";
                    numberOfClouses++;
                }
            }
        }
        return c;
    }
    public void addConstraintToStudent(String name, Date start, Date end, int id) {
        allStudentsConstraints.addContraintToStudent(name,start,end,id);
        allConstraints.add(new Constraint(name,start,end,id));
    }

    public void addConstraintToInstructor(String name, Date start, Date end, int id){
        allInstructorsConstraints.addContraintToInstructor(name,start,end,id);
        allConstraints.add(new Constraint(name,start,end,id));
    }

    public boolean checkIfConstraintExists(String name, Date start, Date end) {
        for(Constraint c:allConstraints){
            if(c.getName().equals(name) && c.getEnd() == end && c.getStart()==start){
                return true;
            }
        }
        return false;
    }

    public void setInstructorForStudent(String intructor, String student){
        allStudentsConstraints.getSpecificStudentByName(student).setInstructorName(intructor);
    }

    public String getInstructorForStudent(String name){
        return allStudentsConstraints.getSpecificStudentByName(name).getInstructorName();
    }
}
