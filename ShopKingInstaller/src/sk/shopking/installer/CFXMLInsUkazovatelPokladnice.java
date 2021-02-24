package sk.shopking.installer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import org.apache.commons.io.FileUtils;

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
import sk.shopking.tools.FXMLTools;

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
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insDone.fxml")));
 	    stage.setScene(scene);
		
	}
	
	public void installing() {
		jpbInstalling.setProgress(0);
		jlAktualnaUloha.setText("Vytváram konfiguračný súbor (0%)");
		if (!InstallPokladnica.installPath.exists()) {
			boolean isCreated = InstallPokladnica.installPath.mkdirs();
			if (isCreated){
				AppSettings.createPropFile(InstallPokladnica.installPath,InstallPokladnica.citackaPort, InstallPokladnica.adresaIP, InstallPokladnica.dokladyPath.getPath(), "" + InstallPokladnica.cisloPokladnice,"" + 20);
			}
			else{
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert.setTitle("Problém s konfiguračným súborom");
				alert.setHeaderText("Konfiguračný súbor sa nepodarilo zapísať");
				alert.setContentText("Skontrolujte, či priečinok s dátami programu existuje!");
				alert.show();
				return;
			}
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
        	alert.showAndWait();
			Platform.exit();
			System.exit(0);
		}
		
		jpbInstalling.setProgress(0.5);
		jlAktualnaUloha.setText("Ukladám do databázy informácie (50%)");
		Pokladnica novaPokladnica = new Pokladnica(InstallPokladnica.cisloPokladnice, InstallPokladnica.dkp, false, null,null);
		List<Pokladnica> pokladniceList = new ArrayList<>();
		pokladniceList.add(novaPokladnica);
		boolean added = Database.addPokladnice(pokladniceList);
		if (!added){
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			alert.setTitle("Duplicitná pokladnica");
			alert.setHeaderText("V databáze sa pokladnica s číslom " + InstallPokladnica.cisloPokladnice + " už nachádza");
			alert.setContentText("Inštalácia sa ukončuje!");
			alert.showAndWait();
			Platform.exit();
			System.exit(0);
		}
		
		jpbInstalling.setProgress(0.75);
		jlAktualnaUloha.setText("Kopírujem .jar aplikáciu (75%)");
		try {
			URL inputUrl = getClass().getResource("/ShopKing.jar");
			FileUtils.copyURLToFile(inputUrl, new File(InstallPokladnica.installPath + File.separator + "ShopKing.jar"));
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			alert.setTitle("Problém kopírovaní programu do priečinku");
			alert.setHeaderText("Program sa nepodarilo zapísať do priečinku APPDATA");
			alert.setContentText("Skontrolujte, či priečinok s dátami programu existuje!");
			alert.showAndWait();
			Platform.exit();
			System.exit(0);
		}
		jpbInstalling.setProgress(1);
		jlAktualnaUloha.setText("Hotovo! (100%)");
		jbNext.setDisable(false);
	}
	
	@FXML
    private void jbStornoAction (ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
   		alert.setTitle("Ukončenie inštalácie");
   		alert.setHeaderText("Inštalácia nebola ukončená");
   		alert.setContentText("Chcete naozaj ukončiť inštaláciu programu ?");
   		Optional<ButtonType> vysledok = alert.showAndWait();
   		if(vysledok.isPresent()){
			if(vysledok.get() == ButtonType.OK) {
				alert.close();
				FXMLTools.closeWindow(event);
			}
			else {
				alert.close();
			}
		}

	}
}
