/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.database.BankingInfoRepository;

public class BankingInfoService {
    private BankingInfoRepository repository;
    private BankingInfo bankingInfo;

    public BankingInfoService(BankingInfoRepository repository) {
        this.repository = repository;
    }

    public BankingInfoService(BankingInfoRepository repository, BankingInfo bankingInfo) {
        this.repository = repository;
        this.bankingInfo = bankingInfo;
    }

    public BankingInfo getBankingInfoById(int id){
        return repository.getById(id);
    }

    public void update(){
        repository.updateDatabase(bankingInfo);
    }

    public int add(){
        return repository.addToDatabase(bankingInfo);
    }

    public void delete(){
        repository.deleteById(bankingInfo.getId());
    }

    public void update(BankingInfo bankingInfo){
        repository.updateDatabase(bankingInfo);
    }

    public int add(BankingInfo bankingInfo){
        return repository.addToDatabase(bankingInfo);
    }

    public void delete(int id){
        repository.deleteById(id);
    }

    public BankingInfo getBankingInfo() {
        return bankingInfo;
    }

    public void setBankingInfo(BankingInfo bankingInfo) {
        this.bankingInfo = bankingInfo;
    }
}
