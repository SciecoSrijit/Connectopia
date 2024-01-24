import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

class CalculatorApp extends JFrame {

    private JTextField number1Field, number2Field, resultField;
    private JButton addButton, subtractButton;

    public CalculatorApp() {
        // Set the title with the current date
        setTitle("Calculator App - " + getCurrentDate());

        // Set layout manager
        setLayout(new BorderLayout());

        // Create and add components
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createInputPanel(), BorderLayout.CENTER);
        add(createResultPanel(), BorderLayout.SOUTH);

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set a larger size
        setSize(400, 250);

        // Pack and set visibility
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Simple Calculator App");
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Increased spacing

        JLabel number1Label = new JLabel("Number 1:");
        number1Field = new JTextField();
        JLabel number2Label = new JLabel("Number 2:");
        number2Field = new JTextField();

        inputPanel.add(number1Label);
        inputPanel.add(number1Field);
        inputPanel.add(number2Label);
        inputPanel.add(number2Field);

        addButton = new JButton("Add");
        subtractButton = new JButton("Subtract");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation('+');
            }
        });

        subtractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation('-');
            }
        });

        inputPanel.add(addButton);
        inputPanel.add(subtractButton);

        return inputPanel;
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel();
        JLabel resultLabel = new JLabel("Result:");
        resultField = new JTextField(15); // Increased the number of columns

        resultField.setEditable(false);

        resultPanel.add(resultLabel);
        resultPanel.add(resultField);

        return resultPanel;
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    private void performOperation(char operation) {
        try {
            double number1 = Double.parseDouble(number1Field.getText());
            double number2 = Double.parseDouble(number2Field.getText());
            double result = 0;

            if (operation == '+') {
                result = number1 + number2;
            } else if (operation == '-') {
                result = number1 - number2;
            }

            resultField.setText(Double.toString(result));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

public class As0P1 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalculatorApp();
            }
        });
    }
}
