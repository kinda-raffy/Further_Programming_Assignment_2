package fp.assignments.assignment_2.controller;

import javafx.scene.control.Alert;

public abstract class BaseController {
  protected void showError(String header, String content) {
    showAlert(Alert.AlertType.ERROR, header, content);
  }

  protected void showSuccess(String header, String content) {
    showAlert(Alert.AlertType.INFORMATION, header, content);
  }

  protected void showConfirmation(String header, String content, Runnable onConfirm) {
    showAlert(Alert.AlertType.CONFIRMATION, header, content);
  }

  protected void showWarning(String header, String content) {
    showAlert(Alert.AlertType.WARNING, header, content);
  }

  protected void showInfo(String header, String content) {
    showAlert(Alert.AlertType.INFORMATION, header, content);
  }

  private void showAlert(Alert.AlertType type, String header, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(header);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
