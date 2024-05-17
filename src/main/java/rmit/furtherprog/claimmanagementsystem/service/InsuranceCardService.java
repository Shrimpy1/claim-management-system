/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.service;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.database.InsuranceCardRepository;

public class InsuranceCardService {
    private InsuranceCardRepository repository;
    private InsuranceCard insuranceCard;

    public InsuranceCardService(InsuranceCardRepository repository) {
        this.repository = repository;
    }

    public InsuranceCardService(InsuranceCardRepository repository, InsuranceCard insuranceCard) {
        this.repository = repository;
        this.insuranceCard = insuranceCard;
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public InsuranceCard getInsuranceCardByNumber(String cardNumber){
        return repository.getByNumber(cardNumber);
    }

    public void update() {
        repository.updateDatabase(insuranceCard);
    }

    public void add() {
        repository.addToDatabase(insuranceCard);
    }

    public void delete(){
        repository.deleteById(insuranceCard.getCardNumber());
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
