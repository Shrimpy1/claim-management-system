/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import rmit.furtherprog.claimmanagementsystem.Main;
import rmit.furtherprog.claimmanagementsystem.database.*;
import rmit.furtherprog.claimmanagementsystem.service.*;
import rmit.furtherprog.claimmanagementsystem.util.AccountManager;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.Connection;

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
        Connection connection = DatabaseManager.getConnection();
        if (validation != null) {
            showAlert("Login Successful", "Welcome, " + username + "!");

            switch (validation){
                case "admin":
                    Main.showAdminPage();
                    break;
                case "policy_owner":
                    PolicyOwnerService policyOwnerService = new PolicyOwnerService(new PolicyOwnerRepository(connection));
                    policyOwnerService.setPolicyOwner(policyOwnerService.getPolicyOwnerById(username));
                    Main.showPolicyOwnerPage(policyOwnerService);
                    break;
                case "dependant":
                    DependantService dependantService = new DependantService(new DependantRepository(connection));
                    dependantService.setDependant(dependantService.getDependantById(username));
                    Main.showDependantPage(dependantService);
                    break;
                case "policyholder":
                    PolicyholderService policyholderService = new PolicyholderService(new PolicyholderRepository(connection));
                    policyholderService.setPolicyholder(policyholderService.getPolicyholderById(username));
                    Main.showPolicyHolderPage(policyholderService);
                    break;
                case "manager":
                    int managerId = IdConverter.fromEmployeeId(username);
                    ManagerService managerService = new ManagerService(new ManagerRepository(connection));
                    managerService.setManager(managerService.getManagerById(managerId));
                    break;
                case "surveyor":
                    int surveyorId = IdConverter.fromEmployeeId(username);
                    SurveyorService surveyorService = new SurveyorService(new SurveyorRepository(connection));
                    surveyorService.setSurveyor(surveyorService.getSurveyorById(surveyorId));
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
