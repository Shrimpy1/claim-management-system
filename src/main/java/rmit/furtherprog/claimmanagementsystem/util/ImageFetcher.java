package rmit.furtherprog.claimmanagementsystem.util;

import javafx.scene.image.Image;

import java.io.*;
import java.net.*;

import org.apache.pdfbox.rendering.PDFRenderer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.core.ResponseBytes;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

public class ImageFetcher{
    private static final String BUCKET_NAME = "documents";
    private static final String ENDPOINT_URL = "https://hccazvuwuxosfaqfckpd.supabase.co/storage/v1/s3";
    private static final String REGION = "ap-southeast-1";
    private static final String ACCESS_KEY = "9e1c51d78b6871f73d06a7e76d100eb7";

    public static Image getImage(String fileName) throws IOException {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, "a765011f644081dd0b23c75b0a1fd952e11199be66dd1e3d97931190bb00ae2a");
        // Create S3 client with endpoint URL and region
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(ENDPOINT_URL))
                .region(Region.of(REGION))
                .credentialsProvider(() -> credentials)
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(objectBytes.asByteArray());

        PDDocument document = PDDocument.load(inputStream);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300); // Render at 300 DPI

//        int targetWidth = 800; // Set desired width
//        int targetHeight = 1000; // Set desired height
//        BufferedImage resizedBufferedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = resizedBufferedImage.createGraphics();
//        AffineTransform at = AffineTransform.getScaleInstance(
//                (double) targetWidth / bufferedImage.getWidth(),
//                (double) targetHeight / bufferedImage.getHeight()
//        );
//        g2d.drawRenderedImage(bufferedImage, at);
//        g2d.dispose();


        Image pdfImage = SwingFXUtils.toFXImage(bufferedImage, null);

        document.close();
        s3Client.close();

        return pdfImage;
    }
}
