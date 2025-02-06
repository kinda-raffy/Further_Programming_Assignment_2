package fp.assignments.assignment_2.controller.home.pane;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.service.BookingService;
import fp.assignments.assignment_2.service.EventService;
import fp.assignments.assignment_2.service.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.chart.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import javafx.beans.binding.Bindings;

public class BookingsController extends BaseController {
  @FXML
  private TableView<Booking> bookingsTable;
  @FXML
  private PieChart venueUtilisationChart;
  @FXML
  private BarChart<String, Number> incomeChart;
  @FXML
  private TableView<Map.Entry<String, Double>> commissionTable;
  @FXML
  private TableColumn<Map.Entry<String, Double>, String> clientColumn;
  @FXML
  private TableColumn<Map.Entry<String, Double>, String> totalCommissionColumn;
  @FXML
  private Label totalCommissionLabel;
  @FXML
  private VBox summarySection;
  @FXML
  private TableColumn<Booking, String> commissionCol;

  private final BookingService bookingService = BookingService.getInstance();
  private final EventService eventService = new EventService();
  private final ObservableList<Booking> bookingsList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    setupBookingsTable();
    setupCommissionTable();
    bookingsTable.setItems(bookingService.getBookings());

    summarySection.visibleProperty().bind(
        Bindings.createBooleanBinding(
            () -> SessionManager.getInstance().isManager(),
            SessionManager.getInstance().currentUserProperty()));
    summarySection.managedProperty().bind(summarySection.visibleProperty());

    commissionCol.visibleProperty().bind(
        Bindings.createBooleanBinding(
            () -> SessionManager.getInstance().isManager(),
            SessionManager.getInstance().currentUserProperty()));

    bookingService.getBookings().addListener((javafx.collections.ListChangeListener<Booking>) c -> {
      updateCharts();
    });

    updateCharts();
  }

  private void setupBookingsTable() {
    TableColumn<Booking, String> clientCol = new TableColumn<>("Client");
    TableColumn<Booking, String> titleCol = new TableColumn<>("Event Title");
    TableColumn<Booking, String> venueCol = new TableColumn<>("Venue Name");
    TableColumn<Booking, String> dateCol = new TableColumn<>("Date");
    commissionCol = new TableColumn<>("Commission");

    clientCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.2));
    titleCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.3));
    venueCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.2));
    dateCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.15));
    commissionCol.prefWidthProperty().bind(bookingsTable.widthProperty().multiply(0.15));

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

    bookingsTable.getColumns().addAll(clientCol, titleCol, venueCol, dateCol, commissionCol);
    bookingsTable.setItems(bookingsList);

    bookingsTable.setRowFactory(tv -> {
      TableRow<Booking> row = new TableRow<>();
      row.setOnMouseClicked(action -> {
        if (action.getButton() == MouseButton.PRIMARY && action.getClickCount() == 2 && !row.isEmpty()) {
          try {
            Event event = eventService.getEventById(row.getItem().eventId());
            if (event != null) {
              LMVMApplication.navigateToEventDetails(event);
            }
          } catch (SQLException | IOException e) {
            showError("Error", "Could not open event details: " + e.getMessage());
          }
        }
      });
      return row;
    });
  }

  private void setupCommissionTable() {
    clientColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
    totalCommissionColumn
        .setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getValue())));

    clientColumn.prefWidthProperty().bind(commissionTable.widthProperty().multiply(0.75));
    totalCommissionColumn.prefWidthProperty().bind(commissionTable.widthProperty().multiply(0.25));
  }

  private void updateCharts() {
    updateVenueUtilisationChart();
    updateIncomeChart();
    updateCommissionTable();
  }

  private void updateVenueUtilisationChart() {
    Map<String, Long> venueCount = bookingService.getBookings().stream()
        .collect(Collectors.groupingBy(Booking::venueName, Collectors.counting()));

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    venueCount.forEach((venue, count) -> pieChartData.add(new PieChart.Data(venue, count)));

    venueUtilisationChart.setData(pieChartData);
  }

  private void updateIncomeChart() {
    XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
    XYChart.Series<String, Number> commissionSeries = new XYChart.Series<>();
    incomeSeries.setName("Income $");
    commissionSeries.setName("Commission $");

    bookingService.getBookings().forEach(booking -> {
      try {
        Event event = eventService.getEventById(booking.eventId());
        if (event != null) {
          String truncatedTitle = event.title().length() > 20
              ? event.title().substring(0, 17) + "..."
              : event.title();

          incomeSeries.getData().add(new XYChart.Data<>(truncatedTitle, booking.totalPrice()));
          commissionSeries.getData().add(new XYChart.Data<>(truncatedTitle, booking.commission()));
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    incomeChart.getData().clear();
    incomeChart.getData().addAll(incomeSeries, commissionSeries);
  }

  private void updateCommissionTable() {
    Map<String, Double> clientCommissions = bookingService.getBookings().stream()
        .collect(Collectors.groupingBy(
            booking -> {
              try {
                Event event = eventService.getEventById(booking.eventId());
                return event != null ? event.clientName() : "Unknown";
              } catch (SQLException e) {
                return "Unknown";
              }
            },
            Collectors.summingDouble(Booking::commission)));

    commissionTable.setItems(FXCollections.observableArrayList(clientCommissions.entrySet()));

    double totalCommission = clientCommissions.values().stream().mapToDouble(Double::doubleValue).sum();
    totalCommissionLabel.setText(String.format("Total Commission: $%.2f", totalCommission));
  }

  private void loadBookings() {
    bookingsList.clear();
  }

  public void refreshBookings() {
    loadBookings();
  }
}
