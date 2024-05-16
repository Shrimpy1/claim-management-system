/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.HistoryManager;
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
                return mapResultSetToPolicyholder(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + customerId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteById(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "DELETE FROM policyholder WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            int rowsDelete = statement.executeUpdate();
            if (rowsDelete > 0){
                System.out.println("Deleted policyholder with ID: " + customerId);
                HistoryManager.write("policyholder", "Deleted with ID: " + customerId);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete policyholder.");
        }
    }

    public List<Policyholder> getAll() {
        List<Policyholder> policyholders = new ArrayList<>();
        String sql = "SELECT * FROM policyholder";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Policyholder policyholder = mapResultSetToPolicyholder(resultSet);
                policyholders.add(policyholder);
            }
        } catch (SQLException e) {
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
        InsuranceCard card = repo.getByNumberPartial(cardNumber);

        Policyholder policyholder = new Policyholder(customerId, fullName);
        policyholder.setInsuranceCard(card);

        Array claimIdArr = resultSet.getArray("claim");
        if (claimIdArr != null){
            Integer[] claimIds = (Integer[]) claimIdArr.getArray();
            ClaimRepository cRepo = new ClaimRepository(connection);
            for (Integer cid : claimIds){
                Claim claim = cRepo.getByIdPartial(IdConverter.toClaimId(cid));
                claim.setInsuredPerson(policyholder);
                policyholder.addClaim(claim);
            }
        }

        Array dependantsIdArr = resultSet.getArray("dependants");
        if (dependantsIdArr != null){
            Integer[] dependantIds = (Integer[]) dependantsIdArr.getArray();
            DependantRepository dRepo = new DependantRepository(connection);
            for (Integer did : dependantIds){
                Dependant dependant = dRepo.getByIdPartial(IdConverter.toCustomerId(did));
                for (Claim claim : dependant.getClaims()){
                    claim.setInsuredPerson(policyholder);
                }
                policyholder.addDependant(dependant);
            }
        }

        return policyholder;
    }

    public Policyholder getByIdPartial(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM policyholder WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToPolicyholderPartial(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + customerId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Policyholder> getAllPartial() {
        List<Policyholder> policyholders = new ArrayList<>();
        String sql = "SELECT * FROM policyholder";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Policyholder policyholder = mapResultSetToPolicyholderPartial(resultSet);
                policyholders.add(policyholder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policyholders;
    }

    private Policyholder mapResultSetToPolicyholderPartial(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");
        String cardNumber = resultSet.getString("insurance_card_number");
        InsuranceCardRepository repo = new InsuranceCardRepository(connection);
        InsuranceCard card = repo.getByNumberPartial(cardNumber);

        Policyholder policyholder = new Policyholder(customerId, fullName);
        policyholder.setInsuranceCard(card);

        return policyholder;
    }

    public Policyholder getByIdWithoutCard(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM policyholder WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToPolicyholderWithoutCard(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + customerId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Policyholder mapResultSetToPolicyholderWithoutCard(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");

        Policyholder policyholder = new Policyholder(customerId, fullName);
        Array claimIdArr = resultSet.getArray("claim");
        if (claimIdArr != null){
            Integer[] claimIds = (Integer[]) claimIdArr.getArray();
            ClaimRepository cRepo = new ClaimRepository(connection);
            for (Integer cid : claimIds){
                Claim claim = cRepo.getByIdPartial(IdConverter.toClaimId(cid));
                claim.setInsuredPerson(policyholder);
                policyholder.addClaim(claim);
            }
        }

        return policyholder;
    }

    public void updateDatabase(Policyholder policyholder) {
        String updateSQL = "UPDATE policyholder SET full_name = ?, insurance_card_number = ?, dependants = ?, claim = ? WHERE customer_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, policyholder.getFullName());
            preparedStatement.setString(2, policyholder.getInsuranceCard().getCardNumber());
            Array dependantIdArray = getDependantIdArray(policyholder);
            preparedStatement.setArray(3, dependantIdArray);
            Array claimIdArray = getClaimIdArray(policyholder);
            preparedStatement.setArray(4, claimIdArray);
            preparedStatement.setInt(5, IdConverter.fromCustomerId(policyholder.getId()));

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Policyholder updated successfully.");
                HistoryManager.write("policyholder", "Updated with ID: " + policyholder.getId());
            } else {
                System.out.println("No policyholder found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the policyholder.");
        }
    }

    public int addToDatabase(Policyholder policyholder){
        String insertSQL = "INSERT INTO policyholder (full_name, insurance_card_number, dependants, claim) VALUES (?, ?, ?, ?) RETURNING customer_id";
        int newId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)){
            preparedStatement.setString(1, policyholder.getFullName());
            preparedStatement.setString(2, policyholder.getInsuranceCard().getCardNumber());
            Array dependantIdArray = getDependantIdArray(policyholder);
            preparedStatement.setArray(3, dependantIdArray);
            Array claimIdArray = getClaimIdArray(policyholder);
            preparedStatement.setArray(4, claimIdArray);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("id");
                    System.out.println("Policyholder added successfully with ID: " + newId);
                    HistoryManager.write("policyholder", "Added with ID: " + policyholder.getId());
                } else {
                    throw new SQLException("Failed to retrieve the ID of the inserted policyholder.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the policyholder.");
        }

        return newId;
    }

    private Array getClaimIdArray(Policyholder policyholder) throws SQLException {
        List<Integer> claimIds = policyholder.getClaims().stream().map(claim -> IdConverter.fromClaimId(claim.getId())).toList();
        return connection.createArrayOf("integer", claimIds.toArray(new Integer[0]));
    }

    private Array getDependantIdArray(Policyholder policyholder) throws SQLException {
        List<Integer> dependantIds = policyholder.getDependants().stream().map(dependant -> IdConverter.fromCustomerId(dependant.getId())).toList();
        return connection.createArrayOf("integer", dependantIds.toArray());
    }

    public static void main(String[] args) throws SQLException, NoDataFoundException {
        Connection cn = DatabaseManager.getConnection();
        PolicyholderRepository repo = new PolicyholderRepository(cn);
        Policyholder ph1 = repo.getById("c0000005");
        System.out.println(ph1.getFullName());
        System.out.println(ph1.getInsuranceCard().getPolicyOwner().getFullName());
        System.out.println(ph1.getInsuranceCard().getCardNumber());
        System.out.println(ph1.getInsuranceCard().getCardHolder().getFullName());
        for (Dependant dependant : ph1.getDependants()){
            System.out.println(dependant.getFullName());
        }
        for (Claim claim : ph1.getClaims()){
            System.out.println(claim.getId());
        }
        ph1.setFullName("Nguyen Kiet");
        repo.updateDatabase(ph1);
    }
}
