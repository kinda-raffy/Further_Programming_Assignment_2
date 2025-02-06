package fp.assignments.assignment_2.service.entity;

import fp.assignments.assignment_2.model.entity.User;
import fp.assignments.assignment_2.service.system.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
  private final DatabaseConnection db;

  public UserService() {
    this.db = DatabaseConnection.getInstance();
  }

  public User authenticateUser(String username, String password) throws SQLException {
    String sql = "SELECT * FROM users WHERE user_name = ? AND password = ?";
    try (var pstmt = db.prepareStatement(sql)) {
      pstmt.setString(1, username);
      pstmt.setString(2, password);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return createUserFromResultSet(rs);
      }
    }
    return null;
  }

  public User createUser(String userName, String password, String firstName,
      String lastName, String type) throws SQLException {
    String sql = """
        INSERT INTO users (user_name, password, first_name, last_name, type)
        VALUES (?, ?, ?, ?, ?)
        RETURNING *
        """;

    try (var pstmt = db.prepareStatement(sql)) {
      pstmt.setString(1, userName);
      pstmt.setString(2, password);
      pstmt.setString(3, firstName);
      pstmt.setString(4, lastName);
      pstmt.setString(5, type);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return createUserFromResultSet(rs);
      }
    }
    return null;
  }

  public void updateUser(Integer id, String userName, String password,
      String firstName, String lastName, String type) throws SQLException {
    String sql = password.isEmpty()
        ? "UPDATE users SET user_name = ?, first_name = ?, last_name = ?, type = ? WHERE id = ?"
        : "UPDATE users SET user_name = ?, password = ?, first_name = ?, last_name = ?, type = ? WHERE id = ?";

    db.executeUpdate(sql, ps -> {
      if (password.isEmpty()) {
        ps.setString(1, userName);
        ps.setString(2, firstName);
        ps.setString(3, lastName);
        ps.setString(4, type);
        ps.setInt(5, id);
      } else {
        ps.setString(1, userName);
        ps.setString(2, password);
        ps.setString(3, firstName);
        ps.setString(4, lastName);
        ps.setString(5, type);
        ps.setInt(6, id);
      }
    });
  }

  public void deleteUser(Integer id) throws SQLException {
    String sql = "DELETE FROM users WHERE id = ?";
    db.executeUpdate(sql, ps -> ps.setInt(1, id));
  }

  public List<User> getAllUsers() throws SQLException {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM users ORDER BY id";

    try (ResultSet rs = db.executeQuery(sql)) {
      while (rs.next()) {
        users.add(createUserFromResultSet(rs));
      }
    }
    return users;
  }

  public User getUserById(Integer id) throws SQLException {
    String sql = "SELECT * FROM users WHERE id = ?";
    try (var pstmt = db.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return createUserFromResultSet(rs);
      }
    }
    return null;
  }

  private User createUserFromResultSet(ResultSet rs) throws SQLException {
    return new User(
        rs.getInt("id"),
        rs.getString("user_name"),
        rs.getString("password"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        rs.getString("type"));
  }
}
