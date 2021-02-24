package sk.shopking.installer;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLInsVyber implements Initializable{
	
	@FXML private RadioButton jbPokladnica,jbServer,jbAdmin;
	private final ToggleGroup buttonGroup = new ToggleGroup();
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		jbPokladnica.setToggleGroup(buttonGroup);
		jbServer.setToggleGroup(buttonGroup);
		jbAdmin.setToggleGroup(buttonGroup);
    }
	
	@FXML
    private void jbBackAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insWelcome.fxml")));
 	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
 	    stage.setScene(scene);
 	    stage.setTitle(ApplicationInfo.APP_NAME + " - Inštalácia");
	}
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		if(jbPokladnica.isSelected()) {
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insKlientPokladnica.fxml")));
	 	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	 	    stage.setScene(scene);
	 	    stage.setTitle(ApplicationInfo.APP_NAME + " - Inštalácia");
		}
		else if(jbAdmin.isSelected()) {
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insKlientAdmin.fxml")));
	 	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	 	    stage.setScene(scene);
	 	    stage.setTitle(ApplicationInfo.APP_NAME + " - Inštalácia");
			
		}
		else {
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insSpol.fxml")));
	 	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	 	    stage.setScene(scene);
	 	    stage.setTitle(ApplicationInfo.APP_NAME + " - Inštalácia");
		}
		
	}
	
	@FXML
    private void jbStornoAction (ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
   		alert.setTitle("Ukončenie inštalácie");
   		alert.setHeaderText("Inštalácia nebola ukončená");
   		alert.setContentText("Chcete naozaj ukončiť inštaláciu programu ?");
   		Optional<ButtonType> vysledok = alert.showAndWait();
   		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
   			alert.close();
			FXMLTools.closeWindow(event);
   		}
   		else {
   			alert.close();
   		}
	}
}
