package fp.assignments.assignment_2.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
  private static final String DB_URL = "jdbc:sqlite:venue_matchmaker.db";
  private static DatabaseService instance;
  private Connection connection;

  private DatabaseService() {
    try {
      connection = DriverManager.getConnection(DB_URL);
      initializeTables();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to initialize database", e);
    }
  }

  public static DatabaseService getInstance() {
    if (instance == null) {
      instance = new DatabaseService();
    }
    return instance;
  }

  private void initializeTables() throws SQLException {
    String createEventsTable = """
            CREATE TABLE IF NOT EXISTS events (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                main_artist TEXT NOT NULL,
                supporting_acts TEXT,
                expected_attendance INTEGER,
                event_date TEXT,
                event_time TEXT,
                event_type TEXT,
                category TEXT,
                is_booked BOOLEAN DEFAULT 0,
                client_id INTEGER
            )
        """;

    String createVenuesTable = """
            CREATE TABLE IF NOT EXISTS venues (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
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
                venue_id INTEGER,
                booking_date_time TEXT,
                total_price REAL,
                commission REAL,
                FOREIGN KEY (event_id) REFERENCES events(id),
                FOREIGN KEY (venue_id) REFERENCES venues(id)
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