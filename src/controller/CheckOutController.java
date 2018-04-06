package controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import database.ItemTableGateway;
import database.PastOrdersGateway;
import database.PaymentMethodsGateway;
import database.TransactionTableGateway;
import database.UserTableGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import model.Item;
import model.PastOrder;
import model.Transaction;
import model.User;
import userInterfaces.AlertHelper;

public class CheckOutController implements Initializable, MyController {

	@FXML private Label totalLabel;
    @FXML private TableView<Item> cartList;
    @FXML private TableColumn<Item, ImageView> imageColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, Integer> qtyColumn;
    @FXML private TableColumn<Item, String> priceColumn;
    @FXML private ComboBox<Object> paymentMethodsBox;
    
	private User user = User.getInstance();
	private Double total = 0.0;
	private ObservableList<Item> items = FXCollections.observableArrayList();
	private ObservableList<Object> paymentMethods = FXCollections.observableArrayList();
	private UserTableGateway gateway;
	private ItemTableGateway itemGateway;
	private PaymentMethodsGateway payGateway;
	private PastOrdersGateway pOrderGateway;
	private TransactionTableGateway tGateway;
	
	public CheckOutController(UserTableGateway gateway, ItemTableGateway itemGateway
			, PaymentMethodsGateway payGateway, PastOrdersGateway pOrderGateway, TransactionTableGateway tGateway) {
    	this.gateway = gateway;
    	this.itemGateway = itemGateway;
    	this.payGateway = payGateway;
    	this.pOrderGateway = pOrderGateway;
    	this.tGateway = tGateway;
	}

	@FXML
	void PlaceOrderButtonClicked(ActionEvent event) {
		int walletIndex = paymentMethods.size() - 2;
		int selectedIndex = paymentMethodsBox.getSelectionModel().getSelectedIndex();
		if(selectedIndex == -1) {
			AlertHelper.showWarningMessage("Error!", "No payment method selected!", AlertType.ERROR);
    		return;
		}
		if(!AlertHelper.showDecisionMessage("Warning", "Are you sure you want to submit this transaction?")) {
			return;
		}
		
		String paymentMethod = paymentMethodsBox.getSelectionModel().getSelectedItem().toString();

		if(selectedIndex == walletIndex) {
			if(user.getWallet() < total) {	//Insufficient Funds
				AlertHelper.showWarningMessage("Error!", "Insufficient funds!", AlertType.ERROR);
				return;
			}
			user.setWallet(user.getWallet() - total);
			paymentMethod = "Wallet";
		}

		/**********************
		 * Add item/s to past orders
		 */
		PastOrder pastOrder = new PastOrder(user.getCart().getCart()
				, paymentMethod, total, new Date().toString());
		int i = pOrderGateway.addPastOrder(pastOrder);
		user.addPastOrder(i);
		gateway.updatePastOrders(user);
		
		/**********************
		 * If items are associated with a seller, add
		 * 		funds to seller
		 */
		for(Entry<Integer, Integer> e : user.getCart().getCart().entrySet()) {
			Item item = itemGateway.getItemById(e.getKey());
			if(item.getSellerId() != null) {
				//Calculate total
				Double total = item.getPrice() * e.getValue();
				//Create transaction
				Transaction transaction = new Transaction(item.getId(), e.getValue(), item.getSellerId()
						, total, new Date().toString());
				tGateway.addTransaction(transaction);
				//Fund seller
				gateway.addFundsToUser(item.getSellerId(), total);
			}
		}
		
		/**********************
		 * Empty user cart
		 */
		user.getCart().emptyCart();
		gateway.updateCart(user);
		
		AlertHelper.showWarningMessage("Success!", "Your transaction was successful!", AlertType.INFORMATION);
		AppController.getInstance().changeView(AppController.MY_CART, null);
	}
	
	@FXML
    void paymentMethodChanged(ActionEvent event) {
		int addPaymentIndex = paymentMethods.size() - 1;
		int selectedIndex = paymentMethodsBox.getSelectionModel().getSelectedIndex();
		
		
		if(selectedIndex == addPaymentIndex) {
			AppController.getInstance().changeView(AppController.ADD_FUNDS, AppController.CHECK_OUT);
			return;
		}
    }

    @FXML
    void backButtonClicked(MouseEvent event) {
    	AppController.getInstance().changeView(AppController.MY_CART, null);
    }

    public void getItems() {
    	String cart = user.getCart().getCart().toString();

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
    	}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getItems();
    	for(Item item : items) {
    		total += (item.getPrice() * item.getQty());
    	}
		
		totalLabel.setText(String.format("Order Total: $%.2f", total));
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
    	imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));

    	cartList.setItems(items);
    	
    	HashMap<String, Integer> payMethods = user.getPaymentMethods();
		paymentMethods = payGateway.getPaymentMethods(payMethods);
		paymentMethods.addAll(String.format("Wallet ($%.2f)", user.getWallet()), "Add Payment Method");
		paymentMethodsBox.setItems(paymentMethods);
	}
}
