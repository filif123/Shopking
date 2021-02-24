package sk.shopking.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.*;
import sk.shopking.tools.*;

/**
 * @author Filip
 *
 */
public class CFXMLPoklStravneListky implements Initializable{

private final UserPokladnik aktualnyUser = (UserPokladnik) CFXMLLogin.getZhoda();
	
	@FXML private TextField jtfHodnotaListka,jtfPocetListkov;
	@FXML private Label jlDoplatit;
	@FXML private TextField jtfZadanaHotovost;
	@FXML private Label jlVydat;

	private int pocetListkov;
	private float hodnotaListka;
	private float cenaZaNakup;
	private float zaplateneListkami;
	private float zaplateneHotovost;
	private float doplatit;
	private List<NakupenyTovar> nakupeneTovary;
	private Pokladnica pokladnica;
	private boolean platbaZrusena = true;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jtfHodnotaListka.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.isEmpty()) {
				jlDoplatit.setText("0,00");
			}
			else if (!newValue.matches("\\d*(\\.\\d*)?||\\d*(\\,\\d*)?")) {
				jtfHodnotaListka.setText(newValue.replaceAll("[^\\d||.||,]", ""));
			}

			else {
				String zadanaSumaString = newValue.replaceAll(",", ".");
				hodnotaListka = Float.parseFloat(zadanaSumaString);
				zaplateneListkami = hodnotaListka * pocetListkov;
				doplatit = cenaZaNakup - zaplateneListkami;
				if (doplatit < 0) {
					doplatit = 0;
				}
				jlDoplatit.setText("" + new DecimalFormat("0.00").format(doplatit));
			}
		});
		
		jtfPocetListkov.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.isEmpty()) {
				jlDoplatit.setText("0,00");
			}
			else if(!Utils.isNumber(newValue)){
				jlDoplatit.setText("0,00");
			}
			else {
				pocetListkov = Integer.parseInt(newValue);
				zaplateneListkami = hodnotaListka * pocetListkov;
				doplatit = cenaZaNakup - zaplateneListkami;
				if (doplatit < 0) {
					doplatit = 0;
				}
				jlDoplatit.setText("" + new DecimalFormat("0.00").format(doplatit));
			}
		});
		
		jtfZadanaHotovost.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*(\\.\\d*)?")) {
				jtfZadanaHotovost.setText(newValue.replaceAll("[^\\d||.]", ""));
			}
			else if (newValue.isEmpty()) {
				jlVydat.setText("0,00");
			}
			else {
				float zadanaSuma =Float.parseFloat(newValue);
				zaplateneHotovost = zadanaSuma;
				jlVydat.setText(new DecimalFormat("0.00").format(zadanaSuma - doplatit));
			}
		});
		
	}

	@FXML
	private void jbOKAction(ActionEvent event) {
		if ((cenaZaNakup != zaplateneListkami + doplatit) || (doplatit > zaplateneHotovost)) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Chyba");
        	alert.setHeaderText("Pri zadávaní hodnôt platby sa našla chyba");
        	alert.setContentText("Skontrolujte a opravte hodnoty platby!");
        	alert.show();
        	return;
		}
		platbaZrusena = false;
		Date actualTime = new Date();
		
		int idDokladu = Database.createNewDoklad(actualTime, pokladnica.getPokladnikUser());
		Nakup tentoNakup = new Nakup(nakupeneTovary, new Doklad(idDokladu, actualTime, pokladnica.getPokladnikUser()));
		Database.setNewNakup(tentoNakup);
		
		ReceiptGenerator receiptCreator = new ReceiptGenerator(nakupeneTovary, pokladnica, idDokladu, new Platba(cenaZaNakup,zaplateneHotovost,pocetListkov,hodnotaListka),actualTime);
		receiptCreator.generateReceipt();
		receiptCreator.printReceipt();
		
		Database.setStavPokladnice(aktualnyUser.getId(), Database.getStavPokladnice(aktualnyUser.getId()) + cenaZaNakup);

		FXMLTools.closeWindow(event);
	}
	
	
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		platbaZrusena = true;
		FXMLTools.closeWindow(event);
	}
	
	public void setNakupeneTovary(List<NakupenyTovar> nakupeneTovary) {
		this.nakupeneTovary = nakupeneTovary;
		float cena = 0;
		for (NakupenyTovar nakup : nakupeneTovary) {
			cena+=nakup.getNakupenaCena();
		}
		cenaZaNakup = cena;
	}
	
	public void setPokladnica(Pokladnica pokladnica) {
		this.pokladnica = pokladnica;
	}
	
	public boolean isPlatbaZrusena() {
		return platbaZrusena;
	}

}
