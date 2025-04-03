package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InventoryOfficerDashboard extends JFrame {
    private JComboBox<String> actionComboBox;
    private JPanel leftPanel;
    private JPanel displayPanel; // Right panel to display information
    private ArrayList<String> inventoryItems = new ArrayList<>(); // Store items for display

    public InventoryOfficerDashboard() {
        super("Inventory Officer Dashboard");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        // Left panel for input
        leftPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        actionComboBox = new JComboBox<>(new String[] { "All Inventory", "Update Item", "Add Item" });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(actionComboBox);

        // Right panel for display
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(displayPanel);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Initial state: All Inventory
        showAllInventoryPanel();

        // Action listener for JComboBox
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

    // Panel for "All Inventory"
    private void showAllInventoryPanel() {
        JLabel searchLabel = new JLabel("Search Item:");
        JTextField searchField = new JTextField();

        leftPanel.add(searchLabel);
        leftPanel.add(searchField);

        // Fill remaining rows with empty labels to maintain grid layout
        for (int i = 0; i < 4; i++) {
            leftPanel.add(new JLabel());
            leftPanel.add(new JLabel());
        }

        searchField.addActionListener(e -> {
            displayPanel.removeAll();
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                // Display all items
                displayAllItems();
            } else {
                // Filter items based on search
                displayFilteredItems(searchText);
            }
            displayPanel.revalidate();
            displayPanel.repaint();
        });

        // Initial display of all items
        displayAllItems();
    }

    // Panel for "Update Item"
    private void showUpdateItemPanel() {
        JLabel productNameLabel = new JLabel("Product Name:");
        JTextField productNameField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JButton updateButton = new JButton("Update Item");

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

                    // Update logic (placeholder: just display for now)
                    displayPanel.removeAll();
                    displayPanel.add(new JLabel("Updated Item:"));
                    displayPanel.add(new JLabel(item));
                    displayPanel.revalidate();
                    displayPanel.repaint();

                    // Clear fields
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

    // Panel for "Add Item"
    private void showAddItemPanel() {
        JLabel productNameLabel = new JLabel("Product Name:");
        JTextField productNameField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JButton addButton = new JButton("Add Item");

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

                    // Add to inventory //Placeholder
                    inventoryItems.add(item);
                    displayPanel.removeAll();
                    displayPanel.add(new JLabel("Added Item:"));
                    displayPanel.add(new JLabel(item));
                    displayPanel.revalidate();
                    displayPanel.repaint();

                    // Clear fields
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
        // Updated inventory data with T-shirt, Short, Bikini
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
            displayPanel.add(new JLabel(item));
        }
        for (String item : inventoryItems) {
            displayPanel.add(new JLabel(item));
        }
    }

    // Helper method to display filtered items
    private void displayFilteredItems(String searchText) {
        displayPanel.add(new JLabel("Search Results for '" + searchText + "':"));
        // Updated inventory data with T-shirt, Short, Bikini
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
            if (item.toLowerCase().contains(searchText.toLowerCase())) {
                displayPanel.add(new JLabel(item));
            }
        }
        for (String item : inventoryItems) {
            if (item.toLowerCase().contains(searchText.toLowerCase())) {
                displayPanel.add(new JLabel(item));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryOfficerDashboard::new);
    }
}