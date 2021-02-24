/**
 * 
 */
package sk.shopking.installer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.sun.javafx.scene.control.SelectedCellsMap;

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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * @author Filip
 *
 */
public class CFXMLInsKlientPokladnica implements Initializable{
	
	@FXML private TextField jtfIP;
	@FXML private Label jlAppPath;
	private File selectedAppPath = new File(System.getenv("APPDATA") + File.separator + "ShopKing");
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		if (InstallPokladnica.adresaIP != null && InstallPokladnica.installPath != null) {
			jtfIP.setText(InstallPokladnica.adresaIP);
			selectedAppPath = InstallPokladnica.installPath;
			jlAppPath.setText(selectedAppPath.toString());
		}
    }
	
	@FXML
    private void jbAppPathAction (ActionEvent event) throws IOException {
		DirectoryChooser dir = new DirectoryChooser();
		dir.setTitle("Vyberte priečinok pre uloženie programu");
		if (! selectedAppPath.exists()) {
			selectedAppPath.mkdirs();
		}
		dir.setInitialDirectory(selectedAppPath);
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        File file = dir.showDialog(stage); 
		if (file != null) {
			selectedAppPath = file;
			jlAppPath.setText(selectedAppPath.toString());
		}
	}

	@FXML
    private void jbBackAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/scenes/instalacia/insVyber.fxml")));
 	    stage.setScene(scene);
	}
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		
		String ip = jtfIP.getText();
		
		if(ip == null || ip.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola zadaná žiadna adresa ku serveru");
        	alert.setContentText("Zadajte adresu serveru!");
        	alert.show();
		}
		else if(selectedAppPath == null){
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol vybratý priečinok pre uloženie programu");
        	alert.setContentText("Vyberte priečinok pre uloženie programu!");
        	alert.show();
		}
		else {
			String pattern = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
		    if (!ip.matches(pattern)) {
		    	Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        	alert.setTitle("Zlé parametre");
	        	alert.setHeaderText("Zadaná IP adresa má nesprávny tvar");
	        	alert.setContentText("Opravte adresu serveru!");
	        	alert.show();
	        	return;
			}
		    
		    InstallPokladnica.adresaIP = ip;
		    InstallPokladnica.installPath = selectedAppPath;
			
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(FXMLLoader.load(getClass().getResource("/scenes/instalacia/insPokladnica.fxml")));
	 	    stage.setScene(scene);
		}
		
		
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
