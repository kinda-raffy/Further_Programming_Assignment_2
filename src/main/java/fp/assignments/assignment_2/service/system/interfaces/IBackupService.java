package fp.assignments.assignment_2.service.system.interfaces;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Interface for backup and restore operations.
 */
public interface IBackupService {
  /**
   * Exports transaction data (events, venues, bookings) to a file.
   *
   * @param file The file to export to.
   * @throws IOException  If an I/O error occurs.
   * @throws SQLException If a database access error occurs.
   */
  void exportTransactionData(File file) throws IOException, SQLException;

  /**
   * Imports transaction data from a file.
   *
   * @param file The file to import from.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   * @throws SQLException           If a database access error occurs.
   */
  void importTransactionData(File file) throws IOException, ClassNotFoundException, SQLException;

  /**
   * Exports master data (users) to a file.
   *
   * @param file The file to export to.
   * @throws IOException  If an I/O error occurs.
   * @throws SQLException If a database access error occurs.
   */
  void exportMasterData(File file) throws IOException, SQLException;

  /**
   * Imports master data from a file.
   *
   * @param file The file to import from.
   * @throws IOException            If an I/O error occurs.
   * @throws ClassNotFoundException If the class of a serialized object cannot be
   *                                found.
   * @throws SQLException           If a database access error occurs.
   */
  void importMasterData(File file) throws IOException, ClassNotFoundException, SQLException;
}
