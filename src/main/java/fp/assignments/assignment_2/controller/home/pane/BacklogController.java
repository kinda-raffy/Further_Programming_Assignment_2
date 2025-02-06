package fp.assignments.assignment_2.controller.home.pane;

import fp.assignments.assignment_2.service.ServiceProvider;
import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.Venue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import java.util.List;
import java.util.stream.Collectors;

public class BacklogController extends BaseController {
  @FXML
  private TableView<Event> eventsTable;
  @FXML
  private TableView<VenueRecommendation> recommendedVenuesTable;
  @FXML
  private CheckBox availableCheck;
  @FXML
  private CheckBox eventTypeCheck;
  @FXML
  private CheckBox venueCategoryCheck;
  @FXML
  private CheckBox capacityCheck;

  private static ObservableList<Event> eventsList = FXCollections.observableArrayList();
  private Event selectedEvent;

  @FXML
  public void initialize() {
    eventsTable.setItems(eventsList);
    setupEventsTable();
    setupRecommendedVenuesTable();
    loadEvents();

    ServiceProvider.run(sp -> sp.bookingService().getBookings().addListener((ListChangeListener<Booking>) c -> {
      selectedEvent = null;
      recommendedVenuesTable.getItems().clear();
      eventsTable.getSelectionModel().clearSelection();
      loadEvents();
    }));

    // Add listeners for criteria changes
    availableCheck.selectedProperty().addListener((obs, old, newVal) -> updateRecommendations());
    eventTypeCheck.selectedProperty().addListener((obs, old, newVal) -> updateRecommendations());
    venueCategoryCheck.selectedProperty().addListener((obs, old, newVal) -> updateRecommendations());
    capacityCheck.selectedProperty().addListener((obs, old, newVal) -> updateRecommendations());
  }

  private void setupEventsTable() {
    // Create columns
    TableColumn<Event, String> clientCol = new TableColumn<>("Client");
    TableColumn<Event, String> titleCol = new TableColumn<>("Title");
    TableColumn<Event, String> artistCol = new TableColumn<>("Artist");
    TableColumn<Event, String> dateCol = new TableColumn<>("Date");

    clientCol.prefWidthProperty().bind(eventsTable.widthProperty().multiply(0.27));
    titleCol.prefWidthProperty().bind(eventsTable.widthProperty().multiply(0.27));
    artistCol.prefWidthProperty().bind(eventsTable.widthProperty().multiply(0.27));
    dateCol.prefWidthProperty().bind(eventsTable.widthProperty().multiply(0.187));

    clientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().clientName()));
    titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().title()));
    artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().mainArtist()));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
    dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().eventDateTime().format(formatter)));

    eventsTable.getColumns().addAll(clientCol, titleCol, artistCol, dateCol);

    eventsTable.setRowFactory(tv -> {
      TableRow<Event> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (!row.isEmpty()) {
          selectedEvent = row.getItem();
          if (event.getClickCount() == 2) {
            showEventDetails(selectedEvent);
          } else if (event.getClickCount() == 1) {
            updateRecommendations();
          }
        }
      });
      return row;
    });
  }

  private void setupRecommendedVenuesTable() {
    TableColumn<VenueRecommendation, String> nameCol = new TableColumn<>("Venue Name");
    TableColumn<VenueRecommendation, String> scoreCol = new TableColumn<>("Compatibility Score");

    nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().venue().nameId()));
    scoreCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().compatibilityScore() + "%"));

    nameCol.prefWidthProperty().bind(recommendedVenuesTable.widthProperty().multiply(0.7));
    scoreCol.prefWidthProperty().bind(recommendedVenuesTable.widthProperty().multiply(0.3));

    recommendedVenuesTable.getColumns().addAll(nameCol, scoreCol);

    // Add double-click handler
    recommendedVenuesTable.setRowFactory(tv -> {
      TableRow<VenueRecommendation> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (!row.isEmpty() && event.getClickCount() == 2) {
          handleVenueSelection(row.getItem());
        }
      });
      return row;
    });
  }

  private void handleVenueSelection(VenueRecommendation recommendation) {
    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
    confirmDialog.setTitle("Create Booking");
    confirmDialog.setHeaderText("Create Booking");
    confirmDialog.setContentText(String.format("Are you sure you want to book %s for the event '%s'?",
        recommendation.venue().nameId(),
        selectedEvent.title()));

    confirmDialog.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        try {
          ServiceProvider.run(sp -> sp.bookingService().createBooking(selectedEvent, recommendation.venue(),
              selectedEvent.eventDateTime()));
        } catch (SQLException e) {
          showError("Error", "Could not create booking: " + e.getMessage());
        }
      }
    });
  }

  public static void loadEvents() {
    eventsList.clear();
    try {
      List<Event> allEvents = ServiceProvider.use(sp -> sp.eventService().getEvents());

      for (Event event : allEvents) {
        try {
          boolean hasBooking = ServiceProvider.use(sp -> sp.bookingService().getBookingForEvent(event.id()) != null);
          if (!hasBooking) {
            eventsList.add(event);
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    } catch (SQLException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error loading data");
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  private void showEventDetails(Event event) {
    try {
      LMVMApplication.navigateToEventDetails(event);
    } catch (IOException e) {
      showError("Error", "Could not open event details: " + e.getMessage());
    }
  }

  public static void reloadEvents() {
    loadEvents();
  }

  private void updateRecommendations() {
    if (selectedEvent == null)
      return;

    try {
      List<Venue> allVenues = ServiceProvider.use(sp -> sp.venueService().getVenues());
      List<VenueRecommendation> recommendations = allVenues.stream()
          .map(venue -> new VenueRecommendation(venue, calculateCompatibility(venue)))
          .filter(recommendation -> recommendation.compatibilityScore() > 0)
          .sorted((a, b) -> b.compatibilityScore() - a.compatibilityScore())
          .collect(Collectors.toList());

      recommendedVenuesTable.setItems(FXCollections.observableArrayList(recommendations));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private int calculateCompatibility(Venue venue) {
    int enabledCriteria = 0;
    int metCriteria = 0;

    if (availableCheck.isSelected()) {
      enabledCriteria++;
      try {
        boolean isAvailable = ServiceProvider.use(sp -> sp.bookingService().isVenueAvailable(venue.nameId(),
            selectedEvent.eventDateTime(),
            selectedEvent.eventDateTime().plusHours(selectedEvent.durationHours())));
        if (isAvailable) {
          metCriteria++;
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    if (eventTypeCheck.isSelected()) {
      enabledCriteria++;
      if (venue.isSuitable(selectedEvent.eventType())) {
        metCriteria++;
      }
    }

    if (venueCategoryCheck.isSelected()) {
      enabledCriteria++;
      if (venue.category().equals(selectedEvent.category())) {
        metCriteria++;
      }
    }

    if (capacityCheck.isSelected()) {
      enabledCriteria++;
      if (venue.capacity() >= selectedEvent.expectedAttendance()) {
        metCriteria++;
      }
    }

    return enabledCriteria > 0 ? (metCriteria * 100) / enabledCriteria : 0;
  }

  private record VenueRecommendation(Venue venue, int compatibilityScore) {
  }
}
