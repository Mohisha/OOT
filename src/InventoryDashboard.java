package src;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

public class InventoryDashboard extends JFrame {
    private int sessionId = 1;

    private JLabel sessionLabel;
    private JButton order_new_stock_button;
    private JComboBox<String> sizeOption, productIdComboBox;
    private JPanel orderDisplayPanel, inputPanel, rightMainPanel, leftMainPanel, sessionPanel, buttonPanel; // Corrected leftMainPane1 to leftMainPanel
    private JTextField quantityField, productNameTextField;
    private JButton update_item_stock_button, updateInventoryButton;

    public InventoryDashboard() {
        super("Inventory Dashboard");

        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);

        // left panel for inputs 
        JPanel leftMainPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Added buttonPanel with FlowLayout
        sessionPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Session panel
        sessionLabel = new JLabel("Inventory Session ID: " + sessionId);
        updateInventoryButton = createStyledButton("Update Inventory");
        sessionPanel.add(sessionLabel);
        sessionPanel.add(updateInventoryButton);
        sessionPanel.setBorder(create_border("Inventory Mode"));

        // Product detail input panel
        productIdComboBox = new JComboBox<>(Database.fetch_item_ids());
        quantityField = new JTextField();
        sizeOption = new JComboBox<>(new String[]{"Small", "Medium", "Large"});
        order_new_stock_button = createStyledButton("Order New Stock");
        update_item_stock_button = createStyledButton("Update Item");

        // Display panel
        orderDisplayPanel = new JPanel();
        orderDisplayPanel.setLayout(new BoxLayout(orderDisplayPanel, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderDisplayPanel);

        checkStockLevel();

        // Add input panel to leftMainPanel
        create_order_input_panel();
  
        leftMainPanel.add(inputPanel, BorderLayout.NORTH);
    

        // right panel for display
        rightMainPanel = new JPanel(new BorderLayout());
        rightMainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        rightMainPanel.add(bottomPanel, BorderLayout.SOUTH);
        rightMainPanel.setBorder(create_border("Inventory Items Needing Restock."));

        // Add main panels to the main frame
        add(leftMainPanel);
        add(rightMainPanel);

        // Add action listeners
        update_item_stock_button.addActionListener(new ButtonHandler());
        updateInventoryButton.addActionListener(new ButtonHandler());
        order_new_stock_button.addActionListener(new ButtonHandler());
        productIdComboBox.addItemListener(new ProductIDHandler());

        setVisible(true);
    }

    public class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == update_item_stock_button) {
                String productId = (String) productIdComboBox.getSelectedItem();
                String quantity = quantityField.getText();
                String size = (String) sizeOption.getSelectedItem();

                if (!productId.isEmpty() && !quantity.isEmpty() && size != null && !size.isEmpty()) {
                    Database.update_item_stock(productId, Integer.valueOf(quantity));
                    JOptionPane.showMessageDialog(null, "Item Stock Updated");
                    orderDisplayPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Fill all fields!");
                }
            }

            if (ae.getSource() == order_new_stock_button) {
                String productId = (String) productIdComboBox.getSelectedItem();
                String quantity = quantityField.getText();
                Database.create_reorder(productId, Integer.valueOf(quantity));

                JOptionPane.showMessageDialog(null, "Inventory updated successfully!");
            }
        }
    }

    public class ProductIDHandler implements ItemListener {
        public void itemStateChanged(ItemEvent ie) {
            String item = productIdComboBox.getSelectedItem().toString();
            productNameTextField.setText(Database.get_item_name_by_id(item));
            quantityField.setText(String.valueOf(Database.get_item_stock_by_id(item)));
        }
    }

    public void create_order_input_panel() {
        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(productIdComboBox);

        inputPanel.add(new JLabel("Product Name:"));
        productNameTextField = new JTextField();
        productNameTextField.setEditable(false);
        inputPanel.add(productNameTextField);

        inputPanel.add(new JLabel("Set New Stock Level:"));
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Size:"));
        inputPanel.add(sizeOption);

        inputPanel.add(order_new_stock_button);
        inputPanel.add(new JLabel());
        inputPanel.add(update_item_stock_button);

        inputPanel.setBorder(create_border("Inventory Inputs"));
    }

    public void checkStockLevel() {
        String[] low_stock_items = Database.fetch_low_stock_items();
        for (String line : low_stock_items) {
            orderDisplayPanel.add(new JLabel(line));
        }
    }

    public Border create_border(String title) {
        Border first = BorderFactory.createBevelBorder(EtchedBorder.RAISED);
        Border second = BorderFactory.createTitledBorder(title);

        return BorderFactory.createCompoundBorder(first, second);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.blue);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryDashboard::new);
    }
}