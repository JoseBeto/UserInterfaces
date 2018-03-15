package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController implements MyController{

	@FXML private TextField emailText;
    @FXML private TextField passwordText;
	
	public LoginController() {
		
	}
	
	@FXML
    void loginButtonClicked(ActionEvent event) {
		System.out.println("Check if email: " + emailText.getText() + " exists with password: " + passwordText.getText());
    }
}
