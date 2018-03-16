package controller;

import database.UserTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
    	User user = User.getInstance();
    	user.setFirstName(firstNameText.getText());
    	user.setLastName(lastNameText.getText());
    	user.setEmail(emailText.getText());
    	user.setPassword(passwordText.getText());
    	user.setMoney(0);
    	
    	gateway.registerUser(user);
    	
    	AlertHelper.showWarningMessage("Success!", "Account created!", AlertType.INFORMATION);
    	
    	AppController.getInstance().changeView(AppController.LIST, null);
    }
}
