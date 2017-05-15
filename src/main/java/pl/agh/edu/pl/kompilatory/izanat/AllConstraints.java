package pl.agh.edu.pl.kompilatory.izanat;

import javax.print.DocFlavor;
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

    public AllConstraints() {
        SpecificSubjectAllConstraints math = new SpecificSubjectAllConstraints();
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
        makeCondition();
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
