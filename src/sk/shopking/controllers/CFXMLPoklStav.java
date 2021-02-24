package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.UserPokladnik;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLPoklStav implements Initializable{

	private final UserPokladnik aktualnyUser = (UserPokladnik) CFXMLLogin.getZhoda();
	
	@FXML private Label jlSuma;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jlSuma.setText(new DecimalFormat("0.00").format(Database.getStavPokladnice(aktualnyUser.getId())));
	}
	
	@FXML
   	private void jbPrijemAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklPrijem.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Výdaj z pokladnice");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.show();

		FXMLTools.closeWindow(event);
    }
	
	@FXML
   	private void jbVydajAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklVydaj.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Výdaj z pokladnice");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.show();

		FXMLTools.closeWindow(event);
    }
	
	@FXML
   	private void jbOKAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
    }
	
	@FXML
   	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
    }
}
