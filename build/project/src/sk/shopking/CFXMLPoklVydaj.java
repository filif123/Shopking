/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * @author Filip
 *
 */
public class CFXMLPoklVydaj implements Initializable{
	
	@FXML private TextField jtfVydaj;
	@FXML private Label jlZostatok;
	
	private float sumaPokladnica;
	private Float novaSumaPokladnica;
	
	public void setUserSuma(float suma) {
		this.sumaPokladnica = suma;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jtfVydaj.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					jtfVydaj.setText(newValue.replaceAll("[^\\d||.]", ""));
					novaSumaPokladnica = null;
		        }
				else if (newValue.isEmpty()) {
					novaSumaPokladnica = null;
					jlZostatok.setText("0,00");
				}
				else {
					try {
						float zadanaSuma = Float.parseFloat(newValue);
						if (zadanaSuma <= 0) {
							novaSumaPokladnica = null;
							jlZostatok.setText("0,00");
						}
						else {
							novaSumaPokladnica = sumaPokladnica - zadanaSuma;
							jlZostatok.setText("" + new DecimalFormat("0.00").format(sumaPokladnica - zadanaSuma));
						}
					} catch (NumberFormatException e) {
						novaSumaPokladnica = null;
						jlZostatok.setText("0,00");
					}
				}	
			}
		});
		
	}

	@FXML
   	private void jbOKAction(ActionEvent event) throws IOException {
		if (novaSumaPokladnica == null) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Nesprávne zadaná suma");
        	alert.setHeaderText("Zadaný suma je nesprávne zadaná (alebo je väčšia ako akt. suma v pokladnici)");
        	alert.setContentText("Opravte zadanú sumu!");
        	alert.show();
		} else {
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
