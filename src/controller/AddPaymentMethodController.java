package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import database.PaymentMethodsGateway;
import database.UserTableGateway;
import userInterfaces.AlertHelper;
import model.CardPayment;
import model.PaymentMethod;
import model.PaypalMethod;
import model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AddPaymentMethodController implements Initializable, MyController 
{
	
	@FXML ComboBox<String> paymentMethodBox;
	ObservableList<String> methodsList;
	
	@FXML BorderPane paymentPane;
	@FXML GridPane paypalGrid;
	@FXML GridPane cardGrid;
	@FXML GridPane bankGrid;
	@FXML GridPane coinGrid;
	
	@FXML TextField paypalEmailText;
	@FXML PasswordField paypalPassText;
	
	@FXML TextField cardNumberText;
	@FXML TextField expDateText;
	@FXML TextField fullnameText;
	
	@FXML Button submitButton;
	
	private User user = User.getInstance();
	private UserTableGateway userGateway;
	private PaymentMethodsGateway payGateway;
	private int view;
	
	public AddPaymentMethodController(UserTableGateway userGateway, PaymentMethodsGateway payGateway, int view) {
		this.userGateway = userGateway;
		this.payGateway = payGateway;
		
		this.view = view;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		// TODO Auto-generated method stub
		methodsList = FXCollections.observableArrayList("Paypal", "Credit Card", "Bank Account", "Bitcoin");
		paymentMethodBox.getItems().setAll(methodsList);

		paymentPane.setCenter(null);
		paymentPane.setLeft(null);
		paymentPane.setRight(null);
		paymentPane.setBottom(null);

		//Prevents user from entering a non digit
		cardNumberText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*")) {
					cardNumberText.setText(oldValue);
				}
			}
		});

		//Prevents user from entering a non digit and more than one decimal point
		expDateText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("(0?[1-9]|1[0-2])?/?[0-9]?[0-9]?$")) {
					expDateText.setText(oldValue);
				}
			}
		});
	}

	//Action Event Listener for Category Box
	@FXML
	public void paymentMethodSelected(ActionEvent event) throws IOException
	{
		switch(paymentMethodBox.getSelectionModel().getSelectedIndex())
		{
			case 0: //Buy
				paymentPane.setCenter(paypalGrid);
				break;
			case 1: //Sell
				paymentPane.setCenter(cardGrid);
				break;
			case 2: //Contact Us
				System.out.println("You selected Bank Account.");
				//paymentPane.setCenter(null);
				paymentPane.setCenter(bankGrid);
				break;
			case 3: //About
				System.out.println("You selected Bitcoin");
				//paymentPane.setCenter(null);
				paymentPane.setCenter(coinGrid);
				break;
		}
	}
	
	@FXML
	public void submitPressed(ActionEvent event) throws IOException
	{
		PaymentMethod payMethod = null;	
		if( paymentPane.getCenter() == null )
		{
			System.out.println("You have not selected a payment method.");
		}
		else
		{
			switch(paymentMethodBox.getSelectionModel().getSelectedIndex())
			{
			case 0: //Buy
				payMethod = new PaypalMethod();
				payMethod.fillMethodDetails(paypalEmailText.getText(), paypalPassText.getText());
				break;
			case 1: //Sell
				payMethod = new CardPayment();
				payMethod.fillMethodDetails(cardNumberText.getText(), expDateText.getText(), fullnameText.getText());
				break;
			case 2: //Contact Us
				System.out.println("You selected Bank Account.");
				//paymentPane.setCenter(null);
				paymentPane.setCenter(bankGrid);
				break;
			case 3: //About
				System.out.println("You selected Bitcoin");
				//paymentPane.setCenter(null);
				paymentPane.setCenter(coinGrid);
				break;
			}
			if(!payMethod.validateMethod()) //Validation failed
				return;

			AlertHelper.showWarningMessage("Success!", "Payment method added!", AlertType.INFORMATION);

			payGateway.addPaymentMethod(payMethod);
			user.addPaymentMethod(payMethod);
			userGateway.updatePaymentMethods(user);

			if(view == AppController.CHECK_OUT)
				AppController.getInstance().changeView(view, null);
			else if(view == AccountController.PAYMETHODS)
				AppController.getInstance().changeView(AppController.MY_ACCOUNT, view);
			else if(view == AccountController.INFO)
				AppController.getInstance().changeView(AppController.MY_ACCOUNT, view);

		}
	}
}
