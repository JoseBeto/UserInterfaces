package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import database.ItemTableGateway;
import database.PastOrdersGateway;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Item;
import model.PastOrder;
import model.User;

public class MyPastOrdersController implements MyController, Initializable {

	@FXML private ListView<PastOrder> pastOrdersList;
	
	private PastOrdersGateway pastOrdersGateway;
	private ItemTableGateway itemTableGateway;
	
	public MyPastOrdersController(PastOrdersGateway pastOrdersGateway, ItemTableGateway itemTableGateway) {
		this.pastOrdersGateway = pastOrdersGateway;
		this.itemTableGateway = itemTableGateway;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		User user = User.getInstance();

		ObservableList<PastOrder> pastOrders = pastOrdersGateway.getPastOrders(user.getPastOrders());

		pastOrdersList.setItems(pastOrders);
		pastOrdersList.setCellFactory(new Callback<ListView<PastOrder>, ListCell<PastOrder>>() {
			@Override
			public ListCell<PastOrder> call(ListView<PastOrder> arg0) {
				return new ListCell<PastOrder>() {

					@Override
					protected void updateItem(PastOrder pastOrder, boolean bln) {
						super.updateItem(pastOrder, bln);
						if (pastOrder != null) {
							ArrayList<HBox> hBoxes = new ArrayList<HBox>();
							HBox spacing = new HBox(new Label(""));
							hBoxes.add(spacing);

							String header = String.format("%-30sDate Purchased: %-50s", " ", pastOrder.getDatePurchased());
							Label headerLabel = new Label(header);
							headerLabel.setStyle("-fx-font: 14 consolas;");
							HBox headerHBox = new HBox(headerLabel);
							hBoxes.add(headerHBox);

							for(Entry<Integer, Integer> e : pastOrder.getItems().entrySet()) {
								Item item = itemTableGateway.getItemById(e.getKey());

								ImageView image = new ImageView(item.getImage());
								image.setFitHeight(100);
								image.setFitWidth(100);

								String s = String.format("\tName: %-30s Qty: %-10s Price: %-20s"
										, item.getName(), e.getValue(), item.getPrice() * e.getValue());

								Label label = new Label(s);
								label.setStyle("-fx-font: 14 consolas;");
								HBox hBox = new HBox(image, label);
								hBoxes.add(hBox);
							}
							String footer = String.format("%-21sTotal: %-29.2f Payment Method: %-30s"
									, " ", pastOrder.getTotal(), pastOrder.getPaymentMethod());
							Label footerLabel = new Label(footer);
							footerLabel.setStyle("-fx-font: 14 consolas;");
							HBox footerHBox = new HBox(footerLabel);
							hBoxes.add(footerHBox);
							HBox spacing2 = new HBox(new Label(""));
							hBoxes.add(spacing2);

							VBox vBox = new VBox();
							vBox.getChildren().addAll(hBoxes);
							vBox.setSpacing(10);
							setGraphic(vBox);
						}
					}
				};
			}
		});
	}
}
