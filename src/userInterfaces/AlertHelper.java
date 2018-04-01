package userInterfaces;

import java.util.Optional;
import controller.AccountController;
import controller.AppController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class AlertHelper {
	
	public static void showWarningMessage(String title, String header, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.showAndWait();
	}
	
	public static boolean showDecisionMessage(String title, String header) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.getButtonTypes().remove(0);
		alert.getButtonTypes().add(ButtonType.YES);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES){
		    return true;
		} else {
		    // ... user chose CANCEL or closed the dialog
			return false;
		}
	}
	
	public static String showInputMessage(String title, String header) {
		TextInputDialog alert = new TextInputDialog();
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText("Enter new list name:");

		// Traditional way to get the response value.
		Optional<String> result = alert.showAndWait();
		if (result.isPresent()){
		    return result.get();
		}

		// The Java 8 way to get the response value (with lambda expression).
		result.ifPresent(name -> result.get());
		return null;
	}
	
	public static Pair<String, Object> showPaymentMethods(ObservableList<Object> paymentMethods) {
		Dialog<Pair<String, Object>> dialog = new Dialog<>();
		dialog.setTitle("Add Funds To Wallet");
		dialog.setHeaderText("Select payment method and amount requesting.");

		ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		paymentMethods.add("Add Payment Method");
		ComboBox<Object> paymentMethodsBox = new ComboBox<Object>(paymentMethods);
		paymentMethodsBox.setPromptText("Payment Methods");
		TextField amountRequesting = new TextField();
		amountRequesting.setPromptText("Amount Requesting");

		Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
		addButton.setDisable(true);

		//Prevents user from entering a non digit and more than one decimal point
		amountRequesting.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					amountRequesting.setText(oldValue);
				}
			}
		});

		paymentMethodsBox.valueProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				addButton.setDisable(newValue.equals(""));
				if(newValue.equals("Add Payment Method")) {
					AppController.getInstance().changeView(AppController.ADD_FUNDS, AccountController.INFO);
					dialog.close();
					return;
				}
			}
		});

		grid.add(new Label("Payment Method:"), 0, 0);
		grid.add(paymentMethodsBox, 1, 0);
		grid.add(new Label("Amount requesting:"), 0, 1);
		grid.add(amountRequesting, 1, 1);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButtonType) {
				return new Pair<>(amountRequesting.getText(), paymentMethodsBox.getSelectionModel().getSelectedItem());
			}
			return null;
		});

		Optional<Pair<String, Object>> result = dialog.showAndWait();

		if(result.isPresent()){
		    return result.get();
		}
		return null;
	}
}
