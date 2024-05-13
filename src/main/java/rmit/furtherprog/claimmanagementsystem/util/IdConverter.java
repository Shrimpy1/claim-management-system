package rmit.furtherprog.claimmanagementsystem.util;

public class IdConverter {
    public static String toCustomerId(int id){
        return String.format("c%07d", id);
    }

    public static int fromCustomerId(String customerId) {
        // Check if the input string has the expected format
        if (customerId.matches("^c\\d{7}$")) {
            // Extract the numeric part of the customer ID string
            String numericPart = customerId.substring(1); // Skip the
            return Integer.parseInt(numericPart);
        } else {
            // Handle invalid input (e.g., throw an exception or return a default value)
            throw new IllegalArgumentException("Invalid customer ID format: " + customerId);
        }
    }
}
