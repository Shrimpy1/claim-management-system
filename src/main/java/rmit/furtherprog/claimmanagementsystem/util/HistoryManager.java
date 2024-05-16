package rmit.furtherprog.claimmanagementsystem.util;

import rmit.furtherprog.claimmanagementsystem.data.model.util.History;
import rmit.furtherprog.claimmanagementsystem.database.DatabaseManager;
import rmit.furtherprog.claimmanagementsystem.database.HistoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class HistoryManager {
    private static HistoryRepository repository;

    static {
        try {
            repository = new HistoryRepository(DatabaseManager.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private HistoryManager() {}

    public static List<History> getAllHistory(){
        return repository.getAll();
    }

    public static void write(String type, String event) {
        repository.addToDatabase(type, event);
    }
}
