/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.util;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Verifier {
    public static boolean verifyCustomerId(String customerId){
        return customerId != null && customerId.matches("^c\\d{7}$");
    }

    public static boolean verifyClaimId(String claimId){
        return claimId != null && claimId.matches("^f\\d{10}$");
    }

    public static boolean verifyCardId(String cardId){
        return cardId != null && cardId.matches("^\\d{10}$");
    }

    public static boolean verifyDate(String date){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            dateFormatter.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
