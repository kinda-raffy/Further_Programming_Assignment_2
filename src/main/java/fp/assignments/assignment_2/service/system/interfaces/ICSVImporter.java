package fp.assignments.assignment_2.service.system.interfaces;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Interface for importing data from CSV files.
 */
public interface ICSVImporter {
  /**
   * Imports venue data from a CSV file.
   * 
   * @param file The CSV file to import.
   * @throws SQLException If a database access error occurs.
   * @throws IOException  If an I/O error occurs.
   */
  void importVenueCSV(File file) throws SQLException, IOException;

  /**
   * Imports event data from a CSV file.
   * 
   * @param file The CSV file to import.
   * @throws IOException  If an I/O error occurs.
   * @throws SQLException If a database access error occurs.
   */
  void importEventCSV(File file) throws IOException, SQLException;
}
