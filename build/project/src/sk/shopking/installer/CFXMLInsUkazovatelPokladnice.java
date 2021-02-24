/**
 * 
 */
package sk.shopking.installer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.Pokladnica;
import sk.shopking.tools.AppSettings;
import sk.shopking.tools.Database;

/**
 * @author Filip
 *
 */
public class CFXMLInsUkazovatelPokladnice implements Initializable{
	
	@FXML private ProgressBar jpbInstalling;
	@FXML private Label jlAktualnaUloha;
	@FXML private Button jbNext;
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		
    }
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/scenes/instalacia/insDone.fxml")));
 	    stage.setScene(scene);
		
	}
	
	public void installing() {
		
		jpbInstalling.setProgress(0);
		jlAktualnaUloha.setText("Vytváram konfiguračný súbor (0%)");
		AppSettings.cestaKuKlientovi = InstallPokladnica.installPath.getPath();
		boolean isAppConfigExists = AppSettings.createPropFile(InstallPokladnica.citackaPort, InstallPokladnica.adresaIP, InstallPokladnica.dokladyPath.getPath(), "" + InstallPokladnica.cisloPokladnice,"" + 20);
		if (!isAppConfigExists) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Problém s konfiguračným súborom");
        	alert.setHeaderText("Konfiguračný súbor sa nepodarilo zapísať");
        	alert.setContentText("Skontrolujte, či priečinok s dátami programu existuje!");
        	alert.show();
        	return;
		}
		
		jpbInstalling.setProgress(0.25);
		jlAktualnaUloha.setText("Pripájam sa na SQL server (25%)");
		boolean isDBAvailable = Database.isConnectionAvailable();
		if (!isDBAvailable) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Problém s SQL serverom");
        	alert.setHeaderText("Na SQL databázu v tomto PC sa nedá pripojiť");
        	alert.setContentText("Skontrolujte, či je SQL server zapnutý!");
        	alert.show();
        	return;
		}
		
		jpbInstalling.setProgress(0.5);
		jlAktualnaUloha.setText("Ukladám do databázy informácie (50%)");
		Pokladnica novaPokladnica = new Pokladnica(InstallPokladnica.cisloPokladnice, InstallPokladnica.dkp, false, null);
		List<Pokladnica> pokladniceList = new ArrayList<Pokladnica>();
		pokladniceList.add(novaPokladnica);
		Database.addPokladnice(pokladniceList);
		
		jpbInstalling.setProgress(0.75);
		jlAktualnaUloha.setText("Kopírujem .jar aplikáciu (75%)");
		File shopkingFile = new File(getClass().getResource("/shopking.jar").getFile());
		try {
			Files.copy(shopkingFile.toPath(), InstallPokladnica.installPath.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		jpbInstalling.setProgress(1);
		jlAktualnaUloha.setText("Hotovo! (100%)");
		jbNext.setDisable(false);
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
