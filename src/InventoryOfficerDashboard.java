package src;

import javax.swing.*;
import java.awt.*;

public class InventoryOfficerDashboard extends JFrame {
    private JComboBox<String> actionComboBox;
    private JPanel leftPanel;
    private JPanel displayPanel;

    public InventoryOfficerDashboard() {
        super("Inventory Officer Dashboard");
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        getContentPane().setBackground(new Color(245, 245, 245));

        leftPanel = new JPanel(new GridLayout(5, 2, 10, 20));
        leftPanel.setBackground(new Color(230, 230, 250));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        actionComboBox = new JComboBox<>(new String[]{"All Inventory", "Update Item", "Add Item"});
        actionComboBox.setFont(new Font("Arial", Font.BOLD, 14));
        actionComboBox.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(230, 230, 250));
        topPanel.add(actionComboBox);

        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(displayPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        showAllInventoryPanel();

        actionComboBox.addActionListener(e -> {
            String selectedAction = (String) actionComboBox.getSelectedItem();
            leftPanel.removeAll();
            displayPanel.removeAll();

            switch (selectedAction) {
                case "All Inventory":
                    showAllInventoryPanel();
                    break;
                case "Update Item":
                    showUpdateItemPanel();
                    break;
                case "Add Item":
                    showAddItemPanel();
                    break;
            }

            leftPanel.revalidate();
            leftPanel.repaint();
            displayPanel.revalidate();
            displayPanel.repaint();
        });

        setVisible(true);
    }

    private void showAllInventoryPanel() {
        JLabel searchLabel = new JLabel("Search Item:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));

        leftPanel.add(searchLabel);
        leftPanel.add(searchField);

        for (int i = 0; i < 4; i++) {
            leftPanel.add(new JLabel());
            leftPanel.add(new JLabel());
        }

        searchField.addActionListener(e -> {
            displayPanel.removeAll();
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                displayAllItems();
            } else {
                displayFilteredItems(searchText);
            }
            displayPanel.revalidate();
            displayPanel.repaint();
        });

        displayAllItems();
    }

    private void showUpdateItemPanel() {
        JLabel productNameLabel = new JLabel("Product Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel quantityLabel = new JLabel("Quantity:");

        productNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField productNameField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        JTextField quantityField = new JTextField(10);

        productNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton updateButton = new JButton("Update Item");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(new Color(173, 216, 230));

        leftPanel.add(productNameLabel);
        leftPanel.add(productNameField);
        leftPanel.add(priceLabel);
        leftPanel.add(priceField);
        leftPanel.add(quantityLabel);
        leftPanel.add(quantityField);
        leftPanel.add(new JLabel());
        leftPanel.add(updateButton);

        updateButton.addActionListener(e -> {
            String name = productNameField.getText().trim();
            String priceText = priceField.getText().trim();
            String qtyText = quantityField.getText().trim();

            if (!name.isEmpty() && !priceText.isEmpty() && !qtyText.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceText);
                    int quantity = Integer.parseInt(qtyText);
                    String item = "Product Name: " + name + ", Price: Rs " + price + ", Stock: " + quantity;

                    displayPanel.removeAll();
                    displayPanel.add(new JLabel("Updated Item:"));
                    displayPanel.add(new JLabel(item));
                    displayPanel.revalidate();
                    displayPanel.repaint();

                    productNameField.setText("");
                    priceField.setText("");
                    quantityField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price or quantity!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
            }
        });
    }

    private void showAddItemPanel() {
        JLabel productNameLabel = new JLabel("Product Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel quantityLabel = new JLabel("Quantity:");

        productNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField productNameField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        JTextField quantityField = new JTextField(10);

        productNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton addButton = new JButton("Add Item");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(144, 238, 144));

        leftPanel.add(productNameLabel);
        leftPanel.add(productNameField);
        leftPanel.add(priceLabel);
        leftPanel.add(priceField);
        leftPanel.add(quantityLabel);
        leftPanel.add(quantityField);
        leftPanel.add(new JLabel());
        leftPanel.add(addButton);

        addButton.addActionListener(e -> {
            String name = productNameField.getText().trim();
            String priceText = priceField.getText().trim();
            String qtyText = quantityField.getText().trim();

            if (!name.isEmpty() && !priceText.isEmpty() && !qtyText.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceText);
                    int quantity = Integer.parseInt(qtyText);
                    String item = "Product Name: " + name + ", Price: Rs " + price + ", Stock: " + quantity;

                    displayPanel.removeAll();
                    displayPanel.add(new JLabel("Added Item:"));
                    displayPanel.add(new JLabel(item));
                    displayPanel.revalidate();
                    displayPanel.repaint();

                    productNameField.setText("");
                    priceField.setText("");
                    quantityField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price or quantity!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
            }
        });
    }

    private void displayAllItems() {
        displayPanel.add(new JLabel("All Inventory:"));
        String[] defaultItems = {
                "Product Name: Tiger Plushie, Price: Rs 45.00, Stock: 34",
                "Product Name: Elephant Lamp, Price: Rs 30.00, Stock: 28",
                "Product Name: Cozy Hoodie, Price: Rs 95.00, Stock: 15",
                "Product Name: Classic T-shirt, Price: Rs 65.00, Stock: 22",
                "Product Name: Jogger Sweatpants, Price: Rs 85.00, Stock: 25",
                "Product Name: Sporty T-shirt, Price: Rs 130.00, Stock: 19",
                "Product Name: Leather Jacket, Price: Rs 380.00, Stock: 5"
        };
        for (String item : defaultItems) {
            JLabel itemLabel = new JLabel(item);
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            displayPanel.add(itemLabel);
        }
    }

    private void displayFilteredItems(String searchText) {
        displayPanel.add(new JLabel("Search Results for '" + searchText + "':"));
        String[] defaultItems = {
                "Product Name: Tiger Plushie, Price: Rs 35.00, Stock: 34",
                "Product Name: Elephant Lamp, Price: Rs 25.00, Stock: 28",
                "Product Name: Cozy Hoodie, Price: Rs 85.00, Stock: 15",
                "Product Name: Classic T-shirt, Price: Rs 55.00, Stock: 22",
                "Product Name: Jogger Sweatpants, Price: Rs 75.00, Stock: 25",
                "Product Name: Sporty T-shirt, Price: Rs 120.00, Stock: 19",
                "Product Name: Leather Jacket, Price: Rs 280.00, Stock: 5"
        };
        for (String item : defaultItems) {
            if (item.toLowerCase().contains(searchText.toLowerCase())) {
                JLabel itemLabel = new JLabel(item);
                itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                displayPanel.add(itemLabel);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryOfficerDashboard::new);
    }
}