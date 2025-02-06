package fp.assignments.assignment_2.model.backup;

import java.io.Serializable;
import java.util.List;

import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.Venue;

public record TransactionData(
    List<Event> events,
    List<Venue> venues,
    List<Booking> bookings) implements Serializable {
  private static final long serialVersionUID = 1L;
}