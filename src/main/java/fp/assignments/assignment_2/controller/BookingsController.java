package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.model.Booking;
import fp.assignments.assignment_2.model.Event;
import fp.assignments.assignment_2.service.BookingService;
import fp.assignments.assignment_2.service.EventService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class BookingsController extends BaseController {
  @FXML
  private TableView<Booking> bookingsTable;

  private final BookingService bookingService = BookingService.getInstance();
  private final EventService eventService = new EventService();
  private final ObservableList<Booking> bookingsList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    setupBookingsTable();
    bookingsTable.setItems(bookingService.getBookings());
  }

  private void setupBookingsTable() {
    // Create columns
    TableColumn<Booking, String> clientCol = new TableColumn<>("Client");
    TableColumn<Booking, String> titleCol = new TableColumn<>("Event Title");
    TableColumn<Booking, String> venueCol = new TableColumn<>("Venue Name");
    TableColumn<Booking, String> dateCol = new TableColumn<>("Date");
    TableColumn<Booking, String> commissionCol = new TableColumn<>("Commission");

    // Set column widths
    clientCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.2));
    titleCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.3));
    venueCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.2));
    dateCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.15));
    commissionCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.15));

    // Set cell value factories
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

    clientCol.setCellValueFactory(data -> {
      try {
        Event event = eventService.getEventById(data.getValue().eventId());
        return new SimpleStringProperty(event != null ? event.clientName() : "");
      } catch (SQLException e) {
        return new SimpleStringProperty("");
      }
    });

    titleCol.setCellValueFactory(data -> {
      try {
        Event event = eventService.getEventById(data.getValue().eventId());
        return new SimpleStringProperty(event != null ? event.title() : "");
      } catch (SQLException e) {
        return new SimpleStringProperty("");
      }
    });

    venueCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().venueName()));

    dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().startDate().format(formatter)));

    commissionCol
        .setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().commission())));

    // Add columns to table
    bookingsTable.getColumns().addAll(clientCol, titleCol, venueCol, dateCol, commissionCol);
    bookingsTable.setItems(bookingsList);
  }

  private void loadBookings() {
    bookingsList.clear();
  }

  public void refreshBookings() {
    loadBookings();
  }
}
