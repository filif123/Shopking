package sk.shopking.controllers;

import java.net.URL;
import java.util.ResourceBundle;

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
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLPoklHmotnost implements Initializable{

	private Integer hmotnost;
	
	@FXML private TextField jtfHmotnost;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		jtfHmotnost.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				if (!newValue.matches("\\d*")) {
					jtfHmotnost.setText(newValue.replaceAll("[^\\d]", ""));
				}
				else {
					hmotnost = Integer.parseInt(newValue);
				}
			}
		});
		
	}
	
	@FXML
	private void jbOKAction (ActionEvent event) {
		if (hmotnost == null) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola zadaná hmotnosť tovaru");
        	alert.setContentText("Zadajte hmotnosť tovaru!");
        	alert.show();
        	return;
		}
		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) {
		hmotnost = null;
		FXMLTools.closeWindow(event);
	}
	
	public Integer returnHmotnost() {
		return hmotnost;
	}

	
}
