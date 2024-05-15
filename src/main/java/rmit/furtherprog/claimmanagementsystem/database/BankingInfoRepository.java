package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

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
                } else {
                    System.out.println("Failed to add BankingInfo.");
                    throw new SQLException("Failed to retrieve the ID of the inserted BankingInfo.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the claim.");
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
                System.out.println("Claim updated successfully.");
            } else {
                System.out.println("No claim found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the claim.");
        }
    }

    public static void main(String[] args) throws SQLException {
        BankingInfoRepository repository = new BankingInfoRepository(DatabaseManager.getConnection());
        BankingInfo info = repository.getById(1);
        info.setNumber("696969696969");
        repository.updateDatabase(info);
    }
}
