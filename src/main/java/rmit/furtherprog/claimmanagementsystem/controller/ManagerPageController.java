package rmit.furtherprog.claimmanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rmit.furtherprog.claimmanagementsystem.Main;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.provider.Surveyor;
import rmit.furtherprog.claimmanagementsystem.database.DatabaseManager;
import rmit.furtherprog.claimmanagementsystem.database.ImageRepository;
import rmit.furtherprog.claimmanagementsystem.database.SurveyorRepository;
import rmit.furtherprog.claimmanagementsystem.service.ManagerService;
import rmit.furtherprog.claimmanagementsystem.service.SurveyorService;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ManagerPageController {
    private ManagerService service;
    private Connection connection;

    public void setService(rmit.furtherprog.claimmanagementsystem.service.ManagerService service) {
        this.service = service;
        welcomeLabel.setText("Welcome " + service.getManager().getName());
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
    private Button viewSurveyorsButton;
    @FXML
    private TextField searchField;
    @FXML
    private VBox claimsContainer;
    @FXML
    private HBox searchBox;
    private Map<String, String> claimsData;

    public void initialize() throws SQLException {
      setConnection(DatabaseManager.getConnection());
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

        Label id = new Label("ID: " + service.getManager().getId());
        Label fullName = new Label("Full name: " + service.getManager().getName());


        VBox userInfo = new VBox(5);
        userInfo.getChildren().addAll(id, fullName);

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void enableSelfInfoEditing() {
        clearAdditionalContent();

        TextField nameField = new TextField(service.getManager().getName());

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

        service.getManager().setName(name);
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
        claimsContainer.setVisible(false);
        searchBox.setManaged(false);
        claimsContainer.setManaged(false);

        Button proposedClaimsButton = new Button("Proposed Claims");
        additionalContentContainer.getChildren().add(proposedClaimsButton);

        proposedClaimsButton.setOnAction(event -> handleProposedClaimsButton());
    }

    @FXML
    private void handleProposedClaimsButton() {
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

        displayClaimsAsHyperlinks(claimsData);
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            displayClaimsAsHyperlinks(claimsData);
        } else {
            Map<String, String> searchResults = searchClaims(searchText);
            displayClaimsAsHyperlinks(searchResults);
        }
    }

    private Map<String, String> searchClaims(String searchText) {
        Map<String, String> searchResults = new TreeMap<>();
        for (Map.Entry<String, String> entry : claimsData.entrySet()) {
            if (entry.getValue().contains(searchText)) {
                searchResults.put(entry.getKey(), entry.getValue());
            }
        }
        return searchResults;
    }

    private void displayClaimsAsHyperlinks(Map<String, String> claims) {
        claimsContainer.getChildren().clear();

        VBox claimsList = new VBox(5);
        for (Map.Entry<String, String> entry : claims.entrySet()) {
            String claimID = entry.getKey();
            String claimInfo = entry.getValue();

            Hyperlink claimLink = new Hyperlink(claimInfo);
           // claimLink.setOnAction(event -> showClaimDetails(claimID));

            claimsList.getChildren().add(claimLink);
        }

        claimsContainer.getChildren().add(claimsList);
    }

    private void showClaimDetails(Claim claim) {
        clearAdditionalContent();
        claimsContainer.getChildren().clear();

        Label titleLabel = new Label("Claim Details: " + claim);
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
                new Label("Banking account: " + claim.getReceiverBankingInfo().getName()),
                new Label("Banking number: " + claim.getReceiverBankingInfo().getNumber())
        );
        additionalContentContainer.getChildren().add(claimDetails);
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
    private void handleApproveButton(String claimID) {
        // Approve function here
    }

    private void handleDenyButton(String claimID) {
        // Deny function here
    }



    public void handleManageSurveyorsButton() {
        clearAdditionalContent();

        Label titleLabel = new Label("Manage Surveyors");
        additionalContentContainer.getChildren().add(titleLabel);
        searchBox.setVisible(false);
        claimsContainer.setVisible(false);
        searchBox.setManaged(false);
        claimsContainer.setManaged(false);

        List<Surveyor> surveyors = new ArrayList<>();
        for (Surveyor surveyor : service.getManager().getSurveyors()){
            surveyors.add(surveyor);
        }

        SurveyorService surveyorService = new SurveyorService(new SurveyorRepository(connection));

        VBox surveyorsList = new VBox(5);
        for (Surveyor surveyor : surveyors) {
            Hyperlink surveyorLink = new Hyperlink(String.valueOf(surveyor.getName()));
            surveyorLink.setOnAction(event -> showSurveyorOptions(surveyorService.getSurveyorById(surveyor.getId())));
            surveyorsList.getChildren().add(surveyorLink);
        }
        additionalContentContainer.getChildren().add(surveyorsList);
    }

    private void showSurveyorOptions(Surveyor surveyor) {
        clearAdditionalContent();

        Label titleLabel = new Label("Surveyor Details: " + surveyor.getName());
        additionalContentContainer.getChildren().add(titleLabel);

        Label id = new Label("ID: " + surveyor.getId());
        Label fullName = new Label("Full name: " + surveyor.getName());

        VBox surveyorDetails = new VBox(5);
        surveyorDetails.getChildren().addAll(id, fullName);


        additionalContentContainer.getChildren().addAll(surveyorDetails);
    }


    private void clearAdditionalContent() {
        additionalContentContainer.getChildren().clear();
    }

}