package com.example;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.util.TreeMap;
import java.util.Map;

public class SurveyorPageController {
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
    private VBox claimsContainer;
    @FXML
    private HBox searchBox;
    private Map<String, String> claimsData;

    public void initialize() {
        String loggedInUserName = getLoggedInUserName(); // Name method
        if (loggedInUserName != null && !loggedInUserName.isEmpty()) {
            welcomeLabel.setText("Welcome Back, " + loggedInUserName);
        } else {
            welcomeLabel.setText("Welcome!");
        }

        searchField.setOnAction(this::handleSearchAction);  // Add search action handler
    }

    // Name fetch logic
    private String getLoggedInUserName() {
        // Placeholder
        return "Surveyor";
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
        claimsContainer.setVisible(false);
        searchBox.setManaged(false);
        claimsContainer.setManaged(false);

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

        // Mock data for user
        String name = "User Name";
        String phone = "123-456-7890";
        String address = "123 Main St, City";
        String email = "user@example.com";
        String password = "password";

        Label nameLabel = new Label("Name: " + name);
        Label phoneLabel = new Label("Phone: " + phone);
        Label addressLabel = new Label("Address: " + address);
        Label emailLabel = new Label("Email: " + email);
        Label passwordLabel = new Label("Password: " + password);

        VBox userInfo = new VBox(5);
        userInfo.getChildren().addAll(nameLabel, phoneLabel, addressLabel, emailLabel, passwordLabel);

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void enableSelfInfoEditing() {
        clearAdditionalContent();

        String name = "User Name";
        String phone = "123-456-7890";
        String address = "123 Main St, City";
        String email = "user@example.com";
        String password = "password";

        // Info to text field
        TextField nameField = new TextField(name);
        TextField phoneField = new TextField(phone);
        TextField addressField = new TextField(address);
        TextField emailField = new TextField(email);
        TextField passwordField = new TextField(password);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveSelfInfo(nameField.getText(), phoneField.getText(), addressField.getText(), emailField.getText(), passwordField.getText()));

        VBox userInfo = new VBox(5);
        userInfo.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Phone:"), phoneField,
                new Label("Address:"), addressField,
                new Label("Email:"), emailField,
                new Label("Password:"), passwordField,
                saveButton
        );

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void saveSelfInfo(String name, String phone, String address, String email, String password) {
        clearAdditionalContent();

        Label nameLabel = new Label("Name: " + name);
        Label phoneLabel = new Label("Phone: " + phone);
        Label addressLabel = new Label("Address: " + address);
        Label emailLabel = new Label("Email: " + email);
        Label passwordLabel = new Label("Password: " + password);

        VBox userInfo = new VBox(5);
        userInfo.getChildren().addAll(nameLabel, phoneLabel, addressLabel, emailLabel, passwordLabel);

        additionalContentContainer.getChildren().add(userInfo);
    }

    @FXML
    public void handleManageClaimsButton() {
        clearAdditionalContent();
        Label titleLabel = new Label("Manage Claims");
        additionalContentContainer.getChildren().add(titleLabel);
        searchBox.setVisible(false);
        claimsContainer.setVisible(false);
        searchBox.setManaged(false);
        claimsContainer.setManaged(false);

        HBox buttonsContainer = new HBox(10);
        Button reviewClaimsButton = new Button("Review Claims");
        Button proposedClaimsButton = new Button("Proposed Claims");
        buttonsContainer.getChildren().add(reviewClaimsButton);
        buttonsContainer.getChildren().add(proposedClaimsButton);
        additionalContentContainer.getChildren().add(buttonsContainer);

        reviewClaimsButton.setOnAction(event -> handleReviewClaimsButton());
        proposedClaimsButton.setOnAction(event -> handleProposedClaimsButton());
    }

    @FXML
    private void handleReviewClaimsButton() {
        clearAdditionalContent();

        searchBox.setVisible(true);
        claimsContainer.setVisible(true);
        searchBox.setManaged(true);
        claimsContainer.setManaged(true);

        initializeClaimsData();
    }

