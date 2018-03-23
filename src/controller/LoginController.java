package controller;

import database.UserTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import userInterfaces.AlertHelper;

public class LoginController implements MyController{

	@FXML private TextField emailText;
    @FXML private PasswordField passwordText;
	
    private UserTableGateway gateway;
    
    public LoginController(UserTableGateway gateway) {
    	this.gateway = gateway;
    }
	
	@FXML
    void loginButtonClicked(ActionEvent event) {
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
			AppController.getInstance().updateAccountBox();
		}
    }
	
	@FXML
    void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
    }
}
