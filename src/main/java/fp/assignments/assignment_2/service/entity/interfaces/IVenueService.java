package fp.assignments.assignment_2.service.entity.interfaces;

import fp.assignments.assignment_2.model.entity.Venue;
import java.sql.SQLException;
import java.util.List;

public interface IVenueService {
  void createVenue(String name, int capacity, String keywords, String category, double hirePrice) throws SQLException;

  void deleteVenue(String venueName) throws SQLException;

  List<Venue> getVenues() throws SQLException;
}
