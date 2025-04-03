package src;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class SalesPersonDashboard extends JFrame {
    private int sessionId = 1;
    private ArrayList<OrderEntry> orders = new ArrayList<>();
    private List<Order> order_list = new ArrayList<>();

    private JLabel totalPriceLabel,currentTotalLabel, sessionLabel;
    private JComboBox<String> transportPreferenceField, sizeOption, productIdComboBox; 
    private double totalPrice = 0.0; 
    private JPanel orderDisplayPanel,inputPanel, rightMainPanel, sessionPanel;
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
        setSize(1000,500);

        //left panel for inputs
        JPanel leftMainPanel = new JPanel(new BorderLayout(5,5));
        inputPanel = new JPanel(new GridLayout(6, 2));
        sessionPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        //session panel
        sessionLabel = new JLabel("Customer Session ID: " + sessionId);
        customerDetailsButton = createStyledButton("Insert Customer Details");
        sessionPanel.add(sessionLabel);
        sessionPanel.add(customerDetailsButton);
        sessionPanel.setBorder(create_border("Input Mode"));

        //product detail input panel
        productIdComboBox = new JComboBox<>(database.fetch_item_ids()); 
        quantityField = new JTextField();
        sizeOption = new JComboBox<>(new String[] { "Small", "Medium", "Large" });
        currentTotalLabel = new JLabel("Current Total Rs 0.0");
        addButton = createStyledButton("Add To Order");

        //display panel
        orderDisplayPanel = new JPanel();
        orderDisplayPanel.setLayout(new BoxLayout(orderDisplayPanel, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderDisplayPanel);

        //add left panel with product details
        create_order_input_panel();
        inputPanel.setSize(500, 500);

        leftMainPanel.add(sessionPanel, BorderLayout.NORTH);
        leftMainPanel.add(inputPanel, BorderLayout.CENTER);

        // Right panel for display
        rightMainPanel = new JPanel(new BorderLayout());
        rightMainPanel.add(scrollPane, BorderLayout.CENTER);
        totalPriceLabel = new JLabel("Total Price Rs 0.0");
        totalPriceLabel.setForeground(Color.RED);
        checkoutButton = createStyledButton("Checkout");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalPriceLabel, BorderLayout.NORTH);
        bottomPanel.add(checkoutButton, BorderLayout.SOUTH);
        bottomPanel.setBorder(create_border("Summary"));
        rightMainPanel.add(bottomPanel, BorderLayout.SOUTH);
        rightMainPanel.setBorder(create_border("Display"));

        //add main panels to main frame
        add(leftMainPanel, BorderLayout.WEST);
        add(rightMainPanel, BorderLayout.CENTER);

        //add action listeners
        addButton.addActionListener(new ButtonHandler());
        checkoutButton.addActionListener(new ButtonHandler());
        customerDetailsButton.addActionListener(new ButtonHandler());
        productIdComboBox.addItemListener(new ProductIDHandler());

        setVisible(true);
    }

    private class CustomerDetailsFrame extends JFrame {
        
        public CustomerDetailsFrame() {
            super("Customer Details");
            setLayout(new GridLayout(7, 2));
            setSize(400, 300);
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
            add(transportPreferenceField = new JComboBox<>(database.fetch_transport_methods()));
            add(new JLabel());
            add(saveButton);

            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
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
                }
            });
            setVisible(true);
        }
    }

    public class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == addButton){
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

                        String order_string = "Product ID: " + productId + ", Quantity: " + qty + ", Size: " + size + ", Price: "+ price + " Rs";
                        
                        JPanel orderPanel = new JPanel(new BorderLayout());
                        JLabel orderLabel = new JLabel(order_string);
                        JButton removeButton = new JButton("X");
                        removeButton.setForeground(Color.RED); 
                        orderPanel.add(orderLabel, BorderLayout.CENTER);
                        orderPanel.add(removeButton, BorderLayout.EAST);

                        OrderEntry orderEntry = new OrderEntry(order_string);
                        orders.add(orderEntry);
                        orderDisplayPanel.add(orderPanel);

                        //create order object for insert in DB
                        order = new Order(Integer.valueOf(productId), qty, price, totalPrice, "No");
                        order_list.add(order);

                        removeButton.addActionListener(re -> {
                            orders.remove(orderEntry);
                            orderDisplayPanel.remove(orderPanel);
                            totalPrice -= price;
                            totalPriceLabel.setText("Total Price Rs: " + totalPrice);
                            orderDisplayPanel.revalidate();
                            orderDisplayPanel.repaint();
                        });

                        // Refresh the display
                        orderDisplayPanel.revalidate();
                        orderDisplayPanel.repaint();

                        // Clear fields
                        productIdComboBox.setSelectedIndex(1); 
                        quantityField.setText("");
                        sizeOption.setSelectedIndex(0);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid quantity!");
                    }
                } 
                else {
                    JOptionPane.showMessageDialog(null, "Fill all fields!");
                }
                // Clear fields
                productIdComboBox.setSelectedIndex(1); 
                quantityField.setText("");
                sizeOption.setSelectedIndex(0);
                
            }
            if(ae.getSource() == customerDetailsButton){
                new CustomerDetailsFrame();
            }
            if(ae.getSource() == checkoutButton){
                if (totalPrice > 0) {
                    new Checkout(customer, order_list);
                } else {
                    JOptionPane.showMessageDialog(null, "No items to checkout!");
                }
            }
        }
    };

    public class ProductIDHandler implements ItemListener{
        public void itemStateChanged(ItemEvent ie){
            String item = productIdComboBox.getSelectedItem().toString();
            unitPriceTextField.setText(String.valueOf(database.get_item_price_by_id(item)));
        }
    }

    public void create_order_input_panel(){
        
        addButton.setText("Add Order");

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(productIdComboBox);

        inputPanel.add(new JLabel("Unit Price: Rs "));
        inputPanel.add(unitPriceTextField = new JTextField());
        unitPriceTextField.setEditable(false);

        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Size:"));
        inputPanel.add(sizeOption);

        inputPanel.add(currentTotalLabel);
        inputPanel.add(addButton);

        
        JLabel receiptLabel = new JLabel("Receipt for Session ID " + sessionId + ":");
        orderDisplayPanel.add(receiptLabel);

        if (orders.isEmpty()) {
            orderDisplayPanel.add(new JLabel("No items added yet."));
        } else {
            for (OrderEntry order : orders) {
                orderDisplayPanel.add(new JLabel(order.orderText));
            }
        }

        inputPanel.revalidate();
        inputPanel.repaint();

        inputPanel.setBorder(create_border("Order Inputs"));
        orderDisplayPanel.revalidate();
        orderDisplayPanel.repaint();
    }   

    public Border create_border(String title){
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

    public void resetOrderPanel() {
        orders.clear();
        orderDisplayPanel.removeAll();
        totalPrice = 0.0;
        totalPriceLabel.setText("Total Price Rs: 0.0");
        sessionId++;
        sessionLabel.setText("Customer Session ID: " + sessionId);
        orderDisplayPanel.revalidate();
        orderDisplayPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SalesPersonDashboard::new);
    }
}