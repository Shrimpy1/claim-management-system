package rmit.furtherprog.claimmanagementsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres";
        String user = "postgres.hccazvuwuxosfaqfckpd";
        String password = "rmitSupa123";

        // Register PostgreSQL JDBC driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }

        // Establish database connection
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static void main(String[] args) {
        try {
            // Get database connection
            Connection connection = getConnection();
            System.out.println("Connected to PostgreSQL database!");

            // Use the connection for database operations (query execution, data retrieval, etc.)


            // Close the connection when done
            connection.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
