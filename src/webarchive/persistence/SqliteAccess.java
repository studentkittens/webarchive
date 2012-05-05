package webarchive.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteAccess implements DBAccess {
	
	private String driver = "org.sqlite.JDBC";
	private String url;
	private File databasePath;
	
	public SqliteAccess(File dbPath) {
		setDatabasePath(dbPath);
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Driver class not found", ex);
		}
	}
	
	public File getDatabasePath() {
		return databasePath;
	}

	public String getUrl() {
		return url;
	}

	private void setDatabasePath(File dbPath) {
		this.databasePath = dbPath;
		url = "jdbc:sqlite:"+dbPath.getAbsolutePath();
	}

	public void createTables(String[] createTableStatements) throws SQLException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			for(String createTable : createTableStatements) {
//				System.out.println("SqliteAccess::createTables "+createTable);
				statement.execute(createTable);
			}
		}

		finally {
			if(connection != null)connection.close();
		}
	}
	/* (non-Javadoc)
	 * @see main.persistence.DBAccess#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}

}
