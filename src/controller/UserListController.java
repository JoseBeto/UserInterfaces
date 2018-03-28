package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class UserListController implements MyController, Initializable {

	@FXML private ComboBox<?> moveListBox;
	@FXML private ListView<?> userLists;
	@FXML private ListView<?> itemList;

	public UserListController() {
		
	}
	
	@FXML
	void removeListClicked(ActionEvent event) {

	}

	@FXML
	void moveListChanged(ActionEvent event) {

	}

	@FXML
	void removeItemClicked(ActionEvent event) {

	}

	@FXML
	void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
