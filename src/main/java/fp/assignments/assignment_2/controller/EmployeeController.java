package fp.assignments.assignment_2.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class EmployeeController extends BaseController {

  public void showAddEmployeeForm() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fp/assignments/assignment_2/view/add-employee-form-view.fxml"));
      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setTitle("Add Staff Member");
      stage.setScene(new Scene(loader.load(), 500, 600));
      stage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
