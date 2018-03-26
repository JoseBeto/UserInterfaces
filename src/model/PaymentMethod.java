package model;

public interface PaymentMethod
{
	public boolean validateMethod();
	public void fillMethodDetails(String... args);
	public double getAmount();
}