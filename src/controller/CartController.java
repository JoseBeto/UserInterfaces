package controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import model.Item;
import model.User;
import userInterfaces.AlertHelper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CartController implements MyController, Initializable {
	@FXML private Label totalLabel;
	@FXML private TableView<Item> cartList;
    @FXML private TableColumn<Item, ImageView> imageColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, Integer> qtyColumn;
    @FXML private TableColumn<Item, String> priceColumn;
	private UserTableGateway gateway;
	private ItemTableGateway itemGateway;
	private User user = User.getInstance();
	private ObservableList<Item> items = FXCollections.observableArrayList();;
	private ObservableList<String> itemNames = FXCollections.observableArrayList();;
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
			if(AlertHelper.showConfirmationMessage("Are you sure you want to submit this transaction?", "Submit transaction?", "Press OK to Confirm."))
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
	void removeButtonClicked(ActionEvent event) {
		if(cartList.getSelectionModel().getSelectedItem() == null)
			return;

		String message = "Are you sure you want to remove this item from your cart?";
		String title = "Warning";
		if(!AlertHelper.showDecisionMessage(title, message))
			return;

		Item item = items.get(itemNames.indexOf(cartList.getSelectionModel().getSelectedItem().getName()));

		if(!user.removeItemFromCart(item.getId(), item.getQty()))
			return;

		gateway.updateCart(user);

		updateCart();
		updateTotalLabel();
	}

	public void updateCart() {
		items.clear();
		itemNames.clear();
    	
    	String cart = user.getCart().toString();

    	Properties props = new Properties();
    	try {
    		props.load(new StringReader(cart.substring(1, cart.length() - 1).replace(",", "\n")));
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	} 
    	for(Entry<Object, Object> e : props.entrySet()) {
    		Item updatedItem = itemGateway.getItemById(Integer.valueOf((String) e.getKey()));
    		updatedItem.setQty(Integer.valueOf((String) e.getValue()));
    		items.add(updatedItem);
    		itemNames.add(updatedItem.getName());
    	}
    }
    
    public void updateTotalLabel() {
    	subtotal = 0.0;
    	int totalItems = 0;
    	
    	for(String name : itemNames) {
    		Item item = items.get(itemNames.indexOf(name));
    		int qty = item.getQty();
    		subtotal += (item.getPrice() * qty);
    		totalItems += qty;
    	}

    	totalLabel.setText(String.format("Subtotal (%d items): $%.2f", totalItems, subtotal));
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	updateCart();
    	updateTotalLabel();

    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
    	imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));

    	ObservableList<Integer> data = FXCollections.observableArrayList(1, 2, 3, 4, 5);
    	qtyColumn.setCellFactory(tc -> {
    		ComboBox<Integer> combo = new ComboBox<>();
    		combo.setPrefWidth(100);
    		combo.getItems().addAll(data);
    		TableCell<Item, Integer> cell = new TableCell<Item, Integer>() {
    			@Override
    			protected void updateItem(Integer reason, boolean empty) {
    				super.updateItem(reason, empty);
    				if (empty) {
    					setGraphic(null);
    				} else {
    					combo.setValue(reason);
    					setGraphic(combo);
    				}
    			}
    		};
    		combo.setOnAction(e -> {
    			String itemName = cartList.getColumns().get(1).getCellData(cell.getIndex()).toString();
    			Item item = items.get(itemNames.indexOf(itemName));
    			
    			user.updateCart(item.getId(), combo.getValue());
    			updateCart();
    			updateTotalLabel();
    			
    			gateway.updateCart(user);
    		});
    		return cell ;
    	});

    	cartList.setItems(items);
    }
}