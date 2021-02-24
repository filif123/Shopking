package sk.shopking.installer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.Obchod;
import sk.shopking.ShopSettings;
import sk.shopking.User;
import sk.shopking.UserAdministrator;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLInsUkazovatelServera implements Initializable{
	
	@FXML private ProgressBar jpbInstalling;
	@FXML private Label jlAktualnaUloha;
	@FXML private Button jbNext;
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		
    }
	
	public void installing() {
		jpbInstalling.setProgress(0);
		jlAktualnaUloha.setText("Pripájam sa na SQL server (0%)");
		try {
			Database.addressToServer = "localhost";
		}catch (Exception ignored){}


		boolean isServerExists = Database.isServerExists("localhost","root","");
		boolean isServerExists2 = Database.isServerExists("localhost","root","hesloroot");
		if(isServerExists2){
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			alert.setTitle("Problém s SQL serverom");
			alert.setHeaderText("Databáza už bola nainštalovaná");
			alert.setContentText("Inštalácia sa ukončí.");
			alert.showAndWait();
			Platform.exit();
			System.exit(0);
		}
		else if (!isServerExists) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Problém s SQL serverom");
        	alert.setHeaderText("Na SQL databázu v tomto PC sa nedá pripojiť");
        	alert.setContentText("Skontrolujte, či je SQL server zapnutý!");
        	alert.showAndWait();
			Platform.exit();
			System.exit(0);
		}
		else {
			Database.prepareServer();
		}
		
		jpbInstalling.setProgress(0.25);
		jlAktualnaUloha.setText("Vytvára sa databáza na SQL serveri (25%)");
		InputStream stream = getClass().getResourceAsStream("/resources/script.sql");
		boolean createDBsuccess = Database.runScriptDB(stream);
		if (!createDBsuccess) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Problém s SQL serverom");
        	alert.setHeaderText("Na SQL nebolo možné vytvoriť databázu");
        	alert.setContentText("Skontrolujte, či nebol súbor so skriptom poškodený!");
        	alert.showAndWait();
			Platform.exit();
			System.exit(0);
		}
		
		jpbInstalling.setProgress(0.5);
		jlAktualnaUloha.setText("Nastavujú sa informácie na export do databázy (50%)");
		Obchod obchod = new Obchod(InstallServer.obchodnyNazov, InstallServer.nazovSpolocnosti, InstallServer.mestoSidlo, InstallServer.ulicaSidlo, InstallServer.pscSidlo, InstallServer.cisloPopisneSidlo, InstallServer.ico, InstallServer.dic, InstallServer.icdph, InstallServer.mestoPrevadzka, InstallServer.ulicaPrevadzka, InstallServer.pscPrevadzka, InstallServer.cisloPopisnePrevadzka, InstallServer.logoSpolocnosti);
		Database.createObchod(obchod);

		ShopSettings settings = new ShopSettings();
		settings.setPoklPociatocnaSuma(InstallServer.pociatocnaSumaPokladnica);
		settings.setPoklMaxSuma(InstallServer.maxSumaPokladnica);
		Database.createShopSettings(settings);
		
		jpbInstalling.setProgress(0.75);
		jlAktualnaUloha.setText("Pridáva sa účet hlavného administrátora (75%)");
		UserAdministrator administrator = new UserAdministrator(0, "Administrátor", "", "admin", User.hashPassword(InstallServer.hesloAdmin));
		List<User> users = new ArrayList<>();
		users.add(administrator);
		Database.addUsers(users);
		
		jpbInstalling.setProgress(1);
		jlAktualnaUloha.setText("Hotovo! (100%)");
		jbNext.setDisable(false);
	}
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		
 	    Node source = (Node) event.getSource();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/instalacia/insDone.fxml"));
	    Parent root = fxmlLoader.load();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(root);
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
