package fp.assignments.assignment_2.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
  private Integer id;
  private String name;
  private String mainArtist;
  private int expectedAttendance;
  private LocalDate eventDate;
  private LocalTime eventTime;
  private String eventType;
  private String category;
  private boolean isBooked;
  private String clientName;

  // Constructor
  public Event(String clientName, String name, String mainArtist, LocalDate eventDate,
      LocalTime eventTime, int expectedAttendance, String eventType, String category) {
    this.clientName = clientName;
    this.name = name;
    this.mainArtist = mainArtist;
    this.eventDate = eventDate;
    this.eventTime = eventTime;
    this.expectedAttendance = expectedAttendance;
    this.eventType = eventType;
    this.category = category;
    this.isBooked = false;
  }

  // Getters and setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getMainArtist() {
    return mainArtist;
  }

  public int getExpectedAttendance() {
    return expectedAttendance;
  }

  public LocalDate getEventDate() {
    return eventDate;
  }

  public LocalTime getEventTime() {
    return eventTime;
  }

  public String getEventType() {
    return eventType;
  }

  public String getCategory() {
    return category;
  }

  public boolean isBooked() {
    return isBooked;
  }

  public void setBooked(boolean booked) {
    isBooked = booked;
  }

  public String getClientName() {
    return clientName;
  }
}
