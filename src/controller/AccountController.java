package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.UserTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.User;
import userInterfaces.AlertHelper;

public class AccountController implements MyController, Initializable{

	@FXML private TextField emailText;
    @FXML private TextField firstNameText;
    @FXML private TextField lastNameText;
    @FXML private TextField passwordText;
    @FXML private Label moneyLabel;
    
	private UserTableGateway gateway;
	private User user = User.getInstance();
	
	public AccountController(UserTableGateway gateway) {
    	this.gateway = gateway;
    }
	
	@FXML
    void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
    	if(emailText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Email field is empty!", AlertType.ERROR);
    		return;
    	} else if(firstNameText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "First name field is empty!", AlertType.ERROR);
    		return;
    	} else if(lastNameText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Last name field is empty!", AlertType.ERROR);
    		return;
    	} else if(passwordText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Password field is empty!", AlertType.ERROR);
    		return;
    	}
    	
    	user.setEmail(emailText.getText());
    	user.setFirstName(firstNameText.getText());
    	user.setLastName(lastNameText.getText());
    	user.setPassword(passwordText.getText());
    	
    	gateway.updateUser(user);
    	
    	AlertHelper.showWarningMessage("Success!", "Account updated!", AlertType.INFORMATION);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	emailText.setText(user.getEmail());
    	firstNameText.setText(user.getFirstName());
    	lastNameText.setText(user.getLastName());
    	passwordText.setText(user.getPassword());
    	moneyLabel.setText("$"+user.getMoney());
	}
}
