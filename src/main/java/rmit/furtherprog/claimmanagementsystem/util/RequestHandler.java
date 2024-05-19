package rmit.furtherprog.claimmanagementsystem.util;

import rmit.furtherprog.claimmanagementsystem.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestHandler {
    private static Connection connection;

    static {
        try {
            connection = DatabaseManager.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getByClaimId(String claimId){
        int databaseId = IdConverter.fromClaimId(claimId);
        String sql = "SELECT * FROM request WHERE claim_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("message");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "None";
    }

    public static void addRequest(String claimId, String message){
        int databaseId = IdConverter.fromClaimId(claimId);
        String sql = "INSERT INTO request(claim_id, message) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            statement.setString(2, message);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0){
                System.out.println("Request added successfully.");
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add new request.");
        }
    }

    public static void updateRequest(String claimId, String message){
        int databaseId = IdConverter.fromClaimId(claimId);
        String sql = "UPDATE request SET message = ? WHERE claim_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, message);
            statement.setInt(2, databaseId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0){
                System.out.println("Request updated successfully.");
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to updated request.");
        }
    }
}
