package fp.assignments.assignment_2.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class DatabaseConnection {
  private static final String DB_URL = "jdbc:sqlite:src/main/resources/fp/assignments/assignment_2/data/venue_matchmaker.db";
  private static DatabaseConnection instance;
  private Connection connection;

  private DatabaseConnection() {
    try {
      connection = DriverManager.getConnection(DB_URL);
      initialiseTables();
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
                FOREIGN KEY (venue_name) REFERENCES venues(name)
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