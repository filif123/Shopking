/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.shopking.tools.Database;
import sk.shopking.tools.Platba;
import sk.shopking.tools.ReceiptCreator;

/**
 * @author Filip
 *
 */
public class CFXMLPoklHotovost implements Initializable{

	@FXML private TextField jtfZadanaHotovost;
	@FXML private Label jlVydat;

	private float cenaZaNakup;
	private float zaplatene;
	private List<NakupenyTovar> nakupeneTovary;
	private Pokladnica pokladnica;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jtfZadanaHotovost.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					jtfZadanaHotovost.setText(newValue.replaceAll("[^\\d||.]", ""));
		        }
				else if (newValue.isEmpty()) {
					jlVydat.setText("0,00");
				}
				else {
					float zadanaSuma =Float.parseFloat(newValue);
					zaplatene = zadanaSuma;
					jlVydat.setText("" + new DecimalFormat("0.00").format(zadanaSuma - cenaZaNakup));
				}	
			}
		});
		
	}

	@FXML
	private void jbOKAction(ActionEvent event) throws IOException {
		Date actualTime = new Date();
		
		int idDokladu = Database.createNewDoklad(actualTime, pokladnica.getPokladnikUser());
		Nakup tentoNakup = new Nakup(nakupeneTovary, new Doklad(idDokladu, actualTime, pokladnica.getPokladnikUser()));
		Database.setNewNakup(tentoNakup);
		
		ReceiptCreator receiptCreator = new ReceiptCreator(nakupeneTovary, pokladnica, idDokladu, new Platba(cenaZaNakup,zaplatene),actualTime);
		receiptCreator.generateReceipt();
		receiptCreator.printReceipt();
		
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	
	
	@FXML
	private void jbStornoAction(ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
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
}
