/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * @author Filip
 *
 */
public class CFXMLPoklHmotnost implements Initializable{

	private Integer hmotnost;
	
	@FXML private TextField jtfHmotnost;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		jtfHmotnost.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.isEmpty()) {
					if (!newValue.matches("\\d*")) {
						jtfHmotnost.setText(newValue.replaceAll("[^\\d]", ""));
			        } 
					else {
						hmotnost = Integer.parseInt(newValue);
					}
				}
			}
		});
		
	}
	
	@FXML
	private void jbOKAction (ActionEvent event) throws IOException {
		if (hmotnost == null) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola zadaná hmotnosť tovaru");
        	alert.setContentText("Zadajte hmotnosť tovaru!");
        	alert.show();
        	return;
		}
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) throws IOException {
		hmotnost = null;
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	public Integer returnHmotnost() {
		return hmotnost;
	}

	
}
