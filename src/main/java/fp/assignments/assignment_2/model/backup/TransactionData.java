package fp.assignments.assignment_2.model.backup;

import java.io.Serializable;
import java.util.List;

import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.Venue;

/**
 * A record to hold transactional data for backup, including events, venues, and
 * bookings.
 * 
 * @param events   The list of events.
 * @param venues   The list of venues.
 * @param bookings The list of bookings.
 */
public record TransactionData(
    List<Event> events,
    List<Venue> venues,
    List<Booking> bookings) implements Serializable {
  private static final long serialVersionUID = 1L;
}