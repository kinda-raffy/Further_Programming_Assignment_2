package fp.assignments.assignment_2.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import fp.assignments.assignment_2.model.entity.Event;

public class EventService {
  private final DatabaseConnection dbService;

  public EventService() {
    this.dbService = DatabaseConnection.getInstance();
  }

  public Event getEventById(Integer eventId) throws SQLException {
    String sql = "SELECT * FROM events WHERE id = ?";

    try (var pstmt = dbService.prepareStatement(sql)) {
      pstmt.setInt(1, eventId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return new Event(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("main_artist"),
            rs.getInt("expected_attendance"),
            LocalDateTime.parse(rs.getString("event_datetime")),
            rs.getInt("duration"),
            rs.getString("event_type"),
            rs.getString("category"),
            rs.getString("client_id"));
      }
    }
    return null;
  }
}