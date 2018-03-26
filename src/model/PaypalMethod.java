package model;

public class PaypalMethod implements PaymentMethod {

	private String paypalEmail;
	private String password;
	private double amountRequested;
	
	@Override
	public boolean validateMethod() {
		// TODO Auto-generated method stub
		if( paypalEmail != null && password != null && amountRequested != 0.0 )
			return true;
		return false;
	}

	@Override
	public void fillMethodDetails(String... args) {
		// TODO Auto-generated method stub
		//if( args.length == 3)
		//{
			paypalEmail = args[0]; //NEED TO SANITIZE INPUT FIRST
			password = args[1];
			amountRequested = Double.parseDouble(args[2]);
		//}
	}

	public String getEmail(){ return paypalEmail; }
	public String getPassword(){ return password; }

	@Override
	public double getAmount() {
		// TODO Auto-generated method stub
		return amountRequested;
	}
}
