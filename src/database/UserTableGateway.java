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
					+ "password, money, cart) values (?, ?, ?, ?, ?, ?)";
			st = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getEmail());
			st.setString(4, user.getPassword());
			st.setDouble(5, user.getMoney());
			st.setString(6, user.getCart().toString());
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
						rs.getString("password"), rs.getDouble("money"), rs.getString("cart"), rs.getInt("role"));
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
						rs.getString("password"), rs.getDouble("money"), rs.getString("cart"), rs.getInt("role"));
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
	
	public void updateUser(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set first_name = ?, last_name = ?, email = ?, "
					+ "password = ?, money = ? where id = ?");
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getEmail());
			st.setString(4, user.getPassword());
			st.setDouble(5, user.getMoney());
			st.setInt(6, user.getId());
			st.executeUpdate();
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
	
	public void updateCart(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set cart = ?, money = ? where id = ?");
			st.setString(1, user.getCart().toString());
			st.setDouble(2, user.getMoney());
			st.setInt(3, user.getId());
			st.executeUpdate();
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
	
	public void insert()
	{
		String sql = "INSERT INTO user(id, first_name, last_name, money, email, password, cart) VALUES(NULL, 'David', 'Martinez', '500.00', 'drmartinez@yahoo.com', 'Pass12', NULL)";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
