package fp.assignments.assignment_2;

import fp.assignments.assignment_2.model.Event;
import fp.assignments.assignment_2.model.Venue;
import fp.assignments.assignment_2.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.stage.FileChooser;
import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TableCell;
import javafx.beans.property.SimpleObjectProperty;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.sql.Date;
import java.sql.Time;

public class MainController {
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

    private DatabaseService dbService;

    @FXML
    private void initialize() {
        dbService = DatabaseService.getInstance();
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

        // Format the price column
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
        // Initialize charts with empty data
        venueUtilizationChart.setTitle("Venue Utilization");
        incomeChart.setTitle("Income vs Commission per Order");
    }

    private void loadData() {
        // Clear existing data
        eventsTable.getItems().clear();
        venuesTable.getItems().clear();

        try {
            Connection conn = dbService.getConnection();

            // Load venues
            String venueSQL = "SELECT * FROM venues";
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(venueSQL)) {
                while (rs.next()) {
                    Venue venue = new Venue(
                            rs.getString("name"),
                            rs.getInt("capacity"),
                            rs.getString("suitability_keywords"),
                            rs.getString("category"),
                            rs.getDouble("hire_price"));
                    venuesTable.getItems().add(venue);
                }
            }

            // Load events
            String eventSQL = "SELECT * FROM events";
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(eventSQL)) {
                while (rs.next()) {
                    Event event = new Event(
                            rs.getString("client_id"),
                            rs.getString("name"),
                            rs.getString("main_artist"),
                            new Date(rs.getLong("event_date")).toLocalDate(),
                            new Time(rs.getLong("event_time")).toLocalTime(),
                            rs.getInt("expected_attendance"),
                            rs.getString("event_type"),
                            rs.getString("category"));
                    eventsTable.getItems().add(event);
                }
            }
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
            importVenues(file);
        }
    }

    @FXML
    private void handleImportEvents() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            importEvents(file);
        }
    }

    private void importVenues(File file) {
        try (Scanner scanner = new Scanner(file)) {
            // Skip header
            if (scanner.hasNextLine())
                scanner.nextLine();

            Connection conn = dbService.getConnection();
            String sql = "INSERT INTO venues (name, capacity, suitability_keywords, category, hire_price) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                while (scanner.hasNextLine()) {
                    String[] data = scanner.nextLine().split(",");
                    String priceStr = data[4].trim().replace("$", "").replace(" / hour", "");

                    pstmt.setString(1, data[0].trim());
                    pstmt.setInt(2, Integer.parseInt(data[1].trim()));
                    pstmt.setString(3, data[2].trim());
                    pstmt.setString(4, data[3].trim());
                    pstmt.setDouble(5, Double.parseDouble(priceStr));
                    pstmt.executeUpdate();
                }
            }
            loadData(); // Refresh the tables
        } catch (Exception e) {
            showError("Error importing venues", e.getMessage());
        }
    }

    private void importEvents(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Skip header
            reader.readLine();

            Connection conn = dbService.getConnection();
            String sql = "INSERT INTO events (client_id, name, main_artist, expected_attendance, event_date, event_time, event_type, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                    // Match EXACTLY what's in your CSV
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d-M-yy"); // for "20-12-24"
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US); // for "8:00 PM"

                    LocalDate date = LocalDate.parse(data[3].trim(), dateFormatter);
                    LocalTime time = LocalTime.parse(data[4].trim(), timeFormatter);

                    pstmt.setString(1, data[0].trim());
                    pstmt.setString(2, data[1].trim());
                    pstmt.setString(3, data[2].trim());
                    pstmt.setInt(4, Integer.parseInt(data[5].trim()));
                    pstmt.setDate(5, java.sql.Date.valueOf(date));
                    pstmt.setTime(6, java.sql.Time.valueOf(time));
                    pstmt.setString(7, data[6].trim());
                    pstmt.setString(8, data[7].trim());
                    pstmt.executeUpdate();
                }
            }
            loadData(); // Refresh the tables
        } catch (IOException e) {
            showError("Error reading file", e.getMessage());
        } catch (DateTimeParseException e) {
            showError("Error parsing date",
                    "Please ensure dates are in format DD-MM-YY and times in HH:MM AM/PM: " + e);
        } catch (SQLException e) {
            showError("Error saving to database", e.getMessage());
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