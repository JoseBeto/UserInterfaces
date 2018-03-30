package model;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;
import javafx.scene.control.Alert.AlertType;
import userInterfaces.AlertHelper;

public class Cart {

	private HashMap<Integer, Integer> cart = new HashMap<Integer, Integer>();
	
	public Cart(String cart) {
		setCart(cart);
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
	
	public void updateCart(int id, int qty) {
		cart.put(id, qty);
	}
	
	public void emptyCart() {
		this.cart = new HashMap<Integer, Integer>();
	}
	
	public Boolean addToCart(Item item, int qty) {
		HashMap<Integer, Integer> cart = getCart();
		if(cart.containsKey(item.getId())) {
			if(cart.get(item.getId()) + qty > 5) {
				AlertHelper.showWarningMessage("Error!", "Cannot have more than 5 of this "
						+ "item in your cart!", AlertType.ERROR);
				return false;
			} else {
				qty += cart.get(item.getId());
				cart.remove(item.getId());
			}
		}
		cart.put(item.getId(), qty);
		return true;
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
}
