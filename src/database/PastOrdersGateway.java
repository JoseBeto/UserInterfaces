package database;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.PastOrder;

public class PastOrdersGateway {

	private Connection conn;

	public PastOrdersGateway(Connection conn) {
		this.conn = conn;
	}

	public int addPastOrder(PastOrder pastOrder) throws AppException {
		PreparedStatement st = null;
		try {
			String statement = "insert into past_orders (items, date_purchased, "
					+ "payment_method_used, total) values (?, ?, ?, ?)";
			st = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, pastOrder.getItems().toString());
			st.setString(2, pastOrder.getDatePurchased());
			st.setString(3, pastOrder.getPaymentMethod());
			st.setDouble(4, pastOrder.getTotal());
			st.executeUpdate();
			
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
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

	public ObservableList<PastOrder> getPastOrders(ArrayList<Integer> pastOrderIds) throws AppException {
		ObservableList<PastOrder> pastOrders = FXCollections.observableArrayList();

		PreparedStatement st = null;
		try {
			for(Integer i : pastOrderIds) {
				st = conn.prepareStatement("select * from past_orders where id = ?");
				st.setInt(1, i);

				ResultSet rs = st.executeQuery();
				if(rs.next()) {
					HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
					String itemString = rs.getString("items");
					
					Properties props = new Properties();
					try {
						props.load(new StringReader(itemString.substring(1, itemString.length() - 1).replace(",", "\n")));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for(Entry<Object, Object> e : props.entrySet()) {
						items.put(Integer.valueOf((String) e.getKey()), Integer.valueOf((String) e.getValue()));
					}
					
					PastOrder pastOrder = new PastOrder(items, rs.getString("payment_method_used")
							, rs.getDouble("total"), rs.getString("date_purchased"));
					pastOrders.add(pastOrder);
				}

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
		return pastOrders;
	}
}
