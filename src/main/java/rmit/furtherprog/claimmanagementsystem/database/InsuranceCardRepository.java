package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.DateParsing;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InsuranceCardRepository {
    private Connection connection;

    public InsuranceCardRepository(Connection connection) {
        this.connection = connection;
    }

    public InsuranceCard getByNumber(String number) {
        String sql = "SELECT * FROM insurance_card WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToInsuranceCard(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + number);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private InsuranceCard mapResultSetToInsuranceCard(ResultSet resultSet) throws SQLException {
        String number = resultSet.getString("number");
        LocalDate expirationDate = DateParsing.stod(resultSet.getString("expiration_date"));
        String policyOwnerId = IdConverter.toCustomerId(resultSet.getInt("policy_owner_id"));
        PolicyOwnerRepository repo = new PolicyOwnerRepository(connection);
        PolicyOwner policyOwner = repo.getByIdPartial(policyOwnerId);

        InsuranceCard card = new InsuranceCard(number, policyOwner, expirationDate);
        return card;
    }

    public InsuranceCard getByNumberPartial(String number) {
        String sql = "SELECT * FROM insurance_card WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToInsuranceCardPartial(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    private InsuranceCard mapResultSetToInsuranceCardPartial(ResultSet resultSet) throws SQLException {
        String number = resultSet.getString("number");
        LocalDate expirationDate = DateParsing.stod(resultSet.getString("expiration_date"));
        String policyOwnerId = IdConverter.toCustomerId(resultSet.getInt("policy_owner_id"));
        PolicyOwnerRepository repo = new PolicyOwnerRepository(connection);
        PolicyOwner policyOwner = repo.getByIdPartial(policyOwnerId);

        InsuranceCard card = new InsuranceCard(number, policyOwner, expirationDate);
        return card;
    }

    public static void main(String[] args) throws SQLException {
        Connection cn = DatabaseManager.getConnection();
        InsuranceCardRepository repo = new InsuranceCardRepository(cn);
        InsuranceCard i1 = repo.getByNumber("1234567890");
        System.out.println(i1.getExpirationDate());
        System.out.println(i1.getPolicyOwner().getFullName());
    }
}
