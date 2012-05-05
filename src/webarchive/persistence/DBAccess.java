package webarchive.persistence;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBAccess {

	public Connection getConnection() throws SQLException;

}