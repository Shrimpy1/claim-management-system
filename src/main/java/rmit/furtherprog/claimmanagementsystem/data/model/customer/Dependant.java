package rmit.furtherprog.claimmanagementsystem.data.model.customer;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;

import java.util.List;

/**
 * One of the main classes of the system
 * Dependant has no more attributes than Customer
 */
public class Dependant extends Customer{
    // Constructors
    public Dependant() {
        super();
    }

    public Dependant(String id, String name) {
        super(id, name);
    }

    public Dependant(String id, String fullName, InsuranceCard insuranceCard) {
        super(id, fullName, insuranceCard);
    }

    public Dependant(String id, String name, InsuranceCard insuranceCard, List<Claim> claims) {
        super(id, name, insuranceCard, claims);
    }
}