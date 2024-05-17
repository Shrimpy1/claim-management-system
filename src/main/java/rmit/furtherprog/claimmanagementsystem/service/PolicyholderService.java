/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.database.PolicyholderRepository;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.util.List;

public class PolicyholderService {
    private PolicyholderRepository repository;
    private Policyholder policyholder;

    public PolicyholderService(PolicyholderRepository repository) {
        this.repository = repository;
    }

    public PolicyholderService(PolicyholderRepository repository, Policyholder policyholder) {
        this.repository = repository;
        this.policyholder = policyholder;
    }

    public Policyholder getPolicyholder() {
        return policyholder;
    }

    public void setPolicyholder(Policyholder policyholder) {
        this.policyholder = policyholder;
    }

    public Policyholder getPolicyholderById(String id){
        return repository.getById(id);
    }

    public List<Policyholder> getAllPolicyholder(){
        return repository.getAll();
    }

    public void update(){
        repository.updateDatabase(policyholder);
    }

    public String add(){
        return IdConverter.toCustomerId(repository.addToDatabase(policyholder));
    }

    public void delete(){
        repository.deleteById(policyholder.getId());
    }

    public void update(Policyholder policyholder){
        repository.updateDatabase(policyholder);
    }

    public String add(Policyholder policyholder){
        return IdConverter.toCustomerId(repository.addToDatabase(policyholder));
    }

    public void delete(String id){
        repository.deleteById(id);
    }
}
