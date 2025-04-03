package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import java.awt.event.ActionEvent;

public class Checkout extends JFrame {
    
    private SalesPersonDashboard parent;
    private JButton checkoutButton;
    private JTextField cardNumberField, checkNumberField;
    private JComboBox<String> paymentMethodCombo;
    private Customer customer;
    private Quotation quotation;

    public Checkout(Customer customer, List<Order> order_list) {
        
        this.customer = customer;

        setTitle("Checkout");
        setLayout(new GridLayout(7, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodCombo = new JComboBox<>(new String[] { "Select Payment Method","Card", "Check" });

        quotation = new Quotation();

        for (Order order : order_list) {
            quotation.AddOrder(order);
        }
        
        quotation.AddTransportCharge(get_transport_charge(customer.transportPreference));
        quotation.AddTransportPreference(customer.transportPreference);

        JLabel transportLabel = new JLabel("Transport Fee Amount(" + quotation.get_transport_preference() + "): ");
        JLabel transportValue = new JLabel("Rs " + quotation.get_transport_charge());

        JLabel orderAmountLabel = new JLabel("Order Amount:");
        JLabel orderAmount = new JLabel("Rs " + quotation.get_order_total());

        JLabel invoiceAmountLabel = new JLabel("Total Amount:");
        JLabel invoiceAmount = new JLabel("Rs " + quotation.get_quotation_total());

        JLabel cardNumberLabel = new JLabel("Card No:");
        cardNumberField = new JTextField();
        cardNumberField.setEnabled(false);

        JLabel checkNumberLabel = new JLabel("Check No:");
        checkNumberField = new JTextField();
        checkNumberField.setEnabled(false);

        checkoutButton = new JButton("Checkout");

        add(paymentMethodLabel);
        add(paymentMethodCombo);
        add(transportLabel);
        add(transportValue);
        add(orderAmountLabel);
        add(orderAmount);
        add(invoiceAmountLabel);
        add(invoiceAmount);
        add(cardNumberLabel);
        add(cardNumberField);
        add(checkNumberLabel);
        add(checkNumberField);
        add(new JLabel());
        add(checkoutButton);

        paymentMethodCombo.addActionListener(new ButtonHandler());
        checkoutButton.addActionListener(new ButtonHandler());

        setVisible(true);
    }

    public class ButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent ae){
            if(ae.getSource() == checkoutButton){
                
                String method = (String) paymentMethodCombo.getSelectedItem();
                if ("Card".equals(method) && cardNumberField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null , "Please enter card number!");
                    return;
                }
                if ("Check".equals(method) && checkNumberField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter check number!");
                    return;
                }

                Database.create_order(customer, quotation);
                JOptionPane.showMessageDialog(null, "Checkout Successful!\nTotal: Rs " + quotation.get_quotation_total());
                
                dispose();
            }

            if(ae.getSource() == paymentMethodCombo){
                String method = (String) paymentMethodCombo.getSelectedItem();
                cardNumberField.setEnabled("Card".equals(method));
                checkNumberField.setEnabled("Check".equals(method));
            }
        }
    }

    public double get_transport_charge(String name){
        String query = "SELECT charge FROM Transport WHERE name=\"" + name + "\"";
        ResultSet resultSet = Database.executeQuery(query);
        double price = 0.00;
        try{
            while (resultSet.next()){
                price = resultSet.getDouble("charge");
            }
            resultSet.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return price;
    }
}