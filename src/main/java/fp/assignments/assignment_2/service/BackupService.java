package fp.assignments.assignment_2.service;

import fp.assignments.assignment_2.model.backup.MasterData;
import fp.assignments.assignment_2.model.backup.TransactionData;
import fp.assignments.assignment_2.model.entity.Booking;
import fp.assignments.assignment_2.model.entity.Event;
import fp.assignments.assignment_2.model.entity.User;
import fp.assignments.assignment_2.model.entity.Venue;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BackupService {
  private static BackupService instance;
  private final DatabaseConnection dbConnection;

  private BackupService() {
    this.dbConnection = DatabaseConnection.getInstance();
  }

  public static BackupService getInstance() {
    if (instance == null) {
      instance = new BackupService();
    }
    return instance;
  }

  public void exportTransactionData(File file) throws IOException, SQLException {
    TransactionData data = ServiceProvider.use(sp -> new TransactionData(
        new ArrayList<>(sp.eventService().getEvents()),
        new ArrayList<>(sp.venueService().getVenues()),
        new ArrayList<>(sp.bookingService().getBookings())));

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
      oos.writeObject(data);
    }
  }

  public void importTransactionData(File file) throws IOException, ClassNotFoundException, SQLException {
    TransactionData data;
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
      data = (TransactionData) ois.readObject();
    }
    // Clear existing data.
    dbConnection.executeUpdate("DELETE FROM bookings", ps -> {
    });
    dbConnection.executeUpdate("DELETE FROM events", ps -> {
    });
    dbConnection.executeUpdate("DELETE FROM venues", ps -> {
    });
    // Import venues.
    for (Venue venue : data.venues()) {
      dbConnection.executeUpdate(
          "INSERT INTO venues (name, capacity, suitability_keywords, category, hire_price) VALUES (?, ?, ?, ?, ?)",
          ps -> {
            ps.setString(1, venue.nameId());
            ps.setInt(2, venue.capacity());
            ps.setString(3, String.join(";", venue.suitabilityKeywords()));
            ps.setString(4, venue.category());
            ps.setDouble(5, venue.hirePricePerHour());
          });
    }

    dbConnection.executeUpdate("PRAGMA foreign_keys=OFF", ps -> {
    });

    // Import events.
    for (Event event : data.events()) {
      dbConnection.executeUpdate(
          "INSERT INTO events (id, name, main_artist, expected_attendance, event_datetime, duration, event_type, category, client_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
          ps -> {
            ps.setInt(1, event.id());
            ps.setString(2, event.title());
            ps.setString(3, event.mainArtist());
            ps.setInt(4, event.expectedAttendance());
            ps.setString(5, event.eventDateTime().toString());
            ps.setInt(6, event.durationHours());
            ps.setString(7, event.eventType());
            ps.setString(8, event.category());
            ps.setString(9, event.clientName());
          });
    }
    // Import bookings.
    for (Booking booking : data.bookings()) {
      dbConnection.executeUpdate(
          "INSERT INTO bookings (id, event_id, venue_name, booking_date_time, total_price, commission) VALUES (?, ?, ?, ?, ?, ?)",
          ps -> {
            ps.setInt(1, booking.id());
            ps.setInt(2, booking.eventId());
            ps.setString(3, booking.venueName());
            ps.setString(4, booking.startDate().toString());
            ps.setDouble(5, booking.totalPrice());
            ps.setDouble(6, booking.commission());
          });
    }

    dbConnection.executeUpdate("UPDATE sqlite_sequence SET seq = (SELECT MAX(id) FROM events) WHERE name = 'events'",
        ps -> {
        });
    dbConnection.executeUpdate(
        "UPDATE sqlite_sequence SET seq = (SELECT MAX(id) FROM bookings) WHERE name = 'bookings'", ps -> {
        });
    dbConnection.executeUpdate("PRAGMA foreign_keys=ON", ps -> {
    });
  }

  public void exportMasterData(File file) throws IOException, SQLException {
    List<User> users = ServiceProvider.use(sp -> sp.userService().getAllUsers());
    MasterData data = new MasterData(users);
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
      oos.writeObject(data);
    }
  }

  public void importMasterData(File file) throws IOException, ClassNotFoundException, SQLException {
    MasterData data;
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
      data = (MasterData) ois.readObject();
    }
    // Clear existing users.
    dbConnection.executeUpdate("DELETE FROM users", ps -> {
    });
    // Import users.
    for (User user : data.users()) {
      ServiceProvider.run(sp -> sp.userService().createUser(
          user.userName(),
          user.password(),
          user.firstName(),
          user.lastName(),
          user.type()));
    }
  }
}