import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Interested{
    String NAME, EMAIL, MOBILE;
    public Interested(String NAME, String EMAIL, String MOBILE){
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.MOBILE = MOBILE;
    }
}
public class message extends JFrame {
    GridBagConstraints gbc = new GridBagConstraints();

    message(String request) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);  // Frame size
        this.setTitle("PG ROOM");
        this.setResizable(false);

        // Panel to hold the content that will be scrollable
        JPanel contentPanel = new JPanel(new GridBagLayout());

        // Fetch the list of interested users
        List<Interested> list = addlist(request);

        if (list.isEmpty()) {
            JLabel emptyLabel = new JLabel("No interested users found.");
            emptyLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
            contentPanel.add(emptyLabel);
        } else {
            gbc.insets = new Insets(10, 10, 10, 10);  // Set insets for spacing
            gbc.gridx = 0;
            gbc.gridy = 0;  // Initialize the Y position
           int i = 1;
            for (Interested interested : list) {
                JLabel messagelabel = new JLabel();

                // Set dynamic text from the Interested object
                messagelabel.setText("<html>Request "+i+"<br>Name: " + interested.NAME + "<br>Email: " + interested.EMAIL + "<br>Mobile: " + interested.MOBILE + "</html>");
                messagelabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));
                messagelabel.setBackground(new Color(2, 21, 34));
                messagelabel.setForeground(new Color(110, 172, 218));
                messagelabel.setOpaque(true);  // Needed to show background color
                messagelabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding for the label content

                gbc.gridy++;  // Move to the next row
                contentPanel.add(messagelabel, gbc);  // Add the label to the content panel
                i++;
            }
        }

        // Wrap the content panel in a JScrollPane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Increase scroll speed
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);  // Increase the scroll speed by setting a higher unit increment

        // Add the scroll pane to the frame
        this.add(scrollPane);

        this.setVisible(true);
    }
    private List<Interested> addlist(String request) {
        List<Interested> list = new ArrayList<>();
        try {
            new dbconnect();
            Connection connection = dbconnect.connection;

            // Fetch the message for the user with ID '789897'
            try (PreparedStatement ps = connection.prepareStatement("select * from user where userid = ?")) {
                ps.setString(1, request);
                try (ResultSet resultset = ps.executeQuery()) {
                    if (resultset.next()) {
                        String mesge = resultset.getString("message");
//                        System.out.println(mesge);
                        int i = 0;
                        int len = mesge.length();
                        if (Objects.equals(len, "null")) {
                            return list;
                        }
                        while (i < len) {
                            if (mesge.charAt(i) == '*') {
                                i++;
                            } else {
                                // Ensure we have enough characters to form a valid substring
                                if (i + 6 <= len) {
                                    String valid = mesge.substring(i, i + 6);

                                    // Fetch details for the 'valid' user ID
                                    try (PreparedStatement ps2 = connection.prepareStatement("select * from user where userid = ?")) {
                                        ps2.setString(1, valid);
                                        try (ResultSet resultset2 = ps2.executeQuery()) {
                                            if (resultset2.next()) {
                                                String Name = resultset2.getString("username");
                                                String Email = resultset2.getString("email");
                                                String Mobile = resultset2.getString("mobileno");
                                                list.add(new Interested(Name, Email, Mobile));
                                            }
                                        }
                                    }
                                }
                                i += 6;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void main(String[] args) {
       new message("789897");
    }
}
