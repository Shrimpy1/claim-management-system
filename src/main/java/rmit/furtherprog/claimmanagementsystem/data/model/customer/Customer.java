package rmit.furtherprog.claimmanagementsystem.data.model.customer;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * One of the main classes of the system
 */
public abstract class Customer {
    private String id;
    private String fullName;
    private InsuranceCard insuranceCard;
    private List<Claim> claims;

    // Constructors
    public Customer() {
        this.id = "";
        this.fullName = "";
        this.claims = new ArrayList<Claim>();
    }

    public Customer(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
        this.claims = new ArrayList<Claim>();
    }

    public Customer(String id, String fullName, InsuranceCard insuranceCard) {
        this.id = id;
        this.fullName = fullName;
        this.insuranceCard = insuranceCard;
    }

    public Customer(String id, String fullName, List<Claim> claims) {
        this.id = id;
        this.fullName = fullName;
        this.claims = claims;
    }

    public Customer(String id, String fullName, InsuranceCard insuranceCard, List<Claim> claims) {
        this.id = id;
        this.fullName = fullName;
        this.insuranceCard = insuranceCard;
        this.claims = claims;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }
    public String getId() {
        return id;
    }
    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }
    public List<Claim> getClaims() {
        return claims;
    }

    //Setters
    public void setId(String id) {
        this.id = id;
    }

    public boolean setInsuranceCard(InsuranceCard card){
        this.insuranceCard = card;
        card.setCardHolder(this);
        return true;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    // Manipulate Claim List
    public void addClaim(Claim claim){
        this.claims.add(claim);
    }
    public void removeClaim(Claim claim){
        this.claims.remove(claim);
    }

    // Override for Set customization
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Customer)) {
            return false;
        }
        return Objects.equals(this.getId(), ((Customer) o).getId());
    }
}