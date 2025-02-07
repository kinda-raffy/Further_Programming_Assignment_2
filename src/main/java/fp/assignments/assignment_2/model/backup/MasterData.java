package fp.assignments.assignment_2.model.backup;

import java.io.Serializable;
import java.util.List;

import fp.assignments.assignment_2.model.entity.User;

/**
 * A record to hold the master data for backup, which includes user information.
 * 
 * @param users The list of users.
 */
public record MasterData(
    List<User> users) implements Serializable {
  private static final long serialVersionUID = 1L;
}