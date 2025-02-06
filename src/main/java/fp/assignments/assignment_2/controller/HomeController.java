package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.service.BookingService;
import fp.assignments.assignment_2.service.HomeService;
import fp.assignments.assignment_2.service.SessionManager;
import fp.assignments.assignment_2.service.BackupService;
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
                AllVenueController.reloadVenues();
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
                BacklogController.reloadEvents();
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

    @FXML
    private void handleTransactionExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("LMVM Files", "*.lmvm"));
        fileChooser.setInitialFileName("Transaction_Data");
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            try {
                BackupService.getInstance().exportTransactionData(file);
            } catch (Exception e) {
                showError("Error exporting transaction data", e.getMessage());
            }
        }
    }

    @FXML
    private void handleTransactionImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("LMVM Files", "*.lmvm"));
        File file = fileChooser.showOpenDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            try {
                BackupService.getInstance().importTransactionData(file);
                AllVenueController.reloadVenues();
                BookingService.getInstance().loadBookings();
                BacklogController.reloadEvents();
            } catch (Exception e) {
                showError("Error importing transaction data", e.getMessage());
            }
        }
    }

    @FXML
    private void handleMasterExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("LMVM Files", "*.lmvm"));
        fileChooser.setInitialFileName("Master_Data");
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            try {
                BackupService.getInstance().exportMasterData(file);
            } catch (Exception e) {
                showError("Error exporting master data", e.getMessage());
            }
        }
    }

    @FXML
    private void handleMasterImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("LMVM Files", "*.lmvm"));
        File file = fileChooser.showOpenDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            try {
                BackupService.getInstance().importMasterData(file);
                AllUserController.loadUsers();
            } catch (Exception e) {
                showError("Error importing master data", e.getMessage());
            }
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        try {
            LMVMApplication.navigateToLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}