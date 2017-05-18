package pl.agh.edu.pl.kompilatory.izanat;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Izochora on 2017-05-15.
 */
public class AllConstraints {
    List<SpecificSubjectAllConstraints> constraints = new ArrayList<SpecificSubjectAllConstraints>();
    private int numberOfClouses = 0;
    private int variables = 0;

    public List<SpecificSubjectAllConstraints> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<SpecificSubjectAllConstraints> constraints) {
        this.constraints = constraints;
    }

    public AllConstraints() {
/*        SpecificSubjectAllConstraints math = new SpecificSubjectAllConstraints();
        SpecificSubjectAllConstraints geo = new SpecificSubjectAllConstraints();
        SpecificSubjectAllConstraints physic = new SpecificSubjectAllConstraints();
        math.addConstrait(new Constraint("Matematyka",1,3,1));
        math.addConstrait(new Constraint("Matematyka",2,5,2));
        geo.addConstrait(new Constraint("Geo",1,3,3));
        geo.addConstrait(new Constraint("Geo",6,8,4));
        physic.addConstrait(new Constraint("Physic",5,8,5));
        physic.addConstrait(new Constraint("Physic",10,11,6));
        constraints.add(math);
        constraints.add(geo);
        constraints.add(physic);
        makeCondition();*/
    }

    public void printAllConstraint(){
        System.out.println("------------");
        for(SpecificSubjectAllConstraints s : constraints){
            for(Constraint c :s.getConstraints()){
                System.out.println(c.getName()+" "+c.getStart()+" "+c.getEnd()+" "+c.getId());
            }
        }
    }

    public void addContraintToStudent(String name, int start, int end, int id){
        if(checkIfStudentExists(name)){
            getSpecificStudentAllContraints(name).add(new Constraint(name, start, end, id));
        }
        else{
            SpecificSubjectAllConstraints s = new SpecificSubjectAllConstraints();
            s.addConstrait(new Constraint(name, start, end, id));
            constraints.add(s);
        }
    }

    public List<Constraint> getSpecificStudentAllContraints(String name){
        for (SpecificSubjectAllConstraints s : constraints){
                if(s.getConstraints().get(0).getName().equals(name)){
                    return s.getConstraints();
            }
        }
        return null;
    }
    public boolean checkIfStudentExists(String name){
        for (SpecificSubjectAllConstraints s : constraints){
            if(s.getConstraints().get(0).getName().equals(name))
                return true;
        }
        return false;
    }

    public List<Constraint> solve(){
        makeCondition();
        ISolver solver = SolverFactory.newDefault ();
        solver.setTimeout (3600); // 1 hour timeout
        DimacsReader reader = new DimacsReader( solver );
        try {
            IProblem problem = reader.parseInstance("elo.cnf");
            if ( problem.isSatisfiable()) {
                System.out.println(" Satisfiable !");
                String decodeMessage = reader.decode( problem.model());
  /*              List <Integer> trueConditions = getTrueConditions(decodeMessage);
                System.out.println("Warunki, które powinny być prawdziwe");
                for(int i: trueConditions){
                    System.out.print(i+" ");
                }*/
                return getTrueConstraints(decodeMessage);

            } else {
                System.out.println(" Unsatisfiable !");
            }
        } catch ( FileNotFoundException e) {
            System.out.print("File not fourd");
        } catch ( ParseFormatException e) {
            System.out.println("ParseFormatException");
            e.printStackTrace();
        } catch ( IOException e) {
            System.out.print("IOExceptopn");
        } catch ( ContradictionException e) {
            System.out.println(" Unsatisfiable ( trivial )!");
        } catch ( TimeoutException e) {
            System.out.println(" Timeout , sorry !");
        }
        return null;
    }

    private List<Constraint> getTrueConstraints(String decodeMessage){
        List<Integer> list = getTrueConditions(decodeMessage);
        List<Constraint> trueConstraints = new ArrayList<Constraint>();
        for(int i:list){
            for(SpecificSubjectAllConstraints specificSubjectAllConstraints : constraints){
                if(specificSubjectAllConstraints.getConstraintById(i)!=null){
                    trueConstraints.add(specificSubjectAllConstraints.getConstraintById(i));
                }
            }
        }
        return trueConstraints;
    }

    private List<Integer> getTrueConditions(String decodeMessage){
        List <Integer> trueConditions = new ArrayList<Integer>();
        System.out.println( decodeMessage);
        for(int i=0;i<decodeMessage.length();i++){
            if(decodeMessage.charAt(i)=='-')
                i=i+2;
            else{
                trueConditions.add(Integer.parseInt(decodeMessage.charAt(i)+""));
                i++;
            }
        }
        return trueConditions;
    }
    public void makeCondition(){
        numberOfClouses = constraints.size();
        String c ="";
        for(SpecificSubjectAllConstraints s: constraints){
            variables += s.getConstraints().size();
        }
        for(SpecificSubjectAllConstraints s :constraints){
            c = c + specificSubjectCondition(s);
        }

        for(int i = 0;i<constraints.size()-1;i++) {
            c = c + checkOverlappingTime(constraints.get(i).getConstraints(), constraints.get(i+1).getConstraints());
        }
        writeToFile("p cnf "+variables+" "+numberOfClouses+"\n"+c);
    }

    private void writeToFile(String c) {
        try{
            PrintWriter writer = new PrintWriter("elo.cnf");
            writer.print(c);
            writer.close();
        } catch (IOException e) {
            System.out.print("nie zapisano do pliku");
        }
    }

    private String checkOverlappingTime(List<Constraint> a, List<Constraint> b){
        String c ="";
        for(Constraint g: a){
            for(Constraint m: b){
                if(!(g.getEnd()<=m.getStart() || m.getEnd()<=g.getStart())){
                    c = c+"-"+g.getId()+" -"+m.getId()+" 0 ";
                    numberOfClouses++;
                }
            }
        }
        return c;
    }
    private String specificSubjectCondition(SpecificSubjectAllConstraints s) {
        String c = "";
        for(Constraint constraint :s.getConstraints()){
            c += constraint.getId()+ " ";
        }
        return c+"0 ";
    }

}
