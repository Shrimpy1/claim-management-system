package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Policyholder;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.DateParsing;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = "SELECT * FROM banking_info WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String bank = resultSet.getString("bank");
                String name = resultSet.getString("name");
                String number = resultSet.getString("number");
                return new BankingInfo(bank, name, number);
            } else {
                throw new NoDataFoundException("No document found with id: " + id);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
