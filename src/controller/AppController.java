package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import database.AppException;
import database.ItemTableGateway;
import database.UserTableGateway;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.Item;
import model.User;
import userInterfaces.AlertHelper;

public class AppController implements Initializable {
	public final static int LIST = 1;
	public final static int ITEM_DETAIL = 2;
	public final static int LOGIN = 3;
	public final static int REGISTER = 4;
	public final static int MY_ACCOUNT = 5;
	public final static int SELL_ITEM = 6;
	public final static int MY_CART = 7;
	public final static int MY_LISTS = 8;
	public final static int ADD_FUNDS = 9;
	
	private static AppController myInstance = null;
	private BorderPane rootPane = null;
	private Connection conn;
	
	@FXML private ComboBox<String> categoryBox;
    @FXML private ComboBox<String> accountBox;
    @FXML private TextField searchBox;

	public AppController() {

	}
	
	public void changeView(int viewType, Object arg) throws AppException {
		try {
			MyController controller = null;
			URL fxmlFile = null;
			switch(viewType) {
				case LIST:
					fxmlFile = this.getClass().getResource("/view/ItemListView.fxml");
					controller = new ItemListController(new ItemTableGateway(conn));
					accountBox.setValue("Account");
					break;
				case ITEM_DETAIL:
					fxmlFile = this.getClass().getResource("/view/ItemDetailView.fxml");
					controller = new ItemDetailController((Item) arg, new UserTableGateway(conn));
					break;
				case LOGIN:
					fxmlFile = this.getClass().getResource("/view/LoginView.fxml");
					controller = new LoginController(new UserTableGateway(conn));
					break;
				case REGISTER:
					fxmlFile = this.getClass().getResource("/view/RegisterView.fxml");
					controller = new RegisterController(new UserTableGateway(conn));
					break;
				case MY_ACCOUNT:
					fxmlFile = this.getClass().getResource("/view/AccountView.fxml");
					controller = new AccountController(new UserTableGateway(conn));
					break;
				case SELL_ITEM:
					fxmlFile = this.getClass().getResource("/view/SellItemView.fxml");
					controller = new SellItemController(new ItemTableGateway(conn));
					break;
				case MY_CART:
					fxmlFile = this.getClass().getResource("/view/CartView.fxml");
					controller = new CartController(new UserTableGateway(conn), new ItemTableGateway(conn));
					break;
				case MY_LISTS:
					fxmlFile = this.getClass().getResource("/view/UserListView.fxml");
					controller = new UserListController(new ItemTableGateway(conn), new UserTableGateway(conn)
							, (String) arg);
					break;
				case ADD_FUNDS:
					fxmlFile = this.getClass().getResource("/view/AddFundsView.fxml");
					//controller = new CartController(new UserTableGateway(conn), new
					break;
			}
		
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(controller);
		
			Parent viewNode = loader.load();
			rootPane.setCenter(viewNode);
		} catch (IOException e) {
			throw new AppException(e);
		}
	}

    @FXML
    void searchEntered(ActionEvent event) {
    	System.out.println("Search entered: " + searchBox.getText());
    }
    
    @FXML
    void searchButtonClicked(MouseEvent event) {
    	System.out.println("Button clicked: " + searchBox.getText());
    }

    @FXML
    void accountBoxChanged(ActionEvent event) {
    	switch(accountBox.getValue()) {
	    	case "Login":
	    		changeView(LOGIN, null);
	    		break;
	    	case "Register":
	    		changeView(REGISTER, null);
	    		break;
	    	case "My Account":
	    		changeView(MY_ACCOUNT, null);
	    		break;
	    	case "Sell Item":
	    		changeView(SELL_ITEM, null);
	    		break;
	    	case "My Cart":
	    		changeView(MY_CART, null);
	    		break;
	    	case "My Lists":
	    		changeView(MY_LISTS, null);
	    		break;
	    	case "Log Out":
	    		User.changeInstance(new User());
				AlertHelper.showWarningMessage("Success!", "Logged out!", AlertType.INFORMATION);
				updateAccountBox();
				break;
    	}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ObservableList<String> data = FXCollections.observableArrayList("Login", "Register");
    	accountBox.getItems().setAll(data);
    }

    public void updateAccountBox() {
    	User user = User.getInstance();
    	if(user.getEmail().equals("guest")) {
    		Platform.runLater(() -> {
				ObservableList<String> data = FXCollections.observableArrayList("Login", "Register");
				accountBox.getItems().setAll(data);
				
				accountBox.setVisibleRowCount(2);
				
				changeView(LIST, null);
	    	});
		} else {
			if(user.getRole() == User.CUSTOMER) {
    			Platform.runLater(() -> {
        			ObservableList<String> data = FXCollections.observableArrayList("My Account", "My Cart", "My Lists", "Log Out");
    				accountBox.getItems().setAll(data);
    				
    				accountBox.setVisibleRowCount(4);
    				
    				changeView(LIST, null);
    	    	});
    		} else if(user.getRole() == User.SELLER) {
    			Platform.runLater(() -> {
        			ObservableList<String> data = FXCollections.observableArrayList("My Account", "Sell Item", "My Cart", "My Lists", "Log Out");
    				accountBox.getItems().setAll(data);
    				
    				accountBox.setVisibleRowCount(5);
    				
    				changeView(LIST, null);
    	    	});
    		}
		}
    }
	
	public static AppController getInstance() {
		if(myInstance == null)
			myInstance = new AppController();
		return myInstance;
	}
	
	public BorderPane getRootPane() {
		return rootPane;
	}

	public void setRootPane(BorderPane rootPane) {
		this.rootPane = rootPane;
	}

	public Connection getConnection() {
		return conn;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}
}