package fp.assignments.assignment_2.model;

import java.io.Serializable;
import java.util.List;

public record TransactionData(
    List<Event> events,
    List<Venue> venues,
    List<Booking> bookings) implements Serializable {
  private static final long serialVersionUID = 1L;
}