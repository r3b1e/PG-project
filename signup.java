import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Signup extends JFrame implements ActionListener {
    JButton submitButton;
    JButton resetButton;
    JTextField namefield;
    JTextField emailfield;
    JTextField mobilefield;  // New mobile field
    JComboBox<String> genderComboBox;
    JPasswordField passwordfield;
    JPasswordField confirmfield;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color accentColor = new Color(230, 126, 34);
    JButton backButton;

    public Signup() {
        // Set custom shape and size for the frame
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 600, 20, 20));
        setSize(500, 600);

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Create gradient background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                LinearGradientPaint gradient = new LinearGradientPaint(
                    new Point(0, 0), new Point(0, getHeight()),
                    new float[]{0f, 1f},
                    new Color[]{primaryColor, secondaryColor}
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Header Label
        JLabel pgroom = new JLabel("PG ROOM");
        pgroom.setBounds(0, 20, 500, 60);
        pgroom.setFont(new Font("Arial", Font.BOLD, 36));
        pgroom.setForeground(Color.WHITE);
        pgroom.setHorizontalAlignment(JLabel.CENTER);

        // Sign Up Label
        JLabel signupicon = new JLabel("Sign Up");
        signupicon.setBounds(0, 80, 500, 40);
        signupicon.setFont(new Font("Arial", Font.BOLD, 28));
        signupicon.setForeground(Color.WHITE);
        signupicon.setHorizontalAlignment(JLabel.CENTER);

        // Input fields and labels
        JLabel[] labels = {
            new JLabel("Name:"),
            new JLabel("Email:"),
            new JLabel("Mobile:"),
            new JLabel("Gender:"),
            new JLabel("Password:"),
            new JLabel("Confirm Password:")
        };

        JComponent[] fields = {
            namefield = new JTextField(),
            emailfield = new JTextField(),
            mobilefield = new JTextField(),
            genderComboBox = new JComboBox<>(new String[]{"Select", "Male", "Female", "Other"}),
            passwordfield = new JPasswordField(),
            confirmfield = new JPasswordField()
        };

        int startY = 140;
        int gap = 60;

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(50, startY + i * gap, 150, 30);
            labels[i].setFont(new Font("Arial", Font.BOLD, 16));
            labels[i].setForeground(Color.WHITE);
            add(labels[i]);

            fields[i].setBounds(200, startY + i * gap, 250, 40);
            fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
            fields[i].setBackground(new Color(255, 255, 255, 220));
            fields[i].setBorder(new RoundedBorder(20));
            add(fields[i]);
        }

        // Buttons
        submitButton = createStyledButton("Sign Up", accentColor);
        resetButton = createStyledButton("Reset", new Color(231, 76, 60));
        submitButton.setBounds(100, 520, 140, 50);
        resetButton.setBounds(260, 520, 140, 50);

        submitButton.addActionListener(this);
        resetButton.addActionListener(this);

        // Close button
        JButton closeButton = createStyledButton("X", new Color(231, 76, 60));
        closeButton.setBounds(450, 10, 40, 40);
        closeButton.addActionListener(e -> System.exit(0));

        // Back button
        backButton = createStyledButton("←", new Color(52, 152, 219));
        backButton.setBounds(10, 10, 40, 40);
        backButton.addActionListener(this);
        backButton.setToolTipText("Back to Login");

        // Add back button to the frame
        add(backButton);

        // Add components
        add(pgroom);
        add(signupicon);
        add(submitButton);
        add(resetButton);
        add(closeButton);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("PG ROOM");
        setResizable(false);

        // Set frame icon
        ImageIcon pgimage = new ImageIcon("pg.png");
        setIconImage(pgimage.getImage());

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(new RoundedBorder(25));
        button.setFocusPainted(false);
        return button;
    }

    // Action Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            System.out.println("Submit button clicked"); // Debug statement
            String name = namefield.getText();
            String email = emailfield.getText();
            String mobile = mobilefield.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            String password = new String(passwordfield.getPassword());
            String confirmPassword = new String(confirmfield.getPassword());

            System.out.println("Validating inputs"); // Debug statement
            if (validateInputs(name, email, mobile, gender, password, confirmPassword)) {
                System.out.println("Inputs validated successfully"); // Debug statement
                try {
                    System.out.println("Attempting database connection"); // Debug statement
                    new DBConnect();
                    Connection connection = DBConnect.connection;
                    PreparedStatement pre = connection.prepareStatement(
                            "INSERT INTO user (userid, username, password, email, mobileno) VALUES (?, ?, ?, ?, ?)"
                    );
                    Random random = new Random();
                    int randomNumber = 100000 + random.nextInt(900000);
                    pre.setString(1, Integer.toString(randomNumber));
                    pre.setString(2, name);
                    pre.setString(3, password);
                    pre.setString(4, email);
                    pre.setString(5, mobile);
                    System.out.println("Executing database insert"); // Debug statement
                    pre.executeUpdate();
                    
                    System.out.println("Database insert successful"); // Debug statement
                    // Show success popup outside of try-catch
                    JOptionPane.showMessageDialog(this, "Sign up successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    System.out.println("Closing signup window"); // Debug statement
                    // Close the signup window and open the login window
                    dispose();
                    new Login();
                } catch(SQLException e1) {
                    e1.printStackTrace();
                    System.out.println("Database error: " + e1.getMessage()); // Debug statement
                    JOptionPane.showMessageDialog(this, "Error occurred while signing up: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method if there's an error
                }
            } else {
                System.out.println("Input validation failed"); // Debug statement
            }
        } else if (e.getSource() == resetButton) {
            namefield.setText("");
            emailfield.setText("");
            mobilefield.setText("");
            genderComboBox.setSelectedIndex(0);
            passwordfield.setText("");
            confirmfield.setText("");
        } else if (e.getSource() == backButton) {
            // Open login page and close current signup page
            new Login();
            this.dispose();
        }
    }

    private boolean validateInputs(String name, String email, String mobile, String gender, String password, String confirmPassword) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (email.isEmpty() || !email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (mobile.isEmpty() || !mobile.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Mobile number must be 10 digits.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (gender.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a gender.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (password.isEmpty() || !password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // Improved password validation
        if (password.length() < 8 || password.length() > 16) {
            JOptionPane.showMessageDialog(this, "Password must be between 8 and 16 characters long.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one special character.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        new Signup();
    }
}

class CustomRoundedBorder extends AbstractBorder {
    private int radius;

    // ... existing code ...
}