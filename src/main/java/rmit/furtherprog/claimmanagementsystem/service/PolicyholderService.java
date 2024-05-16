package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.database.PolicyholderRepository;

import java.util.List;

public class PolicyholderService {
    private PolicyholderRepository repository;

    public PolicyholderService(PolicyholderRepository repository) {
        this.repository = repository;
    }

    public Policyholder getPolicyholderById(String id){
        return repository.getById(id);
    }

    public List<Policyholder> getAllPolicyholder(){
        return repository.getAll();
    }

    public void updateToDatabase(Policyholder policyholder){
        repository.updateDatabase(policyholder);
    }

    public int addToDatabase(Policyholder policyholder){
        return repository.addToDatabase(policyholder);
    }
}
