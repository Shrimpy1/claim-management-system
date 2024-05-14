package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.database.PolicyOwnerRepository;

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
}
