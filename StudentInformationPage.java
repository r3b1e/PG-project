import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private final int SLIDE_SPEED = 10;
    private final int PANEL_WIDTH = 200;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color accentColor = new Color(230, 126, 34);
    private String studentId;

    public StudentInformationPage(String studentId) {
        System.out.println("Starting StudentInformationPage constructor");
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
        if (slideTimer != null && slideTimer.isRunning()) {
            slideTimer.stop();
        }

        slideTimer = new Timer(5, new ActionListener() {
            int width = isPanelVisible ? PANEL_WIDTH : 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPanelVisible) {
                    width -= SLIDE_SPEED;
                    if (width <= 0) {
                        width = 0;
                        isPanelVisible = false;
                        ((Timer)e.getSource()).stop();
                        slidePanel.setVisible(false);
                    }
                } else {
                    width += SLIDE_SPEED;
                    if (width >= PANEL_WIDTH) {
                        width = PANEL_WIDTH;
                        isPanelVisible = true;
                        ((Timer)e.getSource()).stop();
                    }
                }
                slidePanel.setPreferredSize(new Dimension(width, getHeight()));
                slidePanel.revalidate();
                repaint();
            }
        });

        if (!isPanelVisible) {
            slidePanel.setVisible(true);
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
        System.out.println("createContentPanel started");
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        pgCards = new ArrayList<>();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add dummy data
        for (int i = 0; i < 10; i++) {
            String name = "PG " + (i + 1);
            String location = "City " + (i % 3 + 1);
            String price = "₹" + (5000 + i * 1000);
            String roomType = (i % 3 == 0) ? "1BHK" : (i % 3 == 1) ? "2BHK" : "3BHK";
            String institute = "Institute " + (i % 5 + 1);
            String distance = (i * 2) + " km";
            String vacancy = (i % 3 + 1) + "";
            String gender = (i % 2 == 0) ? "Male" : "Female";
            String additionalAddress = "Additional Address " + (i + 1);
            String aboutSelf = "About PG " + (i + 1);
            String phoneNumber = "98765" + (10000 + i);
            String address = "Street " + (i + 1) + ", Area " + (i % 5 + 1) + ", City " + (i % 3 + 1);

            PGCard card = new PGCard(name, location, price, roomType, institute, distance, vacancy, gender, additionalAddress, aboutSelf, phoneNumber, address);
            pgCards.add(card);

            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            contentPanel.add(card, gbc);
        }

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        System.out.println("createContentPanel finished");
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
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 15, 15);
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
        private String name, location, price, roomType, institute, distance, vacancy, gender, additionalAddress, aboutSelf, phoneNumber, address;

        public PGCard(String name, String location, String price, String roomType, String institute, String distance, String vacancy, String gender, String additionalAddress, String aboutSelf, String phoneNumber, String address) {
            this.name = name;
            this.location = location;
            this.price = price;
            this.roomType = roomType;
            this.institute = institute;
            this.distance = distance;
            this.vacancy = vacancy;
            this.gender = gender;
            this.additionalAddress = additionalAddress;
            this.aboutSelf = aboutSelf;
            this.phoneNumber = phoneNumber;
            this.address = address;

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

            JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            infoPanel.setOpaque(false);
            infoPanel.add(createInfoLabel("Location: " + location, new Color(52, 152, 219)));
            infoPanel.add(createInfoLabel("Price: " + price, new Color(46, 204, 113)));
            infoPanel.add(createInfoLabel("Room Type: " + roomType, new Color(230, 126, 34)));
            infoPanel.add(createInfoLabel("Distance: " + distance + " km", new Color(155, 89, 182)));

            JButton interestedButton = createGradientButton("Show Details", primaryColor, secondaryColor);
            interestedButton.addActionListener(e -> showDetailedPopup());

            add(nameLabel, BorderLayout.NORTH);
            add(infoPanel, BorderLayout.CENTER);
            add(interestedButton, BorderLayout.SOUTH);
        }

        private void showDetailedPopup() {
            JDialog popup = new JDialog(StudentInformationPage.this, "PG Details", true);
            popup.setSize(550, 500);
            popup.setLocationRelativeTo(this);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(new Color(240, 248, 255)); // Light blue background

            JLabel titleLabel = new JLabel(name);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(new Color(41, 128, 185)); // Blue color
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            detailsPanel.setOpaque(false);
            detailsPanel.add(createDetailLabel("Location", location));
            detailsPanel.add(createDetailLabel("Price", price));
            detailsPanel.add(createDetailLabel("Room Type", roomType));
            detailsPanel.add(createDetailLabel("Institute", institute));
            detailsPanel.add(createDetailLabel("Distance", distance));
            detailsPanel.add(createDetailLabel("Vacancy", vacancy));
            detailsPanel.add(createDetailLabel("Gender", gender));
            detailsPanel.add(createDetailLabel("Phone", phoneNumber));
            detailsPanel.add(createDetailLabel("Address", address));
            detailsPanel.add(createDetailLabel("Additional Address", additionalAddress));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setOpaque(false);

            JButton interestedButton = createGradientButton("Interested", new Color(46, 204, 113), new Color(39, 174, 96));
            interestedButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(popup, "You've expressed interest in this PG. The owner will be notified.", "Interest Registered", JOptionPane.INFORMATION_MESSAGE);
            });

            JButton closeButton = createGradientButton("Close", new Color(231, 76, 60), new Color(192, 57, 43));
            closeButton.addActionListener(e -> popup.dispose());

            buttonPanel.add(interestedButton);
            buttonPanel.add(closeButton);

            contentPanel.add(titleLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            contentPanel.add(detailsPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            contentPanel.add(buttonPanel);

            popup.add(contentPanel);
            popup.setVisible(true);
        }

        private JPanel createDetailLabel(String label, String value) {
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setOpaque(false);

            JLabel labelComponent = new JLabel(label + ":");
            labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
            labelComponent.setForeground(new Color(52, 73, 94)); // Dark blue-gray color

            JLabel valueComponent = new JLabel(value);
            valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
            valueComponent.setForeground(new Color(44, 62, 80)); // Darker blue-gray color

            panel.add(labelComponent, BorderLayout.WEST);
            panel.add(valueComponent, BorderLayout.CENTER);

            return panel;
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
        System.out.println("Starting main method");
        System.out.println("Starting StudentInformationPage");
        String studentId = "test123"; // Use a test student ID
        SwingUtilities.invokeLater(() -> {
            System.out.println("Creating StudentInformationPage");
            StudentInformationPage page = new StudentInformationPage(studentId);
            System.out.println("Setting StudentInformationPage visible");
            page.setVisible(true);
        });
    }
}