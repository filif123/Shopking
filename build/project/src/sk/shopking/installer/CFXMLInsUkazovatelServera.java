/**
 * 
 */
package sk.shopking.installer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.Obchod;
import sk.shopking.User;
import sk.shopking.UserAdministrator;
import sk.shopking.tools.Database;

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
		Database.ADDRESS_TO_SERVER = "localhost";
		boolean isServerExists = Database.isServerExists();
		if (!isServerExists) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Problém s SQL serverom");
        	alert.setHeaderText("Na SQL databázu v tomto PC sa nedá pripojiť");
        	alert.setContentText("Skontrolujte, či je SQL server zapnutý!");
        	alert.show();
        	return;
		}
		
		jpbInstalling.setProgress(0.25);
		jlAktualnaUloha.setText("Vytvára sa databáza na SQL serveri (25%)");
		URL url = getClass().getResource("/resources/script.sql");
		File f = null;
		try {
			f=new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		boolean createDBsuccess = Database.runScriptDB(f.getPath());
		if (!createDBsuccess) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Problém s SQL serverom");
        	alert.setHeaderText("Na SQL nebolo možné vytvoriť databázu");
        	alert.setContentText("Skontrolujte, či nebol súbor so skriptom poškodený!");
        	alert.show();
        	return;
		}
		
		jpbInstalling.setProgress(0.5);
		jlAktualnaUloha.setText("Nastavujú sa informácie na export do databázy (50%)");
		Obchod obchod = new Obchod(InstallServer.obchodnyNazov, InstallServer.nazovSpolocnosti, InstallServer.mestoSidlo, InstallServer.ulicaSidlo, InstallServer.pscSidlo, InstallServer.cisloPopisneSidlo, InstallServer.ico, InstallServer.dic, InstallServer.icdph, InstallServer.mestoPrevadzka, InstallServer.ulicaPrevadzka, InstallServer.pscPrevadzka, InstallServer.cisloPopisnePrevadzka, InstallServer.logoSpolocnosti, InstallServer.pociatocnaSumaPokladnica, InstallServer.maxSumaPokladnica);
		Database.createObchod(obchod);
		
		jpbInstalling.setProgress(0.75);
		jlAktualnaUloha.setText("Pridáva sa účet hlavného administrátora (75%)");
		UserAdministrator administrator = new UserAdministrator(0, "Administrátor", "", "admin", User.hashPassword(InstallServer.hesloAdmin));
		List<User> users = new ArrayList<User>();
		users.add(administrator);
		Database.addUsers(users);
		
		jpbInstalling.setProgress(1);
		jlAktualnaUloha.setText("Hotovo! (100%)");
		jbNext.setDisable(false);
	}
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/scenes/instalacia/insDone.fxml")));
 	    stage.setScene(scene);
		
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
