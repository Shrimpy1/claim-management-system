/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.provider.Surveyor;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyorRepository {
    private Connection connection;

    public SurveyorRepository(Connection connection) {
        this.connection = connection;
    }

    public Surveyor getById(int id){
        String sql = "SELECT * FROM surveyor WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                return new Surveyor(id, fullName);
            } else {
                throw new NoDataFoundException("No document found with id: " + id);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int addToDatabase(Surveyor surveyor) {
        String insertSQL = "INSERT INTO surveyor (full_name) VALUES (?)  RETURNING id";
        int newId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, surveyor.getName());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("id");
                    System.out.println("Surveyor added successfully with ID: " + newId);
                } else {
                    throw new SQLException("Failed to retrieve the ID of the inserted surveyor.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the surveyor.");
        }

        return newId;
    }

    public void updateDatabase(Surveyor surveyor) {
        String updateSQL = "UPDATE surveyor SET full_name = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, surveyor.getName());
            preparedStatement.setInt(2, surveyor.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Surveyor updated successfully.");
            } else {
                System.out.println("No surveyor found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the surveyor.");
        }
    }

    public static void main(String[] args) throws SQLException {
        SurveyorRepository repository = new SurveyorRepository(DatabaseManager.getConnection());
        Surveyor surveyor = repository.getById(2);
        System.out.println(surveyor.getName());
        surveyor.setName("Elijah Robert");
        repository.updateDatabase(surveyor);
    }
}
