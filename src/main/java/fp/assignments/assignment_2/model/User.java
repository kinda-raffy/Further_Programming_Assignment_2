package fp.assignments.assignment_2.model;

import java.io.Serializable;

// Note: Type is either "manager" or "staff".
public record User(Integer id, String userName, String password, String firstName, String lastName, String type)
                implements Serializable {

        private static final long serialVersionUID = 1L;

        public String getFullName() {
                return firstName + " " + lastName;
        }
}
