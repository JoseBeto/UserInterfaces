package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public static Connection createConnection() throws AppException {			
		try {
			String url = "jdbc:sqlite:src/database/userInterfaces_database.db";
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (SQLException e) {
			return null;
		}
	}
}