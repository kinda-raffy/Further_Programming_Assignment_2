package fp.assignments.assignment_2.controller.landing;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.model.entity.User;
import fp.assignments.assignment_2.service.DatabaseConnection;
import fp.assignments.assignment_2.service.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.ResultSet;
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
        String sql = """
            INSERT INTO users (user_name, password, first_name, last_name, type)
            VALUES (?, ?, ?, ?, 'manager')
            RETURNING *
            """;

        DatabaseConnection.getInstance().executeUpdate(sql, ps -> {
          ps.setString(1, userNameField.getText());
          ps.setString(2, passwordField.getText());
          ps.setString(3, firstNameField.getText());
          ps.setString(4, lastNameField.getText());

          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
            User newUser = new User(
                rs.getInt("id"),
                rs.getString("user_name"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                "manager");

            // Auto-login with new account
            SessionManager.getInstance().setCurrentUser(newUser);
            try {
              LMVMApplication.navigateToHome();
            } catch (IOException e) {
              errorLabel.setText("Error navigating to home: " + e.getMessage());
            }
          }
        });
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
