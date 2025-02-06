package fp.assignments.assignment_2.model;

import java.io.Serializable;
import java.util.List;

public record MasterData(
    List<User> users) implements Serializable {
  private static final long serialVersionUID = 1L;
}