package src;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class SalesPersonDashboard extends JFrame {
    private ArrayList<OrderEntry> orders = new ArrayList<>();
    private List<Order> order_list = new ArrayList<>();

    private JLabel totalPriceLabel, currentTotalLabel, noItemsLabel;
    private JComboBox<String> transportPreferenceField, sizeOption, productIdComboBox;
    private double totalPrice = 0.0;
    private JPanel orderDisplayPanel, inputPanel, rightMainPanel, sessionPanel;
    private JTextField firstNameField, lastNameField, contactField, quantityField, addressField, emailField, unitPriceTextField;
    private JButton addButton, customerDetailsButton, checkoutButton, saveButton;
    private Customer customer;
    private Order order;

    private static class OrderEntry {
        String orderText;

        public OrderEntry(String orderText) {
            this.orderText = orderText;
        }
    }

    public SalesPersonDashboard() {
        super("Sales Person Dashboard");

        setLayout(new BorderLayout(5, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        getContentPane().setBackground(new Color(240, 248, 255));

        JPanel leftMainPanel = new JPanel(new BorderLayout(5, 5));
        leftMainPanel.setForeground(new Color(224, 0, 255));
        inputPanel = new JPanel(new GridBagLayout());
        sessionPanel = new JPanel(new GridBagLayout());

        customerDetailsButton = createStyledButton("Register a customer");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        sessionPanel.add(customerDetailsButton, gbc);
        sessionPanel.setBorder(create_border("Customer Details"));
        sessionPanel.setPreferredSize(new Dimension(sessionPanel.getPreferredSize().width, 150));

        productIdComboBox = new JComboBox<>(Database.fetch_item_ids());
        quantityField = new JTextField();
        sizeOption = new JComboBox<>(new String[]{"Small", "Medium", "Large"});
        currentTotalLabel = new JLabel("Current Total : Rs 0.0");
        addButton = createStyledButton("Add To Order");

        orderDisplayPanel = new JPanel();
        orderDisplayPanel.setLayout(new BoxLayout(orderDisplayPanel, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderDisplayPanel);

        create_order_input_panel();
        inputPanel.setSize(500, 500);

        leftMainPanel.add(sessionPanel, BorderLayout.NORTH);
        leftMainPanel.add(inputPanel, BorderLayout.CENTER);

        rightMainPanel = new JPanel(new BorderLayout());
        rightMainPanel.add(scrollPane, BorderLayout.CENTER);
        totalPriceLabel = new JLabel("Total Price : Rs 0.0");
        totalPriceLabel.setForeground(Color.blue);
        checkoutButton = createStyledButton("Proceed to Checkout");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalPriceLabel, BorderLayout.NORTH);
        bottomPanel.add(checkoutButton, BorderLayout.SOUTH);
        bottomPanel.setPreferredSize(new Dimension(bottomPanel.getWidth(), 250));
        bottomPanel.setBorder(create_border("Final Bill"));
        rightMainPanel.add(bottomPanel, BorderLayout.SOUTH);

        scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), 250));
        rightMainPanel.add(scrollPane, BorderLayout.CENTER);
        rightMainPanel.setBorder(create_border("Invoice Summary"));

        add(leftMainPanel, BorderLayout.WEST);
        add(rightMainPanel, BorderLayout.CENTER);

        addButton.addActionListener(new ButtonHandler());
        checkoutButton.addActionListener(new ButtonHandler());
        customerDetailsButton.addActionListener(new ButtonHandler());
        productIdComboBox.addItemListener(new ProductIDHandler());

        noItemsLabel = new JLabel("No items have been added yet.");
        orderDisplayPanel.add(noItemsLabel);

        setVisible(true);
    }

    private class CustomerDetailsFrame extends JFrame {

        public CustomerDetailsFrame() {
            super("Customer Details");
            setLayout(new GridLayout(7, 2));
            getContentPane().setBackground(new Color(240, 248, 255));
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            firstNameField = new JTextField();
            lastNameField = new JTextField();
            contactField = new JTextField();
            addressField = new JTextField();
            emailField = new JTextField();
            saveButton = new JButton("Save");

            add(new JLabel("First Name:"));
            add(firstNameField);
            add(new JLabel("Last Name:"));
            add(lastNameField);
            add(new JLabel("Contact:"));
            add(contactField);
            add(new JLabel("Address:"));
            add(addressField);
            add(new JLabel("Email:"));
            add(emailField);
            add(new JLabel("Transport Preference"));
            add(transportPreferenceField = new JComboBox<>(Database.fetch_transport_methods()));
            add(new JLabel());
            add(saveButton);

            saveButton.addActionListener(e -> {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String contact = contactField.getText();
                String address = addressField.getText();
                String email = emailField.getText();
                String transport = (String) transportPreferenceField.getSelectedItem();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !contact.isEmpty() && !address.isEmpty() && !email.isEmpty()) {
                    customer = new Customer(firstName, lastName, contact, email, address, transport);
                    JOptionPane.showMessageDialog(null, "Customer details saved");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                }
            });
            setVisible(true);
        }
    }

    public class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == addButton) {
                String productId = (String) productIdComboBox.getSelectedItem();
                String quantity = quantityField.getText();
                String size = (String) sizeOption.getSelectedItem();

                if (!productId.isEmpty() && !quantity.isEmpty() && size != null && !size.isEmpty()) {
                    try {
                        int qty = Integer.parseInt(quantity);
                        double price = qty * Double.parseDouble(unitPriceTextField.getText());
                        totalPrice += price;

                        currentTotalLabel.setText("Current Item Total: Rs " + price);
                        totalPriceLabel.setText("Order Total Rs: " + totalPrice);

                        String order_string = "Product ID: " + productId + ", Quantity: " + qty + ", Size: " + size + ", Price: " + price + " Rs";

                        JPanel orderPanel = new JPanel(new BorderLayout());
                        JLabel orderLabel = new JLabel(order_string);
                        JButton removeButton = new JButton("X");
                        removeButton.setForeground(Color.RED);
                        orderPanel.add(orderLabel, BorderLayout.CENTER);
                        orderPanel.add(removeButton, BorderLayout.EAST);

                        OrderEntry orderEntry = new OrderEntry(order_string);
                        orders.add(orderEntry);

                        orderDisplayPanel.add(orderPanel, 0);

                        if (orders.size() == 1 && noItemsLabel != null) {
                            orderDisplayPanel.remove(noItemsLabel);
                            noItemsLabel = null;
                        }

                        removeButton.addActionListener(re -> {
                            orders.remove(orderEntry);
                            orderDisplayPanel.remove(orderPanel);
                            totalPrice -= price;
                            totalPriceLabel.setText("Total Price Rs: " + totalPrice);
                            orderDisplayPanel.revalidate();
                            orderDisplayPanel.repaint();
                        });

                        orderDisplayPanel.revalidate();
                        orderDisplayPanel.repaint();

                        productIdComboBox.setSelectedIndex(0);
                        quantityField.setText("");
                        sizeOption.setSelectedIndex(0);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid quantity!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Fill all fields!");
                }
                productIdComboBox.setSelectedIndex(0);
                quantityField.setText("");
                sizeOption.setSelectedIndex(0);
            }
            if (ae.getSource() == customerDetailsButton) {
                new CustomerDetailsFrame();
            }
            if (ae.getSource() == checkoutButton) {
                if (totalPrice > 0) {
                    new Checkout(customer, order_list);
                } else {
                    JOptionPane.showMessageDialog(null, "No items to checkout!");
                }
            }
        }
    }

    public class ProductIDHandler implements ItemListener {
        public void itemStateChanged(ItemEvent ie) {
            String item = productIdComboBox.getSelectedItem().toString();
            unitPriceTextField.setText(String.valueOf(Database.get_item_price_by_id(item)));
        }
    }

    public void create_order_input_panel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridy = 2;
        inputPanel.add(new JLabel("Item ID:"), gbc);
        gbc.gridx++;
        productIdComboBox.setPreferredSize(new Dimension(150, 40));
        inputPanel.add(productIdComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Price per unit: Rs "), gbc);
        gbc.gridx++;
        unitPriceTextField = new JTextField();
        unitPriceTextField.setPreferredSize(new Dimension(150, 40));
        unitPriceTextField.setEditable(false);
        inputPanel.add(unitPriceTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx++;
        quantityField.setPreferredSize(new Dimension(150, 40));
        inputPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Size:"), gbc);
        gbc.gridx++;
        sizeOption.setPreferredSize(new Dimension(150, 40));
        inputPanel.add(sizeOption, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        inputPanel.add(currentTotalLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        addButton.setPreferredSize(new Dimension(200, addButton.getPreferredSize().height));
        inputPanel.add(addButton, gbc);

        inputPanel.revalidate();
        inputPanel.repaint();

        inputPanel.setBorder(create_border("Order Details"));
        orderDisplayPanel.revalidate();
        orderDisplayPanel.repaint();
    }

    public Border create_border(String title) {
        Border first = BorderFactory.createBevelBorder(EtchedBorder.RAISED);
        Border second = BorderFactory.createTitledBorder(title);

        return BorderFactory.createCompoundBorder(first, second);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 255, 240));
        button.setForeground(new Color(0, 0, 128));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 50));

        return button;
    }

    public void resetOrderPanel() {
        orders.clear();
        orderDisplayPanel.removeAll();
        totalPrice = 0.0;
        totalPriceLabel.setText("Total Price Rs: 0.0");
        orderDisplayPanel.revalidate();
        orderDisplayPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SalesPersonDashboard::new);
    }
}