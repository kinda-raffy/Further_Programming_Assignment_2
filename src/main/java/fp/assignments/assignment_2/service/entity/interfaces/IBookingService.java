package fp.assignments.assignment_2.service.entity.interfaces;

import javafx.collections.ObservableList;
import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.Venue;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Interface for managing booking-related operations.
 */
public interface IBookingService {
  /**
   * Retrieves all bookings.
   * 
   * @return An ObservableList of all bookings.
   */
  ObservableList<Booking> getBookings();

  /**
   * Loads all bookings from the database.
   */
  void loadBookings();

  /**
   * Retrieves a booking for a specific event.
   * 
   * @param eventId The ID of the event.
   * @return The Booking object if found, null otherwise.
   * @throws SQLException If a database access error occurs.
   */
  Booking getBookingForEvent(Integer eventId) throws SQLException;

  /**
   * Checks if a venue is available for a given time period.
   * 
   * @param venueName The name of the venue.
   * @param startDate The start date and time.
   * @param endDate   The end date and time.
   * @return True if the venue is available, false otherwise.
   * @throws SQLException If a database access error occurs.
   */
  boolean isVenueAvailable(String venueName, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;

  /**
   * Creates a new booking.
   * 
   * @param event           The event to book.
   * @param venue           The venue to book.
   * @param bookingDateTime The date and time of the booking.
   * @throws SQLException If a database access error occurs.
   */
  void createBooking(Event event, Venue venue, LocalDateTime bookingDateTime) throws SQLException;

  /**
   * Deletes a booking for a specific event.
   * 
   * @param eventId The ID of the event associated with the booking.
   * @throws SQLException If a database access error occurs.
   */
  void deleteBooking(Integer eventId) throws SQLException;

  /**
   * Checks if a client has multiple bookings.
   * 
   * @param clientName The name of the client.
   * @return True if the client has multiple bookings, false otherwise.
   * @throws SQLException If a database access error occurs.
   */
  boolean hasMultipleBookings(String clientName) throws SQLException;

  /**
   * Calculates the commission for a booking.
   * 
   * @param totalPrice The total price of the booking.
   * @param clientName The name of the client.
   * @return The calculated commission.
   * @throws SQLException If a database access error occurs.
   */
  double calculateCommission(double totalPrice, String clientName) throws SQLException;
}
