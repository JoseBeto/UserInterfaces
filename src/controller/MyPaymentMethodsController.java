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
import model.Item;
import model.PaymentMethod;
import model.User;

public class MyPaymentMethodsController implements MyController, Initializable {

	@FXML private ListView<PaymentMethod> paymentMethodsList;
	
	private User user = User.getInstance();
	private UserTableGateway gateway;
	private PaymentMethodsGateway payGateway;
	private ObservableList<PaymentMethod> paymentMethods = FXCollections.observableArrayList();
	
	public MyPaymentMethodsController(UserTableGateway gateway, PaymentMethodsGateway payGateway) {
		this.gateway = gateway;
		this.payGateway = payGateway;
	}
	
	@FXML
    void addPaymentMethodClicked(ActionEvent event) {
		AppController.getInstance().changeView(AppController.ADD_FUNDS, null);
		
		//user.addPaymentMethod(paymentMethod);
		//gateway.updatePaymentMethods(user);
    }

    @FXML
    void removePaymentMethodClicked(ActionEvent event) {

    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		HashMap<String, Integer> payMethods = user.getPaymentMethods();
		paymentMethods = payGateway.getPaymentMethods(payMethods);
		
		paymentMethodsList.setItems(paymentMethods);
	}
}
