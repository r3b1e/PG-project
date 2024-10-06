import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;
import java.awt.geom.RoundRectangle2D;

public class Signup extends JFrame implements ActionListener {
    JButton submitButton, resetButton, backButton;
    JTextField namefield, emailfield, mobilefield;
    JComboBox<String> genderComboBox;
    JPasswordField passwordfield, confirmfield;
    private Color primaryColor = new Color(41, 128, 185);
    // private Color secondaryColor = new Color(52, 152, 219);
    private Color accentColor = new Color(230, 126, 34);

    public Signup() {
        initComponents();
    }

    private void initComponents() {
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
                g2d.setColor(primaryColor);
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
            fields[i].setBorder(new RoundedBorder(20, Color.LIGHT_GRAY));
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

        // Add components
        add(backButton);
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
        button.setBorder(new RoundedBorder(25, Color.DARK_GRAY));
        button.setFocusPainted(false);
        return button;
    }

    // Action Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String name = namefield.getText();
            String email = emailfield.getText();
            String mobile = mobilefield.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            String password = new String(passwordfield.getPassword());
            String confirmPassword = new String(confirmfield.getPassword());

            if (validateInputs(name, email, mobile, gender, password, confirmPassword)) {
                try {
                    Connection connection = DBConnect.getConnection();  // Assuming DBConnect class has a getConnection method
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
                    pre.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    dispose();
                    new Login();  // Assuming Login class exists

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error occurred while signing up.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == resetButton) {
            namefield.setText("");
            emailfield.setText("");
            mobilefield.setText("");
            genderComboBox.setSelectedIndex(0);
            passwordfield.setText("");
            confirmfield.setText("");
        } else if (e.getSource() == backButton) {
            new Login();
            dispose();
        }
    }

    private boolean validateInputs(String name, String email, String mobile, String gender, String password, String confirmPassword) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
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
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Signup::new);
    }

    // Custom RoundedBorder class for fields and buttons with simple solid color
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color borderColor;

        public RoundedBorder(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = radius;
            return insets;
        }
    }
}
