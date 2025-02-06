package fp.assignments.assignment_2.controller.form;

import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.controller.home.pane.AllVenueController;
import fp.assignments.assignment_2.service.VenueService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class AddVenueFormController extends BaseController {
  @FXML
  private TextField nameField;
  @FXML
  private TextField capacityField;
  @FXML
  private ComboBox<String> categoryField;
  @FXML
  private TextField hirePriceField;
  @FXML
  private TextField keywordsField;
  @FXML
  private Label errorLabel;

  private final VenueService venueService = new VenueService();

  @FXML
  public void initialize() {
    categoryField.getItems().addAll("Indoor", "Outdoor", "Convertible");
    categoryField.setValue("Indoor");
  }

  @FXML
  private void handleCreate() {
    if (validateFields()) {
      try {
        venueService.createVenue(
            nameField.getText(),
            Integer.parseInt(capacityField.getText()),
            keywordsField.getText(),
            categoryField.getValue(),
            Double.parseDouble(hirePriceField.getText()));
        AllVenueController.reloadVenues();
        closeWindow();
      } catch (SQLException e) {
        if (e.getMessage().contains("UNIQUE")) {
          errorLabel.setText("A venue with this name already exists");
        } else {
          errorLabel.setText("Error creating venue: " + e.getMessage());
        }
      }
    }
  }

  private boolean validateFields() {
    if (nameField.getText().isEmpty() || capacityField.getText().isEmpty() ||
        categoryField.getValue() == null || hirePriceField.getText().isEmpty() ||
        keywordsField.getText().isEmpty()) {
      errorLabel.setText("All fields are required");
      return false;
    }

    try {
      Integer.parseInt(capacityField.getText());
    } catch (NumberFormatException e) {
      errorLabel.setText("Capacity must be a number");
      return false;
    }

    try {
      Double.parseDouble(hirePriceField.getText());
    } catch (NumberFormatException e) {
      errorLabel.setText("Hire price must be a number");
      return false;
    }

    return true;
  }

  @FXML
  private void handleCancel() {
    closeWindow();
  }

  private void closeWindow() {
    ((Stage) nameField.getScene().getWindow()).close();
  }
}
