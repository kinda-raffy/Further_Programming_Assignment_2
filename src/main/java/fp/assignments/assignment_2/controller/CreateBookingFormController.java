package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.model.Event;
import fp.assignments.assignment_2.model.Venue;
import fp.assignments.assignment_2.service.BookingService;
import fp.assignments.assignment_2.service.HomeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class CreateBookingFormController extends BaseController {
  @FXML
  private Label eventNameLabel;
  @FXML
  private Label eventDateTimeLabel;
  @FXML
  private Label eventDurationLabel;
  @FXML
  private ComboBox<Venue> venueComboBox;
  @FXML
  private Label venuePriceLabel;
  @FXML
  private Label errorLabel;
  @FXML
  private Label totalPriceLabel;
  @FXML
  private Label commissionLabel;

  private Event event;
  private final BookingService bookingService = BookingService.getInstance();
  private final HomeService homeService = new HomeService();

  @FXML
  public void initialize() {
    try {
      venueComboBox.getItems().addAll(homeService.loadVenues());
      venueComboBox.setCellFactory(param -> new ListCell<>() {
        @Override
        protected void updateItem(Venue venue, boolean empty) {
          super.updateItem(venue, empty);
          setText(empty ? "" : venue.nameId());
        }
      });
      venueComboBox.setButtonCell(venueComboBox.getCellFactory().call(null));
    } catch (SQLException e) {
      errorLabel.setText("Error loading venues");
    }

    venueComboBox.valueProperty().addListener((obs, oldVenue, newVenue) -> {
      if (newVenue != null) {
        venuePriceLabel.setText(String.format("Venue Price: $%.2f per hour", newVenue.hirePricePerHour()));
        updatePriceDetails(newVenue);
      }
    });
  }

  public void setEvent(Event event) {
    this.event = event;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    eventNameLabel.setText(event.title());
    eventDateTimeLabel.setText("Event Date/Time: " + event.eventDateTime().format(formatter));
    eventDurationLabel.setText("Duration: " + event.durationHours() + " hours");
  }

  @FXML
  private void handleCreateBooking() {
    if (validateForm()) {
      try {
        Venue selectedVenue = venueComboBox.getValue();
        bookingService.createBooking(event, selectedVenue, event.eventDateTime());
        closeDialog();
      } catch (SQLException e) {
        errorLabel.setText("Error creating booking: " + e.getMessage());
      }
    }
  }

  private boolean validateForm() {
    if (venueComboBox.getValue() == null) {
      errorLabel.setText("Please select a venue");
      return false;
    }
    return true;
  }

  @FXML
  private void handleCancel() {
    closeDialog();
  }

  private void closeDialog() {
    ((Stage) eventNameLabel.getScene().getWindow()).close();
  }

  private void updatePriceDetails(Venue venue) {
    double totalPrice = venue.hirePricePerHour() * event.durationHours();
    try {
      double commission = bookingService.calculateCommission(totalPrice, event.clientName());
      totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
      commissionLabel.setText(String.format("Commission: $%.2f (%d%%)",
          commission,
          bookingService.hasMultipleBookings(event.clientName()) ? 9 : 10));
    } catch (SQLException e) {
      errorLabel.setText("Error calculating commission: " + e.getMessage());
    }
  }
}
