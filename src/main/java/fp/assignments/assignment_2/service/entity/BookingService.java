package fp.assignments.assignment_2.service.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.Venue;
import fp.assignments.assignment_2.service.system.DatabaseConnection;

public class BookingService {
  private static BookingService instance;
  private final DatabaseConnection dbConnection;
  private final ObservableList<Booking> bookings;

  private BookingService() {
    this.dbConnection = DatabaseConnection.getInstance();
    this.bookings = FXCollections.observableArrayList();
    loadBookings();
  }

  public static BookingService getInstance() {
    if (instance == null) {
      instance = new BookingService();
    }
    return instance;
  }

  public ObservableList<Booking> getBookings() {
    return bookings;
  }

  public void loadBookings() {
    try {
      String sql = "SELECT b.*, e.duration FROM bookings b " +
          "JOIN events e ON b.event_id = e.id";
      ResultSet rs = dbConnection.executeQuery(sql);
      bookings.clear();
      while (rs.next()) {
        bookings.add(new Booking(
            rs.getInt("id"),
            rs.getInt("event_id"),
            rs.getString("venue_name"),
            LocalDateTime.parse(rs.getString("booking_date_time")),
            LocalDateTime.parse(rs.getString("booking_date_time")).plusHours(rs.getInt("duration")),
            rs.getDouble("total_price"),
            rs.getDouble("commission")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Booking getBookingForEvent(Integer eventId) throws SQLException {
    String sql = "SELECT b.*, e.duration FROM bookings b " +
        "JOIN events e ON b.event_id = e.id " +
        "WHERE b.event_id = ?";
    try (var pstmt = dbConnection.prepareStatement(sql)) {
      pstmt.setInt(1, eventId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return new Booking(
            rs.getInt("id"),
            rs.getInt("event_id"),
            rs.getString("venue_name"),
            LocalDateTime.parse(rs.getString("booking_date_time")),
            LocalDateTime.parse(rs.getString("booking_date_time")).plusHours(rs.getInt("duration")),
            rs.getDouble("total_price"),
            rs.getDouble("commission"));
      }
    }
    return null;
  }

  public boolean isVenueAvailable(String venueName, LocalDateTime startDate, LocalDateTime endDate)
      throws SQLException {
    String sql = "SELECT COUNT(*) as count FROM bookings b " +
        "JOIN events e ON b.event_id = e.id " +
        "WHERE b.venue_name = ? AND " +
        "((b.booking_date_time BETWEEN ? AND ?) OR " +
        "(datetime(b.booking_date_time, '+' || e.duration || ' minutes') BETWEEN ? AND ?))";

    try (var pstmt = dbConnection.prepareStatement(sql)) {
      pstmt.setString(1, venueName);
      pstmt.setString(2, startDate.toString());
      pstmt.setString(3, endDate.toString());
      pstmt.setString(4, startDate.toString());
      pstmt.setString(5, endDate.toString());

      ResultSet rs = pstmt.executeQuery();
      return rs.getInt("count") == 0;
    }
  }

  public void createBooking(Event event, Venue venue, LocalDateTime bookingDateTime) throws SQLException {
    deleteBooking(event.id());

    double totalPrice = venue.hirePricePerHour() * event.durationHours();
    double commission = calculateCommission(totalPrice, event.clientName());

    String sql = "INSERT INTO bookings (event_id, venue_name, booking_date_time, total_price, commission) VALUES (?, ?, ?, ?, ?)";
    dbConnection.executeUpdate(sql, pstmt -> {
      pstmt.setInt(1, event.id());
      pstmt.setString(2, venue.nameId());
      pstmt.setString(3, bookingDateTime.toString());
      pstmt.setDouble(4, totalPrice);
      pstmt.setDouble(5, commission);
    });

    loadBookings();
  }

  public void deleteBooking(Integer eventId) throws SQLException {
    String sql = "DELETE FROM bookings WHERE event_id = ?";
    dbConnection.executeUpdate(sql, pstmt -> pstmt.setInt(1, eventId));
    loadBookings(); // Refresh the observable list
  }

  public boolean hasMultipleBookings(String clientName) throws SQLException {
    String sql = """
        SELECT COUNT(*) as count
        FROM bookings b
        JOIN events e ON b.event_id = e.id
        WHERE e.client_id = ?
        """;

    try (var pstmt = dbConnection.prepareStatement(sql)) {
      pstmt.setString(1, clientName);
      ResultSet rs = pstmt.executeQuery();
      return rs.getInt("count") >= 1;
    } catch (SQLException e) {
      System.out.println("Error checking if client has multiple bookings: " + e.getMessage());
      return false;
    }
  }

  public double calculateCommission(double totalPrice, String clientName) throws SQLException {
    double commissionRate = hasMultipleBookings(clientName) ? 0.09 : 0.10;
    return totalPrice * commissionRate;
  }
}