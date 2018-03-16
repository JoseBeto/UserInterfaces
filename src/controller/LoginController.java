package controller;

import database.UserTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.User;
import userInterfaces.AlertHelper;

public class LoginController implements MyController{

	@FXML private TextField emailText;
    @FXML private TextField passwordText;
	
    private UserTableGateway gateway;
    
    public LoginController(UserTableGateway gateway) {
    	this.gateway = gateway;
    }
	
	@FXML
    void loginButtonClicked(ActionEvent event) {
		System.out.println("Check if email: " + emailText.getText() + " exists with password: " + passwordText.getText());
		
		if(emailText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Email field is empty!", AlertType.ERROR);
    		return;
    	} else if(passwordText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Password field is empty!", AlertType.ERROR);
    		return;
    	}
		
		if(gateway.loginUser(emailText.getText(), passwordText.getText())) {
			AlertHelper.showWarningMessage("Success!", "Logged in!", AlertType.INFORMATION);
			AppController.getInstance().changeView(AppController.LIST, null);
		}
    }
}
