package fp.assignments.assignment_2.controller.home.detail;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.controller.form.EditUserFormController;
import fp.assignments.assignment_2.model.entity.User;
import fp.assignments.assignment_2.service.ServiceProvider;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.binding.Bindings;

/**
 * Controller for displaying and managing user details.
 */
public class UserDetailController extends BaseController {
  @FXML
  private Label titleLabel;
  @FXML
  private Label usernameLabel;
  @FXML
  private Label firstNameLabel;
  @FXML
  private Label lastNameLabel;
  @FXML
  private Label typeLabel;
  @FXML
  private Button deleteButton;
  @FXML
  private Button editButton;

  private User user;
  private Runnable onUserDeleted;
  private Runnable onUserUpdated;

  /**
   * Sets the user whose details are to be displayed and updates the UI.
   *
   * @param user The user to display.
   */
  public void setUser(User user) {
    this.user = user;
    updateLabels();
    updateDeleteButton();
    // Show edit button to managers or the user themselves.
    editButton.visibleProperty().bind(
        Bindings.createBooleanBinding(
            () -> {
              User currentUser = ServiceProvider.use(sp -> sp.session().getCurrentUser());
              return ServiceProvider.use(sp -> sp.session().isManager()) ||
                  (currentUser != null && user != null &&
                      currentUser.id().equals(user.id()));
            },
            new ObjectProperty[] { ServiceProvider.use(sp -> sp.session().currentUserProperty()) }));
    editButton.managedProperty().bind(editButton.visibleProperty());
  }

  /**
   * Sets a callback to be executed after a user is deleted.
   *
   * @param callback The runnable to execute.
   */
  public void setOnUserDeleted(Runnable callback) {
    this.onUserDeleted = callback;
  }

  /**
   * Sets a callback to be executed after a user is updated.
   *
   * @param callback The runnable to execute.
   */
  public void setOnUserUpdated(Runnable callback) {
    this.onUserUpdated = callback;
  }

  /**
   * Initialises the controller and sets up button visibility bindings.
   */
  @FXML
  public void initialize() {
    // Show delete button to only managers.
    deleteButton.visibleProperty().bind(
        Bindings.createBooleanBinding(
            () -> ServiceProvider.use(sp -> sp.session().isManager()),
            new ObjectProperty[] { ServiceProvider.use(sp -> sp.session().currentUserProperty()) }));
    deleteButton.managedProperty().bind(deleteButton.visibleProperty());
  }

  /**
   * Updates the labels with the current user's information.
   */
  private void updateLabels() {
    titleLabel.setText(user.getFullName());
    usernameLabel.setText(user.userName());
    firstNameLabel.setText(user.firstName());
    lastNameLabel.setText(user.lastName());
    typeLabel.setText(user.type());
  }

  /**
   * Updates the state of the delete button based on the current user.
   */
  private void updateDeleteButton() {
    User currentUser = ServiceProvider.use(sp -> sp.session().getCurrentUser());
    // Enable delete button if the user isn't themselves.
    deleteButton.setDisable(currentUser != null && currentUser.id().equals(user.id()));
  }

  /**
   * Handles the editing of the current user.
   */
  @FXML
  private void handleEdit() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fp/assignments/assignment_2/view/form/edit-user-form-view.fxml"));
      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setTitle("Edit User");
      stage.setScene(new Scene(loader.load(), 600, 500));

      EditUserFormController controller = loader.getController();
      controller.setUser(user);
      controller.setOnUserUpdated(() -> {
        if (onUserUpdated != null) {
          onUserUpdated.run();
        }
        try {
          User updatedUser = ServiceProvider.use(sp -> sp.userService().getUserById(user.id()));
          if (updatedUser != null) {
            setUser(updatedUser);
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });

      stage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the deletion of the current user.
   */
  @FXML
  private void handleDelete() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Delete User");
    alert.setHeaderText("Delete User");
    alert.setContentText("Are you sure you want to delete this user?");

    if (alert.showAndWait().orElse(null) == ButtonType.OK) {
      try {
        ServiceProvider.run(sp -> sp.userService().deleteUser(user.id()));

        if (onUserDeleted != null) {
          onUserDeleted.run();
        }
      } catch (SQLException e) {
        e.printStackTrace();
        showError("Failed to delete user", e.getMessage());
      }
    }
  }

  /**
   * Navigates back to the previous screen.
   */
  @FXML
  private void goBack() {
    LMVMApplication.goBack();
  }
}
