package rmit.furtherprog.claimmanagementsystem.controller;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rmit.furtherprog.claimmanagementsystem.Main;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Customer;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.database.*;
import rmit.furtherprog.claimmanagementsystem.service.ClaimService;
import rmit.furtherprog.claimmanagementsystem.service.DependantService;
import rmit.furtherprog.claimmanagementsystem.service.PolicyholderService;
import rmit.furtherprog.claimmanagementsystem.service.SurveyorService;
import rmit.furtherprog.claimmanagementsystem.util.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SurveyorPageController {
    private SurveyorService service;
    private Connection connection;

    public void setService(SurveyorService service) {
        this.service = service;
        welcomeLabel.setText("Welcome " + service.getSurveyor().getName());
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
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
    private Button manageCustomersButton;
    @FXML
    private TextField searchField;
    @FXML
    private VBox infoContainer;
    @FXML
    private ScrollPane infoScrollContainer;
    @FXML
    private HBox searchBox;
    private List<Claim> claimsData;
    private List<Customer> customersData;

    public void initialize() throws SQLException {
        setConnection(DatabaseManager.getConnection());
        claimsData = new ArrayList<>();
        customersData = new ArrayList<>();
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
        searchBox.setVisible(false);
        infoScrollContainer.setVisible(false);
        searchBox.setManaged(false);
        infoScrollContainer.setManaged(false);

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

        Label id = new Label("ID: " + service.getSurveyor().getId());
        Label fullName = new Label("Full name: " + service.getSurveyor().getName());

        VBox userInfo = new VBox(5);
        userInfo.getChildren().addAll(id, fullName);

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void enableSelfInfoEditing() {
        clearAdditionalContent();

        TextField nameField = new TextField(service.getSurveyor().getName());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveSelfInfo(nameField.getText()));

        VBox userInfo = new VBox(5);
        userInfo.getChildren().addAll(
                new Label("Full name:"), nameField,
                saveButton
        );

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void saveSelfInfo(String name) {
        clearAdditionalContent();

        service.getSurveyor().setName(name);
        service.update();

        Label nameLabel = new Label("Full name: " + name);

        VBox userInfo = new VBox(5);
        userInfo.getChildren().addAll(nameLabel);

        additionalContentContainer.getChildren().add(userInfo);
    }

    @FXML
    public void handleManageClaimsButton() {
        clearAdditionalContent();

        Label titleLabel = new Label("Manage Claims");
        additionalContentContainer.getChildren().add(titleLabel);

        searchBox.setVisible(false);
        infoScrollContainer.setVisible(false);
        searchBox.setManaged(false);
        infoScrollContainer.setManaged(false);

        HBox buttonsContainer = new HBox(10);
        Button viewBtn = new Button("Proposed Claims");
        viewBtn.setOnAction(actionEvent -> handleReviewClaimsButton());

        buttonsContainer.getChildren().add(viewBtn);
        additionalContentContainer.getChildren().add(buttonsContainer);
    }
    
    private void handleReviewClaimsButton() {
        clearAdditionalContent();

        searchBox.setVisible(true);
        infoScrollContainer.setVisible(true);
        searchBox.setManaged(true);
        infoScrollContainer.setManaged(true);

        initializeClaimsData();
    }

    private void initializeClaimsData() {
        ClaimService claimService = new ClaimService(new ClaimRepository(connection));
        claimsData = claimService.getNewClaims();

        displayClaimsAsHyperlinks(claimsData);
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            displayClaimsAsHyperlinks(claimsData);
        } else {
            List<Claim> searchResults = searchClaims(searchText);
            displayClaimsAsHyperlinks(searchResults);
        }
    }

    private List<Claim> searchClaims(String searchText) {
        List<Claim> searchResults = new ArrayList<>();
        for (Claim claim : claimsData) {
            if (claim.getId().contains(searchText)) {
                searchResults.add(claim);
            }
        }
        return searchResults;
    }

    private void displayClaimsAsHyperlinks(List<Claim> claims) {
        infoContainer.getChildren().clear();

        VBox claimsList = new VBox(5);
        for (Claim claim : claims) {
            Hyperlink claimLink = new Hyperlink(claim.getId());
            claimLink.setOnAction(event -> showClaimDetails(claim));

            claimsList.getChildren().add(claimLink);
        }

        infoContainer.getChildren().add(claimsList);
    }

    private void showClaimDetails(Claim claim) {
        clearAdditionalContent();
        infoContainer.getChildren().clear();

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
                new Label("Request for more Documents: " + RequestHandler.getByClaimId(claim.getId())),
                new Label("Status: " + claim.getStatus()),
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

        Button requestInfoBtn = new Button("Request More Information");
        requestInfoBtn.setOnAction(actionEvent -> handleRequestInfoButton(claim));
        Button proposeBtn = new Button("Propose Claim");
        proposeBtn.setOnAction(actionEvent -> handleProposeButton(claim));
        HBox btnContainer = new HBox(20, requestInfoBtn, proposeBtn);

        additionalContentContainer.getChildren().addAll(claimDetails, btnContainer);
    }

    private void showImageView(File file) throws IOException {
        Stage imageStage = new Stage();
        ImageView imageView = new ImageView(ImageRepository.renderPdfImage(file));
        imageView.setFitWidth(600);
        imageView.setFitHeight(800);
        VBox vbox = new VBox(imageView);
        Scene scene = new Scene(vbox);
        imageStage.setScene(scene);
        imageStage.show();
    }

    private void handleProposeButton(Claim claim) {
        if (service.getSurveyor().getProposedClaim().contains(claim)){
            informationAlert("Error", "Proposed Claim", "This claim is already in your proposed claim.");
        } else {
            claim.setStatusProcessing();
            updateClaim(claim);
            service.getSurveyor().proposeClaim(claim);
            service.update();
        }
    }

    private void handleRequestInfoButton(Claim claim) {
        String message = openPopup();
        RequestHandler.addRequest(claim.getId(), message);

        additionalContentContainer.getChildren().clear();
    }

    private String openPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Popup");

        TextField textField = new TextField();
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            if (!textField.getText().isEmpty()){
                popupStage.close();
            }
        });

        VBox popupLayout = new VBox(10);
        popupLayout.setPadding(new Insets(10));
        popupLayout.getChildren().addAll(textField, submitButton);

        Scene popupScene = new Scene(popupLayout, 250, 150);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();

        return textField.getText();
    }

    private void updateClaim(Claim claim){
        ClaimService claimService = new ClaimService(new ClaimRepository(connection));
        claimService.update(claim);
    }

    private void informationAlert(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    };

    @FXML
    public void handleManageCustomersButton() {
        clearAdditionalContent();

        Label titleLabel = new Label("Manage Customers");
        additionalContentContainer.getChildren().add(titleLabel);

        searchBox.setVisible(false);
        infoScrollContainer.setVisible(false);
        searchBox.setManaged(false);
        infoScrollContainer.setManaged(false);

        HBox buttonsContainer = new HBox(10);
        Button viewBtn = new Button("View Customers");
        viewBtn.setOnAction(actionEvent -> handleReviewCustomersButton());

        buttonsContainer.getChildren().add(viewBtn);
        additionalContentContainer.getChildren().add(buttonsContainer);
    }

    private void handleReviewCustomersButton() {
        clearAdditionalContent();

        searchBox.setVisible(true);
        infoScrollContainer.setVisible(true);
        searchBox.setManaged(true);
        infoScrollContainer.setManaged(true);

        initializeCustomersData();
    }

    private void initializeCustomersData() {
        PolicyholderService policyholderService = new PolicyholderService(new PolicyholderRepository(connection));
        DependantService dependantService = new DependantService(new DependantRepository(connection));
        customersData.addAll(dependantService.getAllDependants());
        customersData.addAll(policyholderService.getAllPolicyholder());

        displayCustomersAsHyperlinks(customersData);
    }

    private void displayCustomersAsHyperlinks(List<Customer> customers) {
        infoContainer.getChildren().clear();

        VBox customerList = new VBox(5);
        for (Customer customer : customers) {
            Hyperlink claimLink = new Hyperlink(customer.getFullName());
            claimLink.setOnAction(event -> showCustomerDetails(customer));

            customerList.getChildren().add(claimLink);
        }

        infoContainer.getChildren().add(customerList);
    }

    private void showCustomerDetails(Customer customer) {
        clearAdditionalContent();
        infoContainer.getChildren().clear();

        Label titleLabel = new Label("Customer Details: " + customer.getFullName());
        additionalContentContainer.getChildren().add(titleLabel);

        Label id = new Label("ID: " + customer.getId());
        Label fullName = new Label("Full name: " + customer.getFullName());
        Label insuranceCardNumber = new Label("Insurance Card number: " + customer.getInsuranceCard().getCardNumber());

        VBox customerDetails = new VBox(5);
        customerDetails.getChildren().addAll(id, fullName, insuranceCardNumber);

        additionalContentContainer.getChildren().addAll(customerDetails);
    }

    private void clearAdditionalContent() {
        additionalContentContainer.getChildren().clear();
    }
}

