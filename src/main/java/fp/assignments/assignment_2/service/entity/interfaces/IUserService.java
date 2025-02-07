package fp.assignments.assignment_2.service.entity.interfaces;

import fp.assignments.assignment_2.model.entity.User;
import java.sql.SQLException;
import java.util.List;

public interface IUserService {
  User authenticateUser(String username, String password) throws SQLException;

  User createUser(String userName, String password, String firstName, String lastName, String type) throws SQLException;

  void updateUser(Integer id, String userName, String password, String firstName, String lastName, String type)
      throws SQLException;

  void deleteUser(Integer id) throws SQLException;

  List<User> getAllUsers() throws SQLException;

  User getUserById(Integer id) throws SQLException;
}
