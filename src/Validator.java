package src;

import java.util.regex.Pattern;

public class Validator {
    
    /**
     * Validates credit card numbers
     * Checks if the card number is exactly 16 digits
     * 
     * @param cardNumber The card number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidCardNumber(String cardNumber) {
        // Remove any spaces or hyphens that might be in the card number
        String cleanCardNumber = cardNumber.replaceAll("\\s|-", "");
        
        // Check if it's exactly 16 digits
        return cleanCardNumber.matches("\\d{16}");
    }
    
    /**
     * Validates check numbers
     * Checks if the check number is 4-10 digits as per requirements (6 digits as specified)
     * 
     * @param checkNumber The check number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidCheckNumber(String checkNumber) {
        // Check if it's 6 digits
        return checkNumber.matches("\\d{6}");
    }
    
    /**
     * Validates contact numbers
     * Checks if the contact number is exactly 8 digits with no alphabets
     * 
     * @param contact The contact number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidContact(String contact) {
        // Remove any spaces or hyphens that might be in the contact number
        String cleanContact = contact.replaceAll("\\s|-", "");
        
        // Check if it's exactly 8 digits
        return cleanContact.matches("\\d{8}");
    }
    
    /**
     * Validates email addresses
     * Checks if the email contains @ sign and ends with .com, .org, etc.
     * 
     * @param email The email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        // Basic email pattern: something@something.com
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\\.(com|org|net|edu|gov|mil|biz|info|io)$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}