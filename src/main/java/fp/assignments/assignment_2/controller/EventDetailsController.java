package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.model.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;

public class EventDetailsController {
  @FXML
  private Label clientLabel;
  @FXML
  private Label titleLabel;
  @FXML
  private Label artistLabel;
  @FXML
  private Label dateLabel;
  @FXML
  private Label timeLabel;
  @FXML
  private Label attendanceLabel;
  @FXML
  private Label typeLabel;
  @FXML
  private Label categoryLabel;

  public void setEvent(Event event) {
    clientLabel.setText(event.getClientName());
    titleLabel.setText(event.getName());
    artistLabel.setText(event.getMainArtist());
    dateLabel.setText(event.getEventDate().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
    timeLabel.setText(event.getEventTime().format(DateTimeFormatter.ofPattern("h:mm a")));
    attendanceLabel.setText(String.valueOf(event.getExpectedAttendance()));
    typeLabel.setText(event.getEventType());
    categoryLabel.setText(event.getCategory());
  }
}