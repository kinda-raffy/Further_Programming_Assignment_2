package fp.assignments.assignment_2.model;

// Note: Type is either "manager" or "staff".
public record User(Integer id, String userName, String password, String firstName, String lastName, String type) {
        public String getFullName() {
                return firstName + " " + lastName;
        }
}
