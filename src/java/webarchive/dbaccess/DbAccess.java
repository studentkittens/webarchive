package webarchive.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbAccess {

	public Connection getConnection() throws SQLException;

}