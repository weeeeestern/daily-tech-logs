package repository;

import java.io.File;
import java.sql.*;

/**
 * Repository for recording presented categories per user using SQLite.
 */
public class EntryRepository {
    private static final String DB_PATH = ".data/db.sqlite";
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;

    public EntryRepository() {
        initialize();
    }

    /**
     * Records that a category was presented to a user. Inserts a new record or
     * updates the existing record's timestamp when one already exists.
     *
     * @param user     the user identifier
     * @param category the presented category
     */
    public void recordPresented(String user, String category) {
        long now = System.currentTimeMillis();
        String sql = "INSERT INTO presented (user, category, last_presented) VALUES (?, ?, ?) " +
                "ON CONFLICT(user, category) DO UPDATE SET last_presented = excluded.last_presented";

        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, category);
            ps.setLong(3, now);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to record presentation", e);
        }
    }

    private void initialize() {
        // Ensure data directory exists
        File dir = new File(".data");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Failed to create data directory: " + dir.getAbsolutePath());
        }

        String createTable = "CREATE TABLE IF NOT EXISTS presented (" +
                "user TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "last_presented INTEGER NOT NULL, " +
                "PRIMARY KEY(user, category)" +
                ")";

        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTable);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
