package model;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert.AlertType;
import userInterfaces.AlertHelper;

public class User {

	private static User myInstance = null;
	
	private int id;
	private int role;
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty email;
	private SimpleStringProperty password;
	private SimpleDoubleProperty money;
	private HashMap<Integer, Integer> cart;
	
	public final static int CUSTOMER = 2;
	public final static int SELLER = 1;

	public User() {
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.money = new SimpleDoubleProperty();
		this.cart = new HashMap<Integer, Integer>();
		
		setFirstName("guest");
		setLastName("");
		setEmail("");
		setPassword("");
		setMoney(0.0);
		setId(1);
		setRole(CUSTOMER);
	}
	
	public User(String fName, String lName, String email, String password, Double money, String cart, int role) {
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.money = new SimpleDoubleProperty();
		this.cart = new HashMap<Integer, Integer>();
		
		setFirstName(fName);
		setLastName(lName);
		setEmail(email);
		setPassword(password);
		setMoney(money);
		setCart(cart);
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
	
	public HashMap<Integer, Integer> getCart() {
		return cart;
	}

	public void setCart(String cart) {
		if(cart.equals(""))
			return;
		Properties props = new Properties();
		try {
			props.load(new StringReader(cart.substring(1, cart.length() - 1).replace(",", "\n")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Entry<Object, Object> e : props.entrySet()) {
		    this.cart.put(Integer.valueOf((String) e.getKey()), Integer.valueOf((String) e.getValue()));
		}
	}
	
	public Boolean addToCart(Item item, int qty) {
		HashMap<Integer, Integer> cart = getCart();
		if(cart.containsKey(item.getId())) {
			if(cart.get(item.getId()) + qty > 5) {
				AlertHelper.showWarningMessage("Error!", "Cannot have more than 5 of this "
						+ "item!", AlertType.ERROR);
				return false;
			} else {
				qty += cart.get(item.getId());
				cart.remove(item.getId());
			}
		}
		cart.put(item.getId(), qty);
		return true;
	}
	
	public void updateCart(int id, int qty) {
		cart.put(id, qty);
	}
	
	public void emptyCart()
	{
		this.cart = new HashMap<Integer, Integer>();
	}
	
	public Boolean removeItemFromCart(int id, int qty) {
		if(cart.get(id) < qty) {
			AlertHelper.showWarningMessage("Error!", "Select a valid qty to be removed!", AlertType.ERROR);
			return false;
		} else if(cart.get(id) == qty) {
			cart.remove(id);
			return true;
		}
		
		cart.put(id, cart.get(id) - qty);
		return true;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getRole() {
		return role;
	}
	
	public void setRole(int role) {
		this.role = role;
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