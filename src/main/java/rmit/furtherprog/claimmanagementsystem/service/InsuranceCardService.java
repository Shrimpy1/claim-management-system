package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.database.InsuranceCardRepository;

import java.util.List;


public class InsuranceCardService {
    private InsuranceCardRepository repository;

    public InsuranceCardService(InsuranceCardRepository repository) {
        this.repository = repository;
    }

    public InsuranceCard getInsuranceCardByNumber(String cardNumber){
        return repository.getByNumber(cardNumber);
    }

    public void updateToDatabase(InsuranceCard card) {
        repository.updateDatabase(card);
    }

    public void addToDatabase(InsuranceCard card) {
        repository.addToDatabase(card);
    }
}
