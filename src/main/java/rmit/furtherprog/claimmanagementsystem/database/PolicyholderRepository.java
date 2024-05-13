package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PolicyholderRepository {
    private Connection connection;

    public PolicyholderRepository(Connection connection) {
        this.connection = connection;
    }

    public Policyholder getById(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM policyholder WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Map ResultSet to Customer object
                return mapResultSetToPolicyholder(resultSet);
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

    public List<Policyholder> getAll() {
        List<Policyholder> policyholders = new ArrayList<>();
        String sql = "SELECT * FROM policyholder";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Map each row in ResultSet to a Customer object
                Policyholder policyholder = mapResultSetToPolicyholder(resultSet);
                policyholders.add(policyholder);
            }
        } catch (SQLException e) {
            // Handle SQL exception (e.g., log error, throw custom exception)
            e.printStackTrace();
        }
        return policyholders;
    }

    private Policyholder mapResultSetToPolicyholder(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");
        String cardNumber = resultSet.getString("insurance_card_number");
        InsuranceCardRepository repo = new InsuranceCardRepository(connection);
        InsuranceCard card = repo.getByNumberSummarized(cardNumber);

        Policyholder policyholder = new Policyholder(customerId, fullName);
        policyholder.setInsuranceCard(card);

        Array dependants_id = resultSet.getArray("dependants");
        if (dependants_id != null){
            Integer[] dependants = (Integer[]) dependants_id.getArray();
            DependantRepository dRepo = new DependantRepository(connection);
            for (Integer cid : dependants){
                Dependant dependant = dRepo.getById(IdConverter.toCustomerId(cid));
                policyholder.addDependant(dependant);
            }
        }

        return policyholder;
    }

    public static void main(String[] args) throws SQLException, NoDataFoundException {
        Connection cn = DatabaseManager.getConnection();
        PolicyholderRepository repo = new PolicyholderRepository(cn);
        Policyholder ph1 = repo.getById("c0000001");
        System.out.println(ph1.getFullName());
        System.out.println(ph1.getInsuranceCard().getPolicyOwner().getFullName());
        System.out.println(ph1.getInsuranceCard().getCardNumber());
        System.out.println(ph1.getInsuranceCard().getCardHolder().getFullName());
        for (Dependant dependant : ph1.getDependants()){
            System.out.println(dependant.getFullName());
        }
    }
}
