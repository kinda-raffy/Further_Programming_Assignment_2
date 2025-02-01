package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.model.Event;
import fp.assignments.assignment_2.model.Venue;
import fp.assignments.assignment_2.service.DatabaseService;
import fp.assignments.assignment_2.service.HomeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.stage.FileChooser;
import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TableCell;
import javafx.beans.property.SimpleObjectProperty;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalTime;

public class HomeController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableView<Venue> venuesTable;

    @FXML
    private PieChart venueUtilizationChart;

    @FXML
    private BarChart<String, Number> incomeChart;

    private HomeService homeService;

    @FXML
    private void initialize() {
        homeService = new HomeService();
        setupTables();
        setupCharts();
        loadData();
    }

    private void setupTables() {
        // Setup events table
        TableColumn<Event, String> clientCol = new TableColumn<>("Client");
        TableColumn<Event, String> titleCol = new TableColumn<>("Title");
        TableColumn<Event, String> artistCol = new TableColumn<>("Artist");
        TableColumn<Event, LocalDate> dateCol = new TableColumn<>("Date");
        TableColumn<Event, LocalTime> timeCol = new TableColumn<>("Time");
        TableColumn<Event, Integer> attendanceCol = new TableColumn<>("Target Audience");
        TableColumn<Event, String> typeCol = new TableColumn<>("Type");
        TableColumn<Event, String> categoryCol = new TableColumn<>("Category");

        clientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClientName()));
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMainArtist()));
        dateCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEventDate()));
        timeCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEventTime()));
        attendanceCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getExpectedAttendance()).asObject());
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEventType()));
        categoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));

        // Format the date/time column
        dateCol.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        eventsTable.getColumns().addAll(clientCol, titleCol, artistCol, dateCol, timeCol,
                attendanceCol, typeCol, categoryCol);

        // Setup venues table
        TableColumn<Venue, String> nameCol = new TableColumn<>("Name");
        TableColumn<Venue, Integer> capacityCol = new TableColumn<>("Capacity");
        TableColumn<Venue, String> suitabilityCol = new TableColumn<>("Suitable for");
        TableColumn<Venue, String> venueCategoryCol = new TableColumn<>("Category");
        TableColumn<Venue, Double> priceCol = new TableColumn<>("Booking price / hour");

        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        capacityCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCapacity()).asObject());
        suitabilityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSuitabilityKeywords()));
        venueCategoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getHirePrice()).asObject());

        // Format the price column.
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

    private void setupCharts() {
        venueUtilizationChart.setTitle("Venue Utilization");
        incomeChart.setTitle("Income vs Commission per Order");
    }

    private void loadData() {
        eventsTable.getItems().clear();
        venuesTable.getItems().clear();

        try {
            venuesTable.getItems().addAll(homeService.loadVenues());
            eventsTable.getItems().addAll(homeService.loadEvents());
        } catch (SQLException e) {
            showError("Error loading data", e.getMessage());
        }
    }

    @FXML
    private void handleImportVenues() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            try {
                homeService.importVenues(file);
                loadData();
            } catch (Exception e) {
                showError("Error importing venues", e.getMessage());
            }
        }
    }

    @FXML
    private void handleImportEvents() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            try {
                homeService.importEvents(file);
                loadData();
            } catch (IOException e) {
                showError("Error reading file", e.getMessage());
            } catch (DateTimeParseException e) {
                showError("Error parsing date",
                        "Please ensure dates are in format DD-MM-YY and times in HH:MM AM/PM: " + e);
            } catch (SQLException e) {
                showError("Error saving to database", e.getMessage());
            }
        }
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleExportData() {
        // Export data logic
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAutoMatch() {
        // Auto-match logic
    }

    @FXML
    private void handleBackup() {
        // Backup logic
    }
}