package controller;

import java.net.URL;
import java.util.ResourceBundle;
import database.ItemTableGateway;
import database.TransactionTableGateway;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import model.Item;
import model.Transaction;
import model.User;

public class MySellerInfoController implements MyController, Initializable {

	@FXML private ListView<Item> itemsSellingList;
    @FXML private ListView<Transaction> transactonsList;
	
	private ItemTableGateway itemGateway;
	private TransactionTableGateway tGateway;
	private User user = User.getInstance();
	
	public MySellerInfoController(ItemTableGateway itemGateway, TransactionTableGateway tGateway) {
		this.itemGateway = itemGateway;
		this.tGateway = tGateway;
	}
	
	@FXML
    void ItemsSellingClicked(MouseEvent event) {
		/*
		 * Allow seller to edit item details
		 */
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Transaction> transactions = tGateway.getTransactions(user.getEmail());
		
		transactonsList.setItems(transactions);
		
		transactonsList.setCellFactory(param -> new ListCell<Transaction>() {
    		ImageView image = new ImageView();
			@Override
			public void updateItem(Transaction transaction, boolean empty) {
    			super.updateItem(transaction, empty);

    			if (empty) {
    				setText(null);
    				setGraphic(null);
    				return;
    			} else {
    				image.setImage(new Image("/view/transaction.png"));
    				image.setFitHeight(25);
    				image.setFitWidth(25);
    			}
    			Item item = itemGateway.getItemById(transaction.getItemId());
    			
    			String s = "Customer purchased " + transaction.getQty() + 
    					" units of your item: " + item.getName() + "\non " + transaction.getDate()
    					+ ". You received " + String.format("$%.2f", transaction.getTotal());
    			setFont(new Font("consolas", 14));
    			setText(s);
    			setGraphic(image);
    		}
    	});
		
		ObservableList<Item> itemsSelling = itemGateway.getItemsSelling(user.getItemsSelling());
		
		itemsSellingList.setItems(itemsSelling);
		
		itemsSellingList.setCellFactory(param -> new ListCell<Item>() {
    		ImageView image = new ImageView();
			@Override
			public void updateItem(Item item, boolean empty) {
    			super.updateItem(item, empty);

    			if (empty) {
    				setText(null);
    				setGraphic(null);
    				return;
    			} else {
    				image.setImage(item.getImage());
    				image.setFitHeight(100);
    				image.setFitWidth(100);
    			}
    			setFont(new Font("consolas", 14));
    			setText(item.toString());
    			setGraphic(image);
    		}
    	});
	}

}
