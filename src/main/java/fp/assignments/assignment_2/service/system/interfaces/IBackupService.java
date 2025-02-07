package fp.assignments.assignment_2.service.system.interfaces;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public interface IBackupService {
  void exportTransactionData(File file) throws IOException, SQLException;

  void importTransactionData(File file) throws IOException, ClassNotFoundException, SQLException;

  void exportMasterData(File file) throws IOException, SQLException;

  void importMasterData(File file) throws IOException, ClassNotFoundException, SQLException;
}
