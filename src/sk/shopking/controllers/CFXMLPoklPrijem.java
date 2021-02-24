package sk.shopking.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

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
import sk.shopking.UserPokladnik;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLPoklPrijem implements Initializable{

private final UserPokladnik aktualnyUser = (UserPokladnik) CFXMLLogin.getZhoda();
	
	@FXML private Label jlAktualnaSuma;
	@FXML private TextField jtfPrijem;
	@FXML private Label jlPoPridani;
	
	private float sumaPokladnica;
	private Float novaSumaPokladnica;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		sumaPokladnica = Database.getStavPokladnice(aktualnyUser.getId());
		jlAktualnaSuma.setText(new DecimalFormat("0.00").format(sumaPokladnica));
		jtfPrijem.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*(\\.\\d*)?")) {
				jtfPrijem.setText(newValue.replaceAll("[^\\d||.]", ""));
				novaSumaPokladnica = null;
			}
			else if (newValue.isEmpty()) {
				novaSumaPokladnica = null;
				jlPoPridani.setText("0,00");
			}
			else {
				try {
					float zadanaSuma = Float.parseFloat(newValue);
					if (zadanaSuma <= 0) {
						novaSumaPokladnica = null;
						jlPoPridani.setText("0,00");
					}
					else {
						novaSumaPokladnica = sumaPokladnica + zadanaSuma;
						jlPoPridani.setText(new DecimalFormat("0.00").format(sumaPokladnica + zadanaSuma));
					}
				} catch (NumberFormatException e) {
					novaSumaPokladnica = null;
					jlPoPridani.setText("0,00");
				}
			}
		});
		
	}

	@FXML
   	private void jbOKAction(ActionEvent event) {
		if (novaSumaPokladnica == null) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Nesprávne zadaná suma");
        	alert.setHeaderText("Zadaný suma je nesprávne zadaná (alebo je väčšia ako akt. suma v pokladnici)");
        	alert.setContentText("Opravte zadanú sumu!");
        	alert.show();
		} else {
			Database.setStavPokladnice(aktualnyUser.getId(), novaSumaPokladnica);
			FXMLTools.closeWindow(event);
		}
		
    	   
    }
	
	@FXML
   	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
    }

}
