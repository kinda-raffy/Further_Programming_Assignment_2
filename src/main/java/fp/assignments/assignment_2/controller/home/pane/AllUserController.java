package fp.assignments.assignment_2.controller.home.pane;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.controller.BaseController;
import fp.assignments.assignment_2.controller.form.AddUserFormController;
import fp.assignments.assignment_2.model.User;
import fp.assignments.assignment_2.service.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.input.MouseButton;
import javafx.beans.binding.Bindings;
import fp.assignments.assignment_2.service.SessionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AllUserController extends BaseController {
  @FXML
  private TableView<User> userTable;
  private static ObservableList<User> userTableList = FXCollections.observableArrayList();

  @FXML
  private Label userNameLabel;

  @FXML
  private Button addStaffButton;

  @FXML
  public void initialize() {
    setupTableColumns();
    userTable.setItems(userTableList);
    loadUsers();

    userNameLabel.textProperty().bind(
        Bindings.createStringBinding(
            () -> {
              User user = SessionManager.getInstance().getCurrentUser();
              String userName = user != null ? user.userName() : "Anonymous";
              String accountType = user != null ? user.type() : "Unknown";
              return "Logged in as: " + userName + " (" + accountType + ")";
            },
            SessionManager.getInstance().currentUserProperty()));

    // Add visibility binding for the add staff button
    addStaffButton.visibleProperty().bind(
        Bindings.createBooleanBinding(
            () -> SessionManager.getInstance().isManager(),
            SessionManager.getInstance().currentUserProperty()));
    addStaffButton.managedProperty().bind(addStaffButton.visibleProperty());
  }

  private void setupTableColumns() {
    // Create columns
    TableColumn<User, Integer> idCol = new TableColumn<>("ID");
    TableColumn<User, String> usernameCol = new TableColumn<>("Username");
    TableColumn<User, String> firstNameCol = new TableColumn<>("First Name");
    TableColumn<User, String> lastNameCol = new TableColumn<>("Last Name");
    TableColumn<User, String> typeCol = new TableColumn<>("Type");

    idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().id()).asObject());
    usernameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().userName()));
    firstNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().firstName()));
    lastNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().lastName()));
    typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().type()));

    idCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.1));
    usernameCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.25));
    firstNameCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.25));
    lastNameCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.25));
    typeCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.15));

    userTable.getColumns().addAll(idCol, usernameCol, firstNameCol, lastNameCol, typeCol);

    userTable.setRowFactory(tv -> {
      TableRow<User> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
          showUserDetails(row.getItem());
        }
      });
      return row;
    });
  }

  public static void loadUsers() {
    userTableList.clear();
    try {
      String sql = "SELECT * FROM users ORDER BY id";
      ResultSet rs = DatabaseConnection.getInstance().executeQuery(sql);

      while (rs.next()) {
        userTableList.add(new User(
            rs.getInt("id"),
            rs.getString("user_name"),
            rs.getString("password"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("type")));
      }

      userTableList.stream()
          .filter(u -> u.id() == SessionManager.getInstance().getCurrentUser().id())
          .findFirst()
          .ifPresent(user -> SessionManager.getInstance().setCurrentUser(user));

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void showAddUserForm() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fp/assignments/assignment_2/view/form/add-user-form-view.fxml"));
      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setTitle("Add Staff Member");
      stage.setScene(new Scene(loader.load()));

      // Get controller and set up callback
      AddUserFormController controller = loader.getController();
      controller.setOnUserAdded(AllUserController::loadUsers);

      stage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void showUserDetails(User user) {
    try {
      LMVMApplication.navigateToUserDetails(
          user,
          () -> {
            loadUsers();
            LMVMApplication.goBack();
          },
          AllUserController::loadUsers);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
