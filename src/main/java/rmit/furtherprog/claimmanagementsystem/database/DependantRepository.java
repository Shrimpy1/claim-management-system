package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DependantRepository {
    private Connection connection;

    public DependantRepository(Connection connection) {
        this.connection = connection;
    }

    public Dependant getById(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM dependant WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Map ResultSet to Customer object
                return mapResultSetToDependant(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoDataFoundException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Dependant> getAll() {
        List<Dependant> dependants = new ArrayList<>();
        String sql = "SELECT * FROM dependant";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Map each row in ResultSet to a Customer object
                Dependant dependant = mapResultSetToDependant(resultSet);
                dependants.add(dependant);
            }
        } catch (SQLException e) {
            // Handle SQL exception (e.g., log error, throw custom exception)
            e.printStackTrace();
        }
        return dependants;
    }

    private Dependant mapResultSetToDependant(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");
        String cardNumber = resultSet.getString("insurance_card_number");
        InsuranceCardRepository repo = new InsuranceCardRepository(connection);
        InsuranceCard card = repo.getByNumberSummarized(cardNumber);

        Dependant dependant = new Dependant(customerId, fullName);
        dependant.setInsuranceCard(card);

        return dependant;
    }

    public static void main(String[] args) throws SQLException, NoDataFoundException {
        Connection cn = DatabaseManager.getConnection();
        DependantRepository repo = new DependantRepository(cn);
        Dependant d1 = repo.getById("c0000002");
        System.out.println(d1.getFullName());
        System.out.println(d1.getInsuranceCard().getPolicyOwner().getFullName());
        System.out.println(d1.getInsuranceCard().getCardNumber());
        System.out.println(d1.getInsuranceCard().getCardHolder().getFullName());
    }
}
