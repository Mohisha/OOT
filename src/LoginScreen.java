package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, exitButton;

    public LoginScreen() {
        // Set up the frame
        setTitle("Salesperson Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form panel for username and password
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginButton = new JButton("Login");
        exitButton = new JButton("Exit");

        buttonPanel.add(loginButton);

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateSalesperson();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Handle Enter key in password field
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    authenticateSalesperson();
                }
            }
        });

        // Set main panel to frame
        add(mainPanel);

        // Make this frame visible
        setVisible(true);
    }

    private void authenticateSalesperson() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Salesperson salesperson = SalespersonDAO.authenticate(username, password);

        if (salesperson != null) {
            // Start the main application
            new SalesApplication(salesperson);
            this.dispose();
        } else {
            // Login failed
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);

            // Clear password field
            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        // Set the look and feel to match the system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize database connection
        Database.getConnection();

        // Start the login screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen();
            }
        });
    }
}
