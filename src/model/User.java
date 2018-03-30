package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {

	private static User myInstance = null;
	
	private int role;
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty email;
	private SimpleStringProperty password;
	private SimpleDoubleProperty money;
	private Cart cart;
	private Lists lists;
	
	public final static int CUSTOMER = 2;
	public final static int SELLER = 1;

	public User() {
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.money = new SimpleDoubleProperty();
		
		setFirstName("guest");
		setLastName("");
		setEmail("guest");
		setPassword("");
		setMoney(0.0);
		setRole(CUSTOMER);
	}
	
	public User(String fName, String lName, String email, String password, Double money, String cart, String lists, int role) {
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.money = new SimpleDoubleProperty();
		this.cart = new Cart(cart);
		this.lists = new Lists(lists);
		
		setFirstName(fName);
		setLastName(lName);
		setEmail(email);
		setPassword(password);
		setMoney(money);
		setRole(role);
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
	
	public int getRole() {
		return role;
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public Lists getLists() {
		return lists;
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
	
	public static void changeInstance(User user) {
		myInstance = user;
	}
}