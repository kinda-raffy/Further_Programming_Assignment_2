package fp.assignments.assignment_2.controller.form;

import fp.assignments.assignment_2.model.entity.User;
import fp.assignments.assignment_2.service.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.beans.binding.Bindings;
import fp.assignments.assignment_2.service.SessionManager;

public class EditUserFormController {
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
  private VBox typeContainer;

  private User user;
  private Runnable onUserUpdated;

  public void setUser(User user) {
    this.user = user;
    populateFields();
  }

  public void setOnUserUpdated(Runnable callback) {
    this.onUserUpdated = callback;
  }

  @FXML
  public void initialize() {
    typeComboBox.getItems().addAll("manager", "staff");

    typeContainer.visibleProperty().bind(
        Bindings.createBooleanBinding(
            () -> SessionManager.getInstance().isManager(),
            SessionManager.getInstance().currentUserProperty()));
    typeContainer.managedProperty().bind(typeContainer.visibleProperty());
  }

  private void populateFields() {
    userNameField.setText(user.userName());
    firstNameField.setText(user.firstName());
    lastNameField.setText(user.lastName());
    typeComboBox.setValue(user.type());
  }

  @FXML
  private void handleSubmit() {
    if (validateFields()) {
      try {
        String sql = """
            UPDATE users
            SET user_name = ?, first_name = ?, last_name = ?, type = ?
            WHERE id = ?
            """;

        if (!passwordField.getText().isEmpty()) {
          sql = """
              UPDATE users
              SET user_name = ?, password = ?, first_name = ?, last_name = ?, type = ?
              WHERE id = ?
              """;
        }

        DatabaseConnection.getInstance().executeUpdate(sql, ps -> {
          ps.setString(1, userNameField.getText());
          if (!passwordField.getText().isEmpty()) {
            ps.setString(2, passwordField.getText());
            ps.setString(3, firstNameField.getText());
            ps.setString(4, lastNameField.getText());
            ps.setString(5, typeComboBox.getValue());
            ps.setInt(6, user.id());
          } else {
            ps.setString(2, firstNameField.getText());
            ps.setString(3, lastNameField.getText());
            ps.setString(4, typeComboBox.getValue());
            ps.setInt(5, user.id());
          }
        });

        if (onUserUpdated != null) {
          onUserUpdated.run();
        }
        closeWindow();
      } catch (SQLException e) {
        errorLabel.setText("Error updating user: " + e.getMessage());
      }
    }
  }

  private boolean validateFields() {
    if (userNameField.getText().isEmpty() ||
        firstNameField.getText().isEmpty() ||
        lastNameField.getText().isEmpty()) {
      errorLabel.setText("Username, First Name, and Last Name are required");
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
