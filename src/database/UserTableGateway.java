package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert.AlertType;
import model.User;
import userInterfaces.AlertHelper;

public class UserTableGateway {
	private Connection conn;
	public static final int ER_UNIQUE = 19;
	
	public UserTableGateway(Connection conn) {
		this.conn = conn;
	}

	public Boolean registerUser(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into user (first_name, last_name, email, "
					+ "password, wallet, cart, paymentMethod, pastOrders) values (?, ?, ?, ?, ?, ?, ?, ?)");
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getEmail());
			st.setString(4, user.getPassword());
			st.setDouble(5, user.getWallet());
			st.setString(6, user.getCart().getCart().toString());
			st.setString(7, user.getPaymentMethods().toString());
			st.setString(8, user.getPastOrders().toString());
			st.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			if(e.getErrorCode() == ER_UNIQUE)
				return false;
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
				user = new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email")
						, rs.getString("password"), rs.getDouble("wallet"), rs.getString("cart")
						, rs.getString("lists"), rs.getString("paymentMethod")
						, rs.getString("pastOrders"), rs.getInt("role"));
				User.changeInstance(user);
				
				if(user.getRole() == User.SELLER)
					user.setItemsSelling(rs.getString("itemsSelling"));
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
	
	public void updateUser(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set first_name = ?, last_name = ?, "
					+ "password = ?, wallet = ?, role = ? where email = ?");
			st.setString(1, user.getFirstName());
			st.setString(2, user.getLastName());
			st.setString(3, user.getPassword());
			st.setDouble(4, user.getWallet());
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
	
	public void addFundsToUser(String sellerId, Double money) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set wallet = ? where email = ?");
			st.setDouble(1, getUserWalletAmount(sellerId) + money);
			st.setString(2, sellerId);
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
	
	public Double getUserWalletAmount(String id) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from user where email = ?");
			st.setString(1, id);
			
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				return rs.getDouble("wallet");
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
		return null;
	}
	
	public void updateCart(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set cart = ?, wallet = ? where email = ?");
			st.setString(1, user.getCart().getCart().toString());
			st.setDouble(2, user.getWallet());
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
	
	public void updatePastOrders(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set pastOrders = ? where email = ?");
			st.setString(1, user.getPastOrders().toString());
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
	
	public void updateItemsSelling(User user) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update user set itemsSelling = ? where email = ?");
			st.setString(1, user.getItemsSelling().toString());
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
