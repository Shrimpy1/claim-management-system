package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public PolicyOwner getByIdPartial(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM policy_owner WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToPolicyOwnerPartial(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + customerId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<PolicyOwner> getAllPartial() {
        List<PolicyOwner> dependants = new ArrayList<>();
        String sql = "SELECT * FROM policy_owner";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PolicyOwner dependant = mapResultSetToPolicyOwnerPartial(resultSet);
                dependants.add(dependant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dependants;
    }

    private PolicyOwner mapResultSetToPolicyOwnerPartial(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");
        PolicyOwner policyOwner = new PolicyOwner(customerId, fullName);

        return policyOwner;
    }

    private void fetchBeneficiaries(PolicyOwner policyOwner){
        String sql = "SELECT * FROM insurance_card WHERE policy_owner_id = ? AND card_holder_type = 'policyholder'";
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
            e.printStackTrace();
        }
    }

    public void updateDatabase(PolicyOwner policyOwner) {
        String updateSQL = "UPDATE policy_owner SET full_name = ?, beneficiaries = ? WHERE customer_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, policyOwner.getFullName());
            Array claimIdArray = getBeneficiariesIdArray(policyOwner);
            preparedStatement.setArray(2, claimIdArray);
            preparedStatement.setInt(3, IdConverter.fromCustomerId(policyOwner.getId()));

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Policy owner updated successfully.");
            } else {
                System.out.println("No policy owner found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the policy owner.");
        }
    }

    public int addToDatabase(PolicyOwner policyOwner){
        String insertSQL = "INSERT INTO policy_owner (full_name, beneficiaries) VALUES (?, ?) RETURNING customer_id";
        int newId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)){
            preparedStatement.setString(1, policyOwner.getFullName());
            Array beneficiariesIdArray = getBeneficiariesIdArray(policyOwner);
            preparedStatement.setArray(4, beneficiariesIdArray);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("id");
                    System.out.println("Policy owner added successfully with ID: " + newId);
                } else {
                    throw new SQLException("Failed to retrieve the ID of the inserted policy owner.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the policy owner.");
        }

        return newId;
    }

    private Array getBeneficiariesIdArray(PolicyOwner policyOwner) throws SQLException {
        List<Integer> beneficiariesIds = policyOwner.getBeneficiaries().stream().map(customer -> IdConverter.fromCustomerId(customer.getId())).toList();
        return connection.createArrayOf("integer", beneficiariesIds.toArray(new Integer[0]));
    }
}
