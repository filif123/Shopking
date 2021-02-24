package sk.shopking.installer;

import java.io.File;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLInsSpol implements Initializable{
	
	@FXML private TextField jtfNazovSpol,jtfObchodnyNazov,jtfICO,jtfDIC,jtfICDPH,jtfUlicaSpol,jtfCisloSpol,jtfPSCSpol,jtfMestoSpol;
	@FXML private Label jlLogoPath;
	
	private File selectedFileLogo = null;
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		if (InstallServer.nazovSpolocnosti != null && InstallServer.obchodnyNazov != null && InstallServer.ico != null && InstallServer.dic != null && InstallServer.icdph != null
				&& InstallServer.ulicaSidlo != null && InstallServer.cisloPopisneSidlo != null && InstallServer.pscSidlo != null && InstallServer.mestoSidlo != null) {
			
			if (InstallServer.logoSpolocnosti != null) {
				selectedFileLogo = InstallServer.logoSpolocnosti;
				jlLogoPath.setText(selectedFileLogo.toString());
			}

			jtfNazovSpol.setText(InstallServer.nazovSpolocnosti);
			jtfObchodnyNazov.setText(InstallServer.obchodnyNazov);
			jtfICO.setText(InstallServer.ico);
			jtfDIC.setText(InstallServer.dic);
			jtfICDPH.setText(InstallServer.icdph);
			jtfUlicaSpol.setText(InstallServer.ulicaSidlo);
			jtfCisloSpol.setText(InstallServer.cisloPopisneSidlo);
			jtfPSCSpol.setText(InstallServer.pscSidlo);
			jtfMestoSpol.setText(InstallServer.mestoSidlo);
		}
		
    }
	
	@FXML
	private void jbPrehladavatAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Vyberte obrázkový súbor s logom spoločnosti");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		         //new ExtensionFilter("All Files", "*.*"));
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
			if (selectedFile != null) {
				selectedFileLogo = selectedFile;
				jlLogoPath.setText(selectedFileLogo.toString());
			} 
	}
	
	@FXML
    private void jbBackAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insVyber.fxml")));
 	    stage.setScene(scene);
	}
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		String nazovSpol = jtfNazovSpol.getText();
		String obchodnyNazov = jtfObchodnyNazov.getText();
		String ico = jtfICO.getText();
		String dic = jtfDIC.getText();
		String icdph = jtfICDPH.getText();
		String ulicaSpol = jtfUlicaSpol.getText();
		String cisloSpol = jtfCisloSpol.getText();
		String pscSpol = jtfPSCSpol.getText();
		String mestoSpol = jtfMestoSpol.getText();
		
		if(nazovSpol == null || nazovSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadny názov spoločnosti");
        	alert.setContentText("Zadajte názov spoločnosti!");
        	alert.show();
		}
		else if(obchodnyNazov == null || obchodnyNazov.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnen obchodný názov spoločnosti");
        	alert.setContentText("Zadajte obchodný názov spoločnosti!");
        	alert.show();
		}
		else if(ico == null || ico.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne IČO spoločnosti");
        	alert.setContentText("Zadajte IČO spoločnosti!");
        	alert.show();
		}
		else if(dic == null || dic.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne DIČ spoločnosti");
        	alert.setContentText("Zadajte DIČ spoločnosti!");
        	alert.show();
		}
		else if(icdph == null || icdph.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne IČDPH spoločnosti");
        	alert.setContentText("Zadajte IČDPH spoločnosti!");
        	alert.show();
		}
		else if(ulicaSpol == null || ulicaSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnu ulicu sídla spoločnosti");
        	alert.setContentText("Zadajte ulicu sídla spoločnosti!");
        	alert.show();
		}
		else if(pscSpol == null || pscSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnu PSČ sídla spoločnosti");
        	alert.setContentText("Zadajte PSČ sídla spoločnosti!");
        	alert.show();
		}
		else if(cisloSpol == null || cisloSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne číslo popisné sídla spoločnosti");
        	alert.setContentText("Zadajte číslo popisné sídla spoločnosti!");
        	alert.show();
		}
		else if(mestoSpol == null || mestoSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne mesto sídla spoločnosti");
        	alert.setContentText("Zadajte mesto sídla spoločnosti!");
        	alert.show();
		}
		else {
			if (cisloSpol.length() > 5) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        	alert.setTitle("Zlé parametre");
	        	alert.setHeaderText("Zadané číslo popisné sídla je dlhšie ako má byť");
	        	alert.setContentText("Skontrolujte číslo popisné sídla!");
	        	alert.show();
	        	return;
			}
			
			if (selectedFileLogo != null) {
				InstallServer.logoSpolocnosti = selectedFileLogo;
			}
			InstallServer.nazovSpolocnosti = nazovSpol;
			InstallServer.obchodnyNazov = obchodnyNazov;
			InstallServer.ico = ico;
			InstallServer.dic = dic;
			InstallServer.icdph = icdph;
			InstallServer.ulicaSidlo = ulicaSpol;
			InstallServer.cisloPopisneSidlo = cisloSpol;
			InstallServer.pscSidlo = pscSpol;
			InstallServer.mestoSidlo = mestoSpol;
			
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insPrevadzka.fxml")));
	 	    stage.setScene(scene);
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
