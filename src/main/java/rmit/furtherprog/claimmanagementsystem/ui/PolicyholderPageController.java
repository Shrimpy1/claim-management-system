/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.database.ClaimRepository;
import rmit.furtherprog.claimmanagementsystem.database.DatabaseManager;
import rmit.furtherprog.claimmanagementsystem.database.DependantRepository;
import rmit.furtherprog.claimmanagementsystem.database.ImageRepository;
import rmit.furtherprog.claimmanagementsystem.service.ClaimService;
import rmit.furtherprog.claimmanagementsystem.service.DependantService;
import rmit.furtherprog.claimmanagementsystem.service.PolicyholderService;
import rmit.furtherprog.claimmanagementsystem.util.DateParsing;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PolicyholderPageController {
    private PolicyholderService service;

    public void setService(PolicyholderService service) {
        this.service = service;
        welcomeLabel.setText("Welcome " + service.getPolicyholder().getFullName());
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
    private List<File> uploadedFiles;

    public void initialize() {
        this.uploadedFiles = new ArrayList<>();
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

        Label id = new Label("ID: " + service.getPolicyholder().getId());
        Label fullName = new Label("Full name: " + service.getPolicyholder().getFullName());
        Label insuranceCardNumber = new Label("Insurance Card number: " + service.getPolicyholder().getInsuranceCard().getCardNumber());

        VBox userInfo = new VBox(3);
        userInfo.getChildren().addAll(id, fullName, insuranceCardNumber);

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void enableSelfInfoEditing() {
        clearAdditionalContent();

        TextField nameField = new TextField(service.getPolicyholder().getFullName());

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

        service.getPolicyholder().setFullName(name);
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
//        clearAdditionalContent();
//
//        TextField claimDateField = new TextField();
//        TextField insuredPersonField = new TextField();
//        TextField cardNumberField = new TextField();
//        TextField examDateField = new TextField();
//        Button uploadButton = new Button("Upload Document");
//        uploadButton.setOnAction(event -> handleFileUpdate(uploadButton));
//        TextField claimAmountField = new TextField();
//        TextField receiverBankingInfoField = new TextField();
//
//        Button saveButton = new Button("Save");
//        saveButton.setOnAction(event -> saveClaimDetails(claimDateField.getText(), insuredPersonField.getText(), cardNumberField.getText(), examDateField.getText(), uploadedFile != null ? uploadedFile.getName() : "", claimAmountField.getText(), receiverBankingInfoField.getText()));
//
//        VBox claimForm = new VBox(5);
//        claimForm.getChildren().addAll(
//                new Label("Claim Date:"), claimDateField,
//                new Label("Insured Person:"), insuredPersonField,
//                new Label("Card Number:"), cardNumberField,
//                new Label("Exam Date:"), examDateField,
//                new Label("Document:"), uploadButton,
//                new Label("Claim Amount:"), claimAmountField,
//                new Label("Receiver Banking Info:"), receiverBankingInfoField,
//                saveButton
//        );
//
//        additionalContentContainer.getChildren().add(claimForm);
    }

    private void viewClaims() throws SQLException {
        clearAdditionalContent();

        List<String> claimIds = new ArrayList<>();
        for (Claim claim : service.getPolicyholder().getClaims()){
            claimIds.add(claim.getId());
        }

        ClaimService claimService = new ClaimService(new ClaimRepository(DatabaseManager.getConnection()));
        VBox claimsList = new VBox(claimIds.size());
        for (String claimId : claimIds) {
            Hyperlink claimLink = new Hyperlink(claimId);
            claimLink.setOnAction(event -> showClaimDetails(claimService.getClaimById(claimId)));
            claimsList.getChildren().add(claimLink);
        }

        additionalContentContainer.getChildren().add(claimsList);
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
                    showImageView(document);
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
        updateButton.setOnAction(actionEvent -> handleUpdateClaim(claim));

        additionalContentContainer.getChildren().addAll(claimDetails, updateButton);
    }

    private void handleUpdateClaim(Claim claim) {
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
            HBox row = createNewDocumentRow(fileList, document, documentForm);
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
            String claimDate = claimDateField.getText();
            String examDate = examDateField.getText();
            double claimAmount = Double.parseDouble(claimAmountField.getText());
            BankingInfo bankingInfo = new BankingInfo(bankField.getText(), bankingAccountField.getText(), bankingNumberField.getText());
            String documents = fileList.stream().map(File::getName).collect(Collectors.joining(", "));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(documents);
            alert.showAndWait();
            saveClaimDetails(updateForm);
        });

        additionalContentContainer.getChildren().addAll(updateForm, saveButton);
    }



    private void saveClaimDetails(VBox updateForm) {
        clearAdditionalContent();

    }

    public void handleManageDependantsButton() throws SQLException {
        clearAdditionalContent();

        Label titleLabel = new Label("Manage Dependants");
        additionalContentContainer.getChildren().add(titleLabel);

        List<String> dependantIds = new ArrayList<>();
        for (Dependant dependant : service.getPolicyholder().getDependants()){
            dependantIds.add(dependant.getId());
        }

        DependantService dependantService = new DependantService(new DependantRepository(DatabaseManager.getConnection()));

        VBox dependantsList = new VBox(dependantIds.size());
        for (String dependantId : dependantIds) {
            Hyperlink dependantLink = new Hyperlink(dependantId);
            dependantLink.setOnAction(event -> showDependantOptions(dependantService.getDependantById(dependantId)));
            dependantsList.getChildren().add(dependantLink);
        }
        additionalContentContainer.getChildren().add(dependantsList);
    }

    private void showDependantOptions(Dependant dependant) {
        clearAdditionalContent();

        Label titleLabel = new Label("Dependant Details: " + dependant.getFullName());
        additionalContentContainer.getChildren().add(titleLabel);

        Label id = new Label("ID: " + dependant.getId());
        Label fullName = new Label("Full name: " + dependant.getFullName());
        Label insuranceCardNumber = new Label("Insurance Card number: " + dependant.getInsuranceCard().getCardNumber());

        VBox dependantDetails = new VBox(3);
        dependantDetails.getChildren().addAll(id, fullName, insuranceCardNumber);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> enableDependantEditing(dependantDetails, dependant));

        additionalContentContainer.getChildren().addAll(dependantDetails, updateButton);
    }


    private void enableDependantEditing(VBox detailsContainer, Dependant dependant) {
        clearAdditionalContent();
        detailsContainer.getChildren().clear();

        TextField fullName = new TextField(dependant.getFullName());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            try {
                saveDependantInfo(detailsContainer, dependant, fullName.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        detailsContainer.getChildren().addAll(
                new Label("Full name:"), fullName,
                saveButton
        );

        additionalContentContainer.getChildren().add(detailsContainer);
    }

    private void saveDependantInfo(VBox detailsContainer, Dependant dependant, String fullName) throws SQLException {
        detailsContainer.getChildren().clear();

        dependant.setFullName(fullName);
        DependantService dependantService = new DependantService(new DependantRepository(DatabaseManager.getConnection()));
        dependantService.update(dependant);

        Label fullNameLabel = new Label("Full name: " + fullName);

        detailsContainer.getChildren().addAll(fullNameLabel);
    }

    private void clearAdditionalContent() {
        additionalContentContainer.getChildren().clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showImageView(String fileName) throws IOException {
//        Stage imageStage = new Stage();
//
//        ImageView imageView = new ImageView(ImageRepository.getImage(fileName));
//        imageView.setFitWidth(800);
//        imageView.setFitHeight(1000);
//        VBox vbox = new VBox(imageView);
//        Scene scene = new Scene(vbox);
//        imageStage.setScene(scene);
//        imageStage.show();
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

    private void handleFileUpdate(List<File> fileList, File file, Button button, Hyperlink hyperlink) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File newFile = fileChooser.showOpenDialog(button.getScene().getWindow());
        if (newFile != null) {
            if (newFile.getName().toLowerCase().endsWith(".pdf")) {
                if (file != null){fileList.remove(file);}
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
                showError("File must be in PDF format.");
            }
        }
    }

    private void handleMultipleFileUpload(List<File> fileList, VBox form, Button button) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        if (file != null) {
            if (file.getName().toLowerCase().endsWith(".pdf")) {
                uploadedFiles.add(file);
            } else {
                System.err.println("File must be in PDF format.");
                showError("File must be in PDF format.");
            }
        } else {
            button.setText("Empty");
        }

        form.getChildren().removeLast();
        assert file != null;
        form.getChildren().addAll(createNewDocumentRow(fileList, file, form), button);
    }

    private HBox createNewDocumentRow(List<File> fileList, File file, VBox form){
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

    private HBox createNewDocumentRow(List<File> fileList, String fileName, VBox form){
        Hyperlink documentLink = new Hyperlink(fileName);
        documentLink.setOnAction(actionEvent -> {
            try {
                showImageView(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Button editButton = new Button("edit");
        editButton.setOnAction(actionEvent -> handleFileUpdate(fileList, null, editButton, documentLink));
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(documentLink, editButton);
        Button removeButton = new Button("remove");
        removeButton.setOnAction(actionEvent -> form.getChildren().remove(hBox));
        hBox.getChildren().add(removeButton);
        return hBox;
    }
}
