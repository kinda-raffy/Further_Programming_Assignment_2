package fp.assignments.assignment_2.service;

import fp.assignments.assignment_2.model.Event;
import fp.assignments.assignment_2.model.Venue;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeService {
  private final DatabaseConnection dbService;

  public HomeService() {
    this.dbService = DatabaseConnection.getInstance();
  }

  public List<Venue> loadVenues() throws SQLException {
    List<Venue> venues = new ArrayList<>();
    Connection conn = dbService.getConnection();
    String sql = "SELECT * FROM venues";

    try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        venues.add(new Venue(
            rs.getString("name"),
            rs.getInt("capacity"),
            rs.getString("suitability_keywords"),
            rs.getString("category"),
            rs.getDouble("hire_price")));
      }
    }
    return venues;
  }

  public List<Event> loadEvents() throws SQLException {
    List<Event> events = new ArrayList<>();
    Connection conn = dbService.getConnection();
    String sql = "SELECT * FROM events";

    try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        events.add(new Event(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("main_artist"),
            rs.getInt("expected_attendance"),
            LocalDateTime.parse(rs.getString("event_datetime")),
            rs.getInt("duration"),
            rs.getString("event_type"),
            rs.getString("category"),
            rs.getBoolean("is_booked"),
            rs.getString("client_id")));
      }
    }
    return events;
  }

  public void importVenues(File file) throws SQLException, IOException {
    Connection conn = dbService.getConnection();
    String sql = "INSERT OR REPLACE INTO venues (name, capacity, suitability_keywords, category, hire_price) VALUES (?, ?, ?, ?, ?)";

    try (Scanner scanner = new Scanner(file);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      if (scanner.hasNextLine())
        scanner.nextLine(); // Skip header

      while (scanner.hasNextLine()) {
        String[] data = scanner.nextLine().split(",");
        String priceStr = data[4].trim().replace("$", "").replace(" / hour", "");

        pstmt.setString(1, data[0].trim());
        pstmt.setInt(2, Integer.parseInt(data[1].trim()));
        pstmt.setString(3, data[2].trim());
        pstmt.setString(4, data[3].trim());
        pstmt.setDouble(5, Double.parseDouble(priceStr));
        pstmt.executeUpdate();
      }
    }
  }

  public void importEvents(File file) throws IOException, SQLException, DateTimeParseException {
    Connection conn = dbService.getConnection();
    String sql = "INSERT INTO events (client_id, name, main_artist, expected_attendance, event_datetime, event_type, category, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (BufferedReader reader = new BufferedReader(new FileReader(file));
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      reader.readLine(); // Skip header

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Concatenate date and time.
        LocalDate date = LocalDate.parse(data[3].trim(), dateFormatter);
        LocalTime time = LocalTime.parse(data[4].trim().toUpperCase(), timeFormatter);
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        pstmt.setString(1, data[0].trim()); // Client
        pstmt.setString(2, data[1].trim()); // Title
        pstmt.setString(3, data[2].trim()); // Artist
        pstmt.setInt(4, Integer.parseInt(data[6].trim())); // Target Audience (expected_attendance)
        pstmt.setString(5, dateTime.toString()); // Date/Time
        pstmt.setString(6, data[7].trim()); // Type
        pstmt.setString(7, data[8].trim()); // Category
        pstmt.setInt(8, Integer.parseInt(data[5].trim())); // Duration
        pstmt.executeUpdate();
      }
    }
  }
}
