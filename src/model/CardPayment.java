package model;

import javafx.scene.control.Alert.AlertType;
import userInterfaces.AlertHelper;

public class CardPayment implements PaymentMethod {

	private String cardNumber;
	private String expDate;
	private String name;
	private String amountRequested;
	@Override
	public boolean validateMethod() {
		if(cardNumber.length() != 16) {
			AlertHelper.showWarningMessage("Error!", "Card number must be 16 digits!", AlertType.ERROR);
			return false;
		} else if(!expDate.matches("(0?[1-9]|1[0-2])/[0-9][0-9]$")) {
			AlertHelper.showWarningMessage("Error!", "Invalid expiration date! Format must be mm/yy and date must be "
					+ "before current date!", AlertType.ERROR);
			return false;
		} else if(name.equals("")) {
			AlertHelper.showWarningMessage("Error!", "Name field is empty!", AlertType.ERROR);
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
		cardNumber = args[0];
		expDate = args[1];
		name = args[2];
		amountRequested = args[3];
	}

	@Override
	public double getAmount() {
		// TODO Auto-generated method stub
		return Double.parseDouble(amountRequested);
	}
	
	@Override
	public String toString() {
		return "Card ending in " + String.valueOf(cardNumber).substring(12);
	}
}
