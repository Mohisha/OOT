package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    // Database connection properties
    private static final String DB_URL = "jdbc:mysql://localhost:3306/store_management";
    private static final String USER = "root"; // Default phpMyAdmin username
    private static final String PASS = ""; // Default password is empty for XAMPP
    
    private static Connection connection = null;
    
    // Get database connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Create connection
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Database connection established");
            } catch (ClassNotFoundException e) {
                System.out.println("JDBC Driver not found: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Connection failed: " + e.getMessage());
            }
        }
        return connection;
    }
    
    // Execute SQL query and return ResultSet
    public static ResultSet executeQuery(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Query execution failed: " + e.getMessage());
            return null;
        }
    }
    
    // Execute SQL update (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Update execution failed: " + e.getMessage());
            return -1;
        }
    }
    
    // Create order in database
    public static void create_order(Customer customer, Invoice invoice) {
        try {
            // First, check if customer exists, if not create
            int customerId = getOrCreateCustomer(customer);
            
            // Begin transaction
            connection.setAutoCommit(false);
            
            // Create order header
            String paymentMethod = ""; // You need to pass this from Checkout
            String paymentDetails = ""; // You need to pass this from Checkout
            
            String orderQuery = "INSERT INTO OrderHeader (customer_id, transport_preference, transport_charge, " +
                               "total_amount, payment_method, payment_details) VALUES " +
                               "(" + customerId + ", '" + invoice.get_transport_preference() + "', " +
                               invoice.get_transport_charge() + ", " + invoice.get_invoice_total() + ", '" +
                               paymentMethod + "', '" + paymentDetails + "')";
            
            executeUpdate(orderQuery);
            
            // Get the last inserted order ID
            ResultSet rs = executeQuery("SELECT LAST_INSERT_ID() as order_id");
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt("order_id");
            }
            rs.close();
            
            // Create order details
            for (Order order : invoice.get_orders()) {
                String detailQuery = "INSERT INTO OrderDetail (order_id, item_id, quantity, unit_price, " +
                                    "total_price, backorder_status) VALUES " +
                                    "(" + orderId + ", " + order.item_id + ", " + order.quantity + ", " +
                                    order.unitPrice + ", " + order.totalPrice + ", '" + order.backorderstatus + "')";
                executeUpdate(detailQuery);
            }
            
            // Commit transaction
            connection.commit();
            connection.setAutoCommit(true);
            
        } catch (SQLException e) {
            try {
                // Rollback transaction in case of error
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Rollback failed: " + ex.getMessage());
            }
            System.out.println("Order creation failed: " + e.getMessage());
        }
    }
    
    // Get customer ID if exists, otherwise create new customer
    private static int getOrCreateCustomer(Customer customer) throws SQLException {
        String query = "SELECT customer_id FROM Customer WHERE email = '" + customer.email + "'";
        ResultSet rs = executeQuery(query);
        
        if (rs.next()) {
            int customerId = rs.getInt("customer_id");
            rs.close();
            return customerId;
        } else {
            rs.close();
            String insertQuery = "INSERT INTO Customer (first_name, last_name, contact_details, email, address, transport_preference) " +
                               "VALUES ('" + customer.first_name + "', '" + customer.last_name + "', '" + 
                               customer.contactDetails + "', '" + customer.email + "', '" + 
                               customer.address + "', '" + customer.transportPreference + "')";
            executeUpdate(insertQuery);
            
            ResultSet idRs = executeQuery("SELECT LAST_INSERT_ID() as customer_id");
            int customerId = 0;
            if (idRs.next()) {
                customerId = idRs.getInt("customer_id");
            }
            idRs.close();
            return customerId;
        }
    }
    
    // Close database connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
    
    // Method to get items from database
    public static List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM Item";
        
        try {
            ResultSet rs = executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("item_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("unit_price");
                
                Item item = new Item(id, name, description, price);
                items.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving items: " + e.getMessage());
        }
        
        return items;
    }
}
