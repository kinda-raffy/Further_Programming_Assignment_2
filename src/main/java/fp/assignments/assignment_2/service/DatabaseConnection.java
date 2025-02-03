package fp.assignments.assignment_2.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private static final String DB_URL = "jdbc:sqlite:venue_matchmaker.db";
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
                is_booked BOOLEAN DEFAULT 0,
                client_id INTEGER
            )
        """;

    String createVenuesTable = """
            CREATE TABLE IF NOT EXISTS venues (
                name TEXT PRIMARY KEY,
                capacity INTEGER,
                hire_price REAL,
                category TEXT,
                suitability_keywords TEXT,
                is_available BOOLEAN DEFAULT 1
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

    try (var statement = connection.createStatement()) {
      statement.execute(createEventsTable);
      statement.execute(createVenuesTable);
      statement.execute(createBookingsTable);
    }
  }

  public Connection getConnection() {
    return connection;
  }
}