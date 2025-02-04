package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.service.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;

public class AddEmployeeFormController {
  @FXML
  private TextField userNameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private TextField firstNameField;
  @FXML
  private TextField lastNameField;
  @FXML
  private ComboBox<String> typeComboBox;
  @FXML
  private Label errorLabel;

  @FXML
  public void initialize() {
    typeComboBox.getItems().addAll("manager", "staff");
    typeComboBox.setValue("staff");
  }

  @FXML
  private void handleSubmit() {
    if (validateFields()) {
      try {
        String sql = """
            INSERT INTO users (user_name, password, first_name, last_name, type)
            VALUES (?, ?, ?, ?, ?)
            """;

        DatabaseConnection.getInstance().executeUpdate(sql, ps -> {
          ps.setString(1, userNameField.getText());
          ps.setString(2, passwordField.getText());
          ps.setString(3, firstNameField.getText());
          ps.setString(4, lastNameField.getText());
          ps.setString(5, typeComboBox.getValue());
        });

        closeWindow();
      } catch (SQLException e) {
        errorLabel.setText("Error creating user: " + e.getMessage());
      }
    }
  }

  private boolean validateFields() {
    if (userNameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
        firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
      errorLabel.setText("All fields are required");
      return false;
    }
    return true;
  }

  @FXML
  private void handleCancel() {
    closeWindow();
  }

  private void closeWindow() {
    ((Stage) userNameField.getScene().getWindow()).close();
  }
}
