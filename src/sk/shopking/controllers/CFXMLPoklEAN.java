package sk.shopking.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.shopking.Tovar;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLPoklEAN implements Initializable {
	
	private final List<Tovar> vsetkyTovary = Database.getTovary();
	
	//tovar, ktory je vybraty
	private Tovar vybranyTovar;
	//tovar, ktory bol vybraty a bolo stlacene tlacidlo pridat
	private Tovar tovarExport;
	
	@FXML private TextField jtfEAN;
	@FXML private Label jlStav;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jtfEAN.textProperty().addListener((observable, oldValue, newValue) -> {
			Long eanLong;
			try {
				eanLong = Long.parseLong(newValue);
			} catch (NumberFormatException e) {
				if(newValue.isEmpty()) {
					jlStav.setStyle("");
					jlStav.setText("Zadajte EAN");
				}
				else {
					jlStav.setStyle("-fx-text-fill: red;");
					jlStav.setText("Nesprávny kód");
				}
				return;

			}

			for (Tovar tovar : vsetkyTovary) {
				if (eanLong.equals(tovar.getTovarEAN())) {
					jlStav.setStyle("-fx-text-fill: green;");
					jlStav.setText("" + tovar.getTovarName());
					vybranyTovar = tovar;
					return;
				}
			}
			jlStav.setStyle("-fx-text-fill: red;");
			jlStav.setText("Nenájdené");
		});
	}
	
	@FXML
	private void jbAddAction (ActionEvent event) {
		tovarExport = vybranyTovar;
		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	public Tovar returnTovar() {
		return tovarExport;
	}
}
