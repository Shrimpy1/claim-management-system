package rmit.furtherprog.claimmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import rmit.furtherprog.claimmanagementsystem.database.ImageRepository;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("PDF Uploader");

        Button uploadButton = new Button("Upload PDF");
        ImageView imageView = new ImageView();

        uploadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                    // Upload the file to Supabase
                    ImageRepository.uploadFile(selectedFile);
                    // Display the first page of the PDF as an image
                    Image pdfImage = ImageRepository.renderPdfImage(selectedFile);
                    imageView.setImage(pdfImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        VBox root = new VBox(10, uploadButton, imageView);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}