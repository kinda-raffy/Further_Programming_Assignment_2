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

  public static <E extends Exception> void run(ThrowingConsumer<ServiceProvider, E> consumer) throws E {
    ServiceProvider instance = baseInstance();
    consumer.accept(instance);
  }

  public static <R, E extends Exception> R use(ThrowingFunction<ServiceProvider, R, E> function) throws E {
    ServiceProvider instance = baseInstance();
    return function.apply(instance);
  }

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

  public static ServiceProvider baseInstance() {
    if (instance == null) {
      instance = new ServiceProvider();
    }
    return instance;
  }

  public UserService userService() {
    return userService;
  }

  public CSVImporter csvImporter() {
    return csvImporter;
  }

  public VenueService venueService() {
    return venueService;
  }

  public BookingService bookingService() {
    return bookingService;
  }

  public EventService eventService() {
    return eventService;
  }

  public BackupService backupService() {
    return backupService;
  }

  public SessionManager session() {
    return session;
  }

  public DatabaseConnection databaseConnection() {
    return dbConnection;
  }

  @FunctionalInterface
  public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
  }

  @FunctionalInterface
  public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
  }
}
