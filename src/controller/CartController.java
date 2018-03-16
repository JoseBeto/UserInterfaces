package controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;
import database.ItemTableGateway;
import database.UserTableGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
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
		ObservableList<Item> items = FXCollections.observableArrayList();
		ObservableList<String> itemNames = FXCollections.observableArrayList();
		ObservableList<Integer> itemQty = FXCollections.observableArrayList();

		Properties props = new Properties();
		try {
			props.load(new StringReader(cart.substring(1, cart.length() - 1).replace(",", "\n")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Entry<Object, Object> e : props.entrySet()) {
			Item item = itemGateway.getItemById(Integer.valueOf((String) e.getKey()));
			items.add(item);
			itemQty.add(Integer.valueOf((String) e.getValue()));
			itemNames.add(item.getName());
			
			cartList.getItems().add(item.getName());
		}
		
		cartList.setCellFactory(param -> new ListCell<String>() {
			ImageView image = new ImageView();
			@Override
			public void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				
				Item item;
				int qty;
				
				if (empty) {
					setText(null);
					setGraphic(null);
					return;
				} else {
					item = items.get(itemNames.indexOf(name));
					qty = itemQty.get(itemNames.indexOf(name));
					image.setImage(item.getImage());
					image.setFitHeight(100);
					image.setFitWidth(100);
				}
				setText("Name: " + name + "\tQty: " + qty);
				setGraphic(image);
			}
		});
	}
}
