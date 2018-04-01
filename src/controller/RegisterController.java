package controller;

import database.UserTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.User;
import javafx.scene.control.Alert.AlertType;
import userInterfaces.AlertHelper;

public class RegisterController implements MyController{

	@FXML private TextField emailText;
    @FXML private TextField firstNameText;
    @FXML private TextField lastNameText;
    @FXML private TextField passwordText;
    
    private UserTableGateway gateway;
    
    public RegisterController(UserTableGateway gateway) {
    	this.gateway = gateway;
    }

    @FXML
    void registerButtonClicked(ActionEvent event) {
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
    	
    	for(User user : gateway.getUsers()) {
    		if(user.getEmail().equals(emailText.getText())) {
    			AlertHelper.showWarningMessage("Error!", "Email already taken!", AlertType.ERROR);
        		return;
    		}
    	}
    	
    	User user = new User(firstNameText.getText(), lastNameText.getText(), 
    			emailText.getText(), passwordText.getText(), 0.0, "", "{Wish_List={}}", "", User.CUSTOMER);
    	User.changeInstance(user);
    	
    	gateway.registerUser(user);
    	
    	AlertHelper.showWarningMessage("Success!", "Account created!", AlertType.INFORMATION);
    	
    	AppController.getInstance().changeView(AppController.LIST, null);
    	AppController.getInstance().updateAccountBox();
    }
    
    @FXML
    void backButtonClicked(MouseEvent event) {
    	AppController.getInstance().changeView(AppController.LIST, null);
    }
}
