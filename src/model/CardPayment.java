package model;

public class CardPayment implements PaymentMethod {

	private long cardNumber;
	private String expDate;
	private String secCode;
	private double amountRequested;
	@Override
	public boolean validateMethod() {
		
		return true;
	}

	@Override
	public void fillMethodDetails(String... args) {
		// TODO Auto-generated method stub
		cardNumber = Long.parseLong(args[0]);
		expDate = args[1];
		secCode = args[2];
		amountRequested = Double.parseDouble(args[3]);
	}

	@Override
	public double getAmount() {
		// TODO Auto-generated method stub
		return amountRequested;
	}
}
