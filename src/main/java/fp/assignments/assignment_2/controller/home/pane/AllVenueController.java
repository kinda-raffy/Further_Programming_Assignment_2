package fp.assignments.assignment_2.controller.home.pane;

import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.model.entity.Venue;
import fp.assignments.assignment_2.service.ServiceProvider;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class AllVenueController extends BaseController {
  @FXML
  private TableView<Venue> venuesTable;
  @FXML
  private TextField nameSearchField;
  @FXML
  private TextField categorySearchField;
  @FXML
  private TextField capacitySearchField;
  @FXML
  private TextField keywordsSearchField;
  @FXML
  private DatePicker availabilityDatePicker;
  @FXML
  private TextField startTimeField;
  @FXML
  private TextField endTimeField;
  @FXML
  private Button deleteButton;

  private static ObservableList<Venue> venuesList = FXCollections.observableArrayList();
  private static ObservableList<Venue> filteredVenuesList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    venuesTable.setItems(filteredVenuesList);
    setupVenuesTable();
    setupSearchListeners();
    setupTableSelectionListener();
    loadAllVenues();
  }

  private void setupSearchListeners() {
    nameSearchField.textProperty().addListener((observable, oldValue, newValue) -> filterVenues());
    categorySearchField.textProperty().addListener((observable, oldValue, newValue) -> filterVenues());
    capacitySearchField.textProperty().addListener((observable, oldValue, newValue) -> filterVenues());
    keywordsSearchField.textProperty().addListener((observable, oldValue, newValue) -> filterVenues());
    availabilityDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterVenues());
    startTimeField.textProperty().addListener((observable, oldValue, newValue) -> filterVenues());
    endTimeField.textProperty().addListener((observable, oldValue, newValue) -> filterVenues());
  }

  private void filterVenues() {
    String nameQuery = nameSearchField.getText().toLowerCase();
    String categoryQuery = categorySearchField.getText().toLowerCase();
    String capacityQuery = capacitySearchField.getText();
    String keywordsQuery = keywordsSearchField.getText();

    filteredVenuesList.clear();

    for (Venue venue : venuesList) {
      if (matchesAllCriteria(venue, nameQuery, categoryQuery, capacityQuery, keywordsQuery)) {
        if (isAvailableForSelectedTime(venue)) {
          filteredVenuesList.add(venue);
        }
      }
    }

    if (filteredVenuesList.isEmpty()) {
      venuesTable.setPlaceholder(new Label("No venues found"));
    }
  }

  private boolean matchesAllCriteria(Venue venue, String nameQuery, String categoryQuery,
      String capacityQuery, String keywordsQuery) {
    boolean nameMatch = nameQuery.isEmpty() ||
        venue.nameId().toLowerCase().contains(nameQuery);
    boolean categoryMatch = categoryQuery.isEmpty() ||
        venue.category().toLowerCase().contains(categoryQuery);
    boolean capacityMatch = capacityQuery.isEmpty() ||
        String.valueOf(venue.capacity()).startsWith(capacityQuery);
    boolean keywordsMatch = keywordsQuery.isEmpty() || matchesKeywords(venue, keywordsQuery);

    return nameMatch && categoryMatch && capacityMatch && keywordsMatch;
  }

  private boolean matchesKeywords(Venue venue, String keywordsQuery) {
    String[] orGroups = keywordsQuery.split("\\|");

    return Arrays.stream(orGroups)
        .map(String::trim)
        .anyMatch(orGroup -> {
          List<String> andKeywords = Arrays.asList(orGroup.split("&"))
              .stream()
              .map(String::trim)
              .filter(k -> !k.isEmpty())
              .toList();

          return andKeywords.isEmpty() ||
              andKeywords.stream().allMatch(keyword -> venue.suitabilityKeywords().stream()
                  .anyMatch(suitability -> suitability.toLowerCase()
                      .contains(keyword.toLowerCase())));
        });
  }

  private boolean isAvailableForSelectedTime(Venue venue) {
    LocalDate selectedDate = availabilityDatePicker.getValue();
    String startTimeStr = startTimeField.getText();
    String endTimeStr = endTimeField.getText();

    // If no date/time criteria specified, consider venue available
    if (selectedDate == null || startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
      return true;
    }

    try {
      LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
      LocalTime endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"));

      LocalDateTime startDateTime = LocalDateTime.of(selectedDate, startTime);
      LocalDateTime endDateTime = LocalDateTime.of(selectedDate, endTime);

      return ServiceProvider.use(sp -> sp.bookingService().isVenueAvailable(venue.nameId(), startDateTime,
          endDateTime));
    } catch (DateTimeParseException e) {
      // If time format is invalid, ignore availability filter
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  private void setupVenuesTable() {
    TableColumn<Venue, String> nameCol = new TableColumn<>("Name");
    TableColumn<Venue, Integer> capacityCol = new TableColumn<>("Capacity");
    TableColumn<Venue, String> suitabilityCol = new TableColumn<>("Suitable for");
    TableColumn<Venue, String> venueCategoryCol = new TableColumn<>("Category");
    TableColumn<Venue, Double> priceCol = new TableColumn<>("Booking price / hour");

    nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nameId()));
    capacityCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().capacity()).asObject());
    suitabilityCol.setCellValueFactory(
        data -> new SimpleStringProperty(String.join(", ",
            data.getValue().suitabilityKeywords().stream()
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .toList())));
    venueCategoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().category()));
    priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().hirePricePerHour()).asObject());

    priceCol.setCellFactory(column -> new TableCell<>() {
      @Override
      protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(String.format("$%.2f", item));
        }
      }
    });

    venuesTable.getColumns().addAll(nameCol, capacityCol, suitabilityCol,
        venueCategoryCol, priceCol);

    nameCol.prefWidthProperty().bind(venuesTable.widthProperty().multiply(0.25));
    capacityCol.prefWidthProperty().bind(venuesTable.widthProperty().multiply(0.15));
    suitabilityCol.prefWidthProperty().bind(venuesTable.widthProperty().multiply(0.25));
    venueCategoryCol.prefWidthProperty().bind(venuesTable.widthProperty().multiply(0.20));
    priceCol.prefWidthProperty().bind(venuesTable.widthProperty().multiply(0.15));
  }

  private void setupTableSelectionListener() {
    venuesTable.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldSelection, newSelection) -> deleteButton.setDisable(newSelection == null));
  }

  public static void loadAllVenues() {
    venuesList.clear();
    filteredVenuesList.clear();
    try {
      ServiceProvider.run(sp -> {
        venuesList.addAll(sp.venueService().getVenues());
        filteredVenuesList.addAll(sp.venueService().getVenues());
      });
    } catch (SQLException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error loading data");
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public static void reloadVenues() {
    loadAllVenues();
  }

  @FXML
  private void handleCreateVenue() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fp/assignments/assignment_2/view/form/add-venue-form-view.fxml"));
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle("Create Venue");
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();
    } catch (IOException e) {
      showError("Error", "Could not open create venue form: " + e.getMessage());
    }
  }

  @FXML
  private void handleDeleteVenue() {
    Venue selectedVenue = venuesTable.getSelectionModel().getSelectedItem();
    if (selectedVenue != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Delete Venue");
      alert.setHeaderText("Delete " + selectedVenue.nameId());
      alert
          .setContentText("Are you sure you want to delete this venue? This will also delete all associated bookings.");

      if (alert.showAndWait().orElse(null) == ButtonType.OK) {
        try {
          ServiceProvider.run(sp -> sp.venueService().deleteVenue(selectedVenue.nameId()));
          ServiceProvider.run(sp -> sp.bookingService().loadBookings());
          reloadVenues();
        } catch (SQLException e) {
          showError("Error", "Could not delete venue: " + e.getMessage());
        }
      }
    }
  }
}
