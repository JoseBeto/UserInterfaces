package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaction {
	private SimpleIntegerProperty id;
	private SimpleIntegerProperty itemId;
	private SimpleIntegerProperty qty;
	private SimpleStringProperty sellerId;
	private SimpleDoubleProperty total;
	private SimpleStringProperty date;
	
	public Transaction(int itemId, int qty, String sellerId, Double total, String date) {
		this.id = new SimpleIntegerProperty();
		this.itemId = new SimpleIntegerProperty();
		this.qty = new SimpleIntegerProperty();
		this.sellerId = new SimpleStringProperty();
		this.total = new SimpleDoubleProperty();
		this.date = new SimpleStringProperty();
		
		setItemId(itemId);
		setQty(qty);
		setSellerId(sellerId);
		setTotal(total);
		setDate(date);
	}

	public int getId() {
		return id.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public int getItemId() {
		return itemId.get();
	}

	public void setItemId(int itemId) {
		this.itemId.set(itemId);
	}

	public int getQty() {
		return qty.get();
	}

	public void setQty(int qty) {
		this.qty.set(qty);
	}

	public String getSellerId() {
		return sellerId.get();
	}

	public void setSellerId(String sellerId) {
		this.sellerId.set(sellerId);
	}

	public Double getTotal() {
		return total.get();
	}

	public void setTotal(Double total) {
		this.total.set(total);
	}

	public String getDate() {
		return date.get();
	}

	public void setDate(String date) {
		this.date.set(date);
	}
	
	@Override
	public String toString() {
		return getSellerId();
	}
}
