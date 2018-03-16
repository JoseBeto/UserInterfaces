package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {

	private static User myInstance = null;
	
	private int id;
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty email;
	private SimpleStringProperty password;
	private SimpleDoubleProperty money;

	public User() {
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.money = new SimpleDoubleProperty();
		
		setFirstName("guest");
		setLastName("");
		setEmail("");
		setPassword("");
		setMoney(0.0);
		setId(1);
	}
	
	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String fName) {
		this.firstName.set(fName);
	}
	
	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lName) {
		this.lastName.set(lName);
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}
	
	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}
	
	public Double getMoney() {
		return money.get();
	}

	public void setMoney(double money) {
		this.money.set(money);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return firstName.get() + " " + lastName.get();
	}
	
	public static User getInstance() {
		if(myInstance == null)
			myInstance = new User();
		return myInstance;
	}
}