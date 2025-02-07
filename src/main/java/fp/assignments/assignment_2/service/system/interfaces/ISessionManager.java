package fp.assignments.assignment_2.service.system.interfaces;

import fp.assignments.assignment_2.model.entity.User;
import javafx.beans.property.ObjectProperty;

public interface ISessionManager {
  void setCurrentUser(User user);

  User getCurrentUser();

  ObjectProperty<User> currentUserProperty();

  boolean isManager();

  void logout();
}
