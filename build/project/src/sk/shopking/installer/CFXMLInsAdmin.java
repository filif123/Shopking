/**
 * 
 */
package sk.shopking.installer;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.CFXMLAddTovar;
import sk.shopking.CFXMLPasswordChange;

/**
 * @author Filip
 *
 */
public class CFXMLInsAdmin implements Initializable{
	
	private String newPassword;
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		
    }
	
	@FXML
	private void jbPasswordChangeAction(ActionEvent event) throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/passwordChange.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
	    CFXMLPasswordChange passwordChangeController = fxmlLoader.getController();
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Zmena hesla");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.showAndWait();
	    
	    newPassword = passwordChangeController.getNewPassword();
	    
	}
	
	@FXML
    private void jbBackAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/scenes/instalacia/insPrevadzka.fxml")));
 	    stage.setScene(scene);
	}
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		if(newPassword == null) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebolo zadané heslo hlavného administrátora");
        	alert.setContentText("Zadajte heslo hlavného administrátora!");
        	alert.show();
		}
		else {
			InstallServer.hesloAdmin = newPassword;
			
			Node source = (Node) event.getSource();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/instalacia/insInstalaciaUkazovatelServera.fxml"));
		    Parent root = (Parent) fxmlLoader.load();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(root);
	 	    stage.setScene(scene);
	 	    CFXMLInsUkazovatelServera installServerController = fxmlLoader.getController(); 
	 	    installServerController.installing();
	 	    /*scene.addEventHandler(WindowEvent.WINDOW_SHOWN, new  EventHandler<WindowEvent>(){
	 	        @Override
	 	        public void handle(WindowEvent window) {
	 	             
	 	        }
	 	    });*/
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
