package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.database.ClaimRepository;

import java.util.List;

public class ClaimService {
    private ClaimRepository repository;

    public ClaimService(ClaimRepository repository) {
        this.repository = repository;
    }

    public boolean createNewClaim(){
        return true;
    }

    public Claim getClaimById(String id){
        return repository.getById(id);
    }

    public List<Claim> getAllClaim(){
        return repository.getAll();
    }

    public void updateToDatabase(Claim claim) {
        repository.updateDatabase(claim);
    }

    public int addToDatabase(Claim claim) {
        return repository.addToDatabase(claim);
    }
}
