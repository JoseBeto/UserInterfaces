package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class ConnectionFactory {
	public static Connection createConnection() throws AppException {
		Connection conn = null;
		
		//read properties file
		try {
    		Properties props = new Properties();
    		FileInputStream fis = new FileInputStream("db.properties");
	        props.load(fis);
	        fis.close();

	        //create the datasource
	        MysqlDataSource ds = new MysqlDataSource();
	        ds.setURL(props.getProperty("MYSQL_DB_URL"));
	        ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
	        ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
			
			//create connection
	        conn = ds.getConnection();
	        
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		}
				
		return conn;
	}
	
	public static Connection createSqlLiteConnection() throws AppException
	{			
		try {
			String url = "jdbc:sqlite:src/database/userInterfaces_database.db";
		    Connection conn = DriverManager.getConnection(url);
		    return conn;
		} catch (SQLException e) {
		    return null;
		}
	}
}