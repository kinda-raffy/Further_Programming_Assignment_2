package fp.assignments.assignment_2.service.system.interfaces;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public interface ICSVImporter {
  void importVenueCSV(File file) throws SQLException, IOException;

  void importEventCSV(File file) throws IOException, SQLException;
}
