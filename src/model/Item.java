package model;

import database.ItemTableGateway;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item {

	private int id;
	private SimpleStringProperty name;
	private SimpleDoubleProperty price;
	private SimpleStringProperty image;
	private SimpleStringProperty desc;
	private SimpleStringProperty sellerId;
	private int qty;
	
	private ItemTableGateway gateway;
	
	public Item(String name, Double price, String desc, String image, int qty, String sellerId) {
		this.name = new SimpleStringProperty();
		this.price = new SimpleDoubleProperty();
		this.image = new SimpleStringProperty();
		this.desc = new SimpleStringProperty();
		this.sellerId = new SimpleStringProperty();
		
		setName(name);
		setPrice(price);
		setImage(image);
		setDesc(desc);
		setQty(qty);
		setSellerId(sellerId);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public double getPrice() {
		return price.get();
	}
	
	public String getFormattedPrice() {
		return String.format("$%.2f", getPrice());
	}

	public void setPrice(double price) {
		this.price.set(price);
	}
	
	public Image getImage() {
		return (new Image(image.get()));
	}
	
	public ImageView getImageView() {
		ImageView imageView = new ImageView(image.get());
		imageView.setFitHeight(200);
		imageView.setFitWidth(200);
		
		return (imageView);
	}
	
	public String getImageString() {
		return image.get();
	}

	public void setImage(String image) {
		this.image.set(image);
	}
	
	public String getDesc() {
		return desc.get();
	}

	public void setDesc(String desc) {
		this.desc.set(desc);
	}
	
	public int getQty() {
		return qty;
	}
	
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public String getSellerId() {
		return sellerId.get();
	}

	public void setSellerId(String sellerId) {
		this.sellerId.set(sellerId);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public ItemTableGateway getGateway() {
		return gateway;
	}

	public void setGateway(ItemTableGateway gateway) {
		this.gateway = gateway;
	}
	
	public SimpleStringProperty nameProperty() {
		return name;
	}
	public SimpleDoubleProperty priceProperty() {
		return price;
	}

	@Override
	public String toString() {
		return getName();
	}
}
