package controller;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Item;
import model.User;
import userInterfaces.AlertHelper;

public class CartController implements MyController, Initializable{

	//@FXML private ListView<String> cartList;
	//@FXML private ComboBox<String> qtyBox;
	@FXML private ComboBox<String> removeQty;
	@FXML private Label totalLabel;
	@FXML private TableView<Item> itemList;
    @FXML private TableColumn<?, ?> imageColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, Integer> qtyColumn;
    @FXML private TableColumn<Item, String> priceColumn;
	
	private UserTableGateway gateway;
	private ItemTableGateway itemGateway;
	private User user = User.getInstance();
	
	private ObservableList<Item> items;
	private ObservableList<String> itemNames;
	private ObservableList<Integer> itemQty;
    
	private boolean flag;
	private double subtotal;
	
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
		if( user.getMoney() < subtotal )
		{	//Insufficient Funds
			System.out.println("Insufficient Funds.");
			AppController.getInstance().changeView(AppController.ADD_FUNDS, null);
		}
		else
		{	//Sufficient Funds
			if(
				AlertHelper.showConfirmationMessage("Are you sure you want to submit this transaction?", "Submit transaction?", "Press OK to Confirm.")
			  )
			{
				User.getInstance().setMoney(User.getInstance().getMoney() - subtotal);
				User.getInstance().emptyCart();
				new UserTableGateway(AppController.getInstance().getConnection()).updateCart(User.getInstance());
				AlertHelper.showConfirmationMessage("Your transaction was successful!", "Transaction Complete.", "Press OK to Continue.");
				AppController.getInstance().changeView(AppController.LIST, null);
			}
		}
    }
	
	@FXML
    void listClicked(MouseEvent event) {
		String selected = cartList.getSelectionModel().getSelectedItem();
		if(selected == null)
			return;
		
		qtyBox.setValue(itemQty.get(itemNames.indexOf(selected)).toString());
		removeQty.setValue(itemQty.get(itemNames.indexOf(selected)).toString());
    }

    /*@FXML
    void qtyBoxChanged(ActionEvent event) {
    	if(cartList.getSelectionModel().getSelectedItem() == null)
    		return;
    	
    	Item item = items.get(itemNames.indexOf(cartList.getSelectionModel().getSelectedItem()));
    	
    	if(Integer.parseInt(qtyBox.getValue()) == itemQty.get(items.indexOf(item)))
    		return;

    	user.updateCart(item.getId(), Integer.parseInt(qtyBox.getValue()));
    	
    	gateway.updateCart(user);
    	
    	updateCart();
    	updateList();
    	updateTotalLabel();
    }*/

    @FXML
    void removeButtonClicked(ActionEvent event) {
    	if(cartList.getSelectionModel().getSelectedItem() == null)
    		return;
    	
    	Item item = items.get(itemNames.indexOf(cartList.getSelectionModel().getSelectedItem()));

    	if(!user.removeItemFromCart(item.getId(), Integer.parseInt(removeQty.getValue())))
    		return;
    	
    	gateway.updateCart(user);
    	
    	updateCart();
    	updateTotalLabel();
    }

    public void updateCart() {
    	//cartList.getItems().clear();
    	items.clear();
    	itemNames.clear();
    	itemQty.clear();
    	
    	String cart = user.getCart().toString();

    	Properties props = new Properties();
    	try {
    		props.load(new StringReader(cart.substring(1, cart.length() - 1).replace(",", "\n")));
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	} 
    	for(Entry<Object, Object> e : props.entrySet()) {
    		Item updatedItem = itemGateway.getItemById(Integer.valueOf((String) e.getKey()));
    		items.add(updatedItem);
    		itemQty.add(Integer.valueOf((String) e.getValue()));
    		itemNames.add(updatedItem.getName());

    		//cartList.getItems().add(updatedItem.getName());
    	}
    }
    
    public void updateList() {
    	flag = true;
    	
    	cartList.setCellFactory(param -> new ListCell<String>() {
    		ImageView image = new ImageView();
    		
    		@Override
    		public void updateItem(String name, boolean empty) {
    			super.updateItem(name, empty);
    			/* This gets rid of a bug where first item was being run through twice */
    			if(flag && !empty) {
    				flag = false;
    				return;
    			}
    			
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
    				image.setFitHeight(120);
    				image.setFitWidth(120);
    				
    				String s = "";
        			for(int i = 0; i < (25 - item.getName().length()); i++) {
        				s += " ";
        			}
        			
        			setFont(new Font("consolas", 14));
        			setText("Name: " + name + s + "Qty: " + qty + "\t\tPrice: " + String.format("$%.2f", item.getPrice()));
        			setGraphic(image);
    			}
    		}
    	});
    }
    
    public void updateTotalLabel() {
    	subtotal = 0.0;
    	int totalItems = 0;
    	
    	for(String name : itemNames) {
    		Item item = items.get(itemNames.indexOf(name));
    		int qty = itemQty.get(itemNames.indexOf(name));
    		subtotal += (item.getPrice() * qty);
    		totalItems += qty;
    	}

    	totalLabel.setText(String.format("Subtotal (%d items): $%.2f", totalItems, subtotal));
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	ObservableList<String> data = FXCollections.observableArrayList("1", "2", "3", "4", "5");
		removeQty.setItems(data);
		
		items = FXCollections.observableArrayList();
		itemNames = FXCollections.observableArrayList();
		itemQty = FXCollections.observableArrayList();

		updateCart();
		//updateList();
		//updateTotalLabel();
		
		
		
		
		
		
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		/*qtyColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    	qtyColumn.setOnEditCommit(
    	    new EventHandler<CellEditEvent<AuthorBook, String>>() {
    	    	public void handle(CellEditEvent<AuthorBook, String> t) {
    	    		double x = Integer.parseInt(t.getNewValue().toString().substring(0, t.getNewValue().length()));
    	            x /= 100;
    	    		((AuthorBook) t.getTableView().getItems().get(
    	                t.getTablePosition().getRow())
    	                ).setRoyalty(new BigDecimal(x));
    	        }
    	    }
    	);*/
    	
		itemList.setItems(items);
	}
}