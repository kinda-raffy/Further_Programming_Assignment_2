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

/**
 * Controller for the login screen.
 * Handles user authentication and navigation to other screens.
 */
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
      User user = ServiceProvider.use(sp -> sp.userService().authenticateUser(username, password));
      if (user != null) {
        ServiceProvider.run(sp -> sp.session().setCurrentUser(user));
        try {
          LMVMApplication.navigateToHome();
        } catch (IOException e) {
          errorLabel.setText("Error navigating to home: " + e.getMessage());
        }
      } else {
        errorLabel.setText("Invalid username or password");
      }
    } catch (SQLException e) {
      errorLabel.setText("Invalid username or password");
    }
  }

  @FXML
  private void handleManagerSignup() {
    try {
      LMVMApplication.navigateToManagerSignUp();
    } catch (IOException e) {
      errorLabel.setText("Error navigating to sign-up: " + e.getMessage());
    }
  }
}
