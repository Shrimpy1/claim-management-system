package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.DateParsing;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClaimRepository {
    private Connection connection;

    public ClaimRepository(Connection connection) {
        this.connection = connection;
    }

    public Claim getById(String claimId) {
        int databaseId = IdConverter.fromClaimId(claimId);
        String sql = "SELECT * FROM claim WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToClaim(resultSet);
            } else {
                throw new NoDataFoundException("No claim found with id: " + claimId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Claim> getAll() {
        List<Claim> claims = new ArrayList<>();
        String sql = "SELECT * FROM dependant";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Claim claim = mapResultSetToClaim(resultSet);
                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    private Claim mapResultSetToClaim(ResultSet resultSet) throws SQLException {
        String id = IdConverter.toClaimId(resultSet.getInt("id"));
        LocalDate claimDate = DateParsing.stod(resultSet.getString("claim_date"));
        String cardNumber = resultSet.getString("card_number");
        LocalDate examDate = DateParsing.stod(resultSet.getString("exam_date"));
        List<String> documents = Arrays.asList((String[]) resultSet.getArray("documents").getArray());
        double claimAmount = resultSet.getDouble("claim_amount");
        Claim.ClaimStatus status = Claim.ClaimStatus.valueOf(resultSet.getString("status"));
        BankingInfo receiver_banking_info = fetchBankingInfo(resultSet.getInt("banking_info"));
        PolicyholderRepository phRepo = new PolicyholderRepository(connection);
        Policyholder insuredPerson = phRepo.getByIdPartial(IdConverter.toCustomerId(resultSet.getInt("insured_person")));

        return new Claim(id, claimDate, insuredPerson, cardNumber, examDate, documents, claimAmount, status, receiver_banking_info);
    }

    public Claim getByIdPartial(String claimId) {
        int databaseId = IdConverter.fromClaimId(claimId);
        String sql = "SELECT * FROM claim WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, databaseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToClaimPartial(resultSet);
            } else {
                throw new NoDataFoundException("No claim found with id: " + claimId);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Claim> getAllPartial() {
        List<Claim> claims = new ArrayList<>();
        String sql = "SELECT * FROM dependant";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Claim claim = mapResultSetToClaimPartial(resultSet);
                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    private Claim mapResultSetToClaimPartial(ResultSet resultSet) throws SQLException {
        String id = IdConverter.toClaimId(resultSet.getInt("id"));
        LocalDate claimDate = DateParsing.stod(resultSet.getString("claim_date"));
        String cardNumber = resultSet.getString("card_number");
        LocalDate examDate = DateParsing.stod(resultSet.getString("exam_date"));
        List<String> documents = Arrays.asList((String[]) resultSet.getArray("documents").getArray());
        double claimAmount = resultSet.getDouble("claim_amount");
        Claim.ClaimStatus status = Claim.ClaimStatus.valueOf(resultSet.getString("status"));
        BankingInfo receiver_banking_info = fetchBankingInfo(resultSet.getInt("banking_info"));
        return new Claim(id, claimDate, cardNumber, examDate, documents, claimAmount, status, receiver_banking_info);
    }

    private BankingInfo fetchBankingInfo(int id){
        BankingInfoRepository repo = new BankingInfoRepository(connection);
        return repo.getById(id);
    }

    public void updateDatabase(Claim claim) {
        String updateSQL = "UPDATE claim SET claim_date = ?, insured_person = ?, card_number = ?, exam_date = ?, documents = ?, claim_amount = ?, status = ?, banking_info = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setObject(1, claim.getClaimDate());
            preparedStatement.setInt(2, IdConverter.fromCustomerId(claim.getInsuredPerson().getId()));
            preparedStatement.setString(3, claim.getCardNumber());
            preparedStatement.setObject(4, claim.getExamDate());
            Array documentsArray = connection.createArrayOf("text", claim.getDocuments().toArray(new String[0]));
            preparedStatement.setArray(5, documentsArray);
            preparedStatement.setDouble(6, claim.getClaimAmount());
            preparedStatement.setObject(7, claim.getStatus(), java.sql.Types.OTHER);
            preparedStatement.setInt(8, claim.getReceiverBankingInfo().getId());
            preparedStatement.setInt(9, IdConverter.fromClaimId(claim.getId()));

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Claim updated successfully.");
            } else {
                System.out.println("No claim found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update the claim.");
        }
    }

    public int addToDatabase(Claim claim) {
        String insertSQL = "INSERT INTO claim (claim_date, insured_person, card_number, exam_date, documents, claim_amount, status, banking_info) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        int newId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setObject(1, claim.getClaimDate());
            preparedStatement.setInt(2, IdConverter.fromCustomerId(claim.getInsuredPerson().getId()));
            preparedStatement.setString(3, claim.getCardNumber());
            preparedStatement.setObject(4, claim.getExamDate());
            Array documentsArray = connection.createArrayOf("text", claim.getDocuments().toArray(new String[0]));
            preparedStatement.setArray(5, documentsArray);
            preparedStatement.setDouble(6, claim.getClaimAmount());
            preparedStatement.setObject(7, claim.getStatus(), java.sql.Types.OTHER);
            preparedStatement.setInt(8, claim.getReceiverBankingInfo().getId());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("id");
                    System.out.println("Claim added successfully with ID: " + newId);
                } else {
                    throw new SQLException("Failed to retrieve the ID of the inserted Claim.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the claim.");
        }

        return newId;
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseManager.getConnection();
        ClaimRepository repository = new ClaimRepository(connection);
        Claim c1 = repository.getById("f0000000001");
        System.out.println(c1.getId());
        System.out.println(c1.getClaimDate());
        System.out.println(c1.getReceiverBankingInfo().getId());
        System.out.println(c1.getReceiverBankingInfo().getName());
        BankingInfoRepository bRepo = new BankingInfoRepository(connection);
        BankingInfo b1 = bRepo.getById(2);
        c1.setReceiverBankingInfo(b1);
        repository.updateDatabase(c1);

    }
}
