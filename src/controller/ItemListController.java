package controller;

import java.net.URL;
import java.util.ResourceBundle;
import database.ItemTableGateway;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import model.Item;

public class ItemListController implements Initializable, MyController {
	@FXML private ListView<Item> itemList;
	private ObservableList<Item> items;
	private ItemTableGateway gateway;

	public ItemListController(ItemTableGateway gateway) {
    	this.gateway = gateway;
    	items = this.gateway.getItems();
    }
	
	public ItemListController(ObservableList<Item> items) {
    	this.items = items;
    }
	
    @FXML
    void ItemClicked(MouseEvent event) {
    	if(event.getClickCount() > 1) {
    		Item item = itemList.getSelectionModel().getSelectedItem();
   			if(item != null)
    			AppController.getInstance().changeView(AppController.ITEM_DETAIL, item);
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.itemList.setItems(items);

    	itemList.setCellFactory(param -> new ListCell<Item>() {
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
    				image.setFitHeight(75);
    				image.setFitWidth(75);
    			}
    			String s = "";
    			for(int i = 0; i < (25 - item.getName().length()); i++) {
    				s += " ";
    			}
    			setFont(Font.font("consolas"));
    			setText(item.getName() + s + item.getPrice());
    			setGraphic(image);
    		}
    	});
	}
}