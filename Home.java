import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Home extends JFrame implements ActionListener {
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu editMenu;
    JMenu helpMenu;
    JMenuItem Addrequest;
    JMenuItem message;
    JMenuItem Favorite;
    JMenuItem logout;
    JMenuItem exit;
    ImageIcon requesticon;
    ImageIcon messageicon;
    ImageIcon favoriteicon;
    ImageIcon logouticon;
    ImageIcon exiticon;
    ImageIcon menuicon;
    ImageIcon editicon;
    ImageIcon helpicon;


    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();  // Get the original image
        Image resizedImage = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // Resize the image
        return new ImageIcon(resizedImage);  // Return the resized image as an icon
    }

    public Home() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 600);
        this.setTitle("PG ROOM");
        this.setResizable(false);

        JLabel rightlable = new JLabel();
        rightlable.setBounds(0, 0, 450, 600);
        rightlable.setBackground(Color.black);
        rightlable.setOpaque(true);

        JLabel leftLabel = new JLabel();
        leftLabel.setBounds(450, 0, 450, 600);

// Load and scale the image to fit the JLabel
//        ImageIcon backgroundImage = new ImageIcon("src/images/Designer.png");
//        Image img = backgroundImage.getImage();
//        Image scaledImg = img.getScaledInstance(leftLabel.getWidth(), leftLabel.getHeight(), Image.SCALE_SMOOTH);
//
//// Set the scaled image as the icon
//        ImageIcon scaledIcon = new ImageIcon(scaledImg);
//        leftLabel.setIcon(scaledIcon);
//        leftLabel.setOpaque(true); // Optional: use this only if you are setting a background color or need the label to be non-transparent

// Add the JLabel to your frame or panel

//        requesticon = new ImageIcon("C:\\Users\\sanju\\IdeaProjects\\PGroom\\src\\img.png");
        requesticon = resizeIcon(new ImageIcon("src/images/Addrequest.png"), 35, 35);
        messageicon = resizeIcon(new ImageIcon("src/images/Message.png"), 35, 35);
        favoriteicon = resizeIcon(new ImageIcon("src/images/Favorite.png"), 35, 35);
        logouticon = resizeIcon(new ImageIcon("src/images/Logout.png"), 35, 35);
        exiticon = resizeIcon(new ImageIcon("src/images/Exit.png"), 35, 35);
        menuicon = resizeIcon(new ImageIcon("src/images/Menu.png"), 35, 35);
        editicon = resizeIcon(new ImageIcon("src/images/Edit.png"), 35, 35);
        helpicon = resizeIcon(new ImageIcon("src/images/Help.png"), 35, 35);

        // Initialize menu bar and menus
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Menu");
        fileMenu.setIcon(menuicon);
        fileMenu.setFont(new Font(Font.SERIF, Font.BOLD, 30));

        editMenu = new JMenu("Edit");
        editMenu.setIcon(editicon);
        editMenu.setFont(new Font(Font.SERIF, Font.BOLD, 30));


        helpMenu = new JMenu("Help");
        helpMenu.setIcon(helpicon);
        helpMenu.setFont(new Font(Font.SERIF, Font.BOLD, 30));



        // Initialize menu items
        Addrequest = new JMenuItem("MakeRequest");
        message = new JMenuItem("Message");
        Favorite = new JMenuItem("Favorite");
        logout = new JMenuItem("Logout");
        exit = new JMenuItem("Exit");

        // Apply style to all menu items
        setMenuItemStyle(Addrequest, requesticon, KeyEvent.VK_A);
        setMenuItemStyle(message, messageicon, KeyEvent.VK_M);
        setMenuItemStyle(Favorite, favoriteicon, KeyEvent.VK_F);
        setMenuItemStyle(logout, logouticon, KeyEvent.VK_L);
        setMenuItemStyle(exit, exiticon, KeyEvent.VK_E);

        // Add menu items to the file menu
        fileMenu.add(Addrequest);
        fileMenu.add(message);
        fileMenu.add(Favorite);
        fileMenu.add(logout);
        fileMenu.add(exit);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        // Set menu bar properties
        menuBar.setBounds(0, 0, 900, 40);
        menuBar.setFont(new Font(Font.SERIF, Font.BOLD, 5));
        menuBar.setVisible(true);

        // Add action listeners
        Addrequest.addActionListener(this);
        message.addActionListener(this);
        Favorite.addActionListener(this);
        logout.addActionListener(this);
        exit.addActionListener(this);

        // Add menu bar and labels to the frame
        this.add(menuBar);
//        this.add(rightlable);
//        this.add(leftLabel);

        // Set the frame layout and make it visible
        this.setLayout(null);
        this.setVisible(true);
    }

    // Method to set the style for each menu item
    private void setMenuItemStyle(JMenuItem menuItem, ImageIcon icon, int mnemonic) {
        menuItem.setBounds(0, 0, 150, 20);
        menuItem.setFont(new Font(Font.SERIF, Font.BOLD, 25));
        menuItem.setIcon(icon);
        menuItem.setMnemonic(mnemonic);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Addrequest) {
            new Addrequest("738126");
            // Addrequest logic here
        } else if (e.getSource() == message) {
            // message logic here
        } else if (e.getSource() == Favorite) {
            // Favorite logic here
        } else if (e.getSource() == logout) {
            // logout logic here
             dispose();
            new login();

        } else if (e.getSource() == exit) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Home();
    }
}
