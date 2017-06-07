package pl.agh.edu.pl.kompilatory.izanat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by Izochora on 2017-06-02.
 */
public class CreateConstraintsFromFiles {


    private String instructorPath;
    private String studentPath;

    private AllConstraints allConstraints = new AllConstraints();

    public SolveSAT getSolveSAT() {
        return solveSAT;
    }

    private SolveSAT solveSAT = new SolveSAT();
    private int id = 0;

    public CreateConstraintsFromFiles(String instructorPath, String studentPath) {
        this.instructorPath = instructorPath;
        this.studentPath = studentPath;
    }

    public List<Constraint> solveSAT(){
        addInstructorsConstraints(instructorPath);
        addStudentsContraints(studentPath);

        allConstraints.generateSAT();
        solveSAT.setAllConstraints(allConstraints);
        return solveSAT.solve();

    }

    public void addInstructorsConstraints(String path){
        String csvFile = path;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";


        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss");

            while ((line = br.readLine()) != null) {
                String[] instructorConstraints = line.split(cvsSplitBy);
                String startString = instructorConstraints[1];
                LocalDateTime start = LocalDateTime.parse(startString, formatter);
                String endString = instructorConstraints[2];
                LocalDateTime end = LocalDateTime.parse(endString, formatter);
                if(end.isAfter(start)  && start.toLocalDate().equals(end.toLocalDate()))
                    allConstraints.addConstraintToInstructor(instructorConstraints[0],start, end,++id);
                else{
                    System.out.println("Nie dodano ograniczenia "+instructorConstraints[0]+" "+start+" "+end+" "+id+" ");
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss");

            while ((line = br.readLine()) != null) {
                String[] instructorConstraints = line.split(cvsSplitBy);
                String startString = instructorConstraints[1];
                LocalDateTime start = LocalDateTime.parse(startString, formatter);
                String endString = instructorConstraints[2];
                LocalDateTime end = LocalDateTime.parse(endString, formatter);
                if(end.isAfter(start) && allConstraints.checkIfInstructorExists(instructorConstraints[3]) && start.toLocalDate().equals(end.toLocalDate()))
                    allConstraints.addConstraintToStudent(instructorConstraints[0],start, end,++id,instructorConstraints[3]);
                else{
                    System.out.println("Nie dodano ograniczenia "+instructorConstraints[0]+" "+start+" "+end+" "+id+" "+instructorConstraints[3] );
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
