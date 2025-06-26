
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    String name;
    int[] grades;

    Student(String name, int[] grades) {
        this.name = name;
        this.grades = grades;
    }

    int getAverage() {
        int sum = 0;
        for (int g : grades) sum += g;
        return sum / grades.length;
    }

    int getHighest() {
        int max = grades[0];
        for (int g : grades) if (g > max) max = g;
        return max;
    }

    int getLowest() {
        int min = grades[0];
        for (int g : grades) if (g < min) min = g;
        return min;
    }
}

public class GradeTrackerGUI extends JFrame {
    private JTextField nameField;
    private JTextField[] marksFields;
    private JTextArea resultArea;
    private ArrayList<Student> studentList;

    public GradeTrackerGUI() {
        setTitle("Student Grade Tracker");
        setSize(420, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        studentList = new ArrayList<>();


// Name Input
JLabel nameLabel = new JLabel("Student Name:");
nameLabel.setBounds(20, 20, 120, 25);
add(nameLabel);

nameField = new JTextField(20);
nameField.setBounds(150, 20, 200, 25);
add(nameField);

// Marks Input
marksFields = new JTextField[3];
for (int i = 0; i < 3; i++) {
    JLabel markLabel = new JLabel("Mark " + (i + 1) + ":");
    markLabel.setBounds(20, 60 + (i * 30), 120, 25);
    add(markLabel);

    marksFields[i] = new JTextField(5);
    marksFields[i].setBounds(150, 60 + (i * 30), 200, 25);
    marksFields[i].setToolTipText("Enter mark " + (i + 1) + " (0-100)");
    add(marksFields[i]);
}


        // Add Button
        JButton addBtn = new JButton("Add Student");
        addBtn.setBounds(150, 150, 120, 30);
        add(addBtn);
        addBtn.addActionListener(e -> addStudent());

    
        // Report Area
        resultArea = new JTextArea(12, 32);
        resultArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(20, 200, 370, 180); // Adjust size and position as needed
        add(scrollPane);
        

        // Show Report Button
        JButton showBtn = new JButton("Show Report");
        showBtn.setBounds(150, 400, 120, 30);
        add(showBtn);
        showBtn.addActionListener(e -> showReport());

        setVisible(true);
    }

    private void addStudent() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty");
                return;
            }

            int[] marks = new int[3];
            for (int i = 0; i < 3; i++) {
                marks[i] = Integer.parseInt(marksFields[i].getText().trim());
                if (marks[i] < 0 || marks[i] > 100) {
                    throw new NumberFormatException("Marks must be between 0 and 100");
                }
            }

            studentList.add(new Student(name, marks));
            JOptionPane.showMessageDialog(this, "Student Added Successfully!");

            nameField.setText("");
            for (JTextField field : marksFields) field.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric marks (0-100).");
        }
    }

    private void showReport() {
        if (studentList.isEmpty()) {
            resultArea.setText("No students added yet.");
            return;
        }

        StringBuilder sb = new StringBuilder("Summary Report:\n\n");
        for (Student s : studentList) {
            sb.append("Name: ").append(s.name)
              .append(" | Average: ").append(s.getAverage())
              .append(" | Highest: ").append(s.getHighest())
              .append(" | Lowest: ").append(s.getLowest())
              .append("\n");
        }
        resultArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradeTrackerGUI());
    }
}
