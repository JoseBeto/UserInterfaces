package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import database.UserTableGateway;
import userInterfaces.AlertHelper;
import model.CardPayment;
import model.PaymentMethod;
import model.PaypalMethod;
import model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AddFundsController implements Initializable 
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
	@FXML TextField paypalAmountRequested;
	
	@FXML TextField cardNumberText;
	@FXML TextField expDateText;
	@FXML TextField fullnameText;
	@FXML TextField amountText;
	
	@FXML Button submitButton;
	
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
				payMethod.fillMethodDetails(paypalEmailText.getText(), paypalPassText.getText(), paypalAmountRequested.getText());
				break;
			case 1: //Sell
				payMethod = new CardPayment();
				payMethod.fillMethodDetails(cardNumberText.getText(), expDateText.getText(), fullnameText.getText(), amountText.getText());
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

			if(AlertHelper.showConfirmationMessage("Are you sure you want to submit this transaction?", "Submit transaction?", "Press OK to Confirm.")
					&& payMethod.validateMethod())
			{
				System.out.println("Transaction Successful.");
				User.getInstance().setMoney(User.getInstance().getMoney() + payMethod.getAmount());
				new UserTableGateway(AppController.getInstance().getConnection()).updateUser(User.getInstance());
				AlertHelper.showConfirmationMessage("Your submission was successful!", "Transaction Complete.", "Press OK to Continue.");
				AppController.getInstance().changeView(AppController.MY_CART, null);
				User.getInstance().emptyCart();
			}
		}
	}
}
