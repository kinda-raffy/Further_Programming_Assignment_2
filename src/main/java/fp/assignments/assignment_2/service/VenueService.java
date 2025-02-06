package fp.assignments.assignment_2.service;

import java.sql.SQLException;

public class VenueService {
  private final DatabaseConnection dbConnection;

  public VenueService() {
    this.dbConnection = DatabaseConnection.getInstance();
  }

  public void createVenue(String name, int capacity, String keywords, String category, double hirePrice)
      throws SQLException {
    String sql = """
        INSERT INTO venues (name, capacity, suitability_keywords, category, hire_price)
        VALUES (?, ?, ?, ?, ?)
        """;

    dbConnection.executeUpdate(sql, ps -> {
      ps.setString(1, name);
      ps.setInt(2, capacity);
      ps.setString(3, keywords);
      ps.setString(4, category);
      ps.setDouble(5, hirePrice);
    });
  }
}
