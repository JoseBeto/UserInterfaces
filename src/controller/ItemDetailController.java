package controller;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Item;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
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
    
	private Item item;

	public ItemDetailController(Item item) {
		this.item = item;
    }
	
	@FXML
    void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
    }
	
	@FXML
    void addToCartButtonClicked(ActionEvent event) {

    }

	@FXML
    void addToListBoxClicked(ActionEvent event) {

    }

    @FXML
    void buyNowButtonClicked(ActionEvent event) {

    }

    @FXML
    void qtyBoxClicked(ActionEvent event) {
    	System.out.println("Qty changed to: " + qtyBox.getValue());
    }

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameLabel.setText(item.getName());
		priceLabel.setText("$" + item.getPrice());
		itemImage.setImage(item.getImage());
		descLabel.setText(item.getDesc());
		
		ObservableList<String> data = FXCollections.observableArrayList("1", "2", "3", "4", "5");
		qtyBox.setItems(data);
		qtyBox.setStyle("-fx-padding:0.25 0 0.25 110");
		qtyBox.setValue("1");
	}
}
