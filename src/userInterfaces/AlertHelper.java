package userInterfaces;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

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
}
