package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.model.Venue;
import fp.assignments.assignment_2.service.HomeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AllVenueController extends BaseController {
  @FXML
  private TableView<Venue> venuesTable;
  @FXML
  private TextField nameSearchField;
  @FXML
  private TextField categorySearchField;

  private static HomeService homeService = new HomeService();
  private static ObservableList<Venue> venuesList = FXCollections.observableArrayList();
  private static ObservableList<Venue> filteredVenuesList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    venuesTable.setItems(filteredVenuesList);
    setupVenuesTable();
    setupSearchListeners();
    loadAllVenues();
  }

  private void setupSearchListeners() {
    nameSearchField.textProperty().addListener((observable, oldValue, newValue) -> filterVenues());
    categorySearchField.textProperty().addListener((observable, oldValue, newValue) -> filterVenues());
  }

  private void filterVenues() {
    String nameQuery = nameSearchField.getText().toLowerCase();
    String categoryQuery = categorySearchField.getText().toLowerCase();

    filteredVenuesList.clear();

    if (nameQuery.isEmpty() && categoryQuery.isEmpty()) {
      filteredVenuesList.addAll(venuesList);
    } else {
      for (Venue venue : venuesList) {
        boolean nameMatch = nameQuery.isEmpty() ||
            venue.nameId().toLowerCase().contains(nameQuery);
        boolean categoryMatch = categoryQuery.isEmpty() ||
            venue.category().toLowerCase().contains(categoryQuery);

        if (nameMatch && categoryMatch) {
          filteredVenuesList.add(venue);
        }
      }
    }

    if (filteredVenuesList.isEmpty()) {
      venuesTable.setPlaceholder(new Label("No venues found"));
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
        data -> new SimpleStringProperty(String.join(" & ", data.getValue().suitabilityKeywords())));
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

  public static void loadAllVenues() {
    venuesList.clear();
    filteredVenuesList.clear();
    try {
      venuesList.addAll(homeService.loadVenues());
      filteredVenuesList.addAll(homeService.loadVenues());
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
}
