package pl.agh.edu.pl.kompilatory.izanat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Izochora on 2017-06-02.
 */
public class CreateConstraintsFromFiles {


    private AllConstraints allConstraints = new AllConstraints();
    private SolveSAT solveSAT = new SolveSAT();
    private int id = 0;

    public void solveSAT(){
        addInstructorsConstraints("instruktorzy.csv");
        addStudentsContraints("students.csv");
        allConstraints.generateSAT();
        solveSAT.setAllConstraints(allConstraints);
        solveSAT.solve();

    }

    public void addInstructorsConstraints(String path){
        String csvFile = path;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";




        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while ((line = br.readLine()) != null) {
                String[] instructorConstraints = line.split(cvsSplitBy);
                String startString = instructorConstraints[1];
                Date start = format.parse(startString);
                String endString = instructorConstraints[2];
                Date end = format.parse(endString);
                allConstraints.addConstraintToInstructor(instructorConstraints[0],start, end,++id);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addStudentsContraints(String path){
        String csvFile = path;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while ((line = br.readLine()) != null) {
                String[] instructorConstraints = line.split(cvsSplitBy);
                String startString = instructorConstraints[1];
                Date start = format.parse(startString);
                String endString = instructorConstraints[2];
                Date end = format.parse(endString);
                allConstraints.addConstraintToStudent(instructorConstraints[0],start, end,++id);
                allConstraints.setInstructorForStudent(instructorConstraints[3],instructorConstraints[0]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
