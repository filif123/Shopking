package sk.shopking.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLPoklMnozstvo implements Initializable{
	
	private Integer mnozstvo;
	
	@FXML private TextField jtfMnozstvo;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jtfMnozstvo.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				jtfMnozstvo.setText(newValue.replaceAll("[^\\d]", ""));
			}
			else if (!newValue.isEmpty()) {
				mnozstvo = Integer.parseInt(newValue);
			}
		});
		
	}
	
	@FXML
	private void jbOKAction (ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) {
		mnozstvo = null;
		FXMLTools.closeWindow(event);
	}
	
	public Integer returnMnozstvo() {
		return mnozstvo;
	}

}
