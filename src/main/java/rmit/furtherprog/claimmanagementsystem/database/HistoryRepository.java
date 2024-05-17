package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.util.History;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {
    private Connection connection;

    public HistoryRepository(Connection connection) {
        this.connection = connection;
    }

    public List<History> getAll() {
        String stringSQL = "SELECT *, created_at::text FROM history";
        List<History> historyList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(stringSQL)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                History history = mapResultSetToHistoryObject(resultSet);
                historyList.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    private History mapResultSetToHistoryObject(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String timeStampt = resultSet.getString("created_at");
        String type = resultSet.getString("type");
        String event = resultSet.getString("event");

        return new History(id, timeStampt, type, event);
    }

    public void addToDatabase(String type, String event){
        String insertSQL = "INSERT INTO history (type, event) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, event);

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
