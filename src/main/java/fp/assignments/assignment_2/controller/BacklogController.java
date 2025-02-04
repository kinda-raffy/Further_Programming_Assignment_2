package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.model.Event;
import fp.assignments.assignment_2.service.HomeService;
import fp.assignments.assignment_2.LMVMApplication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BacklogController extends BaseController {
  @FXML
  private TableView<Event> eventsTable;

  private static HomeService homeService = new HomeService();
  private static ObservableList<Event> eventsList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    eventsTable.setItems(eventsList);
    setupEventsTable();
    loadEvents();
  }

  private void setupEventsTable() {
    // Create columns
    TableColumn<Event, String> clientCol = new TableColumn<>("Client");
    TableColumn<Event, String> titleCol = new TableColumn<>("Title");
    TableColumn<Event, String> artistCol = new TableColumn<>("Artist");
    TableColumn<Event, String> dateCol = new TableColumn<>("Date");

    // Set column widths to fill the table
    clientCol.prefWidthProperty().bind(eventsTable.widthProperty().multiply(0.27));
    titleCol.prefWidthProperty().bind(eventsTable.widthProperty().multiply(0.27));
    artistCol.prefWidthProperty().bind(eventsTable.widthProperty().multiply(0.27));
    dateCol.prefWidthProperty().bind(eventsTable.widthProperty().multiply(0.187));

    // Set cell value factories
    clientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().clientName()));
    titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().title()));
    artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().mainArtist()));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
    dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().eventDateTime().format(formatter)));

    // Add columns to table
    eventsTable.getColumns().addAll(clientCol, titleCol, artistCol, dateCol);

    // Add double-click handler
    eventsTable.setRowFactory(tv -> {
      TableRow<Event> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && !row.isEmpty()) {
          showEventDetails(row.getItem());
        }
      });
      return row;
    });
  }

  public static void loadEvents() {
    eventsList.clear();
    try {
      eventsList.addAll(homeService.loadEvents());
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
}
