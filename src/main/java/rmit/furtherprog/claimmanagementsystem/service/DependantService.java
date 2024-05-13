package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.database.DependantRepository;

import java.util.List;

public class DependantService {
    private DependantRepository repository;

    public DependantService(DependantRepository repository) {
        this.repository = repository;
    }

    public Dependant getById(String id){
        return repository.getById(id);
    }

}
