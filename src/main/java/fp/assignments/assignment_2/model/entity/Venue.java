package fp.assignments.assignment_2.model.entity;

import java.util.List;
import java.util.Arrays;
import java.io.Serializable;

public record Venue(
    String nameId,
    int capacity,
    String category,
    double hirePricePerHour,
    List<String> suitabilityKeywords) implements Serializable {

  private static final long serialVersionUID = 1L;

  public Venue(String nameId, int capacity, String suitabilityKeywords,
      String category, double hirePrice) {
    this(
        nameId,
        capacity,
        category,
        hirePrice,
        Arrays.asList(suitabilityKeywords.split(";")).stream().map(String::trim).toList());
  }

  public boolean isSuitable(String keyword) {
    return suitabilityKeywords.contains(keyword);
  }
}
