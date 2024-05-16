/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.provider.Surveyor;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.HistoryManager;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SurveyorRepository {
    private Connection connection;

    public SurveyorRepository(Connection connection) {
        this.connection = connection;
    }

    public Surveyor getById(int id){
        String sql = "SELECT * FROM surveyor WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                Array proposedClaimArr = resultSet.getArray("proposed_claim");
                List<Claim> proposedClaimList = getClaimList(proposedClaimArr);
                return new Surveyor(id, fullName, proposedClaimList);
            } else {
                throw new NoDataFoundException("No document found with id: " + id);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<Claim> getClaimList(Array proposedClaimArr) throws SQLException {
        List<Claim> proposedClaimList = new ArrayList<Claim>();
        if (proposedClaimArr != null){
            Integer[] claimIds = (Integer[]) proposedClaimArr.getArray();
            ClaimRepository claimRepository = new ClaimRepository(connection);
            for (Integer claimId : claimIds){
                Claim claim = claimRepository.getById(IdConverter.toClaimId(claimId));
                proposedClaimList.add(claim);
            }
        }
        return proposedClaimList;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM surveyor WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDelete = statement.executeUpdate();
            if (rowsDelete > 0){
                System.out.println("Deleted surveyor with ID: " + id);
                HistoryManager.write("surveyor", "Deleted with ID: " + id);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete surveyor.");
        }
    }

    public int addToDatabase(Surveyor surveyor) {
        String insertSQL = "INSERT INTO surveyor (full_name, proposed_claim) VALUES (?, ?)  RETURNING id";
        int newId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, surveyor.getName());
            Array claimIdArr = getClaimIdArray(surveyor);
            preparedStatement.setArray(2, claimIdArr);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("id");
                    System.out.println("Surveyor added successfully with ID: " + newId);
                    HistoryManager.write("surveyor", "Added with ID: " + surveyor.getId());
                } else {
                    throw new SQLException("Failed to retrieve the ID of the inserted surveyor.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the surveyor.");
        }

        return newId;
    }

    public void updateDatabase(Surveyor surveyor) {
        String updateSQL = "UPDATE surveyor SET full_name = ?, proposed_claim = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, surveyor.getName());
            Array claimIdArr = getClaimIdArray(surveyor);
            preparedStatement.setArray(2, claimIdArr);
            preparedStatement.setInt(3, surveyor.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Surveyor updated successfully.");
                HistoryManager.write("surveyor", "Updated with ID: " + surveyor.getId());
            } else {
                System.out.println("No surveyor found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the surveyor.");
        }
    }

    private Array getClaimIdArray(Surveyor surveyor) throws SQLException {
        List<Integer> claimIdList = surveyor.getProposedClaim().stream().map(claim -> IdConverter.fromClaimId(claim.getId())).toList();
        return connection.createArrayOf("integer", claimIdList.toArray(new Integer[0]));
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseManager.getConnection();
        SurveyorRepository repository = new SurveyorRepository(connection);
        Surveyor surveyor = repository.getById(2);
        System.out.println(surveyor.getName());
//        surveyor.setName("Elijah Robert");
//        ClaimRepository claimRepository = new ClaimRepository(connection);
//        Claim claim = claimRepository.getById("f0000000001");
//        surveyor.proposeClaim(claim);
        for (Claim claim : surveyor.getProposedClaim()){
            System.out.println(claim.getId());
        }
//        repository.updateDatabase(surveyor);
    }
}
