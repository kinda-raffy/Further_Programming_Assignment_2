package fp.assignments.assignment_2.model.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Represents an event record.
 *
 * @param id                 The unique identifier of the event.
 * @param title              The title of the event.
 * @param mainArtist         The main artist performing at the event.
 * @param expectedAttendance The expected number of attendees.
 * @param eventDateTime      The date and time of the event.
 * @param durationHours      The duration of the event in hours.
 * @param eventType          The type of the event (e.g., concert, conference).
 * @param category           The category the event belongs to.
 * @param clientName         The name of the client hosting the event.
 */
public record Event(Integer id, String title, String mainArtist, int expectedAttendance,
        LocalDateTime eventDateTime, int durationHours, String eventType, String category,
        String clientName) implements Serializable {
    private static final long serialVersionUID = 1L;
}
