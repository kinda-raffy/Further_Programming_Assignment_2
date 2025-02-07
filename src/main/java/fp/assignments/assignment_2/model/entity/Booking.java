package fp.assignments.assignment_2.model.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Represents a booking record.
 * 
 * @param id         The unique identifier of the booking.
 * @param eventId    The ID of the event associated with the booking.
 * @param venueName  The name of the venue where the event is booked.
 * @param startDate  The start date and time of the booking.
 * @param endDate    The end date and time of the booking.
 * @param totalPrice The total price of the booking.
 * @param commission The commission earned from the booking.
 */
public record Booking(Integer id, Integer eventId, String venueName, LocalDateTime startDate,
                LocalDateTime endDate, double totalPrice, double commission) implements Serializable {
        private static final long serialVersionUID = 1L;
}
