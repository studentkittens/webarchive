package webarchive.dbaccess;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Implements DbAccess for sqlite dataBase.
 * The database-file is accessed by a given path.
 * @author ccwelich
 */
public class SqliteAccess implements DbAccess {
	
	private String driver = "org.sqlite.JDBC";
	private File databasePath;
	private String url;
	/**
	 * 
	 * @param dbPath the path to the database-file
	 */
	public SqliteAccess(File dbPath) {
		setDatabasePath(dbPath);
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Driver class not found", ex);
		}
	}
	/**
	 * get the database path
	 * @return the database path
	 */
	public File getDatabasePath() {
		return databasePath;
	}

	private void setDatabasePath(File dbPath) {
		this.databasePath = dbPath;
		url = "jdbc:sqlite:"+dbPath.getAbsolutePath();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}

}
