package fp.assignments.assignment_2.service;

import fp.assignments.assignment_2.model.User;

public class SessionManager {
  private static SessionManager instance;
  private User currentUser;

  private SessionManager() {
  }

  public static SessionManager getInstance() {
    if (instance == null) {
      instance = new SessionManager();
    }
    return instance;
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public boolean isManager() {
    return currentUser != null && "manager".equals(currentUser.type());
  }

  public void logout() {
    currentUser = null;
  }
}