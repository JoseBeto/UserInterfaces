package controller;

import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import model.Item;
import model.User;
import userInterfaces.AlertHelper;

public class UserListController implements MyController, Initializable {

	@FXML private ComboBox<String> moveListBox;
	@FXML private ListView<String> userLists;
	@FXML private ListView<Item> itemList;
	
	private ItemTableGateway itemGateway;
	private UserTableGateway userGateway;
	private String startingList;
	private User user = User.getInstance();
	
	private ObservableList<Item> items;
	private ObservableList<String> listNames;
	
	public UserListController(ItemTableGateway itemGateway, UserTableGateway userGateway, String startingList) {
		this.itemGateway = itemGateway;
		this.userGateway = userGateway;
		this.startingList = startingList;
	}
	
	@FXML
    void addListClicked(ActionEvent event) {
		String newList = AlertHelper.showInputMessage("Create list", "Create new list");
		
		if(newList != null && !newList.equals("")) {
			user.getLists().createList(newList);
			listNames.add(newList);
			userGateway.updateLists(user);
	    	
	    	AlertHelper.showWarningMessage("Success!", "List: " + newList + " created!", AlertType.INFORMATION);
		}
		return;
    }
	
	@FXML
	void removeListClicked(ActionEvent event) {
		String listName = userLists.getSelectionModel().getSelectedItem();
		if(listName == null)
			return;
		
		String message = "Are you sure you want to remove list: " + listName;
		String title = "Warning";
		if(!AlertHelper.showDecisionMessage(title, message))
			return;
		
		listNames.remove(listName);
		
		user.getLists().removeList(listName);
		
		userGateway.updateLists(user);
		
		/********************
		 * Updating items shown
		 * 
		 */
		items.clear();
		userListClicked(null);
	}

	@FXML
	void moveListClicked(ActionEvent event) {
		Item item = itemList.getSelectionModel().getSelectedItem();
		String toListName = moveListBox.getSelectionModel().getSelectedItem();

		if(item == null || toListName == null)
			return;

		String fromListName = userLists.getSelectionModel().getSelectedItem();

		items.remove(item);

		user.getLists().addItemToList(toListName, item.getId());
		user.getLists().removeItemFromList(fromListName, item.getId());

		userGateway.updateLists(user);
		
		AppController.getInstance().changeView(AppController.MY_LISTS, fromListName);
		AlertHelper.showWarningMessage("Success!", "Item moved from list: " +
				fromListName + " to list: " + toListName, AlertType.INFORMATION);
	}

	@FXML
	void removeItemClicked(ActionEvent event) {
		Item item = itemList.getSelectionModel().getSelectedItem();
		String listName = userLists.getSelectionModel().getSelectedItem();
		
		if(item == null)
    		return;
    	
		String message = "Are you sure you want to remove item: " + item
				+" from your list: " + listName;
		String title = "Warning";
		if(!AlertHelper.showDecisionMessage(title, message))
			return;

		items.remove(item);
		
    	user.getLists().removeItemFromList(listName, item.getId());
    	userGateway.updateLists(user);
	}
	
	@FXML
    void addToCartButtonClicked(ActionEvent event) {
		Item item = itemList.getSelectionModel().getSelectedItem();
		String listName = userLists.getSelectionModel().getSelectedItem();

		if(item == null || listName == null)
			return;
		
		if(user.getCart().addToCart(item, 1)) {
			items.remove(item);
			user.getLists().removeItemFromList(listName, item.getId());
			userGateway.updateCart(user);
			userGateway.updateLists(user);
			
			AlertHelper.showWarningMessage("Success!", "item added to cart", AlertType.INFORMATION);
		}
    }
	
	@FXML
    void userListClicked(MouseEvent event) {
		String userListName = userLists.getSelectionModel().getSelectedItem();
		
		if(userListName == null)
			return;
		
		moveListBox.setValue(null);
		/*********************
		 * Update moveListBox to other lists
		 * 
		 */
		ObservableList<String> otherListNames = user.getLists().getListNames();
		otherListNames.remove(userListName);
		
		moveListBox.setItems(otherListNames);
		
		/*********************
		 * Set itemList to all items in selected list
		 * 
		 */
		
		items = FXCollections.observableArrayList();
		
		for(Entry<Integer, Integer> e : user.getLists().getListWithName(userListName).entrySet()) {
			items.add(itemGateway.getItemById(e.getKey()));
		}
		
		itemList.setItems(items);

    	itemList.setCellFactory(param -> new ListCell<Item>() {
    		ImageView image = new ImageView();
    		@Override
    		public void updateItem(Item item, boolean empty) {
    			super.updateItem(item, empty);

    			if (empty) {
    				setText(null);
    				setGraphic(null);
    				return;
    			} else {;
    				image.setImage(item.getImage());
    				image.setFitHeight(120);
    				image.setFitWidth(120);
    			}
    			String s = String.format("\t%-50s %-20s", item.getName(), item.getFormattedPrice());
    			setFont(new Font("consolas", 14));
    			setText(s);
    			setGraphic(image);
    		}
    	});
	}

	@FXML
	void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		listNames = user.getLists().getListNames();
		userLists.setItems(listNames);
		if(startingList == null)
			userLists.getSelectionModel().select(0);
		else
			userLists.getSelectionModel().select(startingList);
		userListClicked(null);

		userLists.setCellFactory(TextFieldListCell.forListView());	
		
		userLists.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
			@Override
			public void handle(ListView.EditEvent<String> t) {
				String oldName = listNames.get(userLists.getEditingIndex());
				String newName = t.getNewValue();
				
				String message = "Are you sure you want to rename list: " + oldName
						+ " to: " + newName + "?";
				String title = "Warning";
				if(!AlertHelper.showDecisionMessage(title, message))
					return;
				
				user.getLists().renameList(newName, oldName);
				listNames.setAll(user.getLists().getListNames());
				userGateway.updateLists(user);
				
				userLists.getSelectionModel().select(listNames.indexOf(newName));
			}
		});
	}
}
