package fp.assignments.assignment_2.model;

import java.time.LocalDateTime;

public record Event(Integer id, String title, String mainArtist, int expectedAttendance,
    LocalDateTime eventDateTime, int duration, String eventType, String category,
    String clientName) {
}
