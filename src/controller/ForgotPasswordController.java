package controller;

import java.net.URL;
import java.util.ResourceBundle;
import database.UserTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import userInterfaces.AlertHelper;

public class ForgotPasswordController implements Initializable, MyController {

	@FXML private BorderPane pane;
    @FXML private GridPane emailGrid;
    @FXML private TextField emailText;
    @FXML private GridPane securityGrid;
    @FXML private TextField securityAnswer;
    @FXML private Button submitButton;
    @FXML private Label securityQuestion;
    @FXML private GridPane resetGrid;
    @FXML private TextField newPasswordText;
	
    private UserTableGateway gateway;
    private String email;
    private String securityQA[];
    
	public ForgotPasswordController(UserTableGateway gateway) {
		this.gateway = gateway;
	}

	@FXML
	void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LOGIN, null);
	}
	
	@FXML
    void nextButtonClicked(ActionEvent event) {
		this.email = emailText.getText();
		String securityQA = gateway.getUserSecurityQA(email);
		
		if(securityQA == null) {
			AlertHelper.showWarningMessage("Error!", "Email does not exist!", AlertType.ERROR);
			return;
		}
		this.securityQA = securityQA.split(",");
		
		pane.setCenter(securityGrid);
		
		securityQuestion.setText(this.securityQA[0]);
    }

	@FXML
	void submitPressed(ActionEvent event) {
		if(!securityAnswer.getText().equals(securityQA[1])) {
			AlertHelper.showWarningMessage("Error!", "Security answer does not match!", AlertType.ERROR);
			return;
		}
		
		pane.setCenter(resetGrid);
	}
	
	@FXML
    void resetPasswordClicked(ActionEvent event) {
		String newPassword = newPasswordText.getText();
		if(newPassword.equals("")) {
			AlertHelper.showWarningMessage("Error!", "Password field is empty!", AlertType.ERROR);
			return;
		}
		
		gateway.updatePassword(newPassword, email);
		
		AlertHelper.showWarningMessage("Success!", "Password updated!", AlertType.INFORMATION);
		
		AppController.getInstance().changeView(AppController.LOGIN, null);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pane.setLeft(null);
		pane.setRight(null);
		pane.setCenter(emailGrid);
	}
}
