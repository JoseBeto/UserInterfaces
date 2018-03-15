package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import database.AppException;
import database.ItemTableGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.Item;

public class AppController implements Initializable {
	public final static int LIST = 1;
	public final static int ITEM_DETAIL = 2;
	public final static int LOGIN = 3;

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
					break;
				case ITEM_DETAIL:
					fxmlFile = this.getClass().getResource("/view/ItemDetailView.fxml");
					controller = new ItemDetailController((Item) arg);
					break;
				case LOGIN:
					fxmlFile = this.getClass().getResource("/view/LoginView.fxml");
					controller = new LoginController();
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
    void accountBoxChanged(ActionEvent event) throws IOException {
    	switch(accountBox.getValue()) {
	    	case "Login":
	    		changeView(LOGIN, null);
	    		break;
	    }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> data = FXCollections.observableArrayList("Login");
		accountBox.setItems(data);
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