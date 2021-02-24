/**
 * 
 */
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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * @author Filip
 *
 */
public class CFXMLInsWelcome implements Initializable{
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		
    }
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/scenes/instalacia/insVyber.fxml")));
 	    stage.setScene(scene);
	}
	
	@FXML
    private void jbStornoAction (ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
   		alert.setTitle("Ukončenie inštalácie");
   		alert.setHeaderText("Inštalácia nebola ukončená");
   		alert.setContentText("Chcete naozaj ukončiť inštaláciu programu ?");
   		Optional<ButtonType> vysledok = alert.showAndWait();
   		if(vysledok.get() == ButtonType.OK) {
   			alert.close();
   			Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
   			stage.close();
   		}
   		else {
   			alert.close();
   		}
	}
}
