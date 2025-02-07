package fp.assignments.assignment_2.service.system.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fp.assignments.assignment_2.service.system.DatabaseConnection;

/**
 * Interface for managing database connections and operations.
 */
public interface IDatabaseConnection {
  /**
   * Gets the database connection.
   * 
   * @return The database connection.
   */
  Connection getConnection();

  /**
   * Executes a query and returns the result set.
   * 
   * @param sql The SQL query.
   * @return The result set.
   * @throws SQLException If a database access error occurs.
   */
  ResultSet executeQuery(String sql) throws SQLException;

  /**
   * Prepares a SQL statement.
   * 
   * @param sql The SQL statement.
   * @return The prepared statement.
   * @throws SQLException If a database access error occurs.
   */
  PreparedStatement prepareStatement(String sql) throws SQLException;

  /**
   * Executes an update statement with a prepared statement setter.
   * 
   * @param sql    The SQL update statement.
   * @param setter The prepared statement setter.
   * @return The number of rows affected.
   * @throws SQLException If a database access error occurs.
   */
  int executeUpdate(String sql, DatabaseConnection.PreparedStatementSetter setter) throws SQLException;
}
