package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import database.AppException;
import database.ItemTableGateway;
import database.PastOrdersGateway;
import database.PaymentMethodsGateway;
import database.TransactionTableGateway;
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
import javafx.scene.layout.BorderPane;
import model.Item;
import model.User;
import userInterfaces.AlertHelper;

public class AppController implements Initializable {
	public final static int LIST = 1;
	public final static int ITEM_DETAIL = 2;
	public final static int LOGIN = 3;
	public final static int REGISTER = 4;
	public final static int FORGOT_PASSWORD = 5;
	public final static int MY_ACCOUNT = 6;
	public final static int SELL_ITEM = 7;
	public final static int MY_CART = 8;
	public final static int MY_LISTS = 9;
	public final static int CHECK_OUT = 10;
	public final static int ADD_FUNDS = 11;
	public final static int LIST_BY_CATEGORY = 12;
	public final static int SEARCH = 13;
	
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
				case FORGOT_PASSWORD:
					fxmlFile = this.getClass().getResource("/view/ForgotPasswordView.fxml");
					controller = new ForgotPasswordController(new UserTableGateway(conn));
					break;
				case MY_ACCOUNT:
					fxmlFile = this.getClass().getResource("/view/AccountView.fxml");
					controller = new AccountController(conn, arg);
					break;
				case SELL_ITEM:
					fxmlFile = this.getClass().getResource("/view/SellItemView.fxml");
					controller = new SellItemController(new ItemTableGateway(conn), new UserTableGateway(conn));
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
				case CHECK_OUT:
					fxmlFile = this.getClass().getResource("/view/CheckOutView.fxml");
					controller = new CheckOutController(new UserTableGateway(conn), new ItemTableGateway(conn)
							, new PaymentMethodsGateway(conn), new PastOrdersGateway(conn), new TransactionTableGateway(conn));
					break;
				case ADD_FUNDS:
					fxmlFile = this.getClass().getResource("/view/AddPaymentMethodView.fxml");
					controller = new AddPaymentMethodController(new UserTableGateway(conn), new PaymentMethodsGateway(conn)
							, (Integer) arg);
					break;
				case LIST_BY_CATEGORY:
					String table_column_name = null;
					String search_term = null;
					if( arg != null )
					{
						String[] arr = (String[])arg;
						table_column_name = arr[0];
						search_term = arr[1];
					}
					fxmlFile = this.getClass().getResource("/view/ItemListView.fxml");
					controller = new ItemListController(new ItemTableGateway(conn), table_column_name, search_term);
					break;
				case SEARCH:
					String[] search_terms = {null, null};
					if(arg != null) {
						String[] arr = (String[])arg;
						search_terms[0] = arr[0];
						search_terms[1] = arr[1];
					}
					fxmlFile = this.getClass().getResource("/view/ItemListView.fxml");
					controller = new ItemListController(new ItemTableGateway(conn), search_terms);
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
    	switch(categoryBox.getValue()) {
			case "All Items":
				changeView(SEARCH, new String[] {searchBox.getText(), ""});
				break;
			case "Consoles":
				changeView(SEARCH, new String[]{searchBox.getText(), "console"});
				break;
			case "Games":
				changeView(SEARCH, new String[]{searchBox.getText(), "game"});
				break;
			case "Accessories":
				changeView(SEARCH, new String[]{searchBox.getText(), "Accessory"});
				break;
    	}
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

    @FXML
    void categoryBoxChanged(ActionEvent event) {
    	if(!searchBox.getText().equals(""))
    		searchEntered(event);
    	else {
    		switch(categoryBox.getValue()) {
    		case "All Items":
    			changeView(LIST, null);
    			break;
    		case "Consoles":
    			changeView(LIST_BY_CATEGORY, new String[]{"description", "console"});
    			break;
    		case "Games":
    			changeView(LIST_BY_CATEGORY, new String[]{"description", "game"});
    			break;
    		case "Accessories":
    			changeView(LIST_BY_CATEGORY, new String[]{"description", "Accessory"});
    			break;
    		}
    	}
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ObservableList<String> data = FXCollections.observableArrayList("Login", "Register");
    	ObservableList<String> categoriesData = FXCollections.observableArrayList("All Items", "Consoles", "Games", "Accessories");    	
    	accountBox.getItems().setAll(data);
    	categoryBox.getItems().setAll(categoriesData);
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