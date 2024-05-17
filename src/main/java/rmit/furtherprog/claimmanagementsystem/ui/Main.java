/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rmit.furtherprog.claimmanagementsystem.service.DependantService;
import rmit.furtherprog.claimmanagementsystem.service.PolicyOwnerService;
import rmit.furtherprog.claimmanagementsystem.service.PolicyholderService;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        showLoginPage();
    }

    public static void showLoginPage() throws Exception {
        Parent loginPage = FXMLLoader.load(Main.class.getResource("/login-page.fxml"));
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(new Scene(loginPage, 300, 150));
        primaryStage.show();
    }


    public static void showAdminPage() throws Exception {
        Parent adminPage = FXMLLoader.load(Main.class.getResource("/admin-page.fxml"));
        primaryStage.setTitle("Admin Page");
        primaryStage.setScene(new Scene(adminPage, 1000, 800));
        primaryStage.show();
    }


    public static void showPolicyOwnerPage(PolicyOwnerService service) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/policy-owner-page.fxml"));
        Parent policyOwnerPage = loader.load();

        PolicyOwnerPageController controller = loader.getController();
        controller.setService(service);

        primaryStage.setTitle("Policy Owner Page");
        primaryStage.setScene(new Scene(policyOwnerPage, 1000, 800));
        primaryStage.show();
    }

    public static void showPolicyHolderPage(PolicyholderService service) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/policyholder-page.fxml"));
        Parent policyOwnerPage = loader.load();

        PolicyholderPageController controller = loader.getController();
        controller.setService(service);

        primaryStage.setTitle("Policyholder Page");
        primaryStage.setScene(new Scene(policyOwnerPage, 1000, 800));
        primaryStage.show();
    }

    public static void showDependantPage(DependantService service) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dependant-page.fxml"));
        Parent policyOwnerPage = loader.load();

        DependantPageController controller = loader.getController();
        controller.setService(service);

        primaryStage.setTitle("Dependant Page");
        primaryStage.setScene(new Scene(policyOwnerPage, 1000, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}