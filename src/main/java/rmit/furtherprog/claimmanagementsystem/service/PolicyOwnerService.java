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
    private PolicyOwner policyOwner;

    public PolicyOwnerService(PolicyOwnerRepository repository) {
        this.repository = repository;
    }

    public PolicyOwnerService(PolicyOwnerRepository repository, PolicyOwner policyOwner) {
        this.repository = repository;
        this.policyOwner = policyOwner;
    }

    public PolicyOwner getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(PolicyOwner policyOwner) {
        this.policyOwner = policyOwner;
    }

    public PolicyOwner getPolicyOwnerById(String id){
        return repository.getById(id);
    }

    public List<PolicyOwner> getAllPolicyOwner(){
        return repository.getAll();
    }

    public void update(){
        repository.updateDatabase(policyOwner);
    }

    public String add() {
        return IdConverter.toCustomerId(repository.addToDatabase(policyOwner));
    }

    public void delete(){
        repository.deleteById(policyOwner.getId());
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
