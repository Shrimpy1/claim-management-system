/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.database.BankingInfoRepository;

public class BankingInfoService {
    private BankingInfoRepository repository;

    public BankingInfoService(BankingInfoRepository repository) {
        this.repository = repository;
    }

    public BankingInfo getBankingInfoById(int id){
        return repository.getById(id);
    }

    public void updateToDatabase(BankingInfo bankingInfo){
        repository.updateDatabase(bankingInfo);
    }

    public int addToDatabase(BankingInfo bankingInfo){
        return repository.addToDatabase(bankingInfo);
    }
}