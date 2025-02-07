package fp.assignments.assignment_2.service.system.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fp.assignments.assignment_2.service.system.DatabaseConnection;

public interface IDatabaseConnection {
  Connection getConnection();

  ResultSet executeQuery(String sql) throws SQLException;

  PreparedStatement prepareStatement(String sql) throws SQLException;

  int executeUpdate(String sql, DatabaseConnection.PreparedStatementSetter setter) throws SQLException;
}
