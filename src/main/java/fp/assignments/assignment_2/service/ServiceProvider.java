package fp.assignments.assignment_2.service;

import java.util.function.Consumer;

import fp.assignments.assignment_2.service.entity.BookingService;
import fp.assignments.assignment_2.service.entity.EventService;
import fp.assignments.assignment_2.service.entity.UserService;
import fp.assignments.assignment_2.service.entity.VenueService;
import fp.assignments.assignment_2.service.system.BackupService;
import fp.assignments.assignment_2.service.system.CSVImporter;
import fp.assignments.assignment_2.service.system.DatabaseConnection;
import fp.assignments.assignment_2.service.system.SessionManager;

/**
 * Provides access to various services within the application.
 */
public class ServiceProvider {

  private final CSVImporter csvImporter;
  private final VenueService venueService;
  private final UserService userService;
  private final EventService eventService;
  private final BackupService backupService;
  private final BookingService bookingService;
  private final SessionManager session;
  private final DatabaseConnection dbConnection;
  private static ServiceProvider instance;

  private ServiceProvider() {
    this.dbConnection = DatabaseConnection.getInstance();
    this.userService = new UserService();
    this.csvImporter = new CSVImporter();
    this.venueService = new VenueService();
    this.bookingService = BookingService.getInstance();
    this.eventService = new EventService();
    this.backupService = BackupService.getInstance();
    this.session = SessionManager.getInstance();
  }

  /**
   * Executes a consumer with the service provider instance, handling exceptions.
   * 
   * @param consumer The consumer to execute.
   * @param <E>      The type of exception that might be thrown.
   * @throws E If the consumer throws an exception.
   */
  public static <E extends Exception> void run(ThrowingConsumer<ServiceProvider, E> consumer) throws E {
    ServiceProvider instance = baseInstance();
    consumer.accept(instance);
  }

  /**
   * Applies a function to the service provider instance and returns the result,
   * handling exceptions.
   * 
   * @param function The function to apply.
   * @param <R>      The return type of the function.
   * @param <E>      The type of exception that might be thrown.
   * @return The result of applying the function.
   * @throws E If the function throws an exception.
   */
  public static <R, E extends Exception> R use(ThrowingFunction<ServiceProvider, R, E> function) throws E {
    ServiceProvider instance = baseInstance();
    return function.apply(instance);
  }

  /**
   * Provides a way to access services using their class type (deprecated).
   * 
   * @param serviceClass The class of the service to access.
   * @param consumer     A consumer that will use the requested service.
   * @param <T>          The type of the service.
   */
  @Deprecated
  public static <T> void via(Class<T> serviceClass, Consumer<T> consumer) {
    ServiceProvider instance = baseInstance();
    T service = serviceClass.cast(switch (serviceClass.getSimpleName()) {
      case "CSVImporter" -> instance.csvImporter();
      case "VenueService" -> instance.venueService();
      case "BookingService" -> instance.bookingService();
      case "UserService" -> instance.userService();
      case "EventService" -> instance.eventService();
      case "BackupService" -> instance.backupService();
      case "SessionManager" -> instance.session();
      case "DatabaseConnection" -> instance.databaseConnection();
      default -> throw new IllegalArgumentException("Unknown service: " + serviceClass.getSimpleName());
    });
    consumer.accept(service);
  }

  /**
   * Returns the singleton instance of the ServiceProvider.
   * 
   * @return The ServiceProvider instance.
   */
  public static ServiceProvider baseInstance() {
    if (instance == null) {
      instance = new ServiceProvider();
    }
    return instance;
  }

  /**
   * Returns the UserService instance.
   * 
   * @return The UserService instance.
   */
  public UserService userService() {
    return userService;
  }

  /**
   * Returns the CSVImporter instance.
   * 
   * @return The CSVImporter instance.
   */
  public CSVImporter csvImporter() {
    return csvImporter;
  }

  /**
   * Returns the VenueService instance.
   * 
   * @return The VenueService instance.
   */
  public VenueService venueService() {
    return venueService;
  }

  /**
   * Returns the BookingService instance.
   * 
   * @return The BookingService instance.
   */
  public BookingService bookingService() {
    return bookingService;
  }

  /**
   * Returns the EventService instance.
   * 
   * @return The EventService instance.
   */
  public EventService eventService() {
    return eventService;
  }

  /**
   * Returns the BackupService instance.
   * 
   * @return The BackupService instance.
   */
  public BackupService backupService() {
    return backupService;
  }

  /**
   * Returns the SessionManager instance.
   * 
   * @return The SessionManager instance.
   */
  public SessionManager session() {
    return session;
  }

  /**
   * Returns the DatabaseConnection instance.
   * 
   * @return The DatabaseConnection instance.
   */
  public DatabaseConnection databaseConnection() {
    return dbConnection;
  }

  /**
   * A functional interface for consumers that may throw exceptions.
   * 
   * @param <T> The type of the input to the consumer.
   * @param <E> The type of exception that may be thrown.
   */
  @FunctionalInterface
  public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
  }

  /**
   * A functional interface for functions that may throw exceptions.
   * 
   * @param <T> The type of the input to the function.
   * @param <R> The type of the result of the function.
   * @param <E> The type of exception that may be thrown.
   */
  @FunctionalInterface
  public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
  }
}
