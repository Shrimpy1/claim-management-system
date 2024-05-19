/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rmit.furtherprog.claimmanagementsystem.controller.*;
import rmit.furtherprog.claimmanagementsystem.service.*;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        showLoginPage();
    }

    public static void showLoginPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/login-page.fxml"));
        Parent loginPage = loader.load();

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(new Scene(loginPage, 350, 200));
        primaryStage.show();
    }


    public static void showAdminPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/admin-page.fxml"));
        Parent adminPage = loader.load();

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
    public static void showSurveyorPage(SurveyorService service) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/surveyor-page.fxml"));
        Parent policyOwnerPage = loader.load();

        SurveyorPageController controller = loader.getController();
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

    public static void showManagerPage(ManagerService service) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/manager-page.fxml"));
        Parent managerPage = loader.load();

        ManagerPageController controller = loader.getController();
        controller.setService(service);

        primaryStage.setTitle("Manager Page");
        primaryStage.setScene(new Scene(managerPage, 1000, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
