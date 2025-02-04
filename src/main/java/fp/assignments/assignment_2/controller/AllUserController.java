package fp.assignments.assignment_2.controller;

import fp.assignments.assignment_2.LMVMApplication;
import fp.assignments.assignment_2.model.User;
import fp.assignments.assignment_2.service.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.input.MouseButton;

public class AllUserController extends BaseController {
  @FXML
  private TableView<User> userTable;
  private final ObservableList<User> users = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    setupTableColumns();
    loadUsers();
  }

  private void setupTableColumns() {
    // Create columns
    TableColumn<User, Integer> idCol = new TableColumn<>("ID");
    TableColumn<User, String> usernameCol = new TableColumn<>("Username");
    TableColumn<User, String> firstNameCol = new TableColumn<>("First Name");
    TableColumn<User, String> lastNameCol = new TableColumn<>("Last Name");
    TableColumn<User, String> typeCol = new TableColumn<>("Type");

    // Set cell value factories
    idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().id()).asObject());
    usernameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().userName()));
    firstNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().firstName()));
    lastNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().lastName()));
    typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().type()));

    // Set column widths
    idCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.1));
    usernameCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.25));
    firstNameCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.25));
    lastNameCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.25));
    typeCol.prefWidthProperty().bind(userTable.widthProperty().multiply(0.15));

    // Add columns to table
    userTable.getColumns().addAll(idCol, usernameCol, firstNameCol, lastNameCol, typeCol);
    userTable.setItems(users);

    // Add double click handler
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

  public void loadUsers() {
    users.clear();
    try {
      String sql = "SELECT * FROM users ORDER BY id";
      ResultSet rs = DatabaseConnection.getInstance().executeQuery(sql);

      while (rs.next()) {
        users.add(new User(
            rs.getInt("id"),
            rs.getString("user_name"),
            rs.getString("password"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("type")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void showAddUserForm() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/fp/assignments/assignment_2/view/add-user-form-view.fxml"));
      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setTitle("Add Staff Member");
      stage.setScene(new Scene(loader.load()));

      // Get controller and set up callback
      AddUserFormController controller = loader.getController();
      controller.setOnUserAdded(this::loadUsers);

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
          this::loadUsers);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
