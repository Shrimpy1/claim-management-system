/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.data.model.customer;

import java.util.HashSet;
import java.util.Set;

public class PolicyOwner {
    private String id;
    private String fullName;
    private Set<Customer> beneficiaries;

    public PolicyOwner(String fullName) {
        this.fullName = fullName;
    }

    public PolicyOwner(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
        this.beneficiaries = new HashSet<>();
    }

    public PolicyOwner(String id, String fullName, Set<Customer> beneficiaries) {
        this.id = id;
        this.fullName = fullName;
        this.beneficiaries = beneficiaries;
    }

    public String getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }

    public Set<Customer> getBeneficiaries() {
        return beneficiaries;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBeneficiaries(Set<Customer> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public void addBeneficiaries(Customer customer) {
        this.beneficiaries.add(customer);
        if (customer instanceof Policyholder) {
            this.beneficiaries.addAll(((Policyholder) customer).getDependants());
        }
    }

    public void removeBeneficiaries(Customer customer) {
        this.beneficiaries.remove(customer);
    }
}
