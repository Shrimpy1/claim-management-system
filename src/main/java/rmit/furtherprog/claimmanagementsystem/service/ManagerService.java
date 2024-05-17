/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.provider.Manager;
import rmit.furtherprog.claimmanagementsystem.data.model.provider.Surveyor;
import rmit.furtherprog.claimmanagementsystem.database.ManagerRepository;

import java.util.ArrayList;
import java.util.List;

public class ManagerService {
    private ManagerRepository repository;
    private Manager manager;

    public ManagerService(ManagerRepository repository) {
        this.repository = repository;
    }

    public ManagerService(ManagerRepository repository, Manager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Manager getManagerById(int id){
        return repository.getById(id);
    }

    public void update(){
        repository.updateDatabase(manager);
    }

    public int add(){
        return repository.addToDatabase(manager);
    }

    public void delete(){
        repository.deleteById(manager.getId());
    }

    public void update(Manager manager){
        repository.updateDatabase(manager);
    }

    public int add(Manager manager){
        return repository.addToDatabase(manager);
    }

    public void delete(int id){
        repository.deleteById(id);
    }

    public List<Claim> retrieveProposedClaim(Manager manager){
        List<Claim> claimList = new ArrayList<Claim>();
        for (Surveyor surveyor : manager.getSurveyors()){
            for (Claim claim : surveyor.getProposedClaim()){
                claimList.add(claim);
            }
        }
        return claimList;
    }
}
