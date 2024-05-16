/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.provider.Manager;
import rmit.furtherprog.claimmanagementsystem.database.ManagerRepository;

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
}
