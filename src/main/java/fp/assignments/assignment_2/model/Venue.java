package fp.assignments.assignment_2.model;

import java.util.List;
import java.util.Arrays;

public record Venue(
    String nameId,
    int capacity,
    String category,
    double hirePrice,
    List<String> suitabilityKeywords) {
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
