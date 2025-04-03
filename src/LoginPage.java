package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends JFrame {
    private JButton loginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        super("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBackground(new Color(240, 248, 255)); // AliceBlue background

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(0, 0, 128)); // Navy text
        usernameField = new JTextField();
        usernameField.setBackground(new Color(255, 255, 240)); // Ivory background

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(0, 0, 128)); // Navy text
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(255, 255, 240)); // Ivory background

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(152, 251, 152)); // LightGreen button
        loginButton.setForeground(new Color(0, 100, 0)); // DarkGreen text
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(new LoginHandler());
    }

    public class LoginHandler implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            String username = usernameField.getText();
            String password = PasswordHasher.hash(new String(passwordField.getPassword()));

            String query = "SELECT * FROM user WHERE username=\"" + username + "\" AND password=\"" + password + "\";";

            ResultSet resultSet = database.executeQuery(query);
            try {
                if (resultSet.next() && resultSet.getString("password").equals(password)) {
                    JOptionPane.showMessageDialog(null, "Successfully Logged In: " + username);
                    if (resultSet.getString("role").equals("Salesperson")) {
                        new SalesPersonDashboard().setVisible(true);
                    } else if (resultSet.getString("role").equals("Inventory Officer")) {
                        new InventoryDashboard().setVisible(true);
                    }

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect credentials " + username);
                }
            } catch (SQLException se) {
                System.out.println("Error: " + se.getCause());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginPage().setVisible(true);
            }
        });
    }
}