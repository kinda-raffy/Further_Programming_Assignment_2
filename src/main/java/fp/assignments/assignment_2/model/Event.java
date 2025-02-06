package fp.assignments.assignment_2.model;

import java.time.LocalDateTime;
import java.io.Serializable;

public record Event(Integer id, String title, String mainArtist, int expectedAttendance,
        LocalDateTime eventDateTime, int durationHours, String eventType, String category,
        String clientName) implements Serializable {
    private static final long serialVersionUID = 1L;
}
