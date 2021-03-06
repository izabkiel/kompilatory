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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Izochora on 2017-05-25.
 */
public class SolveSAT {
    AllConstraints allConstraints;

    SolveSAT() {

    }

    public AllConstraints getAllConstraints() {
        return allConstraints;
    }



    public void setAllConstraints(AllConstraints allConstraints) {
        this.allConstraints = allConstraints;
    }


    public List<Constraint> solve() {
        //makeCondition();
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        DimacsReader reader = new DimacsReader(solver);
        try {
            IProblem problem = reader.parseInstance("elo.cnf");
            if (problem.isSatisfiable()) {
                String decodeMessage = reader.decode(problem.model());
                return getTrueConstraints(decodeMessage);

            }
        } catch (FileNotFoundException e) {
            System.out.print("File not fourd");
        } catch (ParseFormatException e) {
            System.out.println("ParseFormatException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.print("IOExceptopn");
        } catch (ContradictionException e) {
            System.out.println(" Unsatisfiable ( trivial )!");
        } catch (TimeoutException e) {
            System.out.println(" Timeout , sorry !");
        }
        return null;
    }

    private List<Constraint> getTrueConstraints(String decodeMessage) {
        List<Integer> list = getTrueConditions(decodeMessage);
        List<Constraint> trueConstraints = new ArrayList<Constraint>();
        for (int i : list) {
            if (allConstraints.getConstraintByID(i) != null) {
                trueConstraints.add(allConstraints.getConstraintByID(i));
            }
        }
        return trueConstraints;
    }

    private List<Integer> getTrueConditions(String decodeMessage) {
        List<Integer> trueConditions = new ArrayList<Integer>();
        String [] devided = decodeMessage.split(" ");
        for(String s : devided){
            if(s.charAt(0)!='-' && s.charAt(0)!='0'){
                trueConditions.add(Integer.parseInt(s));
            }
        }
        return trueConditions;
    }

    public String[][] getTableOfResults(List<Constraint> results) {
        if (results != null) {
            String[][] table = new String[results.size()][4];
            int i = 0;
            for (Constraint con : results) {
                table[i][0] = con.getName();
                table[i][1] = String.valueOf(con.getStart());
                table[i][2] = String.valueOf(con.getEnd());
                table[i][3] = String.valueOf(allConstraints.getInstructorForConstraint(con.getId()));
                i++;
            }
            Arrays.sort(table, new Comparator<String[]>() {
                public int compare(String[] o1, String[] o2) {
                    int comp = o1[3].compareTo(o2[3]);
                    if (comp != 0) return comp;
                    else{
                        LocalDateTime o1Date = LocalDateTime.parse(o1[1]);
                        LocalDateTime o2Date = LocalDateTime.parse(o2[1]);
                        return o1Date.compareTo(o2Date);
                    }

                }
            });
            return table;
        } else return null;
    }
}
