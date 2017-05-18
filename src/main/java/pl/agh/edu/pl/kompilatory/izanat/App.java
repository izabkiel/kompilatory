package pl.agh.edu.pl.kompilatory.izanat;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import javax.swing.*;
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

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new GUI().displayGUI();
            }
        });
    }
}
