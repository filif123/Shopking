/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
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

/**
 * @author Filip
 *
 */
public class CFXMLPoklPLU implements Initializable{

	private List<Tovar> vsetkyTovary = Database.getTovary();
	
	//tovar, ktory je vybraty
	private Tovar vybranyTovar;
	//tovar, ktory bol vybraty a bolo stlacene tlacidlo pridat
	private Tovar tovarExport;
	
	@FXML private TextField jtfPLU;
	@FXML private Label jlStav;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jtfPLU.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Integer pluInt;
				try {
					pluInt = Integer.parseInt(newValue);
				} catch (NumberFormatException e) {
					if(newValue.isEmpty()) {
						jlStav.setStyle("");
						jlStav.setText("Zadajte PLU");
						return;
					}
					else {
						jlStav.setStyle("-fx-text-fill: red;");
						jlStav.setText("Nesprávny kód");
						return;
					}
					
				}
				
				for (Tovar tovar : vsetkyTovary) {
					if (pluInt.equals(tovar.getTovarPLU())) {
						jlStav.setStyle("-fx-text-fill: green;");
						jlStav.setText("" + tovar.getTovarName());
						vybranyTovar = tovar;
						return;
					}
				}
				jlStav.setStyle("-fx-text-fill: red;");
				jlStav.setText("Nenájdené");
			}
			
		});
		
	}
	
	@FXML
	private void jbAddAction (ActionEvent event) throws IOException {
		tovarExport = vybranyTovar;
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	public Tovar returnTovar() {
		return tovarExport;
	}

}
