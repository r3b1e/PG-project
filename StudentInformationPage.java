import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class StudentInformationPage extends JFrame {
    private JPanel headerPanel, searchPanel, filterPanel, contentPanel;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private JButton searchButton, filterButton, messageButton, profileButton, menuButton, addRequestButton;
    private JComboBox<String> locationFilter, priceFilter, roomTypeFilter;
    private List<PGCard> pgCards;
    private JPanel slidePanel;
    private Timer slideTimer;
    private boolean isPanelVisible = false;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private String studentId;

    public StudentInformationPage(String studentId) {
        this.studentId = studentId;
        setTitle("PG Accommodation - Home");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false); // Prevent window resizing

        createHeaderPanel();
        createSearchPanel();
        createFilterPanel();
        createContentPanel();
        createSlidePanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(slidePanel, BorderLayout.WEST);
        slidePanel.setVisible(false);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(1000, 60));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);
        menuButton = createGradientButton("☰ Menu", primaryColor, secondaryColor);
        menuButton.addActionListener(e -> toggleSlidePanel());
        leftPanel.add(menuButton);

        JLabel titleLabel = new JLabel("PG Accommodation");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        addRequestButton = createGradientButton("Add Request", primaryColor, secondaryColor);
        addRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add Request button clicked"); // Debug print
                openAddRequestPage();
            }
        });
        rightPanel.add(addRequestButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void toggleSlidePanel() {
        if (isPanelVisible) {
            // Hide the panel
            slideTimer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (slidePanel.getWidth() > 0) {
                        slidePanel.setPreferredSize(new Dimension(slidePanel.getWidth() - 10, getHeight()));
                        revalidate();
                        repaint();
                    } else {
                        ((Timer)e.getSource()).stop();
                        slidePanel.setVisible(false);
                        isPanelVisible = false;
                    }
                }
            });
        } else {
            // Show the panel
            slidePanel.setPreferredSize(new Dimension(0, getHeight()));
            slidePanel.setVisible(true);
            slideTimer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (slidePanel.getWidth() < 200) {
                        slidePanel.setPreferredSize(new Dimension(slidePanel.getWidth() + 10, getHeight()));
                        revalidate();
                        repaint();
                    } else {
                        ((Timer)e.getSource()).stop();
                        isPanelVisible = true;
                    }
                }
            });
        }
        slideTimer.start();
    }

    private void createSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(secondaryColor);
        searchPanel.setPreferredSize(new Dimension(getWidth(), 60));

        searchField = new JTextField(30);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        searchButton = createGradientButton("Search", new Color(46, 204, 113), new Color(39, 174, 96));
        filterButton = createGradientButton("Filters", new Color(230, 126, 34), new Color(211, 84, 0));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(filterButton);

        filterButton.addActionListener(e -> toggleFilterPanel());
    }

    private void createFilterPanel() {
        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(236, 240, 241));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel filterLabel = new JLabel("Filters");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 18));

        locationFilter = new JComboBox<>(new String[]{"All Locations", "Location 1", "Location 2", "Location 3"});
        priceFilter = new JComboBox<>(new String[]{"All Prices", "Under $500", "$500 - $1000", "Over $1000"});
        roomTypeFilter = new JComboBox<>(new String[]{"All Types", "Single", "Double", "Triple"});

        filterPanel.add(filterLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterPanel.add(new JLabel("Location:"));
        filterPanel.add(locationFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(new JLabel("Price Range:"));
        filterPanel.add(priceFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(new JLabel("Room Type:"));
        filterPanel.add(roomTypeFilter);

        filterPanel.setVisible(false);
    }

    private void createContentPanel() {
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        pgCards = new ArrayList<>();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add sample PG cards (replace with actual data later)
        for (int i = 0; i < 12; i++) {
            PGCard card = new PGCard("PG Name " + (i + 1), "Location " + (i + 1), "$" + (500 + i * 100), "Single");
            pgCards.add(card);
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            contentPanel.add(card, gbc);
        }

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void toggleFilterPanel() {
        if (filterPanel.isVisible()) {
            remove(filterPanel);
            filterPanel.setVisible(false);
        } else {
            add(filterPanel, BorderLayout.WEST);
            filterPanel.setVisible(true);
        }
        revalidate();
        repaint();
    }

    private void createSlidePanel() {
        slidePanel = new JPanel();
        slidePanel.setLayout(new BoxLayout(slidePanel, BoxLayout.Y_AXIS));
        slidePanel.setBackground(new Color(44, 62, 80));
        slidePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        slidePanel.setPreferredSize(new Dimension(0, getHeight()));

        String[] options = {"Help", "Messages", "Log Out"};
        for (String option : options) {
            JButton button = createGradientButton(option, primaryColor, secondaryColor);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            button.addActionListener(e -> handleSlideMenuOption(option));
            slidePanel.add(button);
            slidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        slidePanel.setVisible(false);
    }

    private void handleSlideMenuOption(String option) {
        switch (option) {
            case "Help":
                // Implement help functionality
                JOptionPane.showMessageDialog(this, "Help functionality to be implemented.", "Help", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Messages":
                // Implement messages functionality
                JOptionPane.showMessageDialog(this, "Messages functionality to be implemented.", "Messages", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Log Out":
                // Implement logout functionality
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Perform logout actions here
                    dispose();
                    new Login(); // Assuming you have a Login class to return to the login screen
                }
                break;
        }
    }

    private JButton createGradientButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(
                    new Point(0, 0), 
                    color1, 
                    new Point(0, getHeight()), 
                    color2));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();

                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private class PGCard extends JPanel {
        public PGCard(String name, String location, String price, String roomType) {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(280, 200));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
            nameLabel.setForeground(new Color(41, 128, 185));

            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
            infoPanel.setOpaque(false);
            infoPanel.add(createInfoLabel("Location: " + location, new Color(52, 152, 219)));
            infoPanel.add(createInfoLabel("Price: " + price, new Color(46, 204, 113)));
            infoPanel.add(createInfoLabel("Room Type: " + roomType, new Color(230, 126, 34)));

            // Changed button text from "View Details" to "Interested"
            JButton interestedButton = createGradientButton("Interested", primaryColor, secondaryColor);
            interestedButton.setPreferredSize(new Dimension(280, 30));

            add(nameLabel, BorderLayout.NORTH);
            add(infoPanel, BorderLayout.CENTER);
            add(interestedButton, BorderLayout.SOUTH);
        }

        private JLabel createInfoLabel(String text, Color color) {
            JLabel label = new JLabel(text);
            label.setForeground(color);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            return label;
        }
    }

    private void openAddRequestPage() {
        System.out.println("openAddRequestPage method called"); // Debug print
        SwingUtilities.invokeLater(() -> {
            try {
                AddRequest addRequestPage = new AddRequest(this.studentId);
                addRequestPage.setVisible(true);
                // Remove this line to keep StudentInformationPage open
                // this.setVisible(false);
                System.out.println("AddRequest page created and set visible"); // Debug print
            } catch (Exception e) {
                e.printStackTrace(); // Print any exceptions that occur
                JOptionPane.showMessageDialog(this, "Error opening Add Request page: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        String studentId = ""; // Define and initialize the studentId variable
        SwingUtilities.invokeLater(() -> new StudentInformationPage(studentId));
    }
}