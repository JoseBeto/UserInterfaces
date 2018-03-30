package controller;

import database.UserTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import model.Item;
import model.User;
import userInterfaces.AlertHelper;

public class CreateListController implements MyController {
	
	@FXML private TextField listNameText;

	private UserTableGateway userGateway;
	private Item item;
	
	public CreateListController(UserTableGateway userGateway, Item item) {
		this.userGateway = userGateway;
		this.item = item;
	}
	
    @FXML
    void createListButtonClicked(ActionEvent event) {
    	if(listNameText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "List name not specified!", AlertType.ERROR);
    	}
    	
    	User user = User.getInstance();
    	
    	user.getLists().createList(listNameText.getText());
    	userGateway.updateLists(user);
    	
    	AlertHelper.showWarningMessage("Success!", "List: " + listNameText.getText() + " created!", AlertType.INFORMATION);
    	AppController.getInstance().changeView(AppController.ITEM_DETAIL, item);
    }

    @FXML
    void backButtonClicked(MouseEvent event) {
    	AppController.getInstance().changeView(AppController.ITEM_DETAIL, item);
    }
}
