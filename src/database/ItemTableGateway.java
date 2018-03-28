package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;

public class ItemTableGateway {
	private Connection conn;
	
	public ItemTableGateway(Connection conn) {
		this.conn = conn;
	}
	
	public void AddItem(Item item) throws AppException {
		PreparedStatement st = null;
		try {
			String statement = "insert into item (name, price, image, "
					+ "description) values (?, ?, ?, ?)";
			st = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, item.getName());
			st.setDouble(2, item.getPrice());
			st.setString(3, item.getImageString());
			st.setString(4, item.getDesc());
			st.executeUpdate();

			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			item.setId(rs.getInt(1));
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
	
	public ObservableList<Item> getItems() throws AppException {
		ObservableList<Item> items = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from item");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Item item = new Item(rs.getString("name"), rs.getDouble("price"), rs.getString("description"), rs.getString("image"), 0);
				item.setGateway(this);
				item.setId(rs.getInt("id"));
				items.add(item);
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
		return items;
	}
	
	public Item getItemById(int id) throws AppException {
		PreparedStatement st = null;
		Item item = null;
		try {
			st = conn.prepareStatement("select * from item where id = ?");
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				item = new Item(rs.getString("name"), rs.getDouble("price"), rs.getString("description"), rs.getString("image"), 0);
				item.setGateway(this);
				item.setId(rs.getInt("id"));
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
		return item;
	}
}
