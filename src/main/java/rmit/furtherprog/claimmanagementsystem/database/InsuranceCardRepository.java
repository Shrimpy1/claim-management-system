/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.database;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Customer;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.Dependant;
import rmit.furtherprog.claimmanagementsystem.data.model.customer.PolicyOwner;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.InsuranceCard;
import rmit.furtherprog.claimmanagementsystem.exception.NoDataFoundException;
import rmit.furtherprog.claimmanagementsystem.util.DateParsing;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InsuranceCardRepository {
    private Connection connection;

    public InsuranceCardRepository(Connection connection) {
        this.connection = connection;
    }

    public InsuranceCard getByNumber(String number) {
        String sql = "SELECT * FROM insurance_card WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToInsuranceCard(resultSet);
            } else {
                throw new NoDataFoundException("No data found with input: " + number);
            }
        } catch (SQLException | NoDataFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private InsuranceCard mapResultSetToInsuranceCard(ResultSet resultSet) throws SQLException {
        String number = resultSet.getString("number");
        String cardHolderType = resultSet.getString("card_holder_type");
        String cardHolderId = IdConverter.toCustomerId(resultSet.getInt("card_holder_id"));
        Customer cardHolder;
        if (cardHolderType.equals("dependant")){
            DependantRepository dependantRepository = new DependantRepository(connection);
            cardHolder = dependantRepository.getByIdWithoutCard(cardHolderId);
        } else {
            PolicyholderRepository policyholderRepository = new PolicyholderRepository(connection);
            cardHolder = policyholderRepository.getByIdWithoutCard(cardHolderId);
        }
        LocalDate expirationDate = DateParsing.stod(resultSet.getString("expiration_date"));
        String policyOwnerId = IdConverter.toCustomerId(resultSet.getInt("policy_owner_id"));
        PolicyOwnerRepository repo = new PolicyOwnerRepository(connection);
        PolicyOwner policyOwner = repo.getByIdPartial(policyOwnerId);

        return new InsuranceCard(number, cardHolder, policyOwner, expirationDate);
    }

    public InsuranceCard getByNumberPartial(String number) {
        String sql = "SELECT * FROM insurance_card WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToInsuranceCardPartial(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    private InsuranceCard mapResultSetToInsuranceCardPartial(ResultSet resultSet) throws SQLException {
        String number = resultSet.getString("number");
        LocalDate expirationDate = DateParsing.stod(resultSet.getString("expiration_date"));
        String policyOwnerId = IdConverter.toCustomerId(resultSet.getInt("policy_owner_id"));
        PolicyOwnerRepository repo = new PolicyOwnerRepository(connection);
        PolicyOwner policyOwner = repo.getByIdPartial(policyOwnerId);

        InsuranceCard card = new InsuranceCard(number, policyOwner, expirationDate);
        return card;
    }

    public void updateDatabase(InsuranceCard card){
        String stringSQL = "UPDATE insurance_card SET card_holder_id = ?, card_holder_type = ?, expiration_date = ?, policy_owner_id = ? WHERE number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(stringSQL)) {
            preparedStatement.setInt(1, IdConverter.fromCustomerId(card.getCardHolder().getId()));
            String cardHolderType = (card.getCardHolder() instanceof Dependant)? "dependant" : "policyholder";
            preparedStatement.setString(2, cardHolderType);
            preparedStatement.setObject(3, card.getExpirationDate());
            preparedStatement.setInt(4, IdConverter.fromCustomerId(card.getPolicyOwner().getId()));
            preparedStatement.setString(5, card.getCardNumber());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Insurance card updated successfully.");
            } else {
                System.out.println("No insurance card found with the given card number.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the insurance card.");
        }
    }

    public void addToDatabase(InsuranceCard card) {
        String stringSQL = "INSERT INTO insurance_card (number, card_holder_id, card_holder_type, expiration_date, policy_owner_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(stringSQL)) {
            preparedStatement.setString(1, card.getCardNumber());
            preparedStatement.setInt(2, IdConverter.fromCustomerId(card.getCardHolder().getId()));
            String cardHolderType = (card.getCardHolder() instanceof Dependant)? "dependant" : "policyholder";
            preparedStatement.setString(3, cardHolderType);
            preparedStatement.setObject(4, card.getExpirationDate());
            preparedStatement.setInt(5, IdConverter.fromCustomerId(card.getPolicyOwner().getId()));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Insurance card added successfully.");
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the insurance card.");
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection cn = DatabaseManager.getConnection();
        InsuranceCardRepository repo = new InsuranceCardRepository(cn);
        InsuranceCard i1 = repo.getByNumber("1234567890");
        System.out.println(i1.getExpirationDate());
        System.out.println(i1.getPolicyOwner().getFullName());
        System.out.println(i1.getCardHolder().getFullName());
        LocalDate newExpirationDate = LocalDate.of(2000, 1, 1);
        i1.setExpirationDate(newExpirationDate);
        repo.updateDatabase(i1);
    }
}
