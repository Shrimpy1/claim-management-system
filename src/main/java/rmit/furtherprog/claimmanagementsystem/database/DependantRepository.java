package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                return mapResultSetToDependant(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + customerId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Dependant> getAll() {
        List<Dependant> dependants = new ArrayList<>();
        String sql = "SELECT * FROM dependant";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Dependant dependant = mapResultSetToDependant(resultSet);
                dependants.add(dependant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dependants;
    }

    private Dependant mapResultSetToDependant(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");
        String cardNumber = resultSet.getString("insurance_card_number");
        InsuranceCardRepository iRepo = new InsuranceCardRepository(connection);
        InsuranceCard card = iRepo.getByNumberPartial(cardNumber);

        List<Claim> claimList = new ArrayList<Claim>();
        Array claimId = resultSet.getArray("claim");
        if (claimId != null){
            Integer[] claims = (Integer[]) claimId.getArray();
            ClaimRepository cRepo = new ClaimRepository(connection);
            for (Integer cid : claims){
                Claim claim = cRepo.getById(IdConverter.toClaimId(cid));
                claimList.add(claim);
            }
        }

        Dependant dependant = new Dependant(customerId, fullName, claimList);
        dependant.setInsuranceCard(card);

        return dependant;
    }

    public Dependant getByIdPartial(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM dependant WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToDependantPartial(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + customerId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Dependant> getAllPartial() {
        List<Dependant> dependants = new ArrayList<>();
        String sql = "SELECT * FROM dependant";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Dependant dependant = mapResultSetToDependantPartial(resultSet);
                dependants.add(dependant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dependants;
    }

    private Dependant mapResultSetToDependantPartial(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");
        String cardNumber = resultSet.getString("insurance_card_number");
        InsuranceCardRepository iRepo = new InsuranceCardRepository(connection);
        InsuranceCard card = iRepo.getByNumberPartial(cardNumber);

        List<Claim> claimList = new ArrayList<Claim>();
        Array claimId = resultSet.getArray("claim");
        if (claimId != null){
            Integer[] claims = (Integer[]) claimId.getArray();
            ClaimRepository cRepo = new ClaimRepository(connection);
            for (Integer cid : claims){
                Claim claim = cRepo.getByIdPartial(IdConverter.toClaimId(cid));
                claimList.add(claim);
            }
        }

        Dependant dependant = new Dependant(customerId, fullName, claimList);
        dependant.setInsuranceCard(card);

        return dependant;
    }

    public Dependant getByIdWithoutCard(String customerId) {
        int databaseId = IdConverter.fromCustomerId(customerId);
        String sql = "SELECT * FROM dependant WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToDependantWithoutCard(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + customerId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Dependant mapResultSetToDependantWithoutCard(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String customerId = IdConverter.toCustomerId(id);
        String fullName = resultSet.getString("full_name");

        List<Claim> claimList = new ArrayList<Claim>();
        Array claimId = resultSet.getArray("claim");
        if (claimId != null){
            Integer[] claims = (Integer[]) claimId.getArray();
            ClaimRepository cRepo = new ClaimRepository(connection);
            for (Integer cid : claims){
                Claim claim = cRepo.getByIdPartial(IdConverter.toClaimId(cid));
                claimList.add(claim);
            }
        }

        return new Dependant(customerId, fullName, claimList);
    }

    public void updateDatabase(Dependant dependant){
        String updateSQL = "UPDATE dependant SET full_name = ?, insurance_card_number = ?, claim = ? WHERE customer_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, dependant.getFullName());
            preparedStatement.setString(2, dependant.getInsuranceCard().getCardNumber());
            Array claimIdArray = getClaimIdArray(dependant);
            preparedStatement.setArray(3, claimIdArray);
            preparedStatement.setInt(4, IdConverter.fromCustomerId(dependant.getId()));

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Dependant updated successfully.");
            } else {
                System.out.println("No dependant found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the dependant.");
        }
    }

    public int addToDatabase(Dependant dependant){
        String insertSQL = "INSERT INTO dependant (full_name, insurance_card_number, claim) VALUES (?, ?, ?) RETURNING customer_id";
        int newId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)){
            preparedStatement.setString(1, dependant.getFullName());
            preparedStatement.setString(2, dependant.getInsuranceCard().getCardNumber());
            Array claimIdArray = getClaimIdArray(dependant);
            preparedStatement.setArray(3, claimIdArray);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("id");
                    System.out.println("Dependant added successfully with ID: " + newId);
                } else {
                    throw new SQLException("Failed to retrieve the ID of the inserted Dependant.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the dependant.");
        }

        return newId;
    }

    private Array getClaimIdArray(Dependant dependant) throws SQLException {
        List<Integer> claimIds = dependant.getClaims().stream().map(claim -> IdConverter.fromClaimId(claim.getId())).toList();
        return connection.createArrayOf("integer", claimIds.toArray(new Integer[0]));
    }

    public static void main(String[] args) throws SQLException, NoDataFoundException {
        Connection cn = DatabaseManager.getConnection();
        DependantRepository repo = new DependantRepository(cn);
        Dependant d1 = repo.getById("c0000002");
        System.out.println(d1.getFullName());
        System.out.println(d1.getInsuranceCard().getPolicyOwner().getFullName());
        System.out.println(d1.getInsuranceCard().getCardNumber());
        System.out.println(d1.getInsuranceCard().getCardHolder().getFullName());
        for (Claim claim : d1.getClaims()){
            System.out.println(claim.getId());
        }
        d1.setFullName("Ice Mint");
        repo.updateDatabase(d1);

    }
}
