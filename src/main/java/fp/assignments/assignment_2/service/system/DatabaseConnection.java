package fp.assignments.assignment_2.service.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import fp.assignments.assignment_2.service.ServiceProvider;
import fp.assignments.assignment_2.service.system.interfaces.IDatabaseConnection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.io.File;
import java.io.IOException;

public class DatabaseConnection implements IDatabaseConnection {
  private static final String DB_PROJECT_PATH = "src/main/resources/fp/assignments/assignment_2/data/venue_matchmaker.db";
  private static final String DB_URL = "jdbc:sqlite:" + DB_PROJECT_PATH;
  private static DatabaseConnection instance;
  private Connection connection;
  private boolean isNewDatabase;

  private DatabaseConnection() {
    try {
      File dbFile = new File(DB_PROJECT_PATH);
      isNewDatabase = !dbFile.exists();

      // Create directory if it doesn't exist.
      File dbDirectory = dbFile.getParentFile();
      if (!dbDirectory.exists()) {
        dbDirectory.mkdirs();
      }

      connection = DriverManager.getConnection(DB_URL);
      initialiseTables();

      if (isNewDatabase) {
        seedDatabase();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to initialise database", e);
    }
  }

  public static DatabaseConnection getInstance() {
    if (instance == null) {
      instance = new DatabaseConnection();
    }
    return instance;
  }

  private void initialiseTables() throws SQLException {
    String createEventsTable = """
            CREATE TABLE IF NOT EXISTS events (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                main_artist TEXT NOT NULL,
                expected_attendance INTEGER,
                event_datetime TEXT,
                duration INTEGER,
                event_type TEXT,
                category TEXT,
                client_id TEXT
            )
        """;

    String createVenuesTable = """
            CREATE TABLE IF NOT EXISTS venues (
                name TEXT PRIMARY KEY,
                capacity INTEGER,
                hire_price REAL,
                category TEXT,
                suitability_keywords TEXT
            )
        """;

    String createBookingsTable = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                event_id INTEGER,
                venue_name TEXT,
                booking_date_time TEXT,
                total_price REAL,
                commission REAL,
                FOREIGN KEY (event_id) REFERENCES events(id),
                FOREIGN KEY (venue_name) REFERENCES venues(name) ON DELETE CASCADE
            )
        """;

    String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_name TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                type TEXT NOT NULL CHECK (type IN ('manager', 'staff'))
            )
        """;

    try (var statement = connection.createStatement()) {
      statement.execute(createEventsTable);
      statement.execute(createVenuesTable);
      statement.execute(createBookingsTable);
      statement.execute(createUsersTable);
    }
  }

  private void seedDatabase() {
    try {
      ServiceProvider.run(sp -> {
        File venuesFile = new File(getClass().getResource("/fp/assignments/assignment_2/venues.csv").getFile());
        sp.csvImporter().importVenueCSV(venuesFile);
        File eventsFile = new File(getClass().getResource("/fp/assignments/assignment_2/events.csv").getFile());
        sp.csvImporter().importEventCSV(eventsFile);
      });
    } catch (SQLException | IOException e) {
      throw new RuntimeException("Failed to seed initial data", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to seed initial data", e);
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public ResultSet executeQuery(String sql) throws SQLException {
    Connection conn = getConnection();
    Statement stmt = conn.createStatement();
    return stmt.executeQuery(sql);
  }

  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return connection.prepareStatement(sql);
  }

  public int executeUpdate(String sql, PreparedStatementSetter setter) throws SQLException {
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      setter.setValues(pstmt);
      return pstmt.executeUpdate();
    }
  }

  @FunctionalInterface
  public interface PreparedStatementSetter {
    void setValues(PreparedStatement ps) throws SQLException;
  }
}