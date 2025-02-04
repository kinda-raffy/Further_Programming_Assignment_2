package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.model.User;
import fp.assignments.assignment_2.service.DatabaseConnection;
import fp.assignments.assignment_2.service.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController extends BaseController {
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Label errorLabel;

  @FXML
  private void handleLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      String sql = "SELECT * FROM users WHERE user_name = ? AND password = ?";
      DatabaseConnection.getInstance().executeUpdate(sql, ps -> {
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
          User user = new User(
              rs.getInt("id"),
              rs.getString("user_name"),
              rs.getString("password"),
              rs.getString("first_name"),
              rs.getString("last_name"),
              rs.getString("type"));

          SessionManager.getInstance().setCurrentUser(user);
          try {
            LMVMApplication.navigateToHome();
          } catch (IOException e) {
            errorLabel.setText("Error navigating to home: " + e.getMessage());
          }
        } else {
          errorLabel.setText("Invalid username or password");
        }
      });
    } catch (SQLException e) {
      errorLabel.setText("Invalid username or password");
    }
  }
}
