/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.provider.Surveyor;
import rmit.furtherprog.claimmanagementsystem.database.SurveyorRepository;
import rmit.furtherprog.claimmanagementsystem.util.HistoryManager;
import rmit.furtherprog.claimmanagementsystem.util.RequestHandler;

public class SurveyorService {
    private SurveyorRepository repository;

    public SurveyorService(SurveyorRepository repository) {
        this.repository = repository;
    }

    public Surveyor getSurveyorById(int id){
        return repository.getById(id);
    }

    public void update(Surveyor surveyor){
        repository.updateDatabase(surveyor);
    }

    public int add(Surveyor surveyor){
        return repository.addToDatabase(surveyor);
    }

    public void delete(int id){
        repository.deleteById(id);
    }

    public void request(Claim claim, String message){
        RequestHandler.addRequest(claim.getId(), message);
    }

    public void updateRequest(Claim claim, String message){
        RequestHandler.updateRequest(claim.getId(), message);
    }

    public void removeRequest(Claim claim){

    }

    public String proposeClaim(Surveyor surveyor, Claim claim){
        if (claim.getStatus() == Claim.ClaimStatus.NEW) {
            surveyor.proposeClaim(claim);
            claim.setStatusProcessing();
            HistoryManager.write("claim", "Proposed with ID: " + claim.getId());
            return "Claim has been proposed.";
        } else {
            return "This claim has already been processed.";
        }
    }
}
