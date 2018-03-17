package controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.util.Properties;
import java.util.Map.Entry;
import database.ItemTableGateway;
import database.UserTableGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Item;
import model.User;

public class CartController implements MyController, Initializable{

	@FXML private ListView<String> cartList;
	@FXML private ComboBox<String> qtyBox;
	@FXML private ComboBox<String> removeQty;
	
	private UserTableGateway gateway;
	private ItemTableGateway itemGateway;
	private User user = User.getInstance();
	
	private ObservableList<Item> items;
	private ObservableList<String> itemNames;
	private ObservableList<Integer> itemQty;
	
	public CartController(UserTableGateway gateway, ItemTableGateway itemGateway) {
    	this.gateway = gateway;
    	this.itemGateway = itemGateway;
    }
	
	@FXML
    void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
    }
	
	@FXML
    void checkOutButtonClicked(ActionEvent event) {

    }
	
	@FXML
    void listClicked(MouseEvent event) {
		qtyBox.setValue(itemQty.get(itemNames.indexOf(cartList.getSelectionModel().getSelectedItem())).toString());
		removeQty.setValue(itemQty.get(itemNames.indexOf(cartList.getSelectionModel().getSelectedItem())).toString());
    }

    @FXML
    void qtyBoxChanged(ActionEvent event) {
    	if(cartList.getSelectionModel().getSelectedItem() == null)
    		return;
    	
    	Item item = items.get(itemNames.indexOf(cartList.getSelectionModel().getSelectedItem()));
    	
    	if(Integer.parseInt(qtyBox.getValue()) == itemQty.get(items.indexOf(item)))
    		return;

    	user.updateCart(item.getId(), Integer.parseInt(qtyBox.getValue()));
    	
    	gateway.updateCart(user);
    	
    	updatedList();
    }

    @FXML
    void removeButtonClicked(ActionEvent event) {
    	if(cartList.getSelectionModel().getSelectedItem() == null)
    		return;
    	
    	Item item = items.get(itemNames.indexOf(cartList.getSelectionModel().getSelectedItem()));

    	if(!user.removeItemFromCart(item.getId(), Integer.parseInt(removeQty.getValue())))
    		return;
    	
    	gateway.updateCart(user);
    	
    	updatedList();
    }

    public void updatedList() {
    	cartList.getItems().clear();
    	items.clear();
    	itemNames.clear();
    	itemQty.clear();

    	String cart = user.getCart().toString();
    	
    	items = FXCollections.observableArrayList();
    	itemNames = FXCollections.observableArrayList();
    	itemQty = FXCollections.observableArrayList();

    	Properties props = new Properties();
    	try {
    		props.load(new StringReader(cart.substring(1, cart.length() - 1).replace(",", "\n")));
    	} catch (IOException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	} 
    	for(Entry<Object, Object> e : props.entrySet()) {
    		Item updatedItem = itemGateway.getItemById(Integer.valueOf((String) e.getKey()));
    		items.add(updatedItem);
    		itemQty.add(Integer.valueOf((String) e.getValue()));
    		itemNames.add(updatedItem.getName());

    		cartList.getItems().add(updatedItem.getName());
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
    			String s = "";
    			for(int i = 0; i < (25 - item.getName().length()); i++) {
    				s += " ";
    			}
    			setFont(Font.font("consolas"));
    			setText("Name: " + name + s + "Qty: " + qty);
    			setGraphic(image);
    		}
    	});
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	ObservableList<String> data = FXCollections.observableArrayList("1", "2", "3", "4", "5");
		qtyBox.setItems(data);
		qtyBox.setStyle("-fx-padding:0 0 0 65");
		
		removeQty.setItems(data);
		
		items = FXCollections.observableArrayList();
		itemNames = FXCollections.observableArrayList();
		itemQty = FXCollections.observableArrayList();

		updatedList();
	}
}
