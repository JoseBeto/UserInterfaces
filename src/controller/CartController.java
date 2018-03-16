package controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

import database.ItemTableGateway;
import database.UserTableGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Item;
import model.User;

public class CartController implements MyController, Initializable{

	@FXML private ListView<String> cartList;
	
	private UserTableGateway gateway;
	private ItemTableGateway itemGateway;
	private User user = User.getInstance();
	
	public CartController(UserTableGateway gateway, ItemTableGateway itemGateway) {
    	this.gateway = gateway;
    	this.itemGateway = itemGateway;
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String cart = user.getCart().toString();
		ObservableList<String> items = FXCollections.observableArrayList ("");

		Properties props = new Properties();
		try {
			props.load(new StringReader(cart.substring(1, cart.length() - 1).replace(",", "\n")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Entry<Object, Object> e : props.entrySet()) {
			Item item = itemGateway.getItemById(Integer.valueOf((String) e.getKey()));
		    System.out.println(item.getName());
			//items.add();
		}
		
        cartList.setItems(items);
		System.out.println(user.getCart().toString());
	}
}
