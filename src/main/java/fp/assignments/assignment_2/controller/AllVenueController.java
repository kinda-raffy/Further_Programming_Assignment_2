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

  private static HomeService homeService = new HomeService();
  private static ObservableList<Venue> venuesList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    venuesTable.setItems(venuesList);
    setupVenuesTable();
    loadVenues();
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
        data -> new SimpleStringProperty(String.join("& ", data.getValue().suitabilityKeywords())));
    venueCategoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().category()));
    priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().hirePrice()).asObject());

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
  }

  public static void loadVenues() {
    venuesList.clear();
    try {
      venuesList.addAll(homeService.loadVenues());
    } catch (SQLException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error loading data");
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public static void reloadVenues() {
    loadVenues();
  }
}
