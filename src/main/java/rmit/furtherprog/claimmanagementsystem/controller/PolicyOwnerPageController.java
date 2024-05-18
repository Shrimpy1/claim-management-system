/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rmit.furtherprog.claimmanagementsystem.Main;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Customer;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.database.*;
import rmit.furtherprog.claimmanagementsystem.service.*;
import rmit.furtherprog.claimmanagementsystem.util.DateParsing;
import rmit.furtherprog.claimmanagementsystem.util.Verifier;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class PolicyOwnerPageController {
    private PolicyOwnerService service;
    public void setService(PolicyOwnerService service){
        this.service = service;
        welcomeLabel.setText("Welcome " + service.getPolicyOwner().getFullName());
    }

    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox additionalContentContainer;
    @FXML
    private Button logoutButton;
    @FXML
    private Label servicesLabel;
    @FXML
    private VBox servicesContainer;
    @FXML
    private Button selfInfoButton;
    @FXML
    private Button manageClaimsButton;
    @FXML
    private Button manageDependantsButton;
    @FXML
    private Button calcYearlyButton;

    public void initialize() {

    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        try {
            Main.showLoginPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSelfInfoButton() {
        clearAdditionalContent();

        Label titleLabel = new Label("Self Information");
        additionalContentContainer.getChildren().add(titleLabel);

        HBox buttonsContainer = new HBox(10);
        Button viewButton = new Button("View");
        Button updateButton = new Button("Update");
        buttonsContainer.getChildren().addAll(viewButton, updateButton);
        additionalContentContainer.getChildren().add(buttonsContainer);

        viewButton.setOnAction(event -> showSelfInfo());
        updateButton.setOnAction(event -> enableSelfInfoEditing());
    }

    private void showSelfInfo() {
        clearAdditionalContent();

        Label id = new Label("ID: " + service.getPolicyOwner().getId());
        Label fullName = new Label("Full name: " + service.getPolicyOwner().getFullName());

        VBox userInfo = new VBox(3);
        userInfo.getChildren().addAll(id, fullName);

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void enableSelfInfoEditing() {
        clearAdditionalContent();

        TextField nameField = new TextField(service.getPolicyOwner().getFullName());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveSelfInfo(nameField.getText()));

        VBox userInfo = new VBox(1);
        userInfo.getChildren().addAll(
                new Label("Full name:"), nameField,
                saveButton
        );

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void saveSelfInfo(String name) {
        clearAdditionalContent();

        service.getPolicyOwner().setFullName(name);
        service.update();

        Label nameLabel = new Label("Full name: " + name);

        VBox userInfo = new VBox(1);
        userInfo.getChildren().addAll(nameLabel);

        additionalContentContainer.getChildren().add(userInfo);
    }

    public void handleManageClaimsButton() {
        clearAdditionalContent();

        Label titleLabel = new Label("Manage Claims");
        additionalContentContainer.getChildren().add(titleLabel);

        HBox buttonsContainer = new HBox(10);
        Button fileButton = new Button("File");
        Button viewButton = new Button("View");

        fileButton.setOnAction(event -> handleFileClaim());
        viewButton.setOnAction(event -> {
            try {
                viewClaims();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        buttonsContainer.getChildren().addAll(fileButton, viewButton);

        additionalContentContainer.getChildren().add(buttonsContainer);
    }

    private void handleFileClaim() {
        clearAdditionalContent();

        PolicyOwner currUser = service.getPolicyOwner();

        TextField policyHolderField = new TextField();
        TextField claimDateField = new TextField();
        TextField examDateField = new TextField();
        TextField claimAmountField = new TextField();
        TextField bankField = new TextField();
        TextField bankingAccountField = new TextField();
        TextField bankingNumberField = new TextField();

        VBox claimForm = new VBox(5);
        claimForm.getChildren().addAll(
                new Label("Policyholder ID: "), policyHolderField,
                new Label("Claim Date:"), claimDateField,
                new Label("Exam Date:"), examDateField,
                new Label("Claim Amount:"), claimAmountField,
                new Label("Bank:"), bankField,
                new Label("Banking account:"), bankingAccountField,
                new Label("Banking number:"), bankingNumberField,
                new Label("Documents:")
        );

        List<File> fileList = new ArrayList<>();
        Button uploadNewButton = new Button("Upload New");
        uploadNewButton.setOnAction(event -> handleMultipleFileUpload(fileList, claimForm, uploadNewButton));

        claimForm.getChildren().add(uploadNewButton);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            List<String> policyHolderIds = currUser.getBeneficiaries().stream().filter(customer -> customer instanceof Policyholder)
                    .map(Customer::getId).toList();
            if (!policyHolderIds.contains(policyHolderField.getText())){
                showError("No customer found", "This is not your customer");
            }
            else if (!(Verifier.verifyDate(examDateField.getText()) && Verifier.verifyDate(claimDateField.getText()))){
                showError("Invalid date format", "Date values must follow format yyyy-MM-dd");
            } else {
                String policyHolderId = policyHolderField.getText();
                LocalDate claimDate = DateParsing.stod(claimDateField.getText());
                LocalDate examDate = DateParsing.stod(examDateField.getText());
                double claimAmount = Double.parseDouble(claimAmountField.getText());
                BankingInfo bankingInfo = new BankingInfo(bankField.getText(), bankingAccountField.getText(), bankingNumberField.getText());
                List<String> documents = fileList.stream().map(File::getName).toList();

                for (File document : fileList) {
                    ImageRepository.uploadFile(document);
                }

                try {
                    Connection connection = DatabaseManager.getConnection();
                    ClaimService claimService = new ClaimService(new ClaimRepository(connection));
                    BankingInfoService bankingInfoService = new BankingInfoService(new BankingInfoRepository(connection), bankingInfo);
                    int newId = bankingInfoService.add();
                    BankingInfo newBankingInfo = bankingInfoService.getBankingInfoById(newId);
                    PolicyholderService policyholderService = new PolicyholderService(new PolicyholderRepository(connection));
                    Policyholder customer = policyholderService.getPolicyholderById(policyHolderId);
                    Claim claim = new Claim(claimDate, customer, customer.getInsuranceCard().getCardNumber(), examDate, documents, claimAmount, newBankingInfo);
                    String newClaimId = claimService.add(claim);
                    Claim newClaim = claimService.getClaimById(newClaimId);

                    customer.addClaim(newClaim);
                    DependantService dependantService = new DependantService(new DependantRepository(connection));
                    List<String> dependantIds = customer.getDependants().stream().map(Dependant::getId).toList();
                    for (String id : dependantIds){
                        Dependant dependant = dependantService.getDependantById(id);
                        dependant.addClaim(newClaim);
                        dependantService.update(dependant);
                    }
                    policyholderService.update(customer);

                    List<Hyperlink> documentLinks = new ArrayList<>();
                    for (File document : fileList) {
                        Hyperlink documentLink = new Hyperlink(document.getName());
                        documentLink.setOnAction(actionEvent -> {
                            try {
                                showImageView(document);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        documentLinks.add(documentLink);
                    }

                    VBox claimDetails = new VBox(5);
                    claimDetails.getChildren().addAll(
                            new Label("New Claim Id: " + newClaimId),
                            new Label("Insured Person: " + customer.getFullName()),
                            new Label("Insurance Card: " + customer.getInsuranceCard().getCardNumber()),
                            new Label("Claim Date: " + claimDate),
                            new Label("Exam Date: " + examDate),
                            new Label("Documents: ")
                    );
                    claimDetails.getChildren().addAll(documentLinks);
                    claimDetails.getChildren().addAll(
                            new Label("Claim Amount: " + claimAmount),
                            new Label("Bank: " + bankingInfo.getBank()),
                            new Label("Banking account: " + bankingInfo.getName()),
                            new Label("Banking number: " + bankingInfo.getNumber())
                    );

                    clearAdditionalContent();
                    additionalContentContainer.getChildren().add(claimDetails);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        additionalContentContainer.getChildren().addAll(claimForm, saveButton);
    }

    private void viewClaims() throws SQLException {
        clearAdditionalContent();

        Map<String, List<String>> claimList = new HashMap<>();
        for (Customer customer : service.getPolicyOwner().getBeneficiaries()) {
            List<String> claimIds = new ArrayList<>();
            for (Claim claim : customer.getClaims()) {
                claimIds.add(claim.getId());
            }
            claimList.put(customer.getFullName(), claimIds);
        }

        ClaimService claimService = new ClaimService(new ClaimRepository(DatabaseManager.getConnection()));

        for (String name : claimList.keySet()) {
            VBox claimContainer = new VBox(5);
            Label nameLabel = new Label(name);
            claimContainer.getChildren().add(nameLabel);

            for (String claimId : claimList.get(name)) {
                Hyperlink claimLink = new Hyperlink(claimId);
                claimLink.setOnAction(event -> showClaimDetails(claimService.getClaimById(claimId)));
                claimContainer.getChildren().add(claimLink);
            }

            additionalContentContainer.getChildren().add(claimContainer);
        }
    }

    private void showClaimDetails(Claim claim) {
        clearAdditionalContent();

        Label titleLabel = new Label("Claim Details: " + claim.getId());
        additionalContentContainer.getChildren().add(titleLabel);

        List<Hyperlink> documentLinks = new ArrayList<>();
        for (String document : claim.getDocuments()){
            Hyperlink documentLink = new Hyperlink(document);
            documentLink.setOnAction(actionEvent -> {
                try {
                    File file = ImageRepository.getFile(document);
                    showImageView(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            documentLinks.add(documentLink);
        }

        VBox claimDetails = new VBox(5);
        claimDetails.getChildren().addAll(
                new Label("Claim Date: " + claim.getClaimDate()),
                new Label("Insured Person: " + claim.getInsuredPerson().getFullName()),
                new Label("Card Number: " + claim.getCardNumber()),
                new Label("Exam Date: " + claim.getExamDate()),
                new Label("Documents: "));
        claimDetails.getChildren().addAll(documentLinks);
        claimDetails.getChildren().addAll(
                new Label("Claim Amount: " + claim.getClaimAmount()),
                new Label("Bank: " + claim.getReceiverBankingInfo().getBank()),
                new Label("Bank account: " + claim.getReceiverBankingInfo().getName()),
                new Label("Bank number: " + claim.getReceiverBankingInfo().getNumber())
        );

        Button updateButton = new Button("Update");
        updateButton.setOnAction(actionEvent -> {
            try {
                handleUpdateClaim(claim);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("DELETE CLAIM");
            alert.setHeaderText("Are you sure that you want to delete this claim?");
            alert.setContentText("The action cannot be undone.");

            ButtonType buttonNo = new ButtonType("No");
            ButtonType buttonConfirm = new ButtonType("Confirm");
            alert.getButtonTypes().setAll(buttonNo, buttonConfirm);
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.isPresent() && answer.get() == buttonConfirm){
                try {
                    handleDeleteClaim(claim);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                newAlert.setTitle("CLAIM DELETED");
                newAlert.setHeaderText("The Claim has been deleted");
                newAlert.showAndWait();
            }
        });

        HBox hBox = new HBox(15, updateButton, deleteButton);

        additionalContentContainer.getChildren().addAll(claimDetails, hBox);
    }

    private void handleUpdateClaim(Claim claim) throws IOException {
        clearAdditionalContent();
        VBox updateForm = new VBox(5);

        TextField claimDateField = new TextField(DateParsing.dtos(claim.getClaimDate()));
        TextField examDateField = new TextField(DateParsing.dtos(claim.getExamDate()));
        TextField claimAmountField = new TextField(String.valueOf(claim.getClaimAmount()));
        TextField bankField = new TextField(claim.getReceiverBankingInfo().getBank());
        TextField bankingAccountField = new TextField(claim.getReceiverBankingInfo().getName());
        TextField bankingNumberField = new TextField(claim.getReceiverBankingInfo().getNumber());

        List<File> fileList = new ArrayList<>();
        VBox documentForm = new VBox(5);
        for (String document : claim.getDocuments()){
            File file = ImageRepository.getFile(document);
            HBox row = createNewDocumentRow(fileList, file, documentForm);
            documentForm.getChildren().add(row);
        }
        Button uploadNewButton = new Button("Upload New");
        uploadNewButton.setOnAction(event -> handleMultipleFileUpload(fileList, documentForm, uploadNewButton));

        documentForm.getChildren().add(uploadNewButton);

        updateForm.getChildren().addAll(
                new Label("Claim Date:"), claimDateField,
                new Label("Exam Date:"), examDateField,
                new Label("Claim Amount:"), claimAmountField,
                new Label("Bank:"), bankField,
                new Label("Banking account:"), bankingAccountField,
                new Label("Banking number:"), bankingNumberField,
                new Label("Documents:")
        );
        updateForm.getChildren().add(documentForm);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            LocalDate claimDate = DateParsing.stod(claimDateField.getText());
            LocalDate examDate = DateParsing.stod(examDateField.getText());
            double claimAmount = Double.parseDouble(claimAmountField.getText());
            BankingInfo bankingInfo = new BankingInfo(claim.getReceiverBankingInfo().getId(), bankField.getText(), bankingAccountField.getText(), bankingNumberField.getText());
            List<String> documents = fileList.stream().map(File::getName).toList();

            for (String fileName : claim.getDocuments()){
                ImageRepository.deleteFile(fileName);
            }
            for (File document : fileList){
                ImageRepository.uploadFile(document);
            }

            claim.setClaimDate(claimDate);
            claim.setExamDate(examDate);
            claim.setClaimAmount(claimAmount);
            claim.setDocuments(documents);

            try {
                Connection connection = DatabaseManager.getConnection();
                ClaimService claimService = new ClaimService(new ClaimRepository(connection), claim);
                BankingInfoService bankingInfoService = new BankingInfoService(new BankingInfoRepository(connection), bankingInfo);
                bankingInfoService.update();
                claimService.update();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            List<Hyperlink> documentLinks = new ArrayList<>();
            for (String document : claim.getDocuments()){
                Hyperlink documentLink = new Hyperlink(document);
                documentLink.setOnAction(actionEvent -> {
                    try {
                        File file = ImageRepository.getFile(document);
                        showImageView(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                documentLinks.add(documentLink);
            }

            VBox claimDetails = new VBox(5);
            claimDetails.getChildren().addAll(
                    new Label("Claim Date: " + claimDate),
                    new Label("Exam Date: " + examDate),
                    new Label("Claim Amount: " + claimAmount),
                    new Label("Bank: " + bankingInfo.getBank()),
                    new Label("Banking account: " + bankingInfo.getName()),
                    new Label("Banking number: " + bankingInfo.getNumber()),
                    new Label("Documents:"));
            claimDetails.getChildren().addAll(documentLinks);

            clearAdditionalContent();
            additionalContentContainer.getChildren().add(claimDetails);
        });

        additionalContentContainer.getChildren().addAll(updateForm, saveButton);
    }

    public void handleDeleteClaim(Claim claim) throws SQLException {
        Connection connection = DatabaseManager.getConnection();

        PolicyholderService policyholderService = new PolicyholderService(new PolicyholderRepository(connection));
        DependantService dependantService = new DependantService(new DependantRepository(connection));

        Policyholder policyholder = policyholderService.getPolicyholderById(claim.getInsuredPerson().getId());
        policyholder.removeClaimById(claim.getId());
        for (Dependant dependant : policyholder.getDependants()){
            Dependant d = dependantService.getDependantById(dependant.getId());
            d.removeClaimById(claim.getId());
            dependantService.update(d);
        }
        policyholderService.update(policyholder);

        ClaimService claimService = new ClaimService(new ClaimRepository(connection));
        claimService.setClaim(claimService.getClaimById(claim.getId()));
        claimService.delete();

        clearAdditionalContent();
    }

    public void handleManageDependantsButton() {
        clearAdditionalContent();

        Label titleLabel = new Label("Manage Dependants");
        additionalContentContainer.getChildren().add(titleLabel);

        // Mock dependant list
        String[] dependants = {"Dependant 1", "Dependant 2", "Dependant 3"};
        VBox dependantsList = new VBox(5);
        for (String dependant : dependants) {
            Hyperlink dependantLink = new Hyperlink(dependant);
            dependantLink.setOnAction(event -> showDependantOptions(dependant));
            dependantsList.getChildren().add(dependantLink);
        }
        additionalContentContainer.getChildren().add(dependantsList);
    }

    private void showDependantOptions(String dependant) {
        clearAdditionalContent();

        Label titleLabel = new Label("Dependant Details: " + dependant);
        additionalContentContainer.getChildren().add(titleLabel);

        // Mock data for dependant
        String phone = "123-456-7890";
        String address = "123 Main St, City";
        String email = "dependant@example.com";
        String password = "password";

        Label phoneLabel = new Label("Phone: " + phone);
        Label addressLabel = new Label("Address: " + address);
        Label emailLabel = new Label("Email: " + email);
        Label passwordLabel = new Label("Password: " + password);

        VBox dependantDetails = new VBox(5);
        dependantDetails.getChildren().addAll(phoneLabel, addressLabel, emailLabel, passwordLabel);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> enableDependantEditing(dependantDetails, phone, address, email, password));

        additionalContentContainer.getChildren().addAll(dependantDetails, updateButton);
    }

    private void enableDependantEditing(VBox detailsContainer, String phone, String address, String email, String password) {
        detailsContainer.getChildren().clear();

        TextField phoneField = new TextField(phone);
        TextField addressField = new TextField(address);
        TextField emailField = new TextField(email);
        TextField passwordField = new TextField(password);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveDependantInfo(detailsContainer, phoneField.getText(), addressField.getText(), emailField.getText(), passwordField.getText()));

        detailsContainer.getChildren().addAll(
                new Label("Phone:"), phoneField,
                new Label("Address:"), addressField,
                new Label("Email:"), emailField,
                new Label("Password:"), passwordField,
                saveButton
        );
    }

    private void saveDependantInfo(VBox detailsContainer, String phone, String address, String email, String password) {
        detailsContainer.getChildren().clear();

        Label phoneLabel = new Label("Phone: " + phone);
        Label addressLabel = new Label("Address: " + address);
        Label emailLabel = new Label("Email: " + email);
        Label passwordLabel = new Label("Password: " + password);

        detailsContainer.getChildren().addAll(phoneLabel, addressLabel, emailLabel, passwordLabel);
    }

    private void showImageView(File file) throws IOException {
        Stage imageStage = new Stage();
        ImageView imageView = new ImageView(ImageRepository.renderPdfImage(file));
        imageView.setFitWidth(800);
        imageView.setFitHeight(1000);
        VBox vbox = new VBox(imageView);
        Scene scene = new Scene(vbox);
        imageStage.setScene(scene);
        imageStage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleFileUpdate(List<File> fileList, File file, Button button, Hyperlink hyperlink) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File newFile = fileChooser.showOpenDialog(button.getScene().getWindow());
        if (newFile != null) {
            if (newFile.getName().toLowerCase().endsWith(".pdf")) {
                fileList.remove(file);
                fileList.add(newFile);
                hyperlink.setText(newFile.getName());
                hyperlink.setOnAction(actionEvent -> {
                    try {
                        showImageView(newFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                System.err.println("File must be in PDF format.");
                showError("Wrong file format", "File must be in PDF format.");
            }
        }
    }

    private void handleMultipleFileUpload(List<File> fileList, VBox form, Button button) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        if (file != null) {
            if (file.getName().toLowerCase().endsWith(".pdf")) {
                form.getChildren().removeLast();
                form.getChildren().addAll(createNewDocumentRow(fileList, file, form), button);
            } else {
                System.err.println("File must be in PDF format.");
                showError("Wrong file format", "File must be in PDF format.");
            }
        }
    }

    private HBox createNewDocumentRow(List<File> fileList, File file, VBox form){
        fileList.add(file);
        Hyperlink documentLink = new Hyperlink(file.getName());
        documentLink.setOnAction(actionEvent -> {
            try {
                showImageView(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Button editButton = new Button("edit");
        editButton.setOnAction(actionEvent -> handleFileUpdate(fileList, file, editButton, documentLink));
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(documentLink, editButton);
        Button removeButton = new Button("remove");
        removeButton.setOnAction(actionEvent -> {
            form.getChildren().remove(hBox);
            fileList.remove(file);
        });
        hBox.getChildren().add(removeButton);
        return hBox;
    }

    @FXML
    private void handleCalcYearlyButton() {
        clearAdditionalContent();
        //function here
    }

    private void clearAdditionalContent() {
        additionalContentContainer.getChildren().clear();
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
