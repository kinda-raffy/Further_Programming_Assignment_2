package fp.assignments.assignment_2.service.system;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CSVImporter {
  private final DatabaseConnection dbService;

  public CSVImporter() {
    this.dbService = DatabaseConnection.getInstance();
  }

  public void importVenueCSV(File file) throws SQLException, IOException {
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

  public void importEventCSV(File file) throws IOException, SQLException, DateTimeParseException {
    String checkSql = "SELECT COUNT(*) FROM events WHERE client_id = ? AND main_artist = ? AND name = ? AND event_datetime = ?";
    String insertSql = "INSERT INTO events (name, main_artist, expected_attendance, event_datetime, duration, event_type, category, client_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      reader.readLine(); // Skip header.

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(data[3].trim(), dateFormatter);
        LocalTime time = parseTime(data[4].trim());
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Check if event already exists.
        try (PreparedStatement checkStmt = dbService.prepareStatement(checkSql)) {
          checkStmt.setString(1, data[0].trim()); // Client ID
          checkStmt.setString(2, data[2].trim()); // Artist
          checkStmt.setString(3, data[1].trim()); // Title/name
          checkStmt.setString(4, dateTime.toString()); // Date/Time

          ResultSet rs = checkStmt.executeQuery();
          if (rs.getInt(1) > 0) {
            continue; // Skip this event as it already exists.
          }
        }

        final String[] finalData = data;
        final LocalDateTime finalDateTime = dateTime;

        dbService.executeUpdate(insertSql, pstmt -> {
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

  private LocalTime parseTime(String timeStr) {
    // Standardise time string.
    timeStr = timeStr.replaceAll("\\s+", " ").trim().toLowerCase();

    try {
      // For example: 17:30
      return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
    } catch (DateTimeParseException e1) {
      try {
        // For example: 8:30pm
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("h:mma"));
      } catch (DateTimeParseException e2) {
        try {
          // For example: 8pm
          return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("ha"));
        } catch (DateTimeParseException e3) {
          try {
            // For example: 8 pm
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("h a"));
          } catch (DateTimeParseException e4) {
            // For example: 8:00 pm
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("h:mm a"));
          }
        }
      }
    }
  }
}
