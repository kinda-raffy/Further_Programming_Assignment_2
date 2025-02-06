package fp.assignments.assignment_2.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fp.assignments.assignment_2.model.entity.Venue;

public class VenueService {
  private final DatabaseConnection db;

  public VenueService() {
    this.db = DatabaseConnection.getInstance();
  }

  public void createVenue(String name, int capacity, String keywords, String category, double hirePrice)
      throws SQLException {
    String sql = """
        INSERT INTO venues (name, capacity, suitability_keywords, category, hire_price)
        VALUES (?, ?, ?, ?, ?)
        """;

    db.executeUpdate(sql, ps -> {
      ps.setString(1, name);
      ps.setInt(2, capacity);
      ps.setString(3, keywords);
      ps.setString(4, category);
      ps.setDouble(5, hirePrice);
    });
  }

  public void deleteVenue(String venueName) throws SQLException {
    String sql = "DELETE FROM venues WHERE name = ?";
    db.executeUpdate(sql, ps -> ps.setString(1, venueName));
  }

  public List<Venue> getVenues() throws SQLException {
    List<Venue> venues = new ArrayList<>();
    String sql = "SELECT * FROM venues";

    try (ResultSet rs = db.executeQuery(sql)) {
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
}
