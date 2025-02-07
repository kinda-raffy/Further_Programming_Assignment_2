package fp.assignments.assignment_2.service.entity.interfaces;

import fp.assignments.assignment_2.model.entity.Event;
import java.sql.SQLException;
import java.util.List;

public interface IEventService {
  Event getEventById(Integer eventId) throws SQLException;

  List<Event> getEvents() throws SQLException;
}
