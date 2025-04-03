package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String url = "jdbc:mysql://localhost:3306/store_management2";
    private static final String username = "root";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, "");
    }

    public static ResultSet executeQuery(String query) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static String[] fetch_item_ids() {
        String query = "SELECT itemID FROM items;";
        ResultSet resultset = Database.executeQuery(query);
        List<String> IDs = new ArrayList<>();

        try {
            while (resultset.next()) {
                IDs.add(resultset.getString("itemID"));
            }
            resultset.close();
        } catch (SQLException e) {
            System.out.println("Error fetching item IDs: " + e.getMessage());
        }

        return IDs.toArray(new String[0]);
    }

    public static double get_item_price_by_id(String itemID) {
        String query = "SELECT sellingPrice FROM items WHERE itemID=" + itemID;
        ResultSet resultSet = Database.executeQuery(query);
        double price = 0.00;
        try {
            while (resultSet.next()) {
                price = resultSet.getDouble("sellingPrice");
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return price;
    }

    public static String get_item_name_by_id(String itemID) {
        String query = "SELECT itemName FROM items WHERE itemID=" + itemID;
        ResultSet resultSet = Database.executeQuery(query);
        String name = "";
        try {
            while (resultSet.next()) {
                name = resultSet.getString("itemName");
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return name;
    }

    public static int get_item_stock_by_id(String itemID) {
        String query = "SELECT stockLevel FROM items WHERE itemID=" + itemID;
        ResultSet resultSet = Database.executeQuery(query);
        int stock = 0;
        try {
            while (resultSet.next()) {
                stock = resultSet.getInt("stockLevel");
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return stock;
    }

    public static String[] fetch_transport_methods() {
        String query = "SELECT name FROM Transport;";
        ResultSet resultset = Database.executeQuery(query);
        List<String> transport_names = new ArrayList<>();

        try {
            while (resultset.next()) {
                transport_names.add(resultset.getString("name"));
            }
            resultset.close();
        } catch (SQLException e) {
            System.out.println("Error fetching item IDs: " + e.getMessage());
        }

        return transport_names.toArray(new String[0]);
    }

    public static String[] fetch_low_stock_items() {
        String query = "SELECT itemID, itemName, size, stockLevel, reorderLevel FROM items WHERE stockLevel < reorderLevel;";
        ResultSet resultset = Database.executeQuery(query);
        List<String> low_stock_items = new ArrayList<>();

        try {
            while (resultset.next()) {
                low_stock_items.add(resultset.getString("itemID") + ". " + resultset.getString("itemName") + ". Size: "
                        + resultset.getString("size") + ". The current stock is : " + resultset.getString("stockLevel")
                        + ". Order a minimum of :  " + resultset.getString("reorderLevel"));
            }
            resultset.close();
        } catch (SQLException e) {
            System.out.println("Error fetching item IDs: " + e.getMessage());
        }

        return low_stock_items.toArray(new String[0]);
    }

    public static void create_reorder(String itemID, int qty) {
        String query = "INSERT INTO InventoryUpdate (itemID, quantity) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, itemID);
            preparedStatement.setInt(2, qty);
            preparedStatement.executeUpdate();

            System.out.println("Reorder inserted successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(String username, String password, String phone_number) {
        String query = "INSERT INTO User (username, password, contactNo) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phone_number);
            preparedStatement.executeUpdate();

            System.out.println("Data inserted successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void insert_item(String item_name, double cost_price, double selling_price) {
        String query = "INSERT INTO items (itemName, costPrice, sellingPrice) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, item_name);
            preparedStatement.setDouble(2, cost_price);
            preparedStatement.setDouble(3, selling_price);
            preparedStatement.executeUpdate();

            System.out.println("Item inserted successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void update_item(String item_name, double cost_price, double selling_price) {
        String query = "UPDATE items SET costPrice=?, sellingPrice=? WHERE itemName=?;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDouble(1, cost_price);
            preparedStatement.setDouble(2, selling_price);
            preparedStatement.setString(3, item_name);

            preparedStatement.executeUpdate();

            System.out.println("Item modified successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void update_item_stock(String item_id, int stockLevel) {
        String query = "UPDATE items SET stockLevel=? WHERE itemID=?;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDouble(1, stockLevel);
            preparedStatement.setString(2, item_id);

            preparedStatement.executeUpdate();

            System.out.println("Item stock modified successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void update_item_price(String item_id, double price) {
        String query = "UPDATE items SET sellingPrice=? WHERE itemID=?;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDouble(1, price);
            preparedStatement.setString(2, item_id);

            preparedStatement.executeUpdate();

            System.out.println("Item price modified successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String[] get_new_items() {
        String query = "SELECT * FROM items WHERE sellingPrice IS NULL";
        ResultSet resultset = Database.executeQuery(query);
        List<String> new_items = new ArrayList<>();

        try {
            while (resultset.next()) {
                new_items.add(resultset.getString("ItemID") + ". Name: " + resultset.getString("itemName") + ". Size: "
                        + resultset.getString("size") + ". ");
            }
            resultset.close();
        } catch (SQLException e) {
            System.out.println("Error fetching item IDs: " + e.getMessage());
        }

        return new_items.toArray(new String[0]);
    }

    public static String[] get_all_items() {
        String query = "SELECT itemID, itemName FROM items";
        ResultSet resultset = Database.executeQuery(query);
        List<String> new_items = new ArrayList<>();

        try {
            while (resultset.next()) {
                new_items.add(resultset.getString("ItemID") + ". Name: " + resultset.getString("itemName"));
            }
            resultset.close();
        } catch (SQLException e) {
            System.out.println("Error fetching item IDs: " + e.getMessage());
        }

        return new_items.toArray(new String[0]);
    }

    public static void delete_item(String itemID) {
        String query = "DELETE FROM items WHERE itemID=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, itemID);

            preparedStatement.executeUpdate();

            System.out.println("Item deleted successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void create_order(Customer customer, Quotation quotation) {
        String query = "INSERT INTO Customer (firstName, lastName, contactNo, address, email, transportPreference) VALUES (?, ?, ? ,?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, customer.first_name);
            preparedStatement.setString(2, customer.last_name);
            preparedStatement.setString(3, customer.contactDetails);
            preparedStatement.setString(4, customer.address);
            preparedStatement.setString(5, customer.email);
            preparedStatement.setString(6, customer.transportPreference);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int customerID = generatedKeys.getInt(1);

                    generatedKeys = null;
                    query = "INSERT INTO Quotation (customerID, orderTotal, transportCharge, quotation_total, transportID) VALUES (?, ?, ?, ?, ?)";

                    preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setInt(1, customerID);
                    preparedStatement.setDouble(2, quotation.get_order_total());
                    preparedStatement.setDouble(3, quotation.get_transport_charge());
                    preparedStatement.setDouble(4, quotation.get_quotation_total());
                    preparedStatement.setString(5, customer.transportPreference);

                    affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows > 0) {
                        generatedKeys = preparedStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int quotationID = generatedKeys.getInt(1);

                            generatedKeys = null;
                            query = "INSERT INTO OrderDetails (quotationID, itemID, quantity, unitPrice, totalPrice, backOrderStatus) VALUES (?, ?, ?, ?, ?, ?)";
                            preparedStatement = connection.prepareStatement(query);

                            for (Order order : quotation.get_orders()) {
                                preparedStatement.setInt(1, quotationID);
                                preparedStatement.setInt(2, order.item_id);
                                preparedStatement.setInt(3, order.quantity);
                                preparedStatement.setDouble(4, order.unitPrice);
                                preparedStatement.setDouble(5, order.totalPrice);
                                preparedStatement.setString(6, order.backorderstatus);
                                affectedRows += preparedStatement.executeUpdate();

                                int current_item_stock = get_item_stock_by_id((String.valueOf(order.item_id)));
                                update_item_stock((String.valueOf(order.item_id)), current_item_stock - order.quantity);
                            }

                            if (affectedRows > 0) {
                                System.out.println(affectedRows + " orders added successfully!");
                            } else {
                                System.out.println("Error adding orders");
                            }
                            System.out.println("quotation added successfully! quotation ID: " + quotationID);
                        }
                    } else {
                        System.out.println("quotation insertion failed!");
                    }
                    System.out.println("Customer added successfully! Customer ID: " + customerID);
                }
            } else {
                System.out.println("Customer insertion failed!");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null)
                    generatedKeys.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}