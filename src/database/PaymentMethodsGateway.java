package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CardPayment;
import model.PaymentMethod;
import model.PaypalMethod;

public class PaymentMethodsGateway {

private Connection conn;
	
	public PaymentMethodsGateway(Connection conn) {
		this.conn = conn;
	}

	public void addPaymentMethod(PaymentMethod paymentMethod) throws AppException {
		PreparedStatement st = null;
		try {
			if(paymentMethod.getTypeMethod() == PaymentMethod.PAYPAL) {
				String statement = "insert into paymentMethod_paypal (email, password) values (?, ?)";
				st = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
				st.setString(1, ((PaypalMethod) paymentMethod).getEmail());
				st.setString(2, ((PaypalMethod) paymentMethod).getPassword());
				st.executeUpdate();
			} else if(paymentMethod.getTypeMethod() == PaymentMethod.CREDIT_CARD) {
				String statement = "insert into paymentMethod_creditCard (cardNumber, expDate, name) values (?, ?, ?)";
				st = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
				st.setString(1, ((CardPayment) paymentMethod).getCardNumber());
				st.setString(2, ((CardPayment) paymentMethod).getExpDate());
				st.setString(3, ((CardPayment) paymentMethod).getName());
				st.executeUpdate();
			} 

			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			paymentMethod.setId(rs.getInt(1));
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

	public ObservableList<PaymentMethod> getPaymentMethods(HashMap<Integer, Integer> payMethods) throws AppException {
		ObservableList<PaymentMethod> paymentMethods = FXCollections.observableArrayList();

		PreparedStatement st = null;
		try {
			for(Entry<Integer, Integer> e : payMethods.entrySet()) {
				if(e.getValue() == PaymentMethod.PAYPAL) {
					st = conn.prepareStatement("select * from paymentMethod_paypal where id = ?");
					st.setInt(1, e.getKey());
					
					ResultSet rs = st.executeQuery();
					if(rs.next()) {
						PaymentMethod paymentMethod = new PaypalMethod();
						paymentMethod.fillMethodDetails(rs.getString("email"), rs.getString("password"), "");
						paymentMethod.setId(rs.getInt("id"));
						paymentMethods.add(paymentMethod);
					}
				} else if(e.getValue() == PaymentMethod.CREDIT_CARD) {
					st = conn.prepareStatement("select * from paymentMethod_creditCard where id = ?");
					st.setInt(1, e.getKey());
					ResultSet rs = st.executeQuery();
					if(rs.next()) {
						PaymentMethod paymentMethod = new CardPayment();
						paymentMethod.fillMethodDetails(rs.getString("cardNumber"), rs.getString("expDate"), rs.getString("name"), "");
						paymentMethod.setId(rs.getInt("id"));
						paymentMethods.add(paymentMethod);
					}
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
		return paymentMethods;
	}
}
