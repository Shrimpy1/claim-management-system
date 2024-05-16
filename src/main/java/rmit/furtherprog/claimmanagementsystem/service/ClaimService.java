/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.database.ClaimRepository;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.util.List;

public class ClaimService {
    private ClaimRepository repository;

    public ClaimService(ClaimRepository repository) {
        this.repository = repository;
    }

    public Claim getClaimById(String id){
        return repository.getById(id);
    }

    public List<Claim> getAllClaims(){
        return repository.getAll();
    }

    public List<Claim> getNewClaims() {
        return repository.getAllNew();
    }

    public void update(Claim claim) {
        repository.updateDatabase(claim);
    }

    public String add(Claim claim) {
        return IdConverter.toClaimId(repository.addToDatabase(claim));
    }

    public void delete(String id){
        repository.deleteById(id);
    }
}
