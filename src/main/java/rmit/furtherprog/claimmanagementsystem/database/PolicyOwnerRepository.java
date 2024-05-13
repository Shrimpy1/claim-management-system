package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Customer;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PolicyOwnerRepository {
    private Connection connection;

    public PolicyOwnerRepository(Connection connection) {
        this.connection = connection;
    }

    public PolicyOwner getById(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM policy_owner WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Map ResultSet to Customer object
                return mapResultSetToPolicyOwner(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null; // Customer not found
    }

    public List<PolicyOwner> getAll() {
        List<PolicyOwner> dependants = new ArrayList<>();
        String sql = "SELECT * FROM policy_owner";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Map each row in ResultSet to a Customer object
                PolicyOwner dependant = mapResultSetToPolicyOwner(resultSet);
                dependants.add(dependant);
            }
        } catch (SQLException e) {
            // Handle SQL exception (e.g., log error, throw custom exception)
            e.printStackTrace();
        }
        return dependants;
    }

    private PolicyOwner mapResultSetToPolicyOwner(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");
        PolicyOwner policyOwner = new PolicyOwner(customerId, fullName);

        fetchBeneficiaries(policyOwner);

        return policyOwner;
    }

    public PolicyOwner getByIdSummarized(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM policy_owner WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Map ResultSet to Customer object
                return mapResultSetToPolicyOwnerSummarized(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null; // Customer not found
    }

    public List<PolicyOwner> getAllSummarized() {
        List<PolicyOwner> dependants = new ArrayList<>();
        String sql = "SELECT * FROM policy_owner";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Map each row in ResultSet to a Customer object
                PolicyOwner dependant = mapResultSetToPolicyOwnerSummarized(resultSet);
                dependants.add(dependant);
            }
        } catch (SQLException e) {
            // Handle SQL exception (e.g., log error, throw custom exception)
            e.printStackTrace();
        }
        return dependants;
    }

    private PolicyOwner mapResultSetToPolicyOwnerSummarized(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");
        PolicyOwner policyOwner = new PolicyOwner(customerId, fullName);

        return policyOwner;
    }

    private void fetchBeneficiaries(PolicyOwner policyOwner){
        String sql = "SELECT * FROM insurance_card WHERE policy_owner_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, IdConverter.fromCustomerId(policyOwner.getId()));
            ResultSet results = statement.executeQuery();
            DependantRepository dRepo = new DependantRepository(connection);
            PolicyholderRepository phRepo = new PolicyholderRepository(connection);
            while (results.next()){
                Policyholder policyholder = phRepo.getById(IdConverter.toCustomerId(results.getInt("card_holder_id")));
                policyOwner.addBeneficiaries(policyholder);

                for (Dependant dependant : policyholder.getDependants()){
                    policyOwner.addBeneficiaries(dependant);
                }
            }
        } catch (SQLException e) {
            // Handle SQL exception (e.g., log error, throw custom exception)
            e.printStackTrace();
        }
    }
}
