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
  private Label dateTimeLabel;
  @FXML
  private Label attendanceLabel;
  @FXML
  private Label typeLabel;
  @FXML
  private Label categoryLabel;

  public void setEvent(Event event) {
    titleLabel.setText(event.title());
    typeLabel.setText(event.eventType());
    clientLabel.setText(event.clientName());
    artistLabel.setText(event.mainArtist());
    categoryLabel.setText(event.category());
    attendanceLabel.setText(String.valueOf(event.expectedAttendance()));
    dateTimeLabel.setText(event.eventDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yy h:mm a")));
  }
}
