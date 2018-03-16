package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.Alert.AlertType;
import model.User;
import userInterfaces.AlertHelper;

public class UserTableGateway {
	private Connection conn;
	
	public UserTableGateway(Connection conn) {
		this.conn = conn;
	}

	public void registerUser(User user) throws AppException {
		PreparedStatement st = null;
		try {
			String statement = "insert into user (first_name, last_name, email, "
					+ "password, money) values (?, ?, ?, ?, ?)";
			st = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getEmail());
			st.setString(4, user.getPassword());
			st.setDouble(5, user.getMoney());
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

	public Boolean loginUser(String email, String password) throws AppException {
		PreparedStatement st = null;
		User user;

		try {
			st = conn.prepareStatement("select * from user where email = ?");
			st.setString(1, email);
			ResultSet rs = st.executeQuery();
			
			if(rs.next() == true) {
				if(!rs.getString("password").equals(password)) {
					AlertHelper.showWarningMessage("Error!", "Incorrect password!", AlertType.ERROR);
					return false;
				}
				user = new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), 
						rs.getString("password"), rs.getDouble("money"));
				user.setId(rs.getInt("id"));
				User.changeInstance(user);
			} else {
				AlertHelper.showWarningMessage("Error!", "No account exists with that email!", AlertType.ERROR);
				return false;
			}
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
		return true;
	}
	
	public void setUserById(int id) throws AppException {
		PreparedStatement st = null;
		User user;

		try {
			st = conn.prepareStatement("select * from user where id = ?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			
			if(rs.next() == true) {
				user = new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), 
						rs.getString("password"), rs.getDouble("money"));
				user.setId(rs.getInt("id"));
				User.changeInstance(user);
			}
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
