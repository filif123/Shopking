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
import sk.shopking.tools.AppSettings;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLAutoRefreshPokl implements Initializable{
	
	private int casAutoRefresh = AppSettings.loadCashRegistersAutoRefresh();
	
	@FXML private TextField jtfCas;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		jtfCas.setText("" + casAutoRefresh);
		jtfCas.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				jtfCas.setText(newValue.replaceAll("[^\\d]", ""));
			}
			else {
				casAutoRefresh = Integer.parseInt(newValue);
			}
		});
	}
	
	@FXML
	private void jbOKAction(ActionEvent event) {
		if(casAutoRefresh >= 300) {
			Alert alert = new Alert(AlertType.ERROR);
   			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
           	alert.setTitle("Zlé parametre");
           	alert.setHeaderText("Čas automatickej obnovy obnovy pokladníc má mať menej ako 5 minút");
           	alert.setContentText("Zmente čas automatickej obnovy pokladníc!");
           	alert.show();
		}
		else {
			AppSettings.setCashRegistersAutoRefresh("" + casAutoRefresh);
			FXMLTools.closeWindow(event);
		}
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}

}
