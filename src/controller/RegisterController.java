package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.UserTableGateway;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.User;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import userInterfaces.AlertHelper;

public class RegisterController implements Initializable, MyController {

	@FXML private TextField emailText;
    @FXML private TextField firstNameText;
    @FXML private TextField lastNameText;
    @FXML private TextField passwordText;
    @FXML private ComboBox<String> securityQuestionBox;
    @FXML private TextField securityQuestionAnswer;
    
    private UserTableGateway gateway;
    
    public RegisterController(UserTableGateway gateway) {
    	this.gateway = gateway;
    }

    @FXML
    void registerButtonClicked(ActionEvent event) {
    	if(!emailText.getText().matches("[a-zA-Z.]+[@][a-zA-z]+[.][a-zA-Z]{2,3}$")) {
    		AlertHelper.showWarningMessage("Error!", "Invalid email!", AlertType.ERROR);
    		return;
    	} else if(emailText.getText().equals("")) {
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
    	} else if(securityQuestionBox.getValue() == null) {
    		AlertHelper.showWarningMessage("Error!", "Security question not selected!", AlertType.ERROR);
    		return;
    	} else if(securityQuestionAnswer.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Security answer field is empty!", AlertType.ERROR);
    		return;
    	}
    	
    	String securityQA = securityQuestionBox.getValue() + "," + securityQuestionAnswer.getText();
    	User user = new User(firstNameText.getText(), lastNameText.getText(), emailText.getText()
    			, passwordText.getText(), securityQA, 0.0, "", "{Wish_List={}}", "", "[]", User.CUSTOMER);
    	
    	if(!gateway.registerUser(user)) {
    		AlertHelper.showWarningMessage("Error!", "Email already taken!", AlertType.ERROR);
    		return;
    	}
    	
    	User.changeInstance(user);

    	AlertHelper.showWarningMessage("Success!", "Account created!", AlertType.INFORMATION);

    	AppController.getInstance().changeView(AppController.LIST, null);
    	AppController.getInstance().updateAccountBox();
    }

    @FXML
    void backButtonClicked(MouseEvent event) {
    	AppController.getInstance().changeView(AppController.LIST, null);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	//Prevents user from entering a space
    	emailText.textProperty().addListener(new ChangeListener<String>() {
    		@Override
    		public void changed(ObservableValue<? extends String> observable, String oldValue, 
    				String newValue) {
    			if (!newValue.matches("[a-zA-Z.0-9]*[@]?[a-zA-z]*[.]?[a-zA-Z]*$")) {
    				emailText.setText(oldValue);
				}
    		}
    	});
    	
    	ObservableList<String> data = FXCollections.observableArrayList("What is your favorite movie?", "What is your favorite video game?"
    			, "What is your favorite movie genre?", "What is your favorite video game genre?");
    	securityQuestionBox.getItems().setAll(data);
    }
}
