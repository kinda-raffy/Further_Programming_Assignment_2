package fp.assignments.assignment_2.service;

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

import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.Venue;

public class HomeService {
  private final DatabaseConnection dbService;

  public HomeService() {
    this.dbService = DatabaseConnection.getInstance();
  }

  public List<Venue> loadVenues() throws SQLException {
    List<Venue> venues = new ArrayList<>();
    String sql = "SELECT * FROM venues";

    try (ResultSet rs = dbService.executeQuery(sql)) {
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
    String sql = "SELECT * FROM events";

    try (ResultSet rs = dbService.executeQuery(sql)) {
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
            rs.getString("client_id")));
      }
    }
    return events;
  }

  public void importVenues(File file) throws SQLException, IOException {
    String sql = "INSERT OR REPLACE INTO venues (name, capacity, suitability_keywords, category, hire_price) VALUES (?, ?, ?, ?, ?)";

    try (Scanner scanner = new Scanner(file)) {
      if (scanner.hasNextLine()) {
        scanner.nextLine(); // Skip header
      }

      while (scanner.hasNextLine()) {
        String[] data = scanner.nextLine().split(",");
        String priceStr = data[4].trim().replace("$", "").replace(" / hour", "");
        final String[] finalData = data;

        dbService.executeUpdate(sql, pstmt -> {
          pstmt.setString(1, finalData[0].trim());
          pstmt.setInt(2, Integer.parseInt(finalData[1].trim()));
          pstmt.setString(3, finalData[2].trim());
          pstmt.setString(4, finalData[3].trim());
          pstmt.setDouble(5, Double.parseDouble(priceStr));
        });
      }
    }
  }

  public void importEvents(File file) throws IOException, SQLException, DateTimeParseException {
    String sql = "INSERT INTO events (name, main_artist, expected_attendance, event_datetime, duration, event_type, category, client_id) "
        +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      reader.readLine(); // Skip header

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate date = LocalDate.parse(data[3].trim(), dateFormatter);
        LocalTime time = LocalTime.parse(data[4].trim().toUpperCase(), timeFormatter);
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        final String[] finalData = data;
        final LocalDateTime finalDateTime = dateTime;

        dbService.executeUpdate(sql, pstmt -> {
          pstmt.setString(1, finalData[1].trim()); // Title/name
          pstmt.setString(2, finalData[2].trim()); // Artist
          pstmt.setInt(3, Integer.parseInt(finalData[6].trim())); // Target Audience
          pstmt.setString(4, finalDateTime.toString()); // Date/Time
          pstmt.setInt(5, Integer.parseInt(finalData[5].trim())); // Duration
          pstmt.setString(6, finalData[7].trim()); // Type
          pstmt.setString(7, finalData[8].trim()); // Category
          pstmt.setString(8, finalData[0].trim()); // Client ID
        });
      }
    }
  }
}
