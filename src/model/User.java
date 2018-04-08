package model;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {

	private static User myInstance = null;
	
	private int role;
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty email;
	private SimpleStringProperty password;
	private SimpleStringProperty securityQA;
	private SimpleDoubleProperty wallet;
	private Cart cart;
	private Lists lists;
	private HashMap<String, Integer> paymentMethods = new HashMap<String, Integer>();
	private ArrayList<Integer> pastOrders = new ArrayList<Integer>();
	private ArrayList<Integer> itemsSelling = new ArrayList<Integer>();
	
	public final static int CUSTOMER = 2;
	public final static int SELLER = 1;

	public User() {
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.securityQA = new SimpleStringProperty();
		this.wallet = new SimpleDoubleProperty();
		
		setFirstName("guest");
		setLastName("");
		setEmail("guest");
		setPassword("");
		setWallet(0.0);
		setRole(CUSTOMER);
	}
	
	public User(String fName, String lName, String email, String password, String securityQA, Double wallet
			, String cart, String lists, String paymentMethods, String pastOrders, int role) {
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.securityQA = new SimpleStringProperty();
		this.wallet = new SimpleDoubleProperty();
		this.cart = new Cart(cart);
		this.lists = new Lists(lists);
		
		setFirstName(fName);
		setLastName(lName);
		setEmail(email);
		setPassword(password);
		setSecurityQA(securityQA);
		setWallet(wallet);
		setRole(role);
		setPaymentMethods(paymentMethods);
		setPastOrders(pastOrders);
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
	
	public String getSecurityQA() {
		return securityQA.get();
	}

	public void setSecurityQA(String securityQA) {
		this.securityQA.set(securityQA);
	}
	
	public Double getWallet() {
		return wallet.get();
	}

	public void setWallet(double wallet) {
		this.wallet.set(wallet);
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
	
	public void addPaymentMethod(PaymentMethod paymentMethod) {
		paymentMethods.put(paymentMethod.getKey(), paymentMethod.getTypeMethod());
	}
	
	public void removePaymentMethod(PaymentMethod paymentMethod) {
		paymentMethods.remove(paymentMethod.getKey());
	}
	
	public HashMap<String, Integer> getPaymentMethods() {
		return paymentMethods;
	}
	
	public void setPaymentMethods(String paymentMethods) {
		if(paymentMethods.equals(""))
			return;
		Properties props = new Properties();
		try {
			props.load(new StringReader(paymentMethods.substring(1, paymentMethods.length() - 1).replace(",", "\n")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Entry<Object, Object> e : props.entrySet()) {
		    this.paymentMethods.put((String) e.getKey(), Integer.valueOf((String) e.getValue()));
		}
	}
	
	public void addPastOrder(Integer i) {
		pastOrders.add(i);
	}
	
	public ArrayList<Integer> getPastOrders() {
		return pastOrders;
	}
	
	public void setPastOrders(String pastOrders) {
		if(pastOrders.equals("[]"))
			return;
		
		List<String> list = Arrays.asList(pastOrders.substring(1, pastOrders.length() - 1).split(", "));
		for(String s : list) {
			this.pastOrders.add(Integer.valueOf(s));
		}
	}
	
	public void addItemSelling(Integer i) {
		itemsSelling.add(i);
	}
	
	public ArrayList<Integer> getItemsSelling() {
		return itemsSelling;
	}
	
	public void setItemsSelling(String itemsSelling) {
		if(itemsSelling.equals("[]"))
			return;
		
		List<String> list = Arrays.asList(itemsSelling.substring(1, itemsSelling.length() - 1).split(", "));
		for(String s : list) {
			this.itemsSelling.add(Integer.valueOf(s));
		}
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