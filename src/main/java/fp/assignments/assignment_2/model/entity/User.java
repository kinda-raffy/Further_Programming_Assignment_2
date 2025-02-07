package fp.assignments.assignment_2.model.entity;

import java.io.Serializable;

/**
 * Represents a user record.
 *
 * @param id        The unique identifier of the user.
 * @param userName  The user's username.
 * @param password  The user's password.
 * @param firstName The user's first name.
 * @param lastName  The user's last name.
 * @param type      The type of user (e.g., "manager" or "staff").
 */
public record User(Integer id, String userName, String password, String firstName, String lastName, String type)
                implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Gets the full name of the user.
         *
         * @return A string containing the first name and last name, separated by a
         *         space.
         */
        public String getFullName() {
                return firstName + " " + lastName;
        }
}
