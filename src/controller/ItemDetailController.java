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
    @FXML private ComboBox<?> addToListBox;
    
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
		if(user.getId() == 1) {
			AlertHelper.showWarningMessage("Error!", "Not logged in!", AlertType.ERROR);
			return;
		}
		
		if(user.addToCart(item, Integer.parseInt(qtyBox.getValue()))) {
			gateway.updateCart(user);
			AlertHelper.showWarningMessage("Success!", qtyBox.getValue() + " items added to cart", AlertType.INFORMATION);
		}
    }

	@FXML
    void addToListBoxChanged(ActionEvent event) {
		User user = User.getInstance();
		System.out.println("Retreive user " + user.getFirstName() + "'s list options");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameLabel.setText(item.getName());
		priceLabel.setText("Price: " + String.format("$%.2f", item.getPrice()));
		itemImage.setImage(item.getImage());
		descLabel.setText(item.getDesc());
		
		ObservableList<String> data = FXCollections.observableArrayList("1", "2", "3", "4", "5");
		qtyBox.setItems(data);
		qtyBox.setStyle("-fx-padding:0 0 0 200");
		qtyBox.setValue("1");
	}
}
