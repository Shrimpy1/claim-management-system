package rmit.furtherprog.claimmanagementsystem.data.model.customer;

import java.util.Set;

public class PolicyOwner {
    private String id;
    private String name;
    private Set<Customer> beneficiaries;

    public PolicyOwner(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Customer> getBeneficiaries() {
        return beneficiaries;
    }

    public void setName(String name) {
        this.name = name;
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
