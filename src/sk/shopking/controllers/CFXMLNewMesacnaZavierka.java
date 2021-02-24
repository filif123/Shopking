package sk.shopking.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.DennaZavierka;
import sk.shopking.MesacnaZavierka;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.Utils;
import sk.shopking.tools.ZavierkaGenerator;

/**
 * @author filip
 *
 */
public class CFXMLNewMesacnaZavierka implements Initializable{

	private final List<MesacnaZavierka> vsetkyMesacneZavierky = Database.getMesacneZavierky();
	private final List<DennaZavierka> vsetkyDenneZavierky = Database.getDenneZavierky();
	
	private MesacnaZavierka vytvorenaZavierka;
	private Date intervalOd,intervalDo,now;
	private boolean zavierkaVykonana = false;
	private boolean zavierkaMozeBytVykonana = true;
	
	@FXML private DatePicker jdpOd,jdpDo;
	@FXML private TextField jtfCasZavierky;
	@FXML private TextField jtfCelkovyObrat10,jtfCelkovyZaklad10,jtfCelkoveDPH10;
	@FXML private TextField jtfCelkovyObrat20,jtfCelkovyZaklad20,jtfCelkoveDPH20;
	@FXML private TextField jtfCelkovyObrat;
	@FXML private TextArea jtaOriginal;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		now = new Date();
		intervalDo = now;
		jtfCasZavierky.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(now));
		
		List<Date> datumyZavierok = new ArrayList<>();
		for (MesacnaZavierka mesacnaZavierka : vsetkyMesacneZavierky) {
			datumyZavierok.add(mesacnaZavierka.getCasZavierky());
		}
		
		if (datumyZavierok.isEmpty()) {
			LocalDate dateOd = LocalDate.now().withDayOfMonth(1);
			jdpOd.setValue(dateOd);
			intervalOd = Date.from(dateOd.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			
			LocalDate dateDo = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			jdpDo.setValue(dateDo);
			
			List<DennaZavierka> vybraneDenneZavierky = new ArrayList<>();
			for (DennaZavierka dennaZavierka : vsetkyDenneZavierky) {
				if (Utils.isBetweenDates(dennaZavierka.getCasZavierky(), intervalOd, intervalDo)) {
					vybraneDenneZavierky.add(dennaZavierka);
				}
			}
			
			this.vytvorenaZavierka = new MesacnaZavierka(0, vybraneDenneZavierky, now, intervalOd,intervalDo);

		}
		else {
			Date najblizsiDatum = Collections.min(datumyZavierok, (d1, d2) -> {
				long diff1 = Math.abs(d1.getTime() - now.getTime());
				long diff2 = Math.abs(d2.getTime() - now.getTime());
				return Long.compare(diff1, diff2);
			});
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(najblizsiDatum);
			calendar.add(Calendar.DATE, 1);
			intervalOd = calendar.getTime();
			
			LocalDate dateOd = intervalOd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			jdpOd.setValue(dateOd);
			
			LocalDate dateDo = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			jdpDo.setValue(dateDo);
			
			List<DennaZavierka> vybraneDenneZavierky = new ArrayList<>();
			for (DennaZavierka dennaZavierka : vsetkyDenneZavierky) {
				if (Utils.isBetweenDates(dennaZavierka.getCasZavierky(), intervalOd, intervalDo)) {
					vybraneDenneZavierky.add(dennaZavierka);
				}
			}

			this.vytvorenaZavierka = new MesacnaZavierka(0, vybraneDenneZavierky, now, intervalOd,intervalDo);

		}
		jtfCelkovyObrat10.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getObratSadzba10()));
		jtfCelkovyZaklad10.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getZakladSadzba10()));
		jtfCelkoveDPH10.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getDphSadzba10()));
		jtfCelkovyObrat20.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getObratSadzba20()));
		jtfCelkovyZaklad20.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getZakladSadzba20()));
		jtfCelkoveDPH20.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getDphSadzba20()));
		jtfCelkovyObrat.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getSumaObrat()));
		jtaOriginal.setText(ZavierkaGenerator.generateMesacnaZavierka(vytvorenaZavierka));

		jdpOd.valueProperty().addListener((ov, oldValue, newValue) -> {
			
			if (jdpDo.getValue().isAfter(newValue)) {
				jdpOd.setStyle("");
				jdpDo.setStyle("");
				intervalOd = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
				calculateDennaZavierkaBetweenDates();
			}
			else {
				zavierkaMozeBytVykonana = false;
				jdpOd.setStyle("-fx-background-color: red;");
			}
        });
		jdpDo.valueProperty().addListener((ov, oldValue, newValue) -> {			
			if (jdpOd.getValue().isBefore(newValue)) {
				jdpDo.setStyle("");
				jdpOd.setStyle("");
				intervalDo = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
				calculateDennaZavierkaBetweenDates();
			}
			else {
				zavierkaMozeBytVykonana = false;
				jdpDo.setStyle("-fx-background-color: red;");
			}
        });		

	}
	
	@FXML
	private void jbEvidovatAction(ActionEvent event) {
		if (zavierkaMozeBytVykonana) {
			zavierkaVykonana = true;
			Database.doMesacnaZavierka(intervalOd, intervalDo, now);
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        stage.close();
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	    	alert.setTitle("Neplatné zadanie");
	    	alert.setHeaderText("Zadanie k mesačnej závierke je neplatné");
	    	alert.setContentText("Upravte zadanie tejto mesačnej závierky!");
	    	alert.show();
		}
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	
	public MesacnaZavierka getMesacnaZavierka() {
		return this.vytvorenaZavierka;
	}
	
	public boolean isZavierkaVykonana() {
		return zavierkaVykonana;
	}
	
	private void calculateDennaZavierkaBetweenDates() {
		jtfCasZavierky.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(now));
		
		List<DennaZavierka> vybraneDenneZavierky = new ArrayList<>();
		for (DennaZavierka dennaZavierka : vsetkyDenneZavierky) {
			if (Utils.isBetweenDates(dennaZavierka.getCasZavierky(), intervalOd, intervalDo)) {
				vybraneDenneZavierky.add(dennaZavierka);
			}
		}
		
		for (DennaZavierka vybranaDennaZavierka : vybraneDenneZavierky) {
			for (MesacnaZavierka mesacnaZavierka : vsetkyMesacneZavierky) {
				for (DennaZavierka dennaZavierka : mesacnaZavierka.getDenneZavierky()) {
					if (Utils.isSameDay(vybranaDennaZavierka.getCasZavierky(), dennaZavierka.getCasZavierky())) {
						Alert alert = new Alert(AlertType.ERROR);
						Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
				        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
						new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				    	alert.setTitle("Prelínajúce sa mesačné závierky");
				    	alert.setHeaderText("Mesačná závierka bola v zadanom intervale vykonaná");
				    	alert.setContentText("Upravte nastavený časový interval!");
				    	alert.show();
				    	zavierkaMozeBytVykonana = false;
				    	return;
					}
				}
			}
		}
		
		if (intervalOd.after(now) || intervalDo.after(now)) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	    	alert.setTitle("Neplatný časový interval");
	    	alert.setHeaderText("Zadaný časový interval je neplatný (dátum v budúcnosti)");
	    	alert.setContentText("Upravte nastavený časový interval!");
	    	alert.show();
	    	zavierkaMozeBytVykonana = false;
	    	return;
		}
		
		this.vytvorenaZavierka = new MesacnaZavierka(0, vybraneDenneZavierky, now, intervalOd,intervalDo);
		
		jtfCelkovyObrat10.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getObratSadzba10()));
		jtfCelkovyZaklad10.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getZakladSadzba10()));
		jtfCelkoveDPH10.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getDphSadzba10()));
		
		jtfCelkovyObrat20.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getObratSadzba20()));
		jtfCelkovyZaklad20.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getZakladSadzba20()));
		jtfCelkoveDPH20.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getDphSadzba20()));
		
		jtfCelkovyObrat.setText(new DecimalFormat("#.##").format(vytvorenaZavierka.getSumaObrat()));
		
		jtaOriginal.setText(ZavierkaGenerator.generateMesacnaZavierka(vytvorenaZavierka));
	}

}
