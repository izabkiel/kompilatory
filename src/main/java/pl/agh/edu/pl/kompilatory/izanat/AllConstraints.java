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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Izochora on 2017-05-15.
 */
public class AllConstraints {
    List<SpecificSubjectAllConstraints> constraints = new ArrayList<SpecificSubjectAllConstraints>();
    private int numberOfClouses = 0;
    private int variables = 0;

    public AllConstraints() {
    }


    public void printAllConstraint() {
        System.out.println("------------");
        for (SpecificSubjectAllConstraints s : constraints) {
            for (Constraint c : s.getConstraints()) {
                System.out.println(c.getName() + " " + c.getStart() + " " + c.getEnd() + " " + c.getId());
            }
        }
    }

    public void addContraintToStudent(String name, int start, int end, int id) {
        if (checkIfStudentExists(name)) {
            getSpecificStudentAllContraints(name).add(new Constraint(name, start, end, id));
        } else {
            SpecificSubjectAllConstraints s = new SpecificSubjectAllConstraints();
            s.addConstrait(new Constraint(name, start, end, id));
            constraints.add(s);
        }
    }

    private List<Constraint> getSpecificStudentAllContraints(String name) {
        for (SpecificSubjectAllConstraints s : constraints) {
            if (s.getConstraints().get(0).getName().equals(name)) {
                return s.getConstraints();
            }
        }
        return null;
    }

    private boolean checkIfStudentExists(String name) {
        for (SpecificSubjectAllConstraints s : constraints) {
            if (s.getConstraints().get(0).getName().equals(name))
                return true;
        }
        return false;
    }

    public boolean checkIfConstraintExists(String name, int start, int end) {
        for (SpecificSubjectAllConstraints s : constraints) {
            if (s.checkIfConstraintExist(name, start, end))
                return true;
        }
        return false;
    }

    public List<Constraint> solve() {
        makeCondition();
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        DimacsReader reader = new DimacsReader(solver);
        try {
            IProblem problem = reader.parseInstance("elo.cnf");
            if (problem.isSatisfiable()) {
                System.out.println(" Satisfiable !");
                String decodeMessage = reader.decode(problem.model());
                return getTrueConstraints(decodeMessage);

            } else {
                System.out.println(" Unsatisfiable !");
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
            for (SpecificSubjectAllConstraints specificSubjectAllConstraints : constraints) {
                if (specificSubjectAllConstraints.getConstraintById(i) != null) {
                    trueConstraints.add(specificSubjectAllConstraints.getConstraintById(i));
                }
            }
        }
        return trueConstraints;
    }

    private List<Integer> getTrueConditions(String decodeMessage) {
        List<Integer> trueConditions = new ArrayList<Integer>();
        System.out.println(decodeMessage);
        for (int i = 0; i < decodeMessage.length(); i++) {
            if (decodeMessage.charAt(i) == '-')
                i = i + 2;
            else {
                trueConditions.add(Integer.parseInt(decodeMessage.charAt(i) + ""));
                i++;
            }
        }
        return trueConditions;
    }

    private void makeCondition() {
        numberOfClouses = constraints.size();
        String c = "";
        for (SpecificSubjectAllConstraints s : constraints) {
            variables += s.getConstraints().size();
        }
        for (SpecificSubjectAllConstraints s : constraints) {
            c = c + specificSubjectCondition(s);
        }

        for (int i = 0; i < constraints.size(); i++) {
            for (int j = i + 1; j < constraints.size(); j++)
                c = c + checkOverlappingTime(constraints.get(i).getConstraints(), constraints.get(j).getConstraints());
        }
        writeToFile("p cnf " + variables + " " + numberOfClouses + "\n" + c);
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

    private String checkOverlappingTime(List<Constraint> a, List<Constraint> b) {
        String c = "";
        for (Constraint g : a) {
            for (Constraint m : b) {
                if (!(g.getEnd() <= m.getStart() || m.getEnd() <= g.getStart())) {
                    c = c + "-" + g.getId() + " -" + m.getId() + " 0 ";
                    numberOfClouses++;
                }
            }
        }
        return c;
    }

    private String specificSubjectCondition(SpecificSubjectAllConstraints s) {
        String c = "";
        for (Constraint constraint : s.getConstraints()) {
            c += constraint.getId() + " ";
        }
        return c + "0 ";
    }

    public String[][] getTableOfResults(List<Constraint> results) {
        if (results != null) {
            String[][] table = new String[results.size()][3];
            int i = 0;
            for (Constraint con : results) {
                table[i][0] = con.getName();
                table[i][1] = String.valueOf(con.getStart());
                table[i][2] = String.valueOf(con.getEnd());
                i++;
            }
            Arrays.sort(table, new Comparator<String[]>() {
                public int compare(String[] o1, String[] o2) {
                    return Integer.valueOf(o1[1]).compareTo(Integer.valueOf(o2[1]));
                }
            });
            return table;
        } else return null;
    }

}
