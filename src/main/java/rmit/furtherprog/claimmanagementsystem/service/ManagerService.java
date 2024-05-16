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

    public ManagerService(ManagerRepository repository) {
        this.repository = repository;
    }

    public Manager getManagerById(int id){
        return repository.getById(id);
    }

    public void updateToDatabase(Manager manager){
        repository.updateDatabase(manager);
    }

    public int addToDatabase(Manager manager){
        return repository.addToDatabase(manager);
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