    private void initializeClaimsData() {
        claimsData = new TreeMap<>();
        // Mock data
        claimsData.put("Claim 1", "Claim ID: 125");
        claimsData.put("Claim 2", "Claim ID: 278");
        claimsData.put("Claim 3", "Claim ID: 316");
        claimsData.put("Claim 4", "Claim ID: 467");
        claimsData.put("Claim 5", "Claim ID: 590");

        displayClaims(claimsData);
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            displayClaims(claimsData);
        } else {
            Map<String, String> searchResults = searchClaims(searchText);
            displayClaims(searchResults);
        }
    }

    private Map<String, String> searchClaims(String searchText) {
        Map<String, String> searchResults = new TreeMap<>();
        String lowerCaseSearchText = searchText.toLowerCase();
        for (Map.Entry<String, String> entry : claimsData.entrySet()) {
            if (entry.getValue().toLowerCase().contains(lowerCaseSearchText)) {
                searchResults.put(entry.getKey(), entry.getValue());
            }
        }
        return searchResults;
    }

    private void displayClaims(Map<String, String> claims) {
        claimsContainer.getChildren().clear();

        for (Map.Entry<String, String> entry : claims.entrySet()) {
            String claimID = entry.getKey();
            String claimInfo = entry.getValue();

            Label claimLabel = new Label(claimInfo);
            Button proposeButton = new Button("Propose");
            Button requestInfoButton = new Button("Request More Information");

            proposeButton.setOnAction(event -> handleProposeButton(claimID));
            requestInfoButton.setOnAction(event -> handleRequestInfoButton(claimID));

            HBox claimBox = new HBox(10);
            claimBox.getChildren().addAll(claimLabel, proposeButton, requestInfoButton);
            claimsContainer.getChildren().add(claimBox);
        }
    }

    private void handleProposedClaimsButton() {
        clearAdditionalContent();

        String[] claims = {"Claim 1", "Claim 2", "Claim 3"};
        VBox claimsList = new VBox(5);
        for (String claim : claims) {
            Hyperlink claimLink = new Hyperlink(claim);
            claimLink.setOnAction(event -> showClaimDetails(claim));
            claimsList.getChildren().add(claimLink);
        }
        additionalContentContainer.getChildren().add(claimsList);
    }

    private void showClaimDetails(String claim) {
        clearAdditionalContent();

        Label titleLabel = new Label("Claim Details: " + claim);
        additionalContentContainer.getChildren().add(titleLabel);

        String claimDate = "2024-05-20";
        String insuredPerson = "John Doe";
        String cardNumber = "1234 5678 9012 3456";
        String examDate = "2024-05-25";
        String documents = "sample-document.pdf";
        String claimAmount = "$1000";
        String receiverBankingInfo = "Bank Name: ABC Bank, Account Number: 1234567890";

        Hyperlink documentLink = new Hyperlink(documents);
        documentLink.setOnAction(event -> showImageView(documents));

        Label claimDateLabel = new Label("Claim Date: " + claimDate);
        Label insuredPersonLabel = new Label("Insured Person: " + insuredPerson);
        Label cardNumberLabel = new Label("Card Number: " + cardNumber);
        Label examDateLabel = new Label("Exam Date: " + examDate);
        Label documentLabel = new Label("Document: ");
        Label claimAmountLabel = new Label("Claim Amount: " + claimAmount);
        Label receiverBankingInfoLabel = new Label("Receiver Banking Info: " + receiverBankingInfo);

        Button proposeButton = new Button("Propose");
        Button requestInfoButton = new Button("Request More Information");

        proposeButton.setOnAction(event -> handleProposeButton(claim));
        requestInfoButton.setOnAction(event -> handleRequestInfoButton(claim));

        HBox buttonsContainer = new HBox(10);
        buttonsContainer.getChildren().addAll(proposeButton, requestInfoButton);

        VBox claimDetails = new VBox(5);
        claimDetails.getChildren().addAll(
                claimDateLabel,
                insuredPersonLabel,
                cardNumberLabel,
                examDateLabel,
                documentLabel,
                documentLink,
                claimAmountLabel,
                receiverBankingInfoLabel,
                buttonsContainer
        );

        additionalContentContainer.getChildren().add(claimDetails);
    }

    private void showImageView(String document) {
        Stage imageStage = new Stage();
        ImageView imageView = new ImageView(new Image("file:" + document));
        imageView.setFitWidth(600);
        imageView.setFitHeight(800);
        VBox vbox = new VBox(imageView);
        Scene scene = new Scene(vbox);
        imageStage.setScene(scene);
        imageStage.show();
    }

    private void handleProposeButton(String claimID) {
        // Propose function here :D
    }

    private void handleRequestInfoButton(String claimID) {
        // Request more information function here :D
    }

    @FXML
    public void handleManageCustomersButton() {
        clearAdditionalContent();

        Label titleLabel = new Label("Manage Customers");
        additionalContentContainer.getChildren().add(titleLabel);
        searchBox.setVisible(false);
        claimsContainer.setVisible(false);
        searchBox.setManaged(false);
        claimsContainer.setManaged(false);

        String[] customers = {"Customer 1", "Customer 2", "Customer 3"};
        VBox customersList = new VBox(5);
        for (String customer : customers) {
            Hyperlink customerLink = new Hyperlink(customer);
            customerLink.setOnAction(event -> showCustomerDetails(customer));
            customersList.getChildren().add(customerLink);
        }
        additionalContentContainer.getChildren().add(customersList);
    }

    private void showCustomerDetails(String customer) {
        clearAdditionalContent();

        Label titleLabel = new Label("Customer Details: " + customer);
        additionalContentContainer.getChildren().add(titleLabel);

        String name = "John Doe";
        String address = "123 Main St, City";
        String phone = "123-456-7890";
        String email = "customer@example.com";
        String dob = "01-01-1980";

        Label nameLabel = new Label("Name: " + name);
        Label addressLabel = new Label("Address: " + address);
        Label phoneLabel = new Label("Phone: " + phone);
        Label emailLabel = new Label("Email: " + email);
        Label dobLabel = new Label("Date of Birth: " + dob);

        VBox customerDetails = new VBox(5);
        customerDetails.getChildren().addAll(nameLabel, addressLabel, phoneLabel, emailLabel, dobLabel);

        additionalContentContainer.getChildren().addAll(customerDetails);
    }

    private void clearAdditionalContent() {
        additionalContentContainer.getChildren().clear();
    }
}

