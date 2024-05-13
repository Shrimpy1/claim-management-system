package rmit.furtherprog.claimmanagementsystem.data.model.prop;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Main object of the system
 */
public class Claim {
    public enum ClaimStatus {
        NEW,
        PROCESSING,
        DONE,
        REJECTED
    }
    private String id;
    private LocalDate claimDate;
    private Customer insuredPerson;
    private String cardNumber;
    private LocalDate examDate;
    private List<String> documents;
    private double claimAmount;
    private ClaimStatus status;
    private BankingInfo receiverBankingInfo;

    // Constructors
    public Claim() {
        this.id = "";
        this.cardNumber = "";
        this.documents = new ArrayList<String>();
        this.claimAmount = 0;
        this.status = ClaimStatus.NEW;
    }

    public Claim(String id, LocalDate claimDate, String cardNumber, LocalDate examDate, double claimAmount) {
        this.id = id;
        this.claimDate = claimDate;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.claimAmount = claimAmount;
        this.documents = new ArrayList<String>();
        this.status = ClaimStatus.NEW;
    }

    public Claim(String id, LocalDate claimDate, String cardNumber, LocalDate examDate, List<String> documents, double claimAmount, BankingInfo receiverBankingInfo) {
        this.id = id;
        this.claimDate = claimDate;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.documents = documents;
        this.claimAmount = claimAmount;
        this.receiverBankingInfo = receiverBankingInfo;
        this.status = ClaimStatus.NEW;
    }

    public Claim(String id, LocalDate claimDate, Customer insuredPerson, String cardNumber, LocalDate examDate, List<String> documents, double claimAmount, BankingInfo receiverBankingInfo) {
        this.id = id;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.documents = documents;
        this.claimAmount = claimAmount;
        this.status = ClaimStatus.NEW;
        this.receiverBankingInfo = receiverBankingInfo;
    }

    // Getters
    public String getId() {
        return id;
    }
    public LocalDate getClaimDate() {
        return claimDate;
    }
    public Customer getInsuredPerson() {
        return insuredPerson;
    }
    public String getCardNumber() {
        return cardNumber;
    }
    public LocalDate getExamDate() {
        return examDate;
    }
    public List<String> getDocuments() {
        return documents;
    }
    public double getClaimAmount() {
        return claimAmount;
    }
    public ClaimStatus getStatus() {
        return status;
    }
    public BankingInfo getReceiverBankingInfo() {
        return receiverBankingInfo;
    }

    // Setters
    public void setStatusNew(){
        this.status = ClaimStatus.NEW;
    }
    public void setStatusProcessing(){
        this.status = ClaimStatus.PROCESSING;
    }
    public void setStatusDone(){
        this.status = ClaimStatus.DONE;
    }

    public void setStatusRejected() {
        this.status = ClaimStatus.REJECTED;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }
    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }
    public void setReceiverBankingInfo(BankingInfo receiverBankingInfo) {
        this.receiverBankingInfo = receiverBankingInfo;
    }
    public void setInsuredPerson(Customer insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    // Manipulate Document List
    public void addDocument(String document){
        this.documents.add(document);
    }
    public void removeDocument(String document) {this.documents.remove(document);}
}
