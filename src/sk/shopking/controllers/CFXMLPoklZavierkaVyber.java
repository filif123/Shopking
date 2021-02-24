package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.*;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLPoklZavierkaVyber implements Initializable{
	
	private boolean zavierkaVykonana = false;
	private List<Nakup> nakupyPokladnika;
	private UserPokladnik pokladnik;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
	}
	
	@FXML
	private void jbPrehladovaZavierkaAction(ActionEvent event) throws IOException {
        
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklPrehladovaZavierka.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Prehľadová závierka");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	CFXMLPoklPrehladovaZavierka cfxmlPrehladZavController = fxmlLoader.getController();
    	List<NakupenyTovar> stornovane = Database.getStornovanyTovarPokladnika(pokladnik, new Date());
    	DennaZavierka novaZavierka = new DennaZavierka(0, nakupyPokladnika, new Date(), pokladnik, stornovane);
    	cfxmlPrehladZavController.setDennaZavierka(novaZavierka);
    	stage.show();
	}
	
	@FXML
	private void jbDennaZavierkaAction(ActionEvent event) throws IOException {
        
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklDennaZavierka.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Denná závierka");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	CFXMLPoklDennaZavierka cfxmlDennaZavController = fxmlLoader.getController();
    	List<NakupenyTovar> stornovane = Database.getStornovanyTovarPokladnika(pokladnik, new Date());
    	DennaZavierka novaZavierka = new DennaZavierka(0, nakupyPokladnika, new Date(), pokladnik,stornovane);
    	cfxmlDennaZavController.setDennaZavierka(novaZavierka);
    	stage.showAndWait();
    	if (cfxmlDennaZavController.isZavierkaVykonana()) {
			zavierkaVykonana = true;
			Node source = (Node) event.getSource();
	        Stage thisStage = (Stage) source.getScene().getWindow();
	        thisStage.close();
		}

	}
	
	@FXML
	private void jbIntervalovaZavierkaAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklIntervalovaZavierkaVyber.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Prehľadová závierka");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    CFXMLPoklIntervalovaZavierkaVyber cfxmlIntervalZavController = fxmlLoader.getController();
	    cfxmlIntervalZavController.setPokladnik(pokladnik);
    	stage.show();
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	public void setInfoNaZavierku(UserPokladnik pokladnik,List<Nakup> nakupy) {
		this.nakupyPokladnika = nakupy;
		this.pokladnik = pokladnik;
	}
	
	public boolean isZavierkaVykonana() {
		return zavierkaVykonana;
	}

}
