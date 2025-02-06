package fp.assignments.assignment_2.controller.form;

import fp.assignments.assignment_2.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import fp.assignments.assignment_2.service.ServiceProvider;

public class AddUserFormController {
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

  private Runnable onUserAdded;

  public void setOnUserAdded(Runnable callback) {
    this.onUserAdded = callback;
  }

  @FXML
  public void initialize() {
    typeComboBox.getItems().addAll("manager", "staff");
    typeComboBox.setValue("staff");
  }

  @FXML
  private void handleSubmit() {
    if (validateFields()) {
      try {
        User newUser = ServiceProvider.use(sp -> sp.userService().createUser(
            userNameField.getText(),
            passwordField.getText(),
            firstNameField.getText(),
            lastNameField.getText(),
            typeComboBox.getValue()));

        if (newUser != null && onUserAdded != null) {
          onUserAdded.run();
        }
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
