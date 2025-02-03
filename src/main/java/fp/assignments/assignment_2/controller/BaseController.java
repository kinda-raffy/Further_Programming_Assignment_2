package fp.assignments.assignment_2.controller;

import javafx.scene.control.Alert;

public abstract class BaseController {
  protected void showError(String header, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
