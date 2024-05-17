/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.database.ClaimRepository;
import rmit.furtherprog.claimmanagementsystem.database.DatabaseManager;
import rmit.furtherprog.claimmanagementsystem.database.ImageRepository;
import rmit.furtherprog.claimmanagementsystem.service.ClaimService;
import rmit.furtherprog.claimmanagementsystem.service.DependantService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DependantPageController {
    private DependantService service;
    public void setService(DependantService service){
        this.service = service;
        welcomeLabel.setText("Welcome " + service.getDependant().getFullName());
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
    private Button viewClaimsButton;

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

        Label id = new Label("ID: " + service.getDependant().getId());
        Label fullName = new Label("Full name: " + service.getDependant().getFullName());
        Label insuranceCardNumber = new Label("Insurance Card number: " + service.getDependant().getInsuranceCard().getCardNumber());

        VBox userInfo = new VBox(5);
        userInfo.getChildren().addAll(id, fullName, insuranceCardNumber);

        additionalContentContainer.getChildren().add(userInfo);
    }

    private void enableSelfInfoEditing() {
        clearAdditionalContent();

        TextField nameField = new TextField(service.getDependant().getFullName());

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

        service.getDependant().setFullName(name);
        service.update();

        Label nameLabel = new Label("Full name: " + name);

        VBox userInfo = new VBox(1);
        userInfo.getChildren().addAll(nameLabel);

        additionalContentContainer.getChildren().add(userInfo);
    }

    @FXML
    private void handleViewClaimsButton() throws SQLException {
        clearAdditionalContent();

        List<String> claimIds = new ArrayList<>();
        for (Claim claim : service.getDependant().getClaims()){
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
                new Label("Banking account: " + claim.getReceiverBankingInfo().getName()),
                new Label("Banking number: " + claim.getReceiverBankingInfo().getNumber())
        );
        additionalContentContainer.getChildren().add(claimDetails);
    }

    private void showImageView(String fileName) throws IOException {
        Stage imageStage = new Stage();
        ImageView imageView = new ImageView(ImageRepository.getImage(fileName));
        imageView.setFitWidth(600);
        imageView.setFitHeight(800);
        VBox vbox = new VBox(imageView);
        Scene scene = new Scene(vbox);
        imageStage.setScene(scene);
        imageStage.show();
    }

    private void clearAdditionalContent() {
        additionalContentContainer.getChildren().clear();
    }
}
