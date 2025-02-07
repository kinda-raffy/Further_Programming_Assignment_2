package fp.assignments.assignment_2.service.entity.interfaces;

import fp.assignments.assignment_2.model.entity.Event;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing event-related operations.
 */
public interface IEventService {
  /**
   * Retrieves an event by its ID.
   * 
   * @param eventId The ID of the event.
   * @return The Event object if found, null otherwise.
   * @throws SQLException If a database access error occurs.
   */
  Event getEventById(Integer eventId) throws SQLException;

  /**
   * Retrieves all events.
   * 
   * @return A list of all events.
   * @throws SQLException If a database access error occurs.
   */
  List<Event> getEvents() throws SQLException;
}
