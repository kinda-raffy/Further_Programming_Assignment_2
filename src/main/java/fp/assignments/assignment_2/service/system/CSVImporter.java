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
