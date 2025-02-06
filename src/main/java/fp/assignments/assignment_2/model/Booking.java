package fp.assignments.assignment_2.model;

import java.time.LocalDateTime;
import java.io.Serializable;

public record Booking(Integer id, Integer eventId, String venueName, LocalDateTime startDate,
                LocalDateTime endDate, double totalPrice, double commission) implements Serializable {
        private static final long serialVersionUID = 1L;
}
