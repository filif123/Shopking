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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLInsPrevadzka implements Initializable{
	
	@FXML private TextField jtfUlicaPrev,jtfMestoPrev,jtfCisloPrev,jtfPSCPrev;
	@FXML private TextField jtfPociatocnaSuma,jtfSumaLimit;
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		if (InstallServer.ulicaPrevadzka != null && InstallServer.mestoPrevadzka != null && InstallServer.cisloPopisnePrevadzka != null && InstallServer.pscPrevadzka != null
				&& InstallServer.pociatocnaSumaPokladnica != 0 && InstallServer.maxSumaPokladnica != 0) {
			
			jtfUlicaPrev.setText(InstallServer.ulicaPrevadzka);
			jtfCisloPrev.setText(InstallServer.cisloPopisnePrevadzka);
			jtfPSCPrev.setText(InstallServer.pscPrevadzka );
			jtfMestoPrev.setText(InstallServer.mestoPrevadzka);
			
			jtfPociatocnaSuma.setText("" + InstallServer.pociatocnaSumaPokladnica);
			jtfSumaLimit.setText("" + InstallServer.maxSumaPokladnica);
		}
    }
	
	@FXML
    private void jbBackAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insSpol.fxml")));
 	    stage.setScene(scene);
 	    stage.setTitle(ApplicationInfo.APP_NAME + " - Inštalácia");
	}
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		String ulicaPrev = jtfUlicaPrev.getText();
		String cisloPrev = jtfCisloPrev.getText();
		String pscPrev = jtfPSCPrev.getText();
		String mestoPrev = jtfMestoPrev.getText();
		
		String pocSuma = jtfPociatocnaSuma.getText();
		String limSuma = jtfSumaLimit.getText();
		
		if(ulicaPrev == null || ulicaPrev.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnu ulicu prevádzky");
        	alert.setContentText("Zadajte ulicu prevádzky!");
        	alert.show();
		}
		else if(pscPrev == null || pscPrev.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnu PSČ prevádzky");
        	alert.setContentText("Zadajte PSČ prevádzky!");
        	alert.show();
		}
		else if(cisloPrev == null || cisloPrev.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne číslo popisné prevádzky");
        	alert.setContentText("Zadajte číslo popisné prevádzky!");
        	alert.show();
		}
		else if(mestoPrev == null || mestoPrev.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne mesto prevádzky");
        	alert.setContentText("Zadajte mesto prevádzky!");
        	alert.show();
		}
		else if(pocSuma == null || pocSuma.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola zadaná žiadna počiatočná suma do pokladnice");
        	alert.setContentText("Zadajte počiatočnú sumu do pokladnice!");
        	alert.show();
		}
		else if(limSuma == null || limSuma.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola zadaná žiadna maximálna suma v pokladnici");
        	alert.setContentText("Zadajte maximálnu sumu v pokladnici!");
        	alert.show();
		}
		else {
			if (cisloPrev.length() > 5) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        	alert.setTitle("Zlé parametre");
	        	alert.setHeaderText("Zadané číslo popisné prevádzky je dlhšie ako má byť");
	        	alert.setContentText("Skontrolujte číslo popisné prevádzky!");
	        	alert.show();
	        	return;
			}
			float pocSumaNum,limSumaNum;
			try {
				pocSumaNum = Float.parseFloat(pocSuma);
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        	alert.setTitle("Zlé parametre");
	        	alert.setHeaderText("Počiatočná suma do pokladnice nie je číslo");
	        	alert.setContentText("Zadajte počiatočnú sumu do pokladnice!");
	        	alert.show();
	        	return;
			}
			
			try {
				limSumaNum = Float.parseFloat(limSuma);
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        	alert.setTitle("Zlé parametre");
	        	alert.setHeaderText("Maximálna suma v pokladnici nie je číslo");
	        	alert.setContentText("Zadajte maximálnu sumu v pokladnici!");
	        	alert.show();
	        	return;
			}
			
			InstallServer.cisloPopisnePrevadzka = cisloPrev;
			InstallServer.mestoPrevadzka = mestoPrev;
			InstallServer.pscPrevadzka = pscPrev;
			InstallServer.ulicaPrevadzka = ulicaPrev;
			
			InstallServer.pociatocnaSumaPokladnica = pocSumaNum;
			InstallServer.maxSumaPokladnica = limSumaNum;
			
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insAdmin.fxml")));
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
