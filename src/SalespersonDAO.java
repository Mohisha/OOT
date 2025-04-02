package src;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SalespersonDAO {
    
    /**
     * Authenticate a salesperson with username and password
     * @param username The username to check
     * @param password The password to verify
     * @return Salesperson object if authentication successful, null otherwise
     */
    public static Salesperson authenticate(String username, String password) {
        try {
            // In a real application, you would use prepared statements to prevent SQL injection
            String query = "SELECT * FROM salesperson WHERE username = '" + username + 
                          "' AND password = '" + password + "' AND active = 1";
            
            ResultSet resultSet = Database.executeQuery(query);
            
            if (resultSet != null && resultSet.next()) {
                // Create a Salesperson object from the result set
                Salesperson salesperson = new Salesperson(
                    resultSet.getInt("salesperson_id"),
                    resultSet.getString("username"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone")
                );
                
                resultSet.close();
                return salesperson;
            }
            
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            System.out.println("Authentication failed: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get a salesperson by ID
     * @param salespersonId The ID to look up
     * @return Salesperson object if found, null otherwise
     */
    public static Salesperson getSalespersonById(int salespersonId) {
        try {
            String query = "SELECT * FROM salesperson WHERE salesperson_id = " + salespersonId;
            ResultSet resultSet = Database.executeQuery(query);
            
            if (resultSet != null && resultSet.next()) {
                // Create a Salesperson object from the result set
                Salesperson salesperson = new Salesperson(
                    resultSet.getInt("salesperson_id"),
                    resultSet.getString("username"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone")
                );
                
                resultSet.close();
                return salesperson;
            }
            
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving salesperson: " + e.getMessage());
        }
        
        return null;
    }
}