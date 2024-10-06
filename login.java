import javax.swing.*;
import javax.swing.border.Border;  // Add this import
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.LinearGradientPaint;
import javax.swing.border.LineBorder;

public class Login extends JFrame implements ActionListener {
    JTextField namefield;
    JPasswordField passwordfield;
    JButton loginbutton;
    JButton signupbutton;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color accentColor = new Color(230, 126, 34);

    public Login() {
        // Set custom shape and size for the frame
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 400, 400, 20, 20));
        setSize(400, 400);
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
        pgroom.setBounds(0, 20, 400, 60);
        pgroom.setFont(new Font("Arial", Font.BOLD, 36));
        pgroom.setForeground(Color.WHITE);
        pgroom.setHorizontalAlignment(JLabel.CENTER);

        // Login Label
        JLabel loginicon = new JLabel("Login");
        loginicon.setBounds(0, 80, 400, 40);
        loginicon.setFont(new Font("Arial", Font.BOLD, 28));
        loginicon.setForeground(Color.WHITE);
        loginicon.setHorizontalAlignment(JLabel.CENTER);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 140, 100, 30);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(Color.WHITE);

        namefield = createStyledTextField("Username");
        namefield.setBounds(50, 170, 300, 40);
        namefield.setFont(new Font("Arial", Font.PLAIN, 14));
        namefield.setBackground(new Color(255, 255, 255, 220));
        namefield.setBorder(new LineBorder(new Color(41, 128, 185), 2, true));

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 220, 100, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setForeground(Color.WHITE);

        passwordfield = createStyledPasswordField("Password");
        passwordfield.setBounds(50, 250, 300, 40);
        passwordfield.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordfield.setBackground(new Color(255, 255, 255, 220));
        passwordfield.setBorder(new LineBorder(new Color(41, 128, 185), 2, true));

        // Buttons
        loginbutton = createStyledButton("Login", accentColor);
        signupbutton = createStyledButton("Sign Up", new Color(231, 76, 60));
        loginbutton.setBounds(50, 310, 140, 50);
        signupbutton.setBounds(210, 310, 140, 50);

        loginbutton.addActionListener(this);
        signupbutton.addActionListener(this);

        // Add close button
        JButton closeButton = createStyledButton("X", new Color(231, 76, 60));
        closeButton.setBounds(350, 10, 40, 40);
        closeButton.addActionListener(e -> System.exit(0));
        add(closeButton);

        // Add components
        add(pgroom);
        add(loginicon);
        add(usernameLabel);
        add(namefield);
        add(passwordLabel);
        add(passwordfield);
        add(loginbutton);
        add(signupbutton);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("PG ROOM");
        setResizable(false);

        // Set frame icon
        ImageIcon pgimage = new ImageIcon("pg.png");
        setIconImage(pgimage.getImage());

        setVisible(true);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setForeground(Color.GRAY);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        textField.setOpaque(false);
        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(placeholder);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setForeground(Color.GRAY);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        passwordField.setOpaque(false);
        return passwordField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(new LineBorder(Color.WHITE, 2, true));
        button.setFocusPainted(false);
        return button;
    }

    // Method to create rounded borders for text fields and buttons
    private Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = namefield.getText();
        String password = new String(passwordfield.getPassword());
        boolean istrue = true;
        if (e.getSource() == loginbutton) {
            try {
                new DBConnect();
                Statement statement = DBConnect.statement;
                Connection connection = DBConnect.connection;
                ResultSet resultset = statement.executeQuery("SELECT * FROM user");

                while (resultset.next() && istrue) {
                    if (name.equals(resultset.getString("username")) && password.equals(resultset.getString("password"))) {
                        JOptionPane.showMessageDialog(this, "Login Successful!");
                        istrue = false;
                        this.dispose();
                        new StudentInformationPage(resultset.getString("userid"));
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                // Close resources (resultset, statement, connection) here
            }
            if (istrue) {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            }
        }
        if (e.getSource() == signupbutton) {
            dispose();
            new Signup();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}

// Make sure the RoundedBorder class is defined in this file or properly imported
class RoundedBorder implements Border {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}
