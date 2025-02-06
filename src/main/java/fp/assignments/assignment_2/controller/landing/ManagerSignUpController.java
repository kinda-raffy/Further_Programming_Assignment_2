package fp.assignments.assignment_2.controller.landing;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.model.entity.User;
import fp.assignments.assignment_2.service.ServiceProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;

public class ManagerSignUpController extends BaseController {
  @FXML
  private TextField userNameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private TextField firstNameField;
  @FXML
  private TextField lastNameField;
  @FXML
  private PasswordField pinField;
  @FXML
  private Label errorLabel;

  private static final String VALID_PIN = "909";

  @FXML
  private void handleCreate() {
    if (validateFields()) {
      try {
        User newUser = ServiceProvider.use(sp -> sp.userService().createUser(
            userNameField.getText(),
            passwordField.getText(),
            firstNameField.getText(),
            lastNameField.getText(),
            "manager"));

        if (newUser != null) {
          // Auto-login with new account
          ServiceProvider.run(sp -> sp.session().setCurrentUser(newUser));
          try {
            LMVMApplication.navigateToHome();
          } catch (IOException e) {
            errorLabel.setText("Error navigating to home: " + e.getMessage());
          }
        }
      } catch (SQLException e) {
        errorLabel.setText("That username is already taken");
      }
    }
  }

  private boolean validateFields() {
    if (userNameField.getText().isEmpty() ||
        passwordField.getText().isEmpty() ||
        firstNameField.getText().isEmpty() ||
        lastNameField.getText().isEmpty() ||
        pinField.getText().isEmpty()) {
      errorLabel.setText("All fields are required");
      return false;
    }

    if (!pinField.getText().equals(VALID_PIN)) {
      errorLabel.setText("Invalid authorisation pin");
      return false;
    }
    return true;
  }

  @FXML
  private void handleCancel() {
    try {
      LMVMApplication.navigateToLogin();
    } catch (IOException e) {
      errorLabel.setText("Error returning to login: " + e.getMessage());
    }
  }
}
