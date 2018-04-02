package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import database.AppException;
import database.ItemTableGateway;
import database.PastOrdersGateway;
import database.PaymentMethodsGateway;
import database.UserTableGateway;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class AccountController implements MyController, Initializable{
    
	@FXML private BorderPane contentPane;
	private Connection conn;
	private Object arg;
	
	public final static int INFO = 1;
	public final static int PAYMETHODS = 2;
	public final static int ORDERS = 3;
	
	public AccountController(Connection conn, Object arg) {
		this.arg = arg;
    	this.conn = conn;
    }
	
	@FXML
    void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
    }

    @FXML
    void myInfoClicked(MouseEvent event) {
    	changeView(INFO, null);
    }
    
    @FXML
    void myPastOrdersClicked(MouseEvent event) {
    	changeView(ORDERS, null);
    }

    @FXML
    void myPaymentMethodsClicked(MouseEvent event) {
    	changeView(PAYMETHODS, null);
    }

	public void changeView(int viewType, Object arg) throws AppException {
		try {
			MyController controller = null;
			URL fxmlFile = null;
			switch(viewType) {
				case INFO:
					fxmlFile = this.getClass().getResource("/view/MyInfoView.fxml");
					controller = new MyInfoController(new UserTableGateway(conn));
					break;
				case PAYMETHODS:
					fxmlFile = this.getClass().getResource("/view/MyPaymentMethodsView.fxml");
					controller = new MyPaymentMethodsController(new UserTableGateway(conn), new PaymentMethodsGateway(conn));
					break;
				case ORDERS:
					fxmlFile = this.getClass().getResource("/view/MyPastOrdersView.fxml");
					controller = new MyPastOrdersController(new PastOrdersGateway(conn), new ItemTableGateway(conn));
					break;
			}
		
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(controller);
		
			Parent viewNode = loader.load();
			contentPane.setCenter(viewNode);
		} catch (IOException e) {
			throw new AppException(e);
		}
	}
	
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	if(arg == null)
    		changeView(INFO, null);
    	else
    		changeView((Integer) arg, null);
    }
}
