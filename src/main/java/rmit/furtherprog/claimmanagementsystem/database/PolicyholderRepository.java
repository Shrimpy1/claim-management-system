package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        for (Claim claim : ph1.getClaims()){
            System.out.println(claim.getId());
        }
    }
}
