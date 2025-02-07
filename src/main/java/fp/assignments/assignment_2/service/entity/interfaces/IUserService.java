package fp.assignments.assignment_2.service.entity.interfaces;

import fp.assignments.assignment_2.model.entity.User;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing user-related operations.
 */
public interface IUserService {
  /**
   * Authenticates a user based on username and password.
   * 
   * @param username The username.
   * @param password The password.
   * @return The User object if authentication is successful, null otherwise.
   * @throws SQLException If a database access error occurs.
   */
  User authenticateUser(String username, String password) throws SQLException;

  /**
   * Creates a new user.
   * 
   * @param userName  The username.
   * @param password  The password.
   * @param firstName The first name.
   * @param lastName  The last name.
   * @param type      The user type (manager or staff).
   * @return The created User object.
   * @throws SQLException If a database access error occurs.
   */
  User createUser(String userName, String password, String firstName, String lastName, String type) throws SQLException;

  /**
   * Updates an existing user.
   * 
   * @param id        The ID of the user to update.
   * @param userName  The new username.
   * @param password  The new password (can be empty to keep the old password).
   * @param firstName The new first name.
   * @param lastName  The new last name.
   * @param type      The new user type.
   * @throws SQLException If a database access error occurs.
   */
  void updateUser(Integer id, String userName, String password, String firstName, String lastName, String type)
      throws SQLException;

  /**
   * Deletes a user.
   * 
   * @param id The ID of the user to delete.
   * @throws SQLException If a database access error occurs.
   */
  void deleteUser(Integer id) throws SQLException;

  /**
   * Retrieves all users.
   * 
   * @return A list of all users.
   * @throws SQLException If a database access error occurs.
   */
  List<User> getAllUsers() throws SQLException;

  /**
   * Retrieves a user by their ID.
   * 
   * @param id The ID of the user.
   * @return The User object if found, null otherwise.
   * @throws SQLException If a database access error occurs.
   */
  User getUserById(Integer id) throws SQLException;
}
