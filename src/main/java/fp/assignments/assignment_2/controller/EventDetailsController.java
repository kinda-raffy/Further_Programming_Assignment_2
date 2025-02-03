package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.model.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.time.format.DateTimeFormatter;

public class EventDetailsController extends BaseController {
  @FXML
  private Label clientLabel;
  @FXML
  private Label titleLabel;
  @FXML
  private Label artistLabel;
  @FXML
  private Label dateLabel;
  @FXML
  private Label attendanceLabel;
  @FXML
  private Label typeLabel;
  @FXML
  private Label categoryLabel;
  @FXML
  private Label noBookingLabel;

  public void setEvent(Event event) {
    titleLabel.setText(event.title());
    typeLabel.setText(event.eventType());
    clientLabel.setText(event.clientName());
    artistLabel.setText(event.mainArtist());
    categoryLabel.setText(event.category());
    attendanceLabel.setText(String.valueOf(event.expectedAttendance()));
    dateLabel.setText(event.eventDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

    noBookingLabel.setVisible(!event.isBooked());
  }

  @FXML
  private void handleReassignBooking() {
  }

  public void goBack() {
    LMVMApplication.goBack();
  }
}
