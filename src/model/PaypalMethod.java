package model;

import javafx.scene.control.Alert.AlertType;
import userInterfaces.AlertHelper;

public class PaypalMethod implements PaymentMethod {

	private String paypalEmail;
	private String password;
	private String amountRequested;
	
	@Override
	public boolean validateMethod() {
		if(paypalEmail.equals("")) {
			AlertHelper.showWarningMessage("Error!", "Invalid email!", AlertType.ERROR);
			return false;
		} else if(password.equals("")) {
			AlertHelper.showWarningMessage("Error!", "Paypal password field is empty!", AlertType.ERROR);
			return false;
		} else if(amountRequested.equals("") || Double.parseDouble(amountRequested) == 0) {
			AlertHelper.showWarningMessage("Error!", "Amount requested must be above 0.0!", AlertType.ERROR);
			return false;
		}
		
		return true;
	}

	@Override
	public void fillMethodDetails(String... args) {
		// TODO Auto-generated method stub
		//if( args.length == 3)
		//{
			paypalEmail = args[0]; //NEED TO SANITIZE INPUT FIRST
			password = args[1];
			amountRequested = args[2];
		//}
	}

	public String getEmail(){ return paypalEmail; }
	public String getPassword(){ return password; }

	@Override
	public double getAmount() {
		// TODO Auto-generated method stub
		return Double.parseDouble(amountRequested);
	}
	
	@Override
	public String toString() {
		return "Paypal account " + paypalEmail;
	}

	@Override
	public int getTypeMethod() {
		return PAYPAL;
	}

	@Override
	public String getKey() {
		return paypalEmail;
	}
}
