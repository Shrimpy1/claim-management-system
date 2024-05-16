/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.database.InsuranceCardRepository;

public class InsuranceCardService {
    private InsuranceCardRepository repository;

    public InsuranceCardService(InsuranceCardRepository repository) {
        this.repository = repository;
    }

    public InsuranceCard getInsuranceCardByNumber(String cardNumber){
        return repository.getByNumber(cardNumber);
    }

    public void update(InsuranceCard card) {
        repository.updateDatabase(card);
    }

    public void add(InsuranceCard card) {
        repository.addToDatabase(card);
    }

    public void delete(String number){
        repository.deleteById(number);
    }
}
