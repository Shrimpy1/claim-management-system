package rmit.furtherprog.claimmanagementsystem.util;

import rmit.furtherprog.claimmanagementsystem.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HistoryManager {
    private Connection connection;

    public HistoryManager() throws SQLException {
        this.connection = DatabaseManager.getConnection();
    }

    public void write(String user_id, String user_type, String event) {
        String insertSQL = "INSERT INTO history (user_id, user_type, event) VALUES (?, ?, ?)  RETURNING id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, user_type);
            preparedStatement.setString(3, event);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("History added successfully.");
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to record history.");
        }
    }
}
