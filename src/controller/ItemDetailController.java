package controller;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Item;
import model.User;
import userInterfaces.AlertHelper;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import database.UserTableGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ItemDetailController implements Initializable, MyController {
	@FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private ImageView itemImage;
    @FXML private Label descLabel;
    
    @FXML private ComboBox<String> qtyBox;
    @FXML private ComboBox<String> addToListBox;
    
    private UserTableGateway gateway;
	private Item item;

	public ItemDetailController(Item item, UserTableGateway gateway) {
		this.item = item;
		this.gateway = gateway;
    }
	
	@FXML
    void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
    }
	
	@FXML
    void addToCartButtonClicked(ActionEvent event) {
		User user = User.getInstance();
		if(user.getEmail().equals("guest")) {
			AppController.getInstance().changeView(AppController.LOGIN, null);
			return;
		}
		
		if(user.getCart().addToCart(item, Integer.parseInt(qtyBox.getValue()))) {
			gateway.updateCart(user);
			AlertHelper.showWarningMessage("Success!", qtyBox.getValue() + " items added to cart", AlertType.INFORMATION);
		}
    }

	@FXML
    void addToListBoxChanged(ActionEvent event) {
		User user = User.getInstance();
		if(user.getEmail().equals("guest")) {
			AppController.getInstance().changeView(AppController.LOGIN, null);
			return;
		}

		String listName = addToListBox.getValue();
		
		if(listName.equals("Create list...")) {
			AppController.getInstance().changeView(AppController.ITEM_DETAIL, item);
			
			String newList = AlertHelper.showInputMessage("Create list", "Create new list");
			
			if(newList != null && !newList.equals("")) {
				user.getLists().createList(newList);
		    	user.getLists().addItemToList(newList, item.getId());
				gateway.updateLists(user);

				AlertHelper.showWarningMessage("Success!", "List: " + newList + " created and item: " + item.getName()
					+ " added to list!", AlertType.INFORMATION);
		    	AppController.getInstance().changeView(AppController.ITEM_DETAIL, item);
			}
			return;
		}
		
		user.getLists().addItemToList(listName, item.getId());
		gateway.updateLists(user);

		AppController.getInstance().changeView(AppController.ITEM_DETAIL, item);
		AlertHelper.showWarningMessage("Success!", item.getName() + " added to list: " + listName, AlertType.INFORMATION);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameLabel.setText(item.getName());
		priceLabel.setText("Price: " + item.getFormattedPrice());
		itemImage.setImage(item.getImage());
		descLabel.setText(item.getDesc());

		ObservableList<String> data = FXCollections.observableArrayList("1", "2", "3", "4", "5");
		qtyBox.setItems(data);
		qtyBox.setStyle("-fx-padding:0 0 0 200");
		qtyBox.setValue("1");
		
		ObservableList<String> listData = FXCollections.observableArrayList();
		if(!User.getInstance().getEmail().equals("guest")) {
			listData.setAll(User.getInstance().getLists().getListNames());
		}
		listData.add("Create list...");
		addToListBox.setItems(listData);
	}
}
