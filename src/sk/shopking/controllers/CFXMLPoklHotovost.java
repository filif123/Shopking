package sk.shopking.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.*;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.Platba;
import sk.shopking.tools.ReceiptGenerator;

/**
 * @author Filip
 *
 */
public class CFXMLPoklHotovost implements Initializable{

	private final UserPokladnik aktualnyUser = (UserPokladnik) CFXMLLogin.getZhoda();
	
	@FXML private TextField jtfZadanaHotovost;
	@FXML private Label jlVydat;

	private float cenaZaNakup;
	private float zaplatene;
	private List<NakupenyTovar> nakupeneTovary;
	private Pokladnica pokladnica;
	
	private boolean platbaZrusena = true;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jtfZadanaHotovost.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*(\\.\\d*)?")) {
				jtfZadanaHotovost.setText(newValue.replaceAll("[^\\d||.]", ""));
			}
			else if (newValue.isEmpty()) {
				jlVydat.setText("0,00");
			}
			else {
				float zadanaSuma =Float.parseFloat(newValue);
				zaplatene = zadanaSuma;
				jlVydat.setText(new DecimalFormat("0.00").format(zadanaSuma - cenaZaNakup));
			}
		});
		
	}

	@FXML
	private void jbOKAction(ActionEvent event) {
		if (cenaZaNakup > zaplatene) {
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
		
		ReceiptGenerator receiptCreator = new ReceiptGenerator(nakupeneTovary, pokladnica, idDokladu, new Platba(cenaZaNakup,zaplatene),actualTime);
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
