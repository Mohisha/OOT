package src;

public class Customer {
    public String first_name;
    public String last_name;
    public String contactDetails;
    public String email;
    public String address;
    public String transportPreference;

    public Customer(String first_name, String last_name, String contactDetails, String email, String address, String transportPreference) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.contactDetails = contactDetails;
        this.address = address;
        this.email = email;
        this.transportPreference = transportPreference;
    }
}
