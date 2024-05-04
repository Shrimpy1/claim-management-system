package rmit.furtherprog.claimmanagementsystem.data.model.prop;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Customer;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;

import java.time.LocalDate;

/**
 * One of the main classes of the system
 */
public class InsuranceCard {
    private String cardNumber;
    private Customer cardHolder;
    private PolicyOwner policyOwner;
    private LocalDate expirationDate;

    // Constructors
    public InsuranceCard() {
        this.cardNumber = "";
    }

    public InsuranceCard(String cardNumber, Customer cardHolder, PolicyOwner policyOwner, LocalDate expirationDate) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.policyOwner = policyOwner;
        this.expirationDate = expirationDate;
    }

    // Getters
    public String getCardNumber() {
        return cardNumber;
    }
    public Customer getCardHolder() {
        return cardHolder;
    }
    public PolicyOwner getPolicyOwner() {
        return policyOwner;
    }
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    // Setters
    public void setCardHolder(Customer cardHolder) {
        this.cardHolder = cardHolder;
    }
}
