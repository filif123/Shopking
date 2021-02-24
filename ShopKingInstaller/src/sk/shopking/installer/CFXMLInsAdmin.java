package sk.shopking.installer;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.controllers.CFXMLPasswordChange;
import sk.shopking.tools.AlertResult;
import sk.shopking.tools.FXMLTools;

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
	    Parent root = fxmlLoader.load();
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
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insPrevadzka.fxml")));
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
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/instalacia/insInstalaciaUkazovatelServera.fxml"));
		    Parent root = fxmlLoader.load();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(root);
	 	    stage.setScene(scene);
	 	    CFXMLInsUkazovatelServera installServerController = fxmlLoader.getController();
			Platform.runLater(installServerController::installing);
		}
	}
	
	@FXML
    private void jbStornoAction (ActionEvent event) {
		AlertResult result = FXMLTools.showAlert("Ukončenie inštalácie","Inštalácia nebola ukončená","Chcete naozaj ukončiť inštaláciu programu ?",AlertType.CONFIRMATION);
   		if (result == AlertResult.OK){
			FXMLTools.closeWindow(event);
		}
	}
}
