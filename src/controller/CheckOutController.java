package controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import database.ItemTableGateway;
import database.PaymentMethodsGateway;
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
import model.PaymentMethod;
import model.User;
import userInterfaces.AlertHelper;

public class CheckOutController implements Initializable, MyController {

	@FXML private Label totalLabel;
    @FXML private TableView<Item> cartList;
    @FXML private TableColumn<Item, ImageView> imageColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, Integer> qtyColumn;
    @FXML private TableColumn<Item, String> priceColumn;
    @FXML private ComboBox<PaymentMethod> paymentMethodsBox;
    
	private User user = User.getInstance();
	private Double subtotal;
	private ObservableList<Item> items = FXCollections.observableArrayList();
	private ObservableList<PaymentMethod> paymentMethods = FXCollections.observableArrayList();
	private UserTableGateway gateway;
	private ItemTableGateway itemGateway;
	private PaymentMethodsGateway payGateway;
	
	public CheckOutController(UserTableGateway gateway, ItemTableGateway itemGateway, PaymentMethodsGateway payGateway
			, Double subtotal) {
    	this.gateway = gateway;
    	this.itemGateway = itemGateway;
    	this.subtotal = subtotal;
    	this.payGateway = payGateway;
	}

	public void test() {
		if( user.getMoney() < subtotal ) {	//Insufficient Funds
			AlertHelper.showWarningMessage("Error!", "Insufficient funds!", AlertType.ERROR);
			return;
		}
		else {	//Sufficient Funds
			if(AlertHelper.showDecisionMessage("Warning", "Are you sure you want to submit this transaction?")) {
				user.setMoney(user.getMoney() - subtotal);
				user.getCart().emptyCart();

				gateway.updateCart(user);
				AppController.getInstance().changeView(AppController.MY_CART, null);
				AlertHelper.showWarningMessage("Success!", "Your transaction was successful!", AlertType.INFORMATION);
			}
		}
	}

    @FXML
    void PlaceOrderButtonClicked(ActionEvent event) {
    	
    	
    	
    	AppController.getInstance().changeView(AppController.MY_CART, null);
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
		totalLabel.setText(String.format("Order Total: $%.2f", subtotal));
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
    	imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));

    	getItems();
    	cartList.setItems(items);
    	
    	HashMap<String, Integer> payMethods = user.getPaymentMethods();
		paymentMethods = payGateway.getPaymentMethods(payMethods);
		//paymentMethods.add("test");
		paymentMethodsBox.setItems(paymentMethods);
	}
}
