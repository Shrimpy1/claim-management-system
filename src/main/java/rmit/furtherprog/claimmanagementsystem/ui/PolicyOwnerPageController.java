/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Customer;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.service.PolicyOwnerService;

public class PolicyOwnerPageController {
    private PolicyOwnerService service;
    public void setService(PolicyOwnerService service){
        this.service = service;
    }

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField beneficiaryField;

    @FXML
    private VBox updateInfoSection;

    @FXML
    private VBox viewClaimsSection;

    @FXML
    private VBox fileClaimSection;

    @FXML
    private VBox updateClaimSection;

    @FXML
    private VBox deleteClaimSection;

    @FXML
    private TextArea claimsTextArea;

    @FXML
    private TextArea fileClaimTextArea;

    @FXML
    private TextArea updateClaimTextArea;

    @FXML
    private TextArea deleteClaimTextArea;

    @FXML
    private void handleUpdateInfo() {
        hideAllSections();
        updateInfoSection.setVisible(true);
        updateInfoSection.setManaged(true);

        PolicyOwner policyOwner = service.getPolicyOwner();
        idField.setText(policyOwner.getId());
        nameField.setText(policyOwner.getFullName());

        if (policyOwner.getBeneficiaries() != null){
            StringBuilder beneficiariesText = new StringBuilder();
            for (Customer beneficiary : policyOwner.getBeneficiaries()) {
                beneficiariesText.append(beneficiary.getFullName()).append("\n");
            }
            beneficiaryField.setText(beneficiariesText.toString());
        }
    }

    @FXML
    private void handleViewClaims() {
        hideAllSections();
        viewClaimsSection.setVisible(true);
        viewClaimsSection.setManaged(true);
        // Logic to retrieve and display claims
        claimsTextArea.setText("Display retrieved claims here...");
    }

    @FXML
    private void handleFileClaim() {
        hideAllSections();
        fileClaimSection.setVisible(true);
        fileClaimSection.setManaged(true);
    }

    @FXML
    private void handleUpdateClaim() {
        hideAllSections();
        updateClaimSection.setVisible(true);
        updateClaimSection.setManaged(true);
    }

    @FXML
    private void handleDeleteClaim() {
        hideAllSections();
        deleteClaimSection.setVisible(true);
        deleteClaimSection.setManaged(true);
    }

    @FXML
    private void handleSubmitUpdateInfo() {
        // Logic to update policy owner info
        showAlert("Update Info", "Policy owner info updated successfully!");
    }

    @FXML
    private void handleSubmitFileClaim() {
        // Logic to file a new claim
        showAlert("File Claim", "New claim filed successfully!");
    }

    @FXML
    private void handleSubmitUpdateClaim() {
        // Logic to update an existing claim
        showAlert("Update Claim", "Claim updated successfully!");
    }

    @FXML
    private void handleSubmitDeleteClaim() {
        // Logic to delete a claim
        showAlert("Delete Claim", "Claim deleted successfully!");
    }

    private void hideAllSections() {
        updateInfoSection.setVisible(false);
        updateInfoSection.setManaged(false);
        viewClaimsSection.setVisible(false);
        viewClaimsSection.setManaged(false);
        fileClaimSection.setVisible(false);
        fileClaimSection.setManaged(false);
        updateClaimSection.setVisible(false);
        updateClaimSection.setManaged(false);
        deleteClaimSection.setVisible(false);
        deleteClaimSection.setManaged(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static double calculateAnnualPayment(PolicyOwner policyOwner){
        double totalPayment = 0;
        for (Customer customer : policyOwner.getBeneficiaries()){
            for (Claim claim : customer.getClaims()){
                totalPayment += claim.getClaimAmount() * ((customer instanceof Policyholder)? 1 : 0.6);
            }
        }

        return totalPayment;
    }
}
