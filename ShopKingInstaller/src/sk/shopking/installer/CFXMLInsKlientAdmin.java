package sk.shopking.installer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLInsKlientAdmin implements Initializable{

	@FXML private TextField jtfIP;
	//@FXML private Label jlAppPath;
	private File selectedAppPath = new File(System.getenv("APPDATA") + File.separator + "ShopKing");
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		if (InstallAdmin.adresaIP != null && InstallAdmin.installPath != null) {
			jtfIP.setText(InstallAdmin.adresaIP);
			selectedAppPath = InstallAdmin.installPath;
			//jlAppPath.setText(selectedAppPath.toString());
		}
    }
	
	/*@FXML
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
			//jlAppPath.setText(selectedAppPath.toString());
		}
	}*/

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
		    if (!ip.matches(pattern) && !ip.equals("localhost")) {
		    	Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        	alert.setTitle("Zlé parametre");
	        	alert.setHeaderText("Zadaná IP adresa má nesprávny tvar");
	        	alert.setContentText("Opravte adresu serveru!");
	        	alert.show();
	        	return;
			}
		    
		    InstallAdmin.adresaIP = ip;
		    InstallAdmin.installPath = selectedAppPath;
	 	    
	 	    Node source = (Node) event.getSource();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/instalacia/insInstalaciaUkazovatelAdmina.fxml"));
		    Parent root = fxmlLoader.load();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(root);
	 	    stage.setScene(scene);
	 	    CFXMLInsUkazovatelAdmina installAdminController = fxmlLoader.getController();
			Platform.runLater(installAdminController::installing);

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
