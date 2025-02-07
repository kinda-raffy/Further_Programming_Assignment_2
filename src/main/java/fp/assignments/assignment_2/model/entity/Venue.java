package fp.assignments.assignment_2.model.entity;

import java.util.List;
import java.util.Arrays;
import java.io.Serializable;

/**
 * Represents a venue record.
 * 
 * @param nameId              A unique name identifier for the venue.
 * @param capacity            The maximum capacity of the venue.
 * @param category            The category of the venue (e.g., concert hall,
 *                            conference room).
 * @param hirePricePerHour    The cost to hire the venue per hour.
 * @param suitabilityKeywords A list of keywords describing the venue's
 *                            suitability.
 */
public record Venue(
    String nameId,
    int capacity,
    String category,
    double hirePricePerHour,
    List<String> suitabilityKeywords) implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a Venue object, splitting the suitability keywords string.
   * 
   * @param nameId              The unique name identifier.
   * @param capacity            The capacity of the venue.
   * @param suitabilityKeywords A semicolon-separated string of suitability
   *                            keywords.
   * @param category            The category of the venue.
   * @param hirePrice           The hourly hire price.
   */
  public Venue(String nameId, int capacity, String suitabilityKeywords,
      String category, double hirePrice) {
    this(
        nameId,
        capacity,
        category,
        hirePrice,
        Arrays.asList(suitabilityKeywords.split(";")).stream().map(String::trim).toList());
  }

  /**
   * Checks if the venue is suitable for a given keyword.
   * 
   * @param keyword The keyword to check.
   * @return True if the venue's suitability keywords contain the given keyword,
   *         false otherwise.
   */
  public boolean isSuitable(String keyword) {
    return suitabilityKeywords.contains(keyword);
  }
}
