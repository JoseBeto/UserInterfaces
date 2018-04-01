package model;

public interface PaymentMethod
{
	public final static int PAYPAL = 1;
	public final static int CREDIT_CARD = 2;
	
	public boolean validateMethod();
	public void fillMethodDetails(String... args);
	public double getAmount();
	public int getTypeMethod();
	public void setId(int id);
	public int getId();
}