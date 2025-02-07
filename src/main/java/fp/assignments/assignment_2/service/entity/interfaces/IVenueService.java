package fp.assignments.assignment_2.service.entity.interfaces;

import fp.assignments.assignment_2.model.entity.Venue;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing venue-related operations.
 */
public interface IVenueService {
  /**
   * Creates a new venue.
   * 
   * @param name      The name of the venue.
   * @param capacity  The capacity of the venue.
   * @param keywords  The suitability keywords for the venue.
   * @param category  The category of the venue.
   * @param hirePrice The hire price per hour.
   * @throws SQLException If a database access error occurs.
   */
  void createVenue(String name, int capacity, String keywords, String category, double hirePrice) throws SQLException;

  /**
   * Deletes a venue.
   * 
   * @param venueName The name of the venue to delete.
   * @throws SQLException If a database access error occurs.
   */
  void deleteVenue(String venueName) throws SQLException;

  /**
   * Retrieves all venues.
   * 
   * @return A list of all venues.
   * @throws SQLException If a database access error occurs.
   */
  List<Venue> getVenues() throws SQLException;
}
