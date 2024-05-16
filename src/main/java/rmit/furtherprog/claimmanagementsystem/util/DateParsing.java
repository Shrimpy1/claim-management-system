/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParsing {
    public static LocalDate stod(String dateString) {
        // Define the formatter for the input date format (yyyy/MM/dd)
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the input date string into a LocalDate object using the input formatter
        return LocalDate.parse(dateString, inputFormatter);
    }

    public static String dtos(LocalDate localDate) {
        // Define the formatter for the output date format (MM/dd/yyyy)
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the LocalDate object into a string using the output formatter
        return localDate.format(outputFormatter);
    }
}
