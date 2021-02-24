package sk.shopking.installer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import sk.shopking.tools.AppSettings;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLInsUkazovatelAdmina implements Initializable{
	
	@FXML private ProgressBar jpbInstalling;
	@FXML private Label jlAktualnaUloha;
	@FXML private Button jbNext;
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		
    }
	
	public void installing() {
		
		jpbInstalling.setProgress(0);
		jlAktualnaUloha.setText("Vytváram konfiguračný súbor (0%)");
		if (!InstallAdmin.installPath.exists()) {
			boolean isCreated = InstallAdmin.installPath.mkdirs();
			if (isCreated){
				AppSettings.createPropFile(InstallAdmin.installPath,"", InstallAdmin.adresaIP, "", "" + 0,"" + 20);
			}
			else{
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert.setTitle("Problém s konfiguračným súborom");
				alert.setHeaderText("Konfiguračný súbor sa nepodarilo zapísať");
				alert.setContentText("Skontrolujte, či priečinok s dátami programu existuje!");
				alert.showAndWait();
				Platform.exit();
				System.exit(0);
			}
		}
		
		jpbInstalling.setProgress(0.5);
		jlAktualnaUloha.setText("Kopírujem .jar aplikáciu (50%)");
		try {
			URL inputUrl = getClass().getResource("/ShopKing.jar");
			FileUtils.copyURLToFile(inputUrl, new File(InstallAdmin.installPath + File.separator + "ShopKing.jar"));
		}
		catch (Exception e) {
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
    private void jbNextAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insDone.fxml")));
 	    stage.setScene(scene);
		
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
