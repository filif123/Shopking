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
import sk.shopking.tools.AppSettings;

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
		jtfCas.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					jtfCas.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				else {
					casAutoRefresh = Integer.parseInt(newValue);
				}
			}
		});
	}
	
	@FXML
	private void jbOKAction(ActionEvent event) throws IOException {
		if(casAutoRefresh >= 300) {
			Alert alert = new Alert(AlertType.ERROR);
   			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
           	alert.setTitle("Zlé parametre");
           	alert.setHeaderText("Čas automatickej obnovy obnovy pokladníc má mať menej ako 5 minút");
           	alert.setContentText("Zmeňte čas automatickej obnovy pokladníc!");
           	alert.show();
		}
		else {
			AppSettings.setCashRegistersAutoRefresh("" + casAutoRefresh);
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        stage.close();
		}
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}

}
