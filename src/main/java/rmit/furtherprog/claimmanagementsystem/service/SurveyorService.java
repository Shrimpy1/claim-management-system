/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.provider.Surveyor;
import rmit.furtherprog.claimmanagementsystem.database.SurveyorRepository;

public class SurveyorService {
    private SurveyorRepository repository;

    public SurveyorService(SurveyorRepository repository) {
        this.repository = repository;
    }

    public Surveyor getSurveyorById(int id){
        return repository.getById(id);
    }

    public void updateToDatabase(Surveyor surveyor){
        repository.updateDatabase(surveyor);
    }

    public int addToDatabase(Surveyor surveyor){
        return repository.addToDatabase(surveyor);
    }
}
