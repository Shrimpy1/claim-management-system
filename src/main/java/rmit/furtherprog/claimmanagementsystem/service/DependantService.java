/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.database.DependantRepository;

import java.util.List;

public class DependantService {
    private DependantRepository repository;

    public DependantService(DependantRepository repository) {
        this.repository = repository;
    }

    public Dependant getDependantById(String id){
        return repository.getById(id);
    }

    public List<Dependant> getAllDependants() {
        return repository.getAll();
    }

    public void updateToDatabase(Dependant dependant){
        repository.updateDatabase(dependant);
    }

    public int addToDatabase(Dependant dependant){
        return repository.addToDatabase(dependant);
    }
}
