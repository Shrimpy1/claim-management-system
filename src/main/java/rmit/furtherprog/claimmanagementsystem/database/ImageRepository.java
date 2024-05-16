/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.database;

import javafx.scene.image.Image;

import java.io.*;
import java.net.*;

import org.apache.pdfbox.rendering.PDFRenderer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.core.ResponseBytes;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;

public class ImageRepository {
    private static final String BUCKET_NAME = "documents";
    private static final String ENDPOINT_URL = "https://hccazvuwuxosfaqfckpd.supabase.co/storage/v1/s3";
    private static final String REGION = "ap-southeast-1";
    private static final String ACCESS_KEY = "9e1c51d78b6871f73d06a7e76d100eb7";
    private static final String SECRET_ACCESS_KEY = "a765011f644081dd0b23c75b0a1fd952e11199be66dd1e3d97931190bb00ae2a";

    public static Image getImage(String fileName) throws IOException {
        S3Client client = getClient();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = client.getObjectAsBytes(getObjectRequest);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(objectBytes.asByteArray());

        PDDocument document = PDDocument.load(inputStream);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300);

        Image pdfImage = SwingFXUtils.toFXImage(bufferedImage, null);

        document.close();
        client.close();

        return pdfImage;
    }

    public static void uploadFile(File file){
        S3Client client = getClient();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(file.getName())
                .build();

        client.putObject(putObjectRequest, RequestBody.fromFile(file));

        client.close();
    }

    public static Image renderPdfImage(File file) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        byte[] byteArray = outputStream.toByteArray();

        Image pdfImage = SwingFXUtils.toFXImage(bufferedImage, null);

        document.close();

        return pdfImage;
    }

    private static S3Client getClient(){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_ACCESS_KEY);
        // Create S3 client with endpoint URL and region
        return S3Client.builder()
                .endpointOverride(URI.create(ENDPOINT_URL))
                .region(Region.of(REGION))
                .credentialsProvider(() -> credentials)
                .build();
    }
}
