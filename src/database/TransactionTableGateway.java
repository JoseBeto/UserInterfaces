package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Transaction;

public class TransactionTableGateway {
	
	private Connection conn;

	public TransactionTableGateway(Connection conn) {
		this.conn = conn;
	}

	public void addTransaction(Transaction transaction) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into transactions (itemId, qty, "
					+ "sellerId, total, date) values (?, ?, ?, ?, ?)");
			st.setInt(1, transaction.getItemId());
			st.setInt(2, transaction.getQty());
			st.setString(3, transaction.getSellerId());
			st.setDouble(4, transaction.getTotal());
			st.setString(5, transaction.getDate());
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
	
	public ObservableList<Transaction> getTransactions(String sellerId) throws AppException {
		ObservableList<Transaction> transactions = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from transactions where sellerId = ?");
			st.setString(1, sellerId);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Transaction transaction = new Transaction(rs.getInt("itemId"), rs.getInt("qty")
						, rs.getString("sellerId") , rs.getDouble("total"), rs.getString("date"));
				transaction.setId(rs.getInt("id"));
				transactions.add(transaction);
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
		return transactions;
	}
}
