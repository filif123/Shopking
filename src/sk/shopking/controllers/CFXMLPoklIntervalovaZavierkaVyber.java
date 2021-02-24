package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.*;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.TimeSpinner;

/**
 * @author filip
 *
 */
public class CFXMLPoklIntervalovaZavierkaVyber implements Initializable{
	
	private UserPokladnik pokladnik;
	private final Date casOd = new Date();
	private final Date casDo = new Date();
	
	@FXML private TimeSpinner jsCasOd,jsCasDo;

	
	public void setPokladnik(UserPokladnik pokladnik) {
		this.pokladnik = pokladnik;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Date dnes = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dnes);
		
		jsCasOd.valueProperty().addListener((obs, oldTime, newTime) -> {
			Instant instant1 = newTime.atDate(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))).atZone(ZoneId.systemDefault()).toInstant();
			casOd.setTime(Date.from(instant1).getTime());
		});
		
		jsCasDo.valueProperty().addListener((obs, oldTime, newTime) -> {
			Instant instant2 = newTime.atDate(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))).atZone(ZoneId.systemDefault()).toInstant();
			casDo.setTime(Date.from(instant2).getTime());
		});
	}
	
	@FXML
	private void jbOKAction(ActionEvent event) throws IOException{
		if (casOd.after(casDo)) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Čas začiatku závierky nasleduje po konci závierky");
	        alert.setContentText("Skontrolujte zadaný interval!");
	        alert.show();
	        return;
		}
		Date dnes = new Date();
		
		SimpleDateFormat dFormat = new SimpleDateFormat("HH:mm");
		
		List<Nakup> nakupyPokladnikaDnes = Database.getNakupy(pokladnik, dnes);
    	List<NakupenyTovar> stornovane = Database.getStornovanyTovarPokladnikaInterval(pokladnik,casOd,casDo);
    	
    	List<Nakup> nakupyVIntervale = new ArrayList<>();
    	
    	for (Nakup nakupPokladnikaDnes : nakupyPokladnikaDnes) {
			if (nakupPokladnikaDnes.getDokladNakupu().getCasNakupu().after(casOd) && nakupPokladnikaDnes.getDokladNakupu().getCasNakupu().before(casDo)) {
				nakupyVIntervale.add(nakupPokladnikaDnes);
			}
		}
    	
    	DennaZavierka novaZavierka = new DennaZavierka(0, nakupyVIntervale, new Date(), pokladnik, stornovane);
    			
    	Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklPrehladovaZavierka.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Intervalová závierka od " + dFormat.format(casOd) + " do " + dFormat.format(casDo));
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	CFXMLPoklPrehladovaZavierka cfxmlPrehladZavController = fxmlLoader.getController();
    	cfxmlPrehladZavController.setDennaZavierka(novaZavierka);
    	stage.show();
		
		Node source = (Node) event.getSource();
        Stage thisStage = (Stage) source.getScene().getWindow();
        thisStage.close();
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}

}
