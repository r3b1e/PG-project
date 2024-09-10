import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class signup extends JFrame implements ActionListener {
    JButton submitButton;
    JButton resetButton;
    JTextField namefield;
    JTextField emailfield;
    JComboBox<String> genderComboBox;
    JPasswordField passwordfield;
    JPasswordField confirmfield;

    public signup() {
        // Header Label
        JLabel pgroom = new JLabel();
        pgroom.setBounds(0, 0, 400, 40);
        pgroom.setBackground(Color.CYAN);
        pgroom.setOpaque(true);
        pgroom.setText("PG ROOM");
        pgroom.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        pgroom.setHorizontalAlignment(JLabel.CENTER);



        // Sign Up Label
        JLabel signupicon = new JLabel();
        signupicon.setBounds(0, 50, 400, 30);
        signupicon.setBackground(Color.CYAN);
        signupicon.setOpaque(true);
        signupicon.setText("Sign Up");
        signupicon.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        signupicon.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // Name Label
        JLabel fullnameLabel = new JLabel();
        fullnameLabel.setBounds(20, 90, 100, 30);
        fullnameLabel.setText("Name:");
        fullnameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));

        // Name Field with rounded border
        namefield = new JTextField();
        namefield.setBounds(150, 90, 230, 30);
        namefield.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        namefield.setBorder(new RoundedBorder(10)); // Rounded border

        // Email Label
        JLabel emailLabel = new JLabel();
        emailLabel.setBounds(20, 130, 100, 30);
        emailLabel.setText("Email:");
        emailLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));

        // Email Field with rounded border
        emailfield = new JTextField();
        emailfield.setBounds(150, 130, 230, 30);
        emailfield.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        emailfield.setBorder(new RoundedBorder(10)); // Rounded border

        // Gender Label
        JLabel genderLabel = new JLabel();
        genderLabel.setBounds(20, 170, 100, 30);
        genderLabel.setText("Gender:");
        genderLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));

        // Gender ComboBox with rounded border
        String[] genders = {"Select", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(150, 170, 230, 30);
        genderComboBox.setBackground(Color.white);
        genderComboBox.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        genderComboBox.setBorder(new RoundedBorder(10)); // Rounded border

        // Password Label
        JLabel passwordLabel = new JLabel();
        passwordLabel.setBounds(20, 210, 120, 30);
        passwordLabel.setText("Password:");
        passwordLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));

        // Password Field with rounded border
        passwordfield = new JPasswordField();
        passwordfield.setBounds(150, 210, 230, 30);
        passwordfield.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        passwordfield.setBorder(new RoundedBorder(10)); // Rounded border

        // Confirm Password Label
        JLabel confirmLabel = new JLabel();
        confirmLabel.setBounds(20, 250, 150, 30);
        confirmLabel.setText("Confirm Password:");
        confirmLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));

        // Confirm Password Field with rounded border
        confirmfield = new JPasswordField();
        confirmfield.setBounds(150, 250, 230, 30);
        confirmfield.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        confirmfield.setBorder(new RoundedBorder(10)); // Rounded border

        // Buttons
        submitButton = new JButton("Sign in");
        resetButton = new JButton("Reset");
        submitButton.setBounds(100, 290, 100, 30);
        resetButton.setBounds(220, 290, 100, 30);
        resetButton.setBorder(new RoundedBorder(10));
        submitButton.setBorder(new RoundedBorder(10));
        submitButton.setBackground(Color.WHITE);
        resetButton.setBackground(Color.WHITE);

        submitButton.addActionListener(this);
        resetButton.addActionListener(this);

        // Set up the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 360);  // Slightly increased height to avoid cut-offs
        this.setTitle("PG ROOM");
        this.setLayout(null);
        this.setResizable(false);

        // Add components to frame
        this.add(pgroom);
        this.add(signupicon);
        this.add(fullnameLabel);
        this.add(namefield);
        this.add(emailLabel);
        this.add(emailfield);
        this.add(genderLabel);
        this.add(genderComboBox);
        this.add(passwordLabel);
        this.add(passwordfield);
        this.add(confirmLabel);
        this.add(confirmfield);
        this.add(submitButton);
        this.add(resetButton);

        // Set frame icon
        ImageIcon pgimage = new ImageIcon("pg.png");
        this.setIconImage(pgimage.getImage());

        this.setVisible(true);
    }

    // Action Listener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String name = namefield.getText();
            String email = emailfield.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            String password = new String(passwordfield.getPassword());
            String confirmPassword = new String(confirmfield.getPassword());

            if (name.isEmpty() || email.isEmpty() || gender.equals("Select") || password.isEmpty() || !password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.");
            }
        } else if (e.getSource() == resetButton) {
            namefield.setText("");
            emailfield.setText("");
            genderComboBox.setSelectedIndex(0);
            passwordfield.setText("");
            confirmfield.setText("");
        }
    }

    public static void main(String[] args) {
        new signup();
    }
}

// Rounded Border class for custom field borders
class RoundedBorder extends AbstractBorder {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.GRAY);
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 10, 5, 10);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = 10;
        insets.top = insets.bottom = 5;
        return insets;
    }
}
