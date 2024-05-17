/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.database.DatabaseManager;
import rmit.furtherprog.claimmanagementsystem.database.DependantRepository;
import rmit.furtherprog.claimmanagementsystem.database.PolicyOwnerRepository;
import rmit.furtherprog.claimmanagementsystem.database.PolicyholderRepository;
import rmit.furtherprog.claimmanagementsystem.service.DependantService;
import rmit.furtherprog.claimmanagementsystem.service.PolicyOwnerService;
import rmit.furtherprog.claimmanagementsystem.service.PolicyholderService;
import rmit.furtherprog.claimmanagementsystem.util.AccountManager;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String validation = AccountManager.verifyAccount(username, password);
        if (validation != null) {
            showAlert("Login Successful", "Welcome, " + username + "!");

            switch (validation){
                case "admin":
                    Main.showAdminPage();
                    break;
                case "policy_owner":
                    PolicyOwnerService policyOwnerService = new PolicyOwnerService(new PolicyOwnerRepository(DatabaseManager.getConnection()));
                    policyOwnerService.setPolicyOwner(policyOwnerService.getPolicyOwnerById(username));
                    Main.showPolicyOwnerPage(policyOwnerService);
                    break;
                case "dependant":
                    DependantService dependantService = new DependantService(new DependantRepository(DatabaseManager.getConnection()));
                    dependantService.setDependant(dependantService.getDependantById(username));
                    Main.showDependantPage(dependantService);
                    break;
                case "policyholder":
                    PolicyholderService policyholderService = new PolicyholderService(new PolicyholderRepository(DatabaseManager.getConnection()));
                    policyholderService.setPolicyholder(policyholderService.getPolicyholderById(username));
                    Main.showPolicyHolderPage(policyholderService);
                    break;
                default:
                    showAlert("Error", "Invalid user role");
                    break;
            }
        } else {
            showAlert("Login Failed", "Invalid username or password");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
//        AccountManager.createAccount("c0000002", "123", "dependant");
//        AccountManager.createAccount("c0000001", "123", "policyholder");
    }
}
