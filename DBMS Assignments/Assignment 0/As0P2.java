import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Department {
    private String deptCode;
    private String deptName;

    public Department(String deptCode, String deptName) {
        this.deptCode = deptCode;
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    @Override
    public String toString() {
        return deptName;
    }
}

class Student {
    private String roll;
    private String deptCode;
    private String name;
    private String address;
    private String phone;

    public Student(String roll, String deptCode, String name, String address, String phone) {
        this.roll = roll;
        this.deptCode = deptCode;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getRoll() {
        return roll;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
}

class StudentManagementApp {

    private List<Student> students;
    private List<Department> departments;
    private JFrame mainFrame;

    public StudentManagementApp() {
        students = new ArrayList<>();
        departments = new ArrayList<>();
        // Preload departments
        departments.add(new Department("ChE", "Chemical Engineering"));
        departments.add(new Department("CE", "Civil Engineering"));
        departments.add(new Department("CSE", "Computer Science & Engineering"));
        departments.add(new Department("ConE", "Construction Engineering"));
        departments.add(new Department("EE", "Electrical Engineering"));
        departments.add(new Department("ETCE", "Electronics & Telecommunication Engineering"));
        departments.add(new Department("FTBE", "Food Technology & Bio-Chemical Engineering"));
        departments.add(new Department("IT", "Information Technology"));
        departments.add(new Department("IEE", "Instrumentation & Electronics Engineering"));
        departments.add(new Department("ME", "Mechanical Engineering"));
        departments.add(new Department("MetE", "Metallurgical & Material Engineering"));
        departments.add(new Department("PE", "Power Engineering"));
        departments.add(new Department("PrnE", "Printing Engineering"));
        departments.add(new Department("ProdE", "Production Engineering"));

        // Set up main window
        mainFrame = new JFrame("Student Management System");
        mainFrame.setLayout(new GridLayout(5, 1));

        JButton addBtn = new JButton("Add Student Record");
        JButton searchBtn = new JButton("Search Student Record");
        JButton editBtn = new JButton("Edit Student Record");
        JButton deleteBtn = new JButton("Delete Student Record");
        JButton displayBtn = new JButton("Display All Student Records");

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddStudentWindow();
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSearchStudentWindow();
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditStudentWindow();
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDeleteStudentWindow();
            }
        });

        displayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDisplayStudentWindow();
            }
        });

        mainFrame.add(addBtn);
        mainFrame.add(searchBtn);
        mainFrame.add(editBtn);
        mainFrame.add(deleteBtn);
        mainFrame.add(displayBtn);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void openAddStudentWindow() {
        JFrame addFrame = new JFrame("Add Student Record");
        addFrame.setLayout(new GridLayout(7, 2));

        JTextField rollField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<Department> deptComboBox = new JComboBox<>(departments.toArray(new Department[0]));

        addFrame.add(new JLabel("Roll:"));
        addFrame.add(rollField);
        addFrame.add(new JLabel("Name:"));
        addFrame.add(nameField);
        addFrame.add(new JLabel("Address:"));
        addFrame.add(addressField);
        addFrame.add(new JLabel("Phone:"));
        addFrame.add(phoneField);
        addFrame.add(new JLabel("Department:"));
        addFrame.add(deptComboBox);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roll = rollField.getText();
                String name = nameField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();
                Department selectedDept = (Department) deptComboBox.getSelectedItem();

                if (roll.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(addFrame, "All fields must be filled.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (Student student : students) {
                    if (student.getRoll().equals(roll)) {
                        JOptionPane.showMessageDialog(addFrame, "Roll number must be unique.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Student newStudent = new Student(roll, selectedDept.getDeptCode(), name, address, phone);
                students.add(newStudent);

                JOptionPane.showMessageDialog(addFrame, "Student added successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                clearFields(rollField, nameField, addressField, phoneField);
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields(rollField, nameField, addressField, phoneField);
            }
        });

        addFrame.add(saveBtn);
        addFrame.add(cancelBtn);

        addFrame.setSize(300, 200);
        addFrame.setLocationRelativeTo(mainFrame);
        addFrame.setVisible(true);
    }

    private void openSearchStudentWindow() {
        JFrame searchFrame = new JFrame("Search Student Record");
        searchFrame.setLayout(new GridLayout(3, 2));

        JTextField rollField = new JTextField();

        searchFrame.add(new JLabel("Enter Roll:"));
        searchFrame.add(rollField);

        JButton searchBtn = new JButton("Search");
        JButton cancelBtn = new JButton("Cancel");

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roll = rollField.getText();

                if (roll.isEmpty()) {
                    JOptionPane.showMessageDialog(searchFrame, "Please enter a roll number.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (Student student : students) {
                    if (student.getRoll().equals(roll)) {
                        displayStudentDetails(student);
                        return;
                    }
                }

                JOptionPane.showMessageDialog(searchFrame, "Student not found.", "Not Found",
                        JOptionPane.INFORMATION_MESSAGE);
                clearFields(rollField);
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields(rollField);
            }
        });

        searchFrame.add(searchBtn);
        searchFrame.add(cancelBtn);

        searchFrame.setSize(300, 120);
        searchFrame.setLocationRelativeTo(mainFrame);
        searchFrame.setVisible(true);
    }

    private void displayStudentDetails(Student student) {
        JFrame detailsFrame = new JFrame("Student Details");
        detailsFrame.setLayout(new GridLayout(6, 2));

        detailsFrame.add(new JLabel("Roll:"));
        detailsFrame.add(new JLabel(student.getRoll()));
        detailsFrame.add(new JLabel("Name:"));
        detailsFrame.add(new JLabel(student.getName()));
        detailsFrame.add(new JLabel("Address:"));
        detailsFrame.add(new JLabel(student.getAddress()));
        detailsFrame.add(new JLabel("Phone:"));
        detailsFrame.add(new JLabel(student.getPhone()));
        detailsFrame.add(new JLabel("Department:"));
        detailsFrame.add(new JLabel(getDeptName(student.getDeptCode())));

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailsFrame.dispose();
            }
        });

        detailsFrame.add(closeBtn);

        detailsFrame.setSize(300, 200);
        detailsFrame.setLocationRelativeTo(mainFrame);
        detailsFrame.setVisible(true);
    }

    private void openEditStudentWindow() {
        JFrame editFrame = new JFrame("Edit Student Record");
        editFrame.setLayout(new GridLayout(3, 2));

        JTextField rollField = new JTextField();

        editFrame.add(new JLabel("Enter Roll:"));
        editFrame.add(rollField);

        JButton editBtn = new JButton("Edit");
        JButton cancelBtn = new JButton("Cancel");

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roll = rollField.getText();

                if (roll.isEmpty()) {
                    JOptionPane.showMessageDialog(editFrame, "Please enter a roll number.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (Student student : students) {
                    if (student.getRoll().equals(roll)) {
                        openEditDetailsWindow(student);
                        return;
                    }
                }

                JOptionPane.showMessageDialog(editFrame, "Student not found.", "Not Found",
                        JOptionPane.INFORMATION_MESSAGE);
                clearFields(rollField);
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields(rollField);
            }
        });

        editFrame.add(editBtn);
        editFrame.add(cancelBtn);

        editFrame.setSize(300, 120);
        editFrame.setLocationRelativeTo(mainFrame);
        editFrame.setVisible(true);
    }

    private void openEditDetailsWindow(Student student) {
        JFrame editDetailsFrame = new JFrame("Edit Student Details");
        editDetailsFrame.setLayout(new GridLayout(7, 2));

        JTextField nameField = new JTextField(student.getName());
        JTextField addressField = new JTextField(student.getAddress());
        JTextField phoneField = new JTextField(student.getPhone());
        JComboBox<Department> deptComboBox = new JComboBox<>(departments.toArray(new Department[0]));

        setDeptComboBoxSelection(deptComboBox, student.getDeptCode());

        editDetailsFrame.add(new JLabel("Roll: " + student.getRoll()));
        editDetailsFrame.add(new JLabel(""));
        editDetailsFrame.add(new JLabel("Name:"));
        editDetailsFrame.add(nameField);
        editDetailsFrame.add(new JLabel("Address:"));
        editDetailsFrame.add(addressField);
        editDetailsFrame.add(new JLabel("Phone:"));
        editDetailsFrame.add(phoneField);
        editDetailsFrame.add(new JLabel("Department:"));
        editDetailsFrame.add(deptComboBox);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();
                Department selectedDept = (Department) deptComboBox.getSelectedItem();

                student.setName(name);
                student.setAddress(address);
                student.setPhone(phone);
                student.setDeptCode(selectedDept.getDeptCode());

                JOptionPane.showMessageDialog(editDetailsFrame, "Student details updated successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                // Fields after successful update
                clearFields(nameField, addressField, phoneField);
                editDetailsFrame.dispose();
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields(nameField, addressField, phoneField);
                editDetailsFrame.dispose();
            }
        });

        editDetailsFrame.add(saveBtn);
        editDetailsFrame.add(cancelBtn);

        editDetailsFrame.setSize(300, 200);
        editDetailsFrame.setLocationRelativeTo(mainFrame);
        editDetailsFrame.setVisible(true);
    }

    private void openDeleteStudentWindow() {
        JFrame deleteFrame = new JFrame("Delete Student Record");
        deleteFrame.setLayout(new GridLayout(3, 2));

        JTextField rollField = new JTextField();

        deleteFrame.add(new JLabel("Enter Roll:"));
        deleteFrame.add(rollField);

        JButton deleteBtn = new JButton("Delete");
        JButton cancelBtn = new JButton("Cancel");

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roll = rollField.getText();

                if (roll.isEmpty()) {
                    JOptionPane.showMessageDialog(deleteFrame, "Please enter a roll number.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (int i = 0; i < students.size(); i++) {
                    Student student = students.get(i);
                    if (student.getRoll().equals(roll)) {
                        students.remove(i);
                        JOptionPane.showMessageDialog(deleteFrame, "Student deleted successfully.", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        clearFields(rollField);
                        return;
                    }
                }

                JOptionPane.showMessageDialog(deleteFrame, "Student not found.", "Not Found",
                        JOptionPane.INFORMATION_MESSAGE);
                clearFields(rollField);
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields(rollField);
            }
        });

        deleteFrame.add(deleteBtn);
        deleteFrame.add(cancelBtn);

        deleteFrame.setSize(300, 120);
        deleteFrame.setLocationRelativeTo(mainFrame);
        deleteFrame.setVisible(true);
    }

    private void openDisplayStudentWindow() {
        JFrame displayFrame = new JFrame("Display All Student Records");
        displayFrame.setLayout(new BorderLayout());

        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel();

        // Add columns
        tableModel.addColumn("Roll");
        tableModel.addColumn("Name");
        tableModel.addColumn("Department");
        tableModel.addColumn("Address");
        tableModel.addColumn("Phone");

        int visibleRowCount = 5;
        // Add an empty set of rows to initialize the table
        for (int i = 0; i < visibleRowCount; i++) {
            tableModel.addRow(new String[] { "", "", "", "", "" });
        }

        JButton prevBtn = new JButton("Prev");
        JButton nextBtn = new JButton("Next");

        // Initial display: show only the first set of records
        int rowCount = students.size();
        int[] firstVisibleRow = { 0 };

        // Load the initial set of records
        loadRecords(tableModel, students, firstVisibleRow[0], visibleRowCount);

        // Enable or disable Prev/Next buttons based on the initial state
        prevBtn.setEnabled(false);
        nextBtn.setEnabled(rowCount > visibleRowCount);

        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells un-editable
            }
        };
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));

        table.setRowHeight(30); // Set the desired height (adjust the value as needed)

        JScrollPane scrollPane = new JScrollPane(table);
        displayFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevBtn);
        buttonPanel.add(nextBtn);

        displayFrame.add(buttonPanel, BorderLayout.SOUTH);

        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstVisibleRow[0] -= visibleRowCount;
                loadRecords(tableModel, students, firstVisibleRow[0], visibleRowCount);
                updateButtonStates(prevBtn, nextBtn, firstVisibleRow[0], students.size(), visibleRowCount);
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstVisibleRow[0] += visibleRowCount;
                loadRecords(tableModel, students, firstVisibleRow[0], visibleRowCount);
                updateButtonStates(prevBtn, nextBtn, firstVisibleRow[0], students.size(), visibleRowCount);
            }
        });

        displayFrame.setSize(600, 250);
        displayFrame.setLocationRelativeTo(mainFrame);
        displayFrame.setVisible(true);
    }

    private void loadRecords(DefaultTableModel tableModel, List<Student> students, int start, int count) {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Load new set of records
        int end = Math.min(start + count, students.size());
        for (int i = start; i < end; i++) {
            Student student = students.get(i);
            tableModel.addRow(new String[] {
                    student.getRoll(),
                    student.getName(),
                    getDeptName(student.getDeptCode()),
                    student.getAddress(),
                    student.getPhone()
            });
        }
    }

    private void updateButtonStates(JButton prevBtn, JButton nextBtn, int firstVisibleRow, int rowCount,
            int visibleRowCount) {
        prevBtn.setEnabled(firstVisibleRow > 0);
        nextBtn.setEnabled(firstVisibleRow + visibleRowCount < rowCount);
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void setDeptComboBoxSelection(JComboBox<Department> comboBox, String deptCode) {
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getDeptCode().equals(deptCode)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private String getDeptName(String deptCode) {
        for (Department dept : departments) {
            if (dept.getDeptCode().equals(deptCode)) {
                return dept.getDeptName();
            }
        }
        return "";
    }
}

public class As0P2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentManagementApp();
            }
        });
    }
}