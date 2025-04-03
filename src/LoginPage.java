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
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.setBackground(Color.GREEN);
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
                        new InventoryOfficerDashboard().setVisible(true);
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