package pl.agh.edu.pl.kompilatory.izanat;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Izochora on 2017-05-18.
 */
public class GUI {
    private JFrame frame;
    private JButton addNewStudentButton;
    private static int count;
    private String [] hours = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

    private AllConstraints allConstraints = new AllConstraints();
    public GUI()
    {
        count = 1;
    }

    public void displayGUI() {
        frame = new JFrame("JFrame Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        addNewStudentButton = new JButton("Add Student for his constraints");
        GridLayout layoutForConstraints = new GridLayout(0, 1, 2, 2);
        final JPanel constraintsPanel = new JPanel(layoutForConstraints);
        JButton generateSchedule = new JButton("Generate schedule");
        addNewStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                final JPanel studentConstraintPanel = new JPanel(new BorderLayout());
                JButton addConstraintToStudent = new JButton("Add Constraint to student");
                final JTextField studentNameField = new JTextField();
                studentNameField.setName("user" + count);
                studentNameField.setPreferredSize(new Dimension(100,20));
                JPanel fieldsForConstraints = new JPanel(new FlowLayout());
                fieldsForConstraints.add(new JLabel("Student name"));
                fieldsForConstraints.add(studentNameField);
                fieldsForConstraints.add(addConstraintToStudent);
                studentConstraintPanel.add(fieldsForConstraints,BorderLayout.NORTH);
               final JPanel concreteStudentConstraintsPanel = new JPanel(new GridLayout(0, 1, 2, 2));
                addConstraintToStudent.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        final String studentName  = studentNameField.getText();
                        if(studentName.equals("")){
                            JOptionPane.showMessageDialog(null, "Please enter the student name");
                        }
                        else {
                            JPanel lessonTimeForStudentPanel = new JPanel(new FlowLayout());
                            final JComboBox startLesson = new JComboBox(hours);
                            final JComboBox endLesson = new JComboBox(hours);
                            JButton addConstraintButton = new JButton("Add constraint");
                            lessonTimeForStudentPanel.add(new Label("Add time constraint for "+studentName));
                            lessonTimeForStudentPanel.add(new Label("Start of lesson"));
                            lessonTimeForStudentPanel.add(startLesson);
                            lessonTimeForStudentPanel.add(new Label("End of lesson"));
                            lessonTimeForStudentPanel.add(endLesson);
                            lessonTimeForStudentPanel.add(addConstraintButton);
                            addConstraintButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    allConstraints.addContraintToStudent(studentNameField.getText(),Integer.parseInt(startLesson.getSelectedItem().toString()),Integer.parseInt(endLesson.getSelectedItem().toString()),count);
                                    count++;
                                }
                            });
                            concreteStudentConstraintsPanel.add(lessonTimeForStudentPanel);
                            studentConstraintPanel.add(concreteStudentConstraintsPanel, BorderLayout.CENTER);
                            frame.validate();
                            frame.repaint();
                        }
                    }
                });
                constraintsPanel.add(studentConstraintPanel);

                frame.validate();
                frame.repaint();
            }
        });

        generateSchedule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ea) {
                allConstraints.printAllConstraint();
                List<Constraint> trueConstraint = allConstraints.solve();
                JFrame result = new JFrame("Schedule");
                result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                result.setLayout(new GridLayout(0, 1, 2, 2));
                if(trueConstraint!=null){
                    for(Constraint c:trueConstraint){
                        result.add(new Label(c.getName()+" "+c.getStart()+" "+c.getEnd()));
                    }
                }
                else
                    result.add(new Label(" Unsatisfiable"));
                result.setLocationByPlatform(true);
                result.setVisible(true);
            }
        });
        frame.add(generateSchedule,BorderLayout.SOUTH);
        frame.add(addNewStudentButton,BorderLayout.NORTH);
        frame.add(constraintsPanel,BorderLayout.CENTER);
        frame.setSize(new Dimension(1000,500));
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
