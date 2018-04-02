package controller;

import java.net.URL;
import java.util.ResourceBundle;
import database.PastOrdersGateway;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.PastOrder;
import model.User;

public class MyPastOrdersController implements MyController, Initializable {

	@FXML private ListView<PastOrder> pastOrdersList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		User user = User.getInstance();
		
		PastOrdersGateway gateway = new PastOrdersGateway(AppController.getInstance().getConnection());
		ObservableList<PastOrder> test = gateway.getPastOrders(user.getPastOrders());
		
		for(PastOrder pastOrder : test) {
			System.out.println(pastOrder.getDatePurchased());
			System.out.println(pastOrder.getItems());
			System.out.println(pastOrder.getTotal());
			System.out.println(pastOrder.getPaymentMethod());
		}
	}

}
