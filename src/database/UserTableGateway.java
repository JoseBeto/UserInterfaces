package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
			st = conn.prepareStatement("insert into user (first_name, last_name, email, "
					+ "password, money, cart, paymentMethod) values (?, ?, ?, ?, ?, ?, ?)");
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getEmail());
			st.setString(4, user.getPassword());
			st.setDouble(5, user.getMoney());
			st.setString(6, user.getCart().getCart().toString());
			st.setString(7, user.getPaymentMethods().toString());
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
				user = new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("password")
						, rs.getDouble("money"), rs.getString("cart"), rs.getString("lists"), rs.getString("paymentMethod") , rs.getInt("role"));
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
	
	public ObservableList<User> getUsers() throws AppException {
		ObservableList<User> users = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from user");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				User user = new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("password")
						, rs.getDouble("money"), rs.getString("cart"), rs.getString("lists"), rs.getString("paymentMethod") , rs.getInt("role"));
				users.add(user);
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
		return users;
	}
	
	public void updateUser(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set first_name = ?, last_name = ?, "
					+ "password = ?, money = ?, role = ? where email = ?");
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getPassword());
			st.setDouble(4, user.getMoney());
			st.setInt(5, user.getRole());
			st.setString(6, user.getEmail());
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
			st = conn.prepareStatement("update user set cart = ?, money = ? where email = ?");
			st.setString(1, user.getCart().getCart().toString());
			st.setDouble(2, user.getMoney());
			st.setString(3, user.getEmail());
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
	
	public void updateLists(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set lists = ? where email = ?");
			st.setString(1, user.getLists().getLists().toString());
			st.setString(2, user.getEmail());
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
	
	public void updatePaymentMethods(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set paymentMethod = ? where email = ?");
			st.setString(1, user.getPaymentMethods().toString());
			st.setString(2, user.getEmail());
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
}
