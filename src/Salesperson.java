package src;

public class Salesperson {
    private int salespersonId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    
    public Salesperson(int salespersonId, String username, String firstName, 
                     String lastName, String email, String phone) {
        this.salespersonId = salespersonId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
    
    public int getSalespersonId() {
        return salespersonId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}