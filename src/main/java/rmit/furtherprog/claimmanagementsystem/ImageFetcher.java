package rmit.furtherprog.claimmanagementsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.SdkBytes;

public class ImageFetcher  extends Application {
    private static final String BUCKET_NAME = "documents";
    private static final String endpointUrl = "https://hccazvuwuxosfaqfckpd.supabase.co/storage/v1/s3";
    private static final String region = "ap-southeast-1";
    private static final String accessKey = "9e1c51d78b6871f73d06a7e76d100eb7";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Image image = getImage("Ej0NvzFUwAAFm4-.jpg");
        ImageView imageView = new ImageView(image);
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Supabase Image Viewer");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

    public static Image getImage(String fileName){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, "a765011f644081dd0b23c75b0a1fd952e11199be66dd1e3d97931190bb00ae2a");
        // Create S3 client with endpoint URL and region
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpointUrl))
                .region(Region.of(region))
                .credentialsProvider(() -> credentials)
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
        SdkBytes imageData = SdkBytes.fromByteArray(objectBytes.asByteArray());

        // Convert image data to JavaFX Image
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData.asByteArray());
        return new Image(inputStream);
    }
}
