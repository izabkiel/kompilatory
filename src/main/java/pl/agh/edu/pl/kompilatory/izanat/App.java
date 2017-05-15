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
import java.io.Reader;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        AllConstraints all = new AllConstraints();
        ISolver solver = SolverFactory.newDefault ();
        solver.setTimeout (3600); // 1 hour timeout
        DimacsReader reader = new DimacsReader( solver );
        try {
            IProblem problem = reader.parseInstance("elo.cnf");
            if ( problem.isSatisfiable()) {
                System.out.println(" Satisfiable !");
                System.out.println( reader.decode( problem.model()));
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
    }
}
