import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {
    private JPanel sidePanel, contentPanel, headerPanel;
    private JButton optionsBtn, addRequestBtn, messageBtn, favoriteBtn, logoutBtn, exitBtn, searchBtn;
    private JLabel titleLabel;
    private final int SIDE_PANEL_WIDTH = 200;

    public Home() {
        setTitle("PG ROOM");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create header panel
        createHeaderPanel();

        // Create content panel
        createContentPanel();

        // Create side panel with buttons from old UI
        createSidePanelWithOldButtons();

        setVisible(true);
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 50));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        // Create options button
        optionsBtn = new JButton("Options");
        optionsBtn.setPreferredSize(new Dimension(100, 30));
        optionsBtn.setBackground(new Color(200, 200, 200)); // Lighter background for contrast
        optionsBtn.setForeground(Color.BLACK); // Changed to black for visibility
        optionsBtn.setFocusPainted(false);
        optionsBtn.addActionListener(e -> toggleSidePanel());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(optionsBtn);

        // Create title label
        titleLabel = new JLabel("PG ROOMS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create search button (top-right)
        searchBtn = new JButton("Search");
        searchBtn.setPreferredSize(new Dimension(100, 30));
        searchBtn.setBackground(new Color(200, 200, 200)); // Lighter background for contrast
        searchBtn.setForeground(Color.BLACK); // Changed to black for visibility
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> performSearch()); // Action for search

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(searchBtn);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());

        add(contentPanel, BorderLayout.CENTER);
    }

    private void createSidePanelWithOldButtons() {
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, getHeight()));
        sidePanel.setBackground(new Color(50, 50, 50));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        addRequestBtn = createSideButton("Make Request", "src/images/Addrequest.png");
        messageBtn = createSideButton("Message", "src/images/Message.png");
        favoriteBtn = createSideButton("Favorite", "src/images/Favorite.png");
        logoutBtn = createSideButton("Logout", "src/images/Logout.png");
        exitBtn = createSideButton("Exit", "src/images/Exit.png");

        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(addRequestBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(messageBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(favoriteBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(logoutBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(exitBtn);
        sidePanel.add(Box.createVerticalGlue());

        sidePanel.setVisible(false);
        add(sidePanel, BorderLayout.WEST);
    }

    private JButton createSideButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setIcon(resizeIcon(new ImageIcon(iconPath), 24, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 70, 70));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.addActionListener(this::handleButtonAction);
        return button;
    }

    private void toggleSidePanel() {
        sidePanel.setVisible(!sidePanel.isVisible());
        revalidate();
        repaint();
    }

    private void handleButtonAction(ActionEvent e) {
        if (e.getSource() == addRequestBtn) {
            new AddRequest("738126");
        } else if (e.getSource() == messageBtn) {
            // message logic here
        } else if (e.getSource() == favoriteBtn) {
            // Favorite logic here
        } else if (e.getSource() == logoutBtn) {
            dispose();
            new Login();
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

    private void performSearch() {
        // Logic for search button action
        JOptionPane.showMessageDialog(this, "Search button clicked");
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home());
    }
}