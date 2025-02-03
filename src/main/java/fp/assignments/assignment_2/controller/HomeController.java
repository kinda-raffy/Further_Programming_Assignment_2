package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.service.HomeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.*;
import java.sql.*;
import java.time.format.DateTimeParseException;

public class HomeController extends BaseController {
    @FXML
    private TabPane mainTabPane;

    private HomeService homeService;

    @FXML
    public void initialize() {
        homeService = new HomeService();
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
                refreshAllControllers();
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
                refreshAllControllers();
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

    private void refreshAllControllers() {
        // Get references to child controllers and refresh their data
        for (Tab tab : mainTabPane.getTabs()) {
            if (tab.getContent() != null) {
                if (tab.getContent().getId().equals("backlogView")) {
                    ((BacklogController) tab.getContent().getUserData()).loadEvents();
                } else if (tab.getContent().getId().equals("venuesView")) {
                    ((AllVenueController) tab.getContent().getUserData()).loadVenues();
                }
            }
        }
    }

    @FXML
    private void handleExportData() {
        // Export data logic
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}