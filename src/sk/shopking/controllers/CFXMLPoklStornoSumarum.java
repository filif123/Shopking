package sk.shopking.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.Doklad;
import sk.shopking.JednotkaType;
import sk.shopking.NakupenyTovar;
import sk.shopking.UserPokladnik;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * @author filip
 *
 */
public class CFXMLPoklStornoSumarum implements Initializable{

	@FXML private Label jlSumaStorno,jlPocetPoloziek;
	
	private Doklad doklad;
	private UserPokladnik pokladnik;
	private List<NakupenyTovar> polozkyNaOdstranenie;
	
	private boolean stornoVykonane = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	@FXML
	private void jbExecuteAction(ActionEvent event) {
		for (NakupenyTovar nakupenyTovar : polozkyNaOdstranenie) {
			if (nakupenyTovar.getNoveMnozstvoPoStorne() == 0 || nakupenyTovar.getTovarJednotka().equals(JednotkaType.G) || nakupenyTovar.getTovarJednotka().equals(JednotkaType.KG)) {
				Database.stornoNakupenyTovar(doklad.getIdDoklad(), nakupenyTovar,pokladnik);
			}
			else {
				Database.stornoNakupenyTovar(doklad.getIdDoklad(), nakupenyTovar, nakupenyTovar.getNoveMnozstvoPoStorne(),pokladnik);
			}
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
    	alert.setTitle("Stornovanie úspešné");
    	alert.setHeaderText("Požadované nákupné položky sa úspešne stornovali");
    	alert.showAndWait();
    	
    	stornoVykonane = true;

		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	public void setInfoNaStorno(Doklad doklad,List<NakupenyTovar> tovaryNaStorno, UserPokladnik pokladnik) {
		this.polozkyNaOdstranenie = tovaryNaStorno;
		this.doklad = doklad;
		this.pokladnik = pokladnik;
		
		int pocetPol = 0;
		float suma = 0;
		for (NakupenyTovar nakupenyTovar : tovaryNaStorno) {
			if (nakupenyTovar.getTovarJednotka().equals(JednotkaType.KS)) {
				pocetPol = pocetPol + ((int)nakupenyTovar.getNakupeneMnozstvo() - nakupenyTovar.getNoveMnozstvoPoStorne());
				suma = suma + ((int)nakupenyTovar.getNakupeneMnozstvo() - nakupenyTovar.getNoveMnozstvoPoStorne()) * nakupenyTovar.getTovarJednotkovaCena();
			}
			else {
				pocetPol = pocetPol + 1;
				suma = suma + nakupenyTovar.getNakupenaCena();
			}
			
		}
		
		jlPocetPoloziek.setText("" + pocetPol);
		jlSumaStorno.setText(new DecimalFormat("0.00").format(suma));
	}
	
	public boolean stornoVykonane() {
		return stornoVykonane;
	}

}
