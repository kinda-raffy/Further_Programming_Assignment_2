package fp.assignments.assignment_2.model;

public class Venue {
  private Integer id;
  private String name;
  private int capacity;
  private String suitabilityKeywords;
  private String category;
  private double hirePrice;
  private boolean isAvailable;

  public Venue(String name, int capacity, String suitabilityKeywords,
      String category, double hirePrice) {
    this.name = name;
    this.capacity = capacity;
    this.suitabilityKeywords = suitabilityKeywords;
    this.category = category;
    this.hirePrice = hirePrice;
    this.isAvailable = true;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public int getCapacity() {
    return capacity;
  }

  public String getSuitabilityKeywords() {
    return suitabilityKeywords;
  }

  public String getCategory() {
    return category;
  }

  public double getHirePrice() {
    return hirePrice;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }
}
