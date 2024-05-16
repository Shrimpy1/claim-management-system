/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.HistoryManager;

import java.sql.*;

public class BankingInfoRepository {
    private Connection connection;

    public BankingInfoRepository(Connection connection) {
        this.connection = connection;
    }

    public BankingInfo getById(int id){
        String sql = "SELECT * FROM banking_info WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String bank = resultSet.getString("bank");
                String name = resultSet.getString("name");
                String number = resultSet.getString("number");
                return new BankingInfo(id, bank, name, number);
            } else {
                throw new NoDataFoundException("No document found with id: " + id);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteById(int id){
        String sql = "DELETE FROM banking_info WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDelete = statement.executeUpdate();
            if (rowsDelete > 0){
                System.out.println("Deleted banking info with ID: " + id);
                HistoryManager.write("banking info", "Deleted with ID: " + id);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete banking info.");
        }
    }

    public int addToDatabase(BankingInfo bankingInfo) {
        String insertSQL = "INSERT INTO banking_info (bank, name, number) VALUES (?, ?, ?)  RETURNING id";
        int newId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, bankingInfo.getBank());
            preparedStatement.setString(2, bankingInfo.getName());
            preparedStatement.setString(3, bankingInfo.getNumber());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("id");
                    System.out.println("BankingInfo added successfully with ID: " + newId);
                    HistoryManager.write("banking info", "Added successfully with ID: " + newId);
                } else {
                    throw new SQLException("Failed to retrieve the ID of the inserted banking info.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the banking info.");
        }

        return newId;
    }

    public void updateDatabase(BankingInfo bankingInfo) {
        String updateSQL = "UPDATE banking_info SET bank = ?, name = ?, number = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, bankingInfo.getBank());
            preparedStatement.setString(2, bankingInfo.getName());
            preparedStatement.setString(3, bankingInfo.getNumber());
            preparedStatement.setInt(4, bankingInfo.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Banking info updated successfully.");
                HistoryManager.write("banking info", "Updated with ID: " + bankingInfo.getId());
            } else {
                System.out.println("No banking info found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the banking info.");
        }
    }
}
