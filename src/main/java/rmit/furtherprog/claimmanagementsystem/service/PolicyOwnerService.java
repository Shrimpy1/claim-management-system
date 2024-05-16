/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.database.PolicyOwnerRepository;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.util.List;

public class PolicyOwnerService {
    private PolicyOwnerRepository repository;

    public PolicyOwnerService(PolicyOwnerRepository repository) {
        this.repository = repository;
    }

    public PolicyOwner getPolicyOwnerById(String id){
        return repository.getById(id);
    }

    public List<PolicyOwner> getAllPolicyOwner(){
        return repository.getAll();
    }

    public void update(PolicyOwner policyOwner){
        repository.updateDatabase(policyOwner);
    }

    public String add(PolicyOwner policyOwner) {
        return IdConverter.toCustomerId(repository.addToDatabase(policyOwner));
    }

    public void delete(String id){
        repository.deleteById(id);
    }
}
