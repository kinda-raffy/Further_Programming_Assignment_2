package fp.assignments.assignment_2.controller.home;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.controller.home.pane.AllUserController;
import fp.assignments.assignment_2.controller.home.pane.AllVenueController;
import fp.assignments.assignment_2.controller.home.pane.BacklogController;
import fp.assignments.assignment_2.service.ServiceProvider;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.*;
import java.time.format.DateTimeParseException;
import javafx.beans.binding.Bindings;

public class HomeController extends BaseController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    private Menu backupMenu;

    @FXML
    public void initialize() {

        backupMenu.visibleProperty().bind(
                Bindings.createBooleanBinding(
                        () -> ServiceProvider.use(sp -> sp.session().isManager()),
                        new ObjectProperty[] { ServiceProvider.use(sp -> sp.session().currentUserProperty()) }));
    }

    @FXML
    private void handleImportVenues() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            try {
                ServiceProvider.run(sp -> sp.csvImporter().importVenueCSV(file));
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
                ServiceProvider.run(sp -> sp.csvImporter().importEventCSV(file));
                BacklogController.reloadEvents();
            } catch (IOException e) {
                showError("Error reading file", e.getMessage());
            } catch (DateTimeParseException e) {
                showError("Error parsing date",
                        "Please ensure dates are in format DD-MM-YY and times in HH:MM AM/PM: " + e);
            } catch (Exception e) {
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
            ServiceProvider.run(sp -> {
                try {
                    sp.backupService().exportTransactionData(file);
                } catch (Exception e) {
                    showError("Error exporting transaction data", e.getMessage());
                }
            });
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
                ServiceProvider.run(sp -> sp.backupService().importTransactionData(file));
                AllVenueController.reloadVenues();
                ServiceProvider.run(sp -> sp.bookingService().loadBookings());
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
                ServiceProvider.run(sp -> sp.backupService().exportMasterData(file));
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
                ServiceProvider.run(sp -> sp.backupService().importMasterData(file));
                AllUserController.loadUsers();
            } catch (Exception e) {
                showError("Error importing master data", e.getMessage());
            }
        }
    }

    @FXML
    private void handleLogout() {
        ServiceProvider.run(sp -> sp.session().logout());
        try {
            LMVMApplication.navigateToLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}