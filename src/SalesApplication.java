package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SalesApplication extends JFrame {

    private JTextField firstNameField, lastNameField, contactField, emailField;
    private JTextArea addressArea;
    private JComboBox<String> transportPreferenceComboBox;
    private JButton addItemButton, checkoutButton;
    private JComboBox<Item> itemComboBox;
    private JTextField quantityField;
    private JTextArea orderSummary;
    private List<Order> orderList;
    private Customer currentCustomer;
    public SalesApplication(Salesperson salesperson) {
        super("Sales Application - Logged in as: " + salesperson.getFirstName() + " " + salesperson.getLastName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        orderList = new ArrayList<>();

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel customerDetailsPanel = new JPanel();
        customerDetailsPanel.setLayout(new GridLayout(6, 2, 5, 3));
        customerDetailsPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        firstNameField.setPreferredSize(new Dimension(200, 25));
        firstNameField.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        lastNameField.setPreferredSize(new Dimension(200, 25));
        lastNameField.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        JLabel contactLabel = new JLabel("Contact:");
        contactField = new JTextField();
        contactField.setPreferredSize(new Dimension(200, 25));
        contactField.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        JLabel addressLabel = new JLabel("Address:");
        addressArea = new JTextArea(2, 20);
        addressArea.setPreferredSize(new Dimension(200, 40));
        addressArea.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 25));
        emailField.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        JLabel transportLabel = new JLabel("Transport:");
        transportPreferenceComboBox = new JComboBox<>(new String[]{"Standard", "Express", "International"});
        transportPreferenceComboBox.setPreferredSize(new Dimension(200, 25));
        transportPreferenceComboBox.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        customerDetailsPanel.add(firstNameLabel);
        customerDetailsPanel.add(firstNameField);
        customerDetailsPanel.add(lastNameLabel);
        customerDetailsPanel.add(lastNameField);
        customerDetailsPanel.add(contactLabel);
        customerDetailsPanel.add(contactField);
        customerDetailsPanel.add(addressLabel);
        customerDetailsPanel.add(addressScrollPane);
        customerDetailsPanel.add(emailLabel);
        customerDetailsPanel.add(emailField);
        customerDetailsPanel.add(transportLabel);
        customerDetailsPanel.add(transportPreferenceComboBox);

        leftPanel.add(customerDetailsPanel);
        leftPanel.add(Box.createVerticalStrut(10));

        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Item Selection"));

        JLabel itemLabel = new JLabel("Select Item:");
        itemComboBox = new JComboBox<>();
        loadItems();

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField("1");

        addItemButton = new JButton("Add to Order");

        selectionPanel.add(itemLabel);
        selectionPanel.add(itemComboBox);
        selectionPanel.add(quantityLabel);
        selectionPanel.add(quantityField);
        selectionPanel.add(new JLabel());
        selectionPanel.add(addItemButton);

        leftPanel.add(selectionPanel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));

        orderSummary = new JTextArea();
        orderSummary.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderSummary);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        checkoutButton = new JButton("Proceed to Checkout");
        rightPanel.add(checkoutButton, BorderLayout.SOUTH);

        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);
        mainSplitPane.setDividerLocation(350);

        add(mainSplitPane);

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemToOrder();
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (orderList.isEmpty()) {
                    JOptionPane.showMessageDialog(SalesApplication.this,
                            "Please add items to your order first.", "Empty Order", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Validate customer fields before proceeding
                if (!validateCustomerFields()) {
                    return;
                }
                
                currentCustomer = new Customer(
                        firstNameField.getText(),
                        lastNameField.getText(),
                        contactField.getText(),
                        emailField.getText(),
                        addressArea.getText(),
                        (String) transportPreferenceComboBox.getSelectedItem()
                );
                new Checkout(currentCustomer, orderList);
                SalesApplication.this.dispose();
            }
        });

        setVisible(true);
    }
    
    private boolean validateCustomerFields() {
        // Check if required fields are not empty
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            firstNameField.requestFocus();
            return false;
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            lastNameField.requestFocus();
            return false;
        }
        
        // Validate contact
        String contact = contactField.getText().trim();
        if (contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Contact number is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            contactField.requestFocus();
            return false;
        }
        
        if (!Validator.isValidContact(contact)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid contact number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            contactField.requestFocus();
            return false;
        }
        
        // Validate email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        
        if (!Validator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", 
                                          "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        
        return true;
    }

    private void loadItems() {
        List<Item> items = Database.getItems();
        for (Item item : items) {
            itemComboBox.addItem(item);
        }
    }

    private void addItemToOrder() {
        Item selectedItem = (Item) itemComboBox.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double unitPrice = selectedItem.getUnitPrice();
        double totalPrice = unitPrice * quantity;

        Order order = new Order(
                selectedItem.getItemId(),
                quantity,
                unitPrice,
                totalPrice,
                "Available"
        );

        orderList.add(order);
        updateOrderSummary();

        quantityField.setText("1");
    }

    private void updateOrderSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Items in order:\n\n");

        double total = 0;
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            Item item = getItemById(order.item_id);
            String itemName = (item != null) ? item.getName() : "Item #" + order.item_id;

            summary.append(i + 1).append(". ");
            summary.append(itemName).append(" - ");
            summary.append(order.quantity).append(" units x Rs ").append(order.unitPrice);
            summary.append(" = Rs ").append(order.totalPrice).append("\n");

            total += order.totalPrice;
        }

        summary.append("\nSubtotal: Rs ").append(total);

        orderSummary.setText(summary.toString());
    }

    private Item getItemById(int itemId) {
        List<Item> items = Database.getItems();
        for (Item item : items) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // You need to instantiate and show your login frame here.
                // The SalesApplication will be created upon successful login.
                
            }
        });
    }
}