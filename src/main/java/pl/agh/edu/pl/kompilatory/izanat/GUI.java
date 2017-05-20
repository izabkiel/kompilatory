package pl.agh.edu.pl.kompilatory.izanat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Izochora on 2017-05-18.
 */
public class GUI {
    private JFrame frame;
    private JButton addNewStudentButton;
    private static int count;
    private String [] hours = {"8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","1","2","3","4","5","6","7"};

    private AllConstraints allConstraints = new AllConstraints();
    public GUI()
    {
        count = 1;
    }

    public void displayGUI() {
        frame = new JFrame("Schedule generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        addNewStudentButton = new JButton("Add new Student and his preferences");
        GridLayout layoutForConstraints = new GridLayout(0, 1, 2, 2);
        final JPanel constraintsPanel = new JPanel(layoutForConstraints);
        JButton generateSchedule = new JButton("Generate schedule");
        addNewStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                final JPanel studentConstraintPanel = new JPanel(new BorderLayout());
                JButton addConstraintToStudent = new JButton("Add preferences for student");
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
                            JButton addConstraintButton = new JButton("Add");
                            lessonTimeForStudentPanel.add(new Label("Add prefered hours for "+studentName));
                            lessonTimeForStudentPanel.add(new Label("Start of lesson"));
                            lessonTimeForStudentPanel.add(startLesson);
                            lessonTimeForStudentPanel.add(new Label("End of lesson"));
                            lessonTimeForStudentPanel.add(endLesson);
                            lessonTimeForStudentPanel.add(addConstraintButton);
                            addConstraintButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    int start = Integer.parseInt(startLesson.getSelectedItem().toString());
                                    int end = Integer.parseInt(endLesson.getSelectedItem().toString());
                                    if(end<=start){
                                        JOptionPane.showMessageDialog(null, "Lesson cannot end before it's start");
                                    }
                                    else {
                                        if(!allConstraints.checkIfConstraintExists(studentNameField.getText(),start,end)) {
                                            allConstraints.addContraintToStudent(studentNameField.getText(), start, end, count);
                                            count++;
                                        }
                                    }
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
                String[][] trueConstraint = allConstraints.getTableOfResults(allConstraints.solve());
                JFrame resultWindow = new JFrame("Schedule");
                resultWindow.setLayout(new GridLayout(0, 1, 2, 2));
                //resultWindow.setSize(100,100);

                if(trueConstraint!=null){
                    String[] columnNames = {"Students name", "Start of the lesson", "end of the lesson"};
                    JTable table = new JTable(trueConstraint, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);
                    resultWindow.add(scrollPane);
                }
                else
                    resultWindow.add(new Label(" Unsatisfiable"));
                resultWindow.pack();

                resultWindow.setLocationByPlatform(true);
                resultWindow.setVisible(true);
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
