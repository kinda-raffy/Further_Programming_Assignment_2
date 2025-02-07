package fp.assignments.assignment_2.service.system;

import fp.assignments.assignment_2.model.entity.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import fp.assignments.assignment_2.service.system.interfaces.ISessionManager;

public class SessionManager implements ISessionManager {
  private static SessionManager instance;
  private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

  private SessionManager() {
  }

  public static SessionManager getInstance() {
    if (instance == null) {
      instance = new SessionManager();
    }
    return instance;
  }

  public void setCurrentUser(User user) {
    this.currentUser.set(user);
  }

  public User getCurrentUser() {
    return currentUser.get();
  }

  public ObjectProperty<User> currentUserProperty() {
    return currentUser;
  }

  public boolean isManager() {
    User user = getCurrentUser();
    return user != null && "manager".equals(user.type());
  }

  public void logout() {
    currentUser.set(null);
  }
}