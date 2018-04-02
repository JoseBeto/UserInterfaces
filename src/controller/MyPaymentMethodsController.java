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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
		
		paymentMethodsList.setCellFactory(param -> new ListCell<Object>() {
    		ImageView image = new ImageView();
    		@Override
    		public void updateItem(Object object, boolean empty) {
    			super.updateItem(object, empty);

    			if (empty) {
    				setText(null);
    				setGraphic(null);
    				return;
    			} else {
    				if(((PaymentMethod) object).getTypeMethod() == PaymentMethod.PAYPAL)
    					image.setImage(new Image("/view/paypal.png"));
    				else if(((PaymentMethod) object).getTypeMethod() == PaymentMethod.CREDIT_CARD)
    					image.setImage(new Image("/view/creditCard.png"));
    				image.setFitHeight(25);
    				image.setFitWidth(25);
    			}
    			setText(object.toString());
    			setGraphic(image);
    		}
    	});
	}
}
