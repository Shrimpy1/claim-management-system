/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.database.DependantRepository;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

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

    public void update(Dependant dependant){
        repository.updateDatabase(dependant);
    }

    public String add(Dependant dependant){
        return IdConverter.toCustomerId(repository.addToDatabase(dependant));
    }

    public void delete(String id){
        repository.deleteById(id);
    }
}
