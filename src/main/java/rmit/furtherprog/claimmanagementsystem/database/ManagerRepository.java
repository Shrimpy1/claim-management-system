/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.provider.Manager;
import rmit.furtherprog.claimmanagementsystem.data.model.provider.Surveyor;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;

import java.sql.*;
import java.util.List;

public class ManagerRepository {
    private Connection connection;

    public ManagerRepository(Connection connection) {
        this.connection = connection;
    }

    public Manager getById(int id){
        String sql = "SELECT * FROM manager WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                Manager manager = new Manager(id, fullName);
                Array surveyorsIdArr = resultSet.getArray("surveyor_id");
                if (surveyorsIdArr != null){
                    Integer[] surveyorsIds = (Integer[]) surveyorsIdArr.getArray();
                    SurveyorRepository surveyorRepository = new SurveyorRepository(connection);
                    for (Integer eid : surveyorsIds){
                        Surveyor surveyor = surveyorRepository.getById(eid);
                        manager.addSurveyor(surveyor);
                    }
                }
                return manager;
            } else {
                throw new NoDataFoundException("No document found with id: " + id);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int addToDatabase(Manager manager) {
        String insertSQL = "INSERT INTO manager (full_name, surveyor_id) VALUES (?, ?)  RETURNING id";
        int newId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, manager.getName());
            Array surveyorIdArr = getSurveyorIdArray(manager);
            preparedStatement.setArray(2, surveyorIdArr);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("id");
                    System.out.println("Manager added successfully with ID: " + newId);
                } else {
                    throw new SQLException("Failed to retrieve the ID of the inserted manager.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the manager.");
        }

        return newId;
    }

    public void updateDatabase(Manager manager) {
        String updateSQL = "UPDATE manager SET full_name = ?, surveyor_id = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, manager.getName());
            Array surveyorIdArr = getSurveyorIdArray(manager);
            preparedStatement.setArray(2, surveyorIdArr);
            preparedStatement.setInt(3, manager.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Manager updated successfully.");
            } else {
                System.out.println("No manager found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the manager.");
        }
    }

    private Array getSurveyorIdArray(Manager manager) throws SQLException {
        List<Integer> surveorIds = manager.getSurveyors().stream().map(Surveyor::getId).toList();
        return connection.createArrayOf("integer", surveorIds.toArray(new Integer[0]));
    }

    public static void main(String[] args) throws SQLException {
        ManagerRepository repository = new ManagerRepository(DatabaseManager.getConnection());
        Manager manager = repository.getById(1);
        System.out.println(manager.getName());
        for (Surveyor surveyor : manager.getSurveyors()){
            System.out.println(surveyor.getName());
        }
//        manager.setName("Will Smith");
//        repository.updateDatabase(manager);
    }
}
