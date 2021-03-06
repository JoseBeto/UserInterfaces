package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import database.PaymentMethodsGateway;
import database.UserTableGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.User;
import userInterfaces.AlertHelper;

public class MyInfoController implements MyController, Initializable {

	@FXML private TextField emailText;
    @FXML private TextField firstNameText;
    @FXML private TextField lastNameText;
    @FXML private TextField passwordText;
    @FXML private Label walletLabel;
    @FXML private CheckBox sellerCheckbox;
    @FXML private ComboBox<String> securityQuestionBox;
    @FXML private TextField securityAnswer;
    
    private UserTableGateway gateway;
	private User user = User.getInstance();
	
	public MyInfoController(UserTableGateway gateway) {
		this.gateway = gateway;
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
    	} else if(securityAnswer.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Security answer field is empty!", AlertType.ERROR);
    		return;
    	}
    	String securityQA = securityQuestionBox.getValue() + "," + securityAnswer.getText();
    	
    	user.setEmail(emailText.getText());
    	user.setFirstName(firstNameText.getText());
    	user.setLastName(lastNameText.getText());
    	user.setPassword(passwordText.getText());
    	if(sellerCheckbox.isSelected()) {
    		user.setRole(User.SELLER);
    		gateway.updateItemsSelling(user);
    	}
    	else
    		user.setRole(User.CUSTOMER);
    	user.setSecurityQA(securityQA);

    	gateway.updateUser(user);

    	AlertHelper.showWarningMessage("Success!", "Account updated!", AlertType.INFORMATION);
    	AppController.getInstance().updateAccountBox();
    }
    
    @FXML
    void addFundsClicked(ActionEvent event) {
    	HashMap<String, Integer> payMethods = user.getPaymentMethods();
    	Pair<String,Object> amountPaymentMethod = AlertHelper.showPaymentMethods(new PaymentMethodsGateway
    			(AppController.getInstance().getConnection()).getPaymentMethods(payMethods));

    	if(amountPaymentMethod == null) //User hit cancel
    		return;
    	else if(amountPaymentMethod.getKey().isEmpty()){ //User didn't enter an amount
    		return;
    	} else {
    		Double money = Double.parseDouble(amountPaymentMethod.getKey());
    		user.setWallet(user.getWallet() + money);
    		
    		AppController.getInstance().changeView(AppController.MY_ACCOUNT, null);
    		AlertHelper.showWarningMessage("Success!", (String.format("$%.2f dollars added to wallet!", money)) , AlertType.INFORMATION);
    		
    		gateway.updateUser(user);
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		emailText.setText(user.getEmail());
		firstNameText.setText(user.getFirstName());
		lastNameText.setText(user.getLastName());
		passwordText.setText(user.getPassword());
		walletLabel.setText(String.format("$%.2f", user.getWallet()));
		if(user.getRole() == User.SELLER)
			sellerCheckbox.setSelected(true);

		ObservableList<String> data = FXCollections.observableArrayList("What is your favorite movie?", "What is your favorite video game?"
				, "What is your favorite movie genre?", "What is your favorite video game genre?");
		securityQuestionBox.getItems().setAll(data);

		String securityQA = gateway.getUserSecurityQA(user.getEmail());
		String secQA[] = securityQA.split(",");
		
		securityQuestionBox.getSelectionModel().select(secQA[0]);
		securityAnswer.setText(secQA[1]);
	}
}
