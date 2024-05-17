package rmit.furtherprog.claimmanagementsystem.util;

import rmit.furtherprog.claimmanagementsystem.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {
    private static Connection connection;

    static {
        try {
            connection = DatabaseManager.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createAccount(String username, String password, String accountType){
        String sql = "INSERT INTO account(username, password, account_type) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, accountType);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Account created successfully.");
                HistoryManager.write("account", "Created with the username: " + username);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create new account.");
        }
    }

    public static void deleteAccount(String username){
        String sql = "DELETE FROM account WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, username);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0){
                HistoryManager.write("account", "Deleted with the username: " + username);
                System.out.println("Deleted the account successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete the account.");
        }
    }

    public static String verifyAccount(String username, String password){
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("account_type");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(verifyAccount("c0000001", "123"));
    }
}
