package fp.assignments.assignment_2.model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int eventId;
    private int venueId;
    private LocalDateTime bookingDateTime;
    private double totalPrice;
    private double commission;

    public Booking(int id, int eventId, int venueId, LocalDateTime bookingDateTime, double totalPrice,
            double commission) {
        this.id = id;
        this.eventId = eventId;
        this.venueId = venueId;
        this.bookingDateTime = bookingDateTime;
        this.totalPrice = totalPrice;
        this.commission = commission;
    }
}
