package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import database.ItemTableGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import model.Item;
import userInterfaces.AlertHelper;

public class SellItemController implements MyController{

    @FXML private TextField nameText;
    @FXML private TextField priceText;
    @FXML private TextArea description;
    @FXML private ImageView imageAddedIndicator;
    
    private File file;
    private Boolean imageAdded = false;
    private ItemTableGateway gateway;
	
	public SellItemController(ItemTableGateway gateway) {
		this.gateway = gateway;
	}
	
	@FXML
    void saveButtonClicked(ActionEvent event) {
		if(nameText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Name field is empty!", AlertType.ERROR);
    		return;
    	} else if(priceText.getText().equals("")) {
    		AlertHelper.showWarningMessage("Error!", "Price field is empty!", AlertType.ERROR);
    		return;
    	} else if(!imageAdded) {
    		AlertHelper.showWarningMessage("Error!", "No image added!", AlertType.ERROR);
    		return;
    	}
		
		String name = nameText.getText().replaceAll(" ", "_");
		String image = "/itemIcons/" + name + ".jpg";
		
		try {
            File dest = new File(System.getProperty("user.dir") + "/src/itemIcons/" + name + ".jpg");
            Files.copy(file.toPath(), dest.toPath());
        } catch (IOException ex) {
        	AlertHelper.showWarningMessage("Error!", "Image Error!", AlertType.ERROR);
    		return;
        }
		
    	Item item;
    	item = new Item(nameText.getText(), Double.valueOf(priceText.getText()), 
    			description.getText(), image, 0);
    	
    	gateway.AddItem(item);
    	
    	try { //Giving time for project to refresh itself
			Thread.sleep(1700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	AlertHelper.showWarningMessage("Success!", "Item created!", AlertType.INFORMATION);
    	
    	AppController.getInstance().changeView(AppController.LIST, null);
    }

    @FXML
    void backButtonClicked(ActionEvent event) {
    	AppController.getInstance().changeView(AppController.LIST, null);
    }

    @FXML
    void addImageClicked(ActionEvent event) {
		while(true){ //Escapes by file existing or user selecting no/close option in message box/filechooser
			JFileChooser fileChooser = new JFileChooser("."); 
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
			FileFilter imageFilter = new FileNameExtensionFilter(
				    "Image files", ImageIO.getReaderFileSuffixes());
			fileChooser.addChoosableFileFilter(imageFilter);
		    fileChooser.setAcceptAllFileFilterUsed(false);
			
			int ret = fileChooser.showOpenDialog(null);
			
			if (ret == JFileChooser.CANCEL_OPTION)
				return;
			 file = fileChooser.getSelectedFile();
			 if(file.exists()){
				 break;
			 }
			 String message = "File " + file.getName() + " does not exist in \n" + file.getParent() + ":\n" + "Do you want to select a different file?";
	         String title = "Warning";
	         int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
	         if(reply == JOptionPane.NO_OPTION || reply == JOptionPane.CLOSED_OPTION){
	        	 return;
	         }
		}
		imageAddedIndicator.setImage(new Image("/view/checkmark.png"));
		
    	imageAdded = true;
    }
}