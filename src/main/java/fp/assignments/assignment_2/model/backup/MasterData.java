package fp.assignments.assignment_2.model.backup;

import java.io.Serializable;
import java.util.List;

import fp.assignments.assignment_2.model.entity.User;

public record MasterData(
    List<User> users) implements Serializable {
  private static final long serialVersionUID = 1L;
}