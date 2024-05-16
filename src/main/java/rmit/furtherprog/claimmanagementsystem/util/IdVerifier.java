/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.util;

public class IdVerifier {
    public static boolean verifyCustomerId(String customerId){
        return customerId != null && customerId.matches("^c\\d{7}$");
    }

    public static boolean verifyClaimId(String claimId){
        return claimId != null && claimId.matches("^f\\d{10}$");
    }

    public static boolean verifyCardId(String cardId){
        return cardId != null && cardId.matches("^\\d{10}$");
    }
}
