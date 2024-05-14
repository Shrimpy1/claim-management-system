package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                return new BankingInfo(bank, name, number);
            } else {
                throw new NoDataFoundException("No document found with id: " + id);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
