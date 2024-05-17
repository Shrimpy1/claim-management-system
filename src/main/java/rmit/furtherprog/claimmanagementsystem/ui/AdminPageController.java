/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rmit.furtherprog.claimmanagementsystem.data.model.util.History;
import rmit.furtherprog.claimmanagementsystem.util.HistoryManager;

import java.io.IOException;
import java.util.List;

public class AdminPageController {
    @FXML
    private void handleShowHistory() {
        List<History> historyList = HistoryManager.getAllHistory();

        try {
            // Load the history page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/history-page.fxml"));
            Parent root = loader.load();

            // Get the controller and set the history data
            HistoryPageController controller = loader.getController();
            controller.setHistoryData(historyList);

            // Create a new stage for the pop-up window
            Stage stage = new Stage();
            stage.setTitle("History");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCrudOperations() {
        showAlert(AlertType.INFORMATION, "CRUD Operations", "This will perform CRUD operations.");
    }

    @FXML
    private void handleCalculateClaimedSum() {
        showAlert(AlertType.INFORMATION, "Calculate Claimed Sum", "This will calculate the claimed sum.");
    }

    @FXML
    private void handleLogout() {
        try {
            Main.showLoginPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}