package fp.assignments.assignment_2.controller;

import javafx.scene.control.Alert;

/**
 * Base controller class for handling common UI operations like showing alerts.
 * It provides methods for displaying different types of alerts to the user.
 */
public abstract class BaseController {
  /**
   * Displays an error alert.
   *
   * @param header  The header text of the alert.
   * @param content The content text of the alert.
   */
  protected void showError(String header, String content) {
    showAlert(Alert.AlertType.ERROR, header, content);
  }

  /**
   * Displays a success alert.
   *
   * @param header  The header text of the alert.
   * @param content The content text of the alert.
   */
  protected void showSuccess(String header, String content) {
    showAlert(Alert.AlertType.INFORMATION, header, content);
  }

  /**
   * Displays a confirmation alert.
   *
   * @param header    The header text of the alert.
   * @param content   The content text of the alert.
   * @param onConfirm A runnable to execute on confirmation.
   */
  protected void showConfirmation(String header, String content, Runnable onConfirm) {
    showAlert(Alert.AlertType.CONFIRMATION, header, content);
  }

  /**
   * Displays a warning alert.
   *
   * @param header  The header text of the alert.
   * @param content The content text of the alert.
   */
  protected void showWarning(String header, String content) {
    showAlert(Alert.AlertType.WARNING, header, content);
  }

  /**
   * Displays an info alert.
   *
   * @param header  The header text of the alert.
   * @param content The content text of the alert.
   */
  protected void showInfo(String header, String content) {
    showAlert(Alert.AlertType.INFORMATION, header, content);
  }

  /**
   * Displays a generic alert with the specified type, header, and content.
   *
   * @param type    The type of the alert.
   * @param header  The header text of the alert.
   * @param content The content text of the alert.
   */
  private void showAlert(Alert.AlertType type, String header, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(header);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
