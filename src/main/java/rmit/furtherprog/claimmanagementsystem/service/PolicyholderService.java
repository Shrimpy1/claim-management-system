package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.database.PolicyholderRepository;

public class PolicyholderService {
    private PolicyholderRepository repository;

    public PolicyholderService(PolicyholderRepository repository) {
        this.repository = repository;
    }

    public Policyholder getById(String id){
        return repository.getById(id);
    }
}
