import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D; 

public class WelcomePage extends JFrame implements ActionListener {
    private JButton loginButton, signupButton;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color accentColor = new Color(230, 126, 34);

    public WelcomePage() {
        // Set custom shape and size for the frame
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 400, 20, 20));
        setSize(500, 400);

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

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to PG Accommodation");
        welcomeLabel.setBounds(0, 50, 500, 50);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Find your perfect PG stay");
        subtitleLabel.setBounds(0, 100, 500, 30);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Login button
        loginButton = createStyledButton("Login", accentColor);
        loginButton.setBounds(150, 200, 200, 50);
        loginButton.addActionListener(this);

        // Sign up button
        signupButton = createStyledButton("Sign Up", new Color(46, 204, 113));
        signupButton.setBounds(150, 270, 200, 50);
        signupButton.addActionListener(this);

        // Close button
        JButton closeButton = createStyledButton("X", new Color(231, 76, 60));
        closeButton.setBounds(450, 10, 40, 40);
        closeButton.addActionListener(e -> System.exit(0));

        // Add components to the frame
        add(welcomeLabel);
        add(subtitleLabel);
        add(loginButton);
        add(signupButton);
        add(closeButton);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("PG Accommodation");
        setResizable(false);

        // Set frame icon (if you have one)
        // ImageIcon pgimage = new ImageIcon("pg.png");
        // setIconImage(pgimage.getImage());

        setVisible(true);
    }

    // Add this new method to create styled text fields
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

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(new RoundedBorder(25));
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            new Login();
            this.dispose();
        } else if (e.getSource() == signupButton) {
            new Signup();
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage());
    }
}

class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color color;

    RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius / 2, this.radius / 2, this.radius / 2, this.radius / 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}