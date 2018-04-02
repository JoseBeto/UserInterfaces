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
import javafx.scene.control.ListView;
import model.PaymentMethod;
import model.User;
import userInterfaces.AlertHelper;

public class MyPaymentMethodsController implements MyController, Initializable {

	@FXML private ListView<Object> paymentMethodsList;
	
	private User user = User.getInstance();
	private UserTableGateway gateway;
	private PaymentMethodsGateway payGateway;
	private ObservableList<Object> paymentMethods = FXCollections.observableArrayList();
	
	public MyPaymentMethodsController(UserTableGateway gateway, PaymentMethodsGateway payGateway) {
		this.gateway = gateway;
		this.payGateway = payGateway;
	}
	
	@FXML
	void addPaymentMethodClicked(ActionEvent event) {
		AppController.getInstance().changeView(AppController.ADD_FUNDS, AccountController.PAYMETHODS);
	}

	@FXML
	void removePaymentMethodClicked(ActionEvent event) {
		if(paymentMethodsList.getSelectionModel().getSelectedItem() == null)
			return;

		String message = "Are you sure you want to remove this payment method?";
		String title = "Warning";
		if(!AlertHelper.showDecisionMessage(title, message))
			return;
		
		Object selectedPaymentMethod = paymentMethodsList.getSelectionModel().getSelectedItem();
		
		paymentMethods.remove(selectedPaymentMethod);
		user.removePaymentMethod((PaymentMethod) selectedPaymentMethod);
		
		gateway.updatePaymentMethods(user);
		payGateway.removePaymentMethod((PaymentMethod) selectedPaymentMethod);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		HashMap<String, Integer> payMethods = user.getPaymentMethods();
		paymentMethods = payGateway.getPaymentMethods(payMethods);

		paymentMethodsList.setItems(paymentMethods);
	}
}
