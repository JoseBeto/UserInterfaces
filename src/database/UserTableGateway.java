package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.User;

public class UserTableGateway {
	private Connection conn;
	
	public UserTableGateway(Connection conn) {
		this.conn = conn;
	}
	
	public void registerUser(User user) throws AppException {
		PreparedStatement st = null;
		try {
			String statement = "insert into user (first_name, last_name, email, "
					+ "money) values (?, ?, ?, ?)";
			st = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getEmail());
			st.setDouble(4, user.getMoney());
			st.executeUpdate();
			
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			user.setId(rs.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
}
