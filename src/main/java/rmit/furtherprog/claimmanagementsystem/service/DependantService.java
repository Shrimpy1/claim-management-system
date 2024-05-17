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
    private Dependant dependant;

    public DependantService(DependantRepository repository) {
        this.repository = repository;
    }

    public DependantService(DependantRepository repository, Dependant dependant) {
        this.repository = repository;
        this.dependant = dependant;
    }

    public Dependant getDependant() {
        return dependant;
    }

    public void setDependant(Dependant dependant) {
        this.dependant = dependant;
    }

    public Dependant getDependantById(String id){
        return repository.getById(id);
    }

    public List<Dependant> getAllDependants() {
        return repository.getAll();
    }

    public void update(){
        repository.updateDatabase(dependant);
    }

    public String add(){
        return IdConverter.toCustomerId(repository.addToDatabase(dependant));
    }

    public void delete(){
        repository.deleteById(dependant.getId());
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
