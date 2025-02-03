package fp.assignments.assignment_2.model;

import java.time.LocalDateTime;

public record Booking(Integer id, Integer eventId, String venueName, LocalDateTime startDate,
        LocalDateTime endDate, double totalPrice, double commission) {
}
