package pl.agh.edu.pl.kompilatory.izanat;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Created by Izochora on 2017-05-18.
 */
public class GUI {
    private JFrame frame;
    private static int count;
    // private String[] hours = {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "1", "2", "3", "4", "5", "6", "7"};
    private LinkedList<String> instructorsNames = new LinkedList<String>();
    private AllConstraints allConstraints = new AllConstraints();
    private SolveSAT solveSAT = new SolveSAT();
    private final JPanel studentPanel = new JPanel(new BorderLayout());
    private final JPanel instructorPanel = new JPanel(new BorderLayout());
    private final JPanel readFromFilePanel = new JPanel(new GridLayout(2,1));
    private String instructorPath = "";
    private String studentPath = "";
    private final JTabbedPane mainTabs = new JTabbedPane();

    public GUI() {
        count = 1;
    }

    public void displayGUI() {
        frame = new JFrame("Schedule generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);


        JTabbedPane tabbedPane = new JTabbedPane();

        mainTabs.addTab("Load constrains from file", readFromFilePanel);
        mainTabs.addTab("Add constrains manually", tabbedPane);


        ImageIcon icon = createImageIcon("images/middle.gif");

        studentConstraintsDisplay();
        instructorConstraintDisplay();
        fileTabDisplay();


        tabbedPane.addTab("Set instructor constraints", icon, instructorPanel);
        tabbedPane.addTab("Set student constraints", icon, studentPanel);

        frame.add(mainTabs, BorderLayout.NORTH);
        frame.setSize(new Dimension(1000, 500));
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

    }

    private void fileTabDisplay() {

        JPanel addFilesPanel = new JPanel();
        JPanel generateSchPanel = new JPanel();
        final JFileChooser fcI = new JFileChooser();
        final JButton getConstraintsFromFileButton = new JButton("Load file with instructor constraints");
        Label studentLabel =new Label("no file loaded");
        Label instructorLabel =new Label("no file loaded");
        addFilesPanel.add(getConstraintsFromFileButton);
        FileNameExtensionFilter filter3 = new FileNameExtensionFilter("csv", "csv");
        fcI.setFileFilter(filter3);
        getConstraintsFromFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == getConstraintsFromFileButton) {
                    int returnVal = fcI.showOpenDialog(readFromFilePanel);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fcI.getSelectedFile();
                        instructorPath = file.getAbsolutePath();
                        instructorLabel.setText(file.getName());
                    }
                }
            }
        });

        final JButton getStudentConstraintsFromFileButton = new JButton("Load file with student constraints");
        final JFileChooser fcS = new JFileChooser();
        fcS.setFileFilter(filter3);
        getStudentConstraintsFromFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == getStudentConstraintsFromFileButton) {
                    int returnVal = fcS.showOpenDialog(readFromFilePanel);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fcS.getSelectedFile();
                        studentPath = file.getAbsolutePath();
                        studentLabel.setText(file.getName());
                    }
                }
            }
        });

        JPanel studentChooserPanel = new JPanel(new GridLayout(2,1));
        studentChooserPanel.add(getStudentConstraintsFromFileButton);

        studentChooserPanel.add(studentLabel);
        addFilesPanel.add(studentChooserPanel);
        readFromFilePanel.add(addFilesPanel);
        JPanel instructorChooserPanel = new JPanel(new GridLayout(2,1));
        instructorChooserPanel.add(getConstraintsFromFileButton);

        instructorChooserPanel.add(instructorLabel);
        addFilesPanel.add(instructorChooserPanel);

        JButton generateSchedule = new JButton("Generate schedule");
        generateSchPanel.add(generateSchedule);
        readFromFilePanel.add(generateSchPanel);

        generateSchedule.addActionListener(e -> {
            CreateConstraintsFromFiles createConstraintsFromFiles = new CreateConstraintsFromFiles(instructorPath, studentPath);
            List<Constraint> solution = createConstraintsFromFiles.solveSAT();
            SolveSAT solveSAT = createConstraintsFromFiles.getSolveSAT();
            generatingSchedule(solution, solveSAT);
        });


    }


    private void instructorConstraintDisplay() {
        JButton addNewInstructorButton = new JButton("Add new instructor and his preferences");
        JPanel addNewInstructorPanel = new JPanel();
        addNewInstructorPanel.add(addNewInstructorButton);
        GridLayout layoutForConstraints = new GridLayout(0, 1, 2, 2);
        final JPanel constraintsPanel = new JPanel(layoutForConstraints);

        addNewInstructorButton.addActionListener(e -> {
            final JPanel instructorConstraintPanel = new JPanel(new BorderLayout());
            JButton addConstraintToInstructor = new JButton("Add working hours for instructor");
            final JTextField instructorNameField = new JTextField();
            instructorNameField.setName("instructor" + count);
            instructorNameField.setPreferredSize(new Dimension(100, 20));
            JPanel fieldsForConstraints = new JPanel(new FlowLayout());
            fieldsForConstraints.add(new JLabel("Instructor's name"));
            fieldsForConstraints.add(instructorNameField);
            fieldsForConstraints.add(addConstraintToInstructor);
            instructorConstraintPanel.add(fieldsForConstraints, BorderLayout.NORTH);
            final JPanel concreteInstructorConstraintsPanel = new JPanel(new GridLayout(0, 1, 2, 2));
            addConstraintToInstructor.addActionListener(e1 -> {
                final String instructorName = instructorNameField.getText();
                if (instructorName.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter the instructor's name");
                } else {
                    JPanel lessonTimeForStudentPanel = new JPanel(new FlowLayout());
                    final JTextField startLesson = new JTextField();
                    final JTextField endLesson = new JTextField();
                    startLesson.setPreferredSize(new Dimension(150, 20));
                    endLesson.setPreferredSize(new Dimension(150, 20));
                    final JButton addConstraintButton = new JButton("Add");
                    lessonTimeForStudentPanel.add(new Label("Add work hours for " + instructorName));
                    lessonTimeForStudentPanel.add(new Label("Start at"));
                    lessonTimeForStudentPanel.add(startLesson);
                    lessonTimeForStudentPanel.add(new Label("End at"));
                    lessonTimeForStudentPanel.add(endLesson);
                    lessonTimeForStudentPanel.add(addConstraintButton);
                    if (!instructorsNames.contains(instructorNameField.getText().toString())) {
                        instructorsNames.add(instructorNameField.getText().toString());
                    }
                    addConstraintButton.addActionListener(e11 -> {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String startString = startLesson.getText();
                        LocalDateTime start = LocalDateTime.parse(startString, formatter);
                        String endString = endLesson.getText();
                        LocalDateTime end = LocalDateTime.parse(endString, formatter);
                        if (end.isBefore(start)) {
                            JOptionPane.showMessageDialog(null, "Lesson cannot end before it's start");
                        } else {
                            if (!allConstraints.checkIfConstraintExists(instructorNameField.getText(), start, end)) {
                                allConstraints.addConstraintToInstructor(instructorNameField.getText(), start, end, count);
                                //allConstraints.setInstructorForStudent("paweł",studentNameField.getText());
                                count++;
                                addConstraintButton.setEnabled(false);
                            }
                        }
                    });
                    concreteInstructorConstraintsPanel.add(lessonTimeForStudentPanel);
                    instructorConstraintPanel.add(concreteInstructorConstraintsPanel, BorderLayout.CENTER);
                    frame.validate();
                    frame.repaint();
                }
            });
            constraintsPanel.add(instructorConstraintPanel);
            instructorPanel.add(constraintsPanel);
            frame.validate();
            frame.repaint();
        });

        instructorPanel.add(addNewInstructorPanel, BorderLayout.NORTH);
    }

    private void studentConstraintsDisplay() {
        GridLayout layoutForConstraints = new GridLayout(0, 1, 2, 2);
        final JPanel constraintsPanel = new JPanel(layoutForConstraints);
        JButton addNewStudentButton = new JButton("Add new Student and his preferences");
        JButton generateSchedule = new JButton("Generate schedule");
        studentPanel.setVisible(true);


        addNewStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                final JPanel studentConstraintPanel = new JPanel(new BorderLayout());
                JButton addConstraintToStudent = new JButton("Add preferences for student");
                final JTextField studentNameField = new JTextField();
                studentNameField.setName("user" + count);
                studentNameField.setPreferredSize(new Dimension(100, 20));
                JPanel fieldsForConstraints = new JPanel(new FlowLayout());
                fieldsForConstraints.add(new JLabel("Student name"));
                fieldsForConstraints.add(studentNameField);
                fieldsForConstraints.add(addConstraintToStudent);
                studentConstraintPanel.add(fieldsForConstraints, BorderLayout.NORTH);
                final JPanel concreteStudentConstraintsPanel = new JPanel(new GridLayout(0, 1, 2, 2));
                final JComboBox instructor = new JComboBox(instructorsNames.toArray());
                addConstraintToStudent.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        final String studentName = studentNameField.getText();
                        if (studentName.equals("")) {
                            JOptionPane.showMessageDialog(null, "Please enter the student name");
                        } else {
                            JPanel lessonTimeForStudentPanel = new JPanel(new FlowLayout());
                            final JTextField startLesson = new JTextField();
                            final JTextField endLesson = new JTextField();
                            startLesson.setPreferredSize(new Dimension(150, 20));
                            endLesson.setPreferredSize(new Dimension(150, 20));
                            final JButton addConstraintButton = new JButton("Add");
                            lessonTimeForStudentPanel.add(new Label("Add prefered hours for " + studentName));
                            lessonTimeForStudentPanel.add(new Label("Start of lesson"));
                            lessonTimeForStudentPanel.add(startLesson);
                            lessonTimeForStudentPanel.add(new Label("End of lesson"));
                            lessonTimeForStudentPanel.add(endLesson);
                            JPanel choodeInstructorPanel = new JPanel();
                            lessonTimeForStudentPanel.add(new Label("Select instructor"));
                            lessonTimeForStudentPanel.add(instructor);
                            concreteStudentConstraintsPanel.add(choodeInstructorPanel);
                            lessonTimeForStudentPanel.add(addConstraintButton);
                            addConstraintButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    String startString = startLesson.getText();
                                    LocalDateTime start = LocalDateTime.parse(startString, formatter);
                                    String endString = endLesson.getText();
                                    LocalDateTime end = LocalDateTime.parse(endString, formatter);
                                    if (end.isBefore(start)) {
                                        JOptionPane.showMessageDialog(null, "Lesson cannot end before it's start");
                                    } else {
                                        if (!allConstraints.checkIfConstraintExists(studentNameField.getText(), start, end)) {
                                            allConstraints.addConstraintToStudent(studentNameField.getText(), start, end, count, instructor.getSelectedItem().toString());
                                            //allConstraints.setInstructorForStudent(instructor.getSelectedItem().toString(),studentNameField.getText());
                                            count++;
                                            addConstraintButton.setEnabled(false);
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
                studentPanel.add(constraintsPanel);
                frame.validate();
                frame.repaint();
            }
        });

        generateSchedule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ea) {
                allConstraints.generateSAT();
                allConstraints.printAllConstraint();
                solveSAT.setAllConstraints(allConstraints);
                generatingSchedule(solveSAT.solve(), solveSAT);
            }
        });

        JPanel addNewStudentPanel = new JPanel();
        addNewStudentPanel.add(addNewStudentButton);
        JPanel generateSchedulePanel = new JPanel();
        generateSchedulePanel.add(generateSchedule);
        studentPanel.add(addNewStudentPanel, BorderLayout.NORTH);
        studentPanel.add(generateSchedulePanel, BorderLayout.SOUTH);
    }

    private void generatingSchedule(List<Constraint> solution, SolveSAT solveSAT) {
        String[][] trueConstraint = solveSAT.getTableOfResults(solution);
        JFrame resultWindow = new JFrame("Schedule");
        resultWindow.setLayout(new GridLayout(0, 1, 2, 2));

        if (trueConstraint != null) {
            String[] columnNames = {"Students name", "Start of the lesson", "End of the lesson", "Instructor"};
            JTable table = new JTable(trueConstraint, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            resultWindow.add(scrollPane);
        } else
            resultWindow.add(new Label(" Unsatisfiable"));
        resultWindow.pack();

        resultWindow.setLocationByPlatform(true);
        resultWindow.setVisible(true);
    }

    protected static ImageIcon createImageIcon(String path) {
        if (path != null) {
            return new ImageIcon(path);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
