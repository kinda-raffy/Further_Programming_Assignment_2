package fp.assignments.assignment_2.service.entity.interfaces;

import javafx.collections.ObservableList;
import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.Venue;
import java.sql.SQLException;
import java.time.LocalDateTime;

public interface IBookingService {
  ObservableList<Booking> getBookings();

  void loadBookings();

  Booking getBookingForEvent(Integer eventId) throws SQLException;

  boolean isVenueAvailable(String venueName, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;

  void createBooking(Event event, Venue venue, LocalDateTime bookingDateTime) throws SQLException;

  void deleteBooking(Integer eventId) throws SQLException;

  boolean hasMultipleBookings(String clientName) throws SQLException;

  double calculateCommission(double totalPrice, String clientName) throws SQLException;
}
