package fp.assignments.assignment_2.service.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.service.system.DatabaseConnection;

public class EventService {
  private final DatabaseConnection db;

  public EventService() {
    this.db = DatabaseConnection.getInstance();
  }

  public Event getEventById(Integer eventId) throws SQLException {
    String sql = "SELECT * FROM events WHERE id = ?";

    try (var pstmt = db.prepareStatement(sql)) {
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

  public List<Event> getEvents() throws SQLException {
    List<Event> events = new ArrayList<>();
    String sql = "SELECT * FROM events";

    try (ResultSet rs = db.executeQuery(sql)) {
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
}