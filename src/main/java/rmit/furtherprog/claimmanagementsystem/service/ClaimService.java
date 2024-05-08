package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.database.ClaimRepository;

public class ClaimService {
    private ClaimRepository repository;

    public ClaimService(ClaimRepository repository) {
        this.repository = repository;
    }

    public boolean createNewClaim(){
        return true;
    }
}
