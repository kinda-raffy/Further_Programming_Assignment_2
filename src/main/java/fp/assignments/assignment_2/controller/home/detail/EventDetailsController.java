package fp.assignments.assignment_2.controller.home.detail;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.controller.form.CreateBookingFormController;
import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.service.ServiceProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javafx.collections.ListChangeListener;

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
  @FXML
  private VBox bookingDetailsBox;
  @FXML
  private Label bookingVenueLabel;
  @FXML
  private Label bookingDateTimeLabel;
  @FXML
  private Label bookingTotalPriceLabel;
  @FXML
  private Label bookingCommissionLabel;
  @FXML
  private GridPane bookingDetailsGrid;
  @FXML
  private Button deleteBookingButton;
  private Event currentEvent;

  @FXML
  public void initialize() {
    ServiceProvider.run(sp -> sp.bookingService().getBookings().addListener((ListChangeListener<Booking>) c -> {
      if (currentEvent != null) {
        refreshBookingDetails();
      }
    }));
  }

  public void setEvent(Event event) {
    this.currentEvent = event;
    titleLabel.setText(event.title());
    typeLabel.setText(event.eventType());
    clientLabel.setText(event.clientName());
    artistLabel.setText(event.mainArtist());
    categoryLabel.setText(event.category());
    attendanceLabel.setText(String.valueOf(event.expectedAttendance()));
    dateLabel.setText(event.eventDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

    noBookingLabel.setVisible(false);
    refreshBookingDetails();
  }

  @FXML
  private void handleReassignBooking() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fp/assignments/assignment_2/view/form/create-booking-form-view.fxml"));
      VBox bookingForm = loader.load();

      CreateBookingFormController controller = loader.getController();
      controller.setEvent(currentEvent);

      Stage dialog = new Stage();
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.setTitle("Create Booking");
      dialog.setScene(new Scene(bookingForm));
      dialog.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void refreshBookingDetails() {
    try {
      Booking booking = ServiceProvider.use(sp -> sp.bookingService().getBookingForEvent(currentEvent.id()));
      if (booking != null) {
        noBookingLabel.setVisible(false);
        noBookingLabel.setManaged(false);

        bookingDetailsGrid.setManaged(true);
        bookingDetailsGrid.setVisible(true);

        deleteBookingButton.setVisible(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        bookingVenueLabel.setText(booking.venueName());
        bookingDateTimeLabel.setText(booking.startDate().format(formatter));
        bookingTotalPriceLabel.setText(String.format("$%.2f", booking.totalPrice()));
        bookingCommissionLabel.setText(String.format("$%.2f", booking.commission()));
      } else {
        noBookingLabel.setManaged(true);
        noBookingLabel.setVisible(true);

        bookingDetailsGrid.setVisible(false);
        bookingDetailsGrid.setManaged(false);

        deleteBookingButton.setVisible(false);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleDeleteBooking() {
    try {
      ServiceProvider.run(sp -> sp.bookingService().deleteBooking(currentEvent.id()));
      LMVMApplication.goBack();
    } catch (SQLException e) {
      showError("Error", "Could not delete booking: " + e.getMessage());
    }
  }

  public void goBack() {
    LMVMApplication.goBack();
  }
}
