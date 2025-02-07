package fp.assignments.assignment_2.service.system.interfaces;

import fp.assignments.assignment_2.model.entity.User;
import javafx.beans.property.ObjectProperty;

/**
 * Interface for managing user sessions.
 */
public interface ISessionManager {
  /**
   * Sets the currently logged-in user.
   * 
   * @param user The user to set as current.
   */
  void setCurrentUser(User user);

  /**
   * Gets the currently logged-in user.
   * 
   * @return The current user, or null if no user is logged in.
   */
  User getCurrentUser();

  /**
   * Gets the property representing the current user.
   * 
   * @return The current user property.
   */
  ObjectProperty<User> currentUserProperty();

  /**
   * Checks if the current user is a manager.
   * 
   * @return True if the current user is a manager, false otherwise.
   */
  boolean isManager();

  /**
   * Logs out the current user.
   */
  void logout();
}
