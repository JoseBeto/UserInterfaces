package model;

import java.util.HashMap;

public class PastOrder {

	private HashMap<Integer, Integer> items;
	private Object paymentMethod;
	private Double total;
	private String datePurchased;
	
	public PastOrder(HashMap<Integer, Integer> items, Object paymentMethod, Double total, String datePurchased) {
		this.items = items;
		this.paymentMethod = paymentMethod;
		this.total = total;
		this.datePurchased = datePurchased;
	}
	
	public HashMap<Integer, Integer> getItems() {
		return items;
	}
	
	public String getPaymentMethod() {
		return paymentMethod.toString();
	}
	
	public Double getTotal() {
		return total;
	}
	
	public String getDatePurchased() {
		return datePurchased;
	}
}
