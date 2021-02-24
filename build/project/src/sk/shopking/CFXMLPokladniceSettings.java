/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sk.shopking.tools.Database;

/**
 * @author Filip
 *
 */
public class CFXMLPokladniceSettings implements Initializable{
	private List<Pokladnica> pokladniceFromDB = Database.getPokladnice();
	private List<Pokladnica> pridanePokladnice = new ArrayList<>();
	private List<Pokladnica> upravenePokladnice = new ArrayList<>();
	private List<Integer> upravenePokladniceOldIDs = new ArrayList<>();
	private List<Pokladnica> zmazanePokladnice = new ArrayList<>();
	private boolean okID = true,okDKP = true;
	
	@FXML private TextField jtfPokladnicaNumber;
	@FXML private TextField jtfPokladnicaDKP;
	@FXML private ListView<Pokladnica> jlvPokladnice;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jlvPokladnice.getItems().addAll(pokladniceFromDB);
		jlvPokladnice.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Pokladnica vybranaPokladnica = jlvPokladnice.getSelectionModel().getSelectedItem();
				if (!jlvPokladnice.getSelectionModel().isEmpty()) {
					jtfPokladnicaNumber.setText("" + vybranaPokladnica.getIdPokladnice());
					jtfPokladnicaDKP.setText("" + vybranaPokladnica.getDkpPokladnice());
				}	
			}
		});
		jtfPokladnicaNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					jtfPokladnicaNumber.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				else if (newValue.isEmpty()) {
					
				}
				else {
					int idPokladnice = Integer.parseInt(newValue);
					for (int i = 0; i < pokladniceFromDB.size();i++) {
						if (idPokladnice == pokladniceFromDB.get(i).getIdPokladnice() && jlvPokladnice.getSelectionModel().getSelectedIndex() != i) {
							jtfPokladnicaNumber.setStyle("-fx-text-fill: red;");
							okID = false;
							return;
						}
					}
					jtfPokladnicaNumber.setStyle("");
					okID = true;
				}
			}
		});
		jtfPokladnicaDKP.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					jtfPokladnicaDKP.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				else if (newValue.isEmpty()) {
					
				}
				else {
					String dkpPokladnice = newValue;
					for (int i = 0; i < pokladniceFromDB.size();i++) {
						if (dkpPokladnice.equals(pokladniceFromDB.get(i).getDkpPokladnice()) && jlvPokladnice.getSelectionModel().getSelectedIndex() != i) {
							jtfPokladnicaDKP.setStyle("-fx-text-fill: red;");
							okDKP = false;
							return;
						}
					}
					jtfPokladnicaDKP.setStyle("");
					okDKP = true;
				}
			}
		});
	}
	
	@FXML
	private void jbAddAction(ActionEvent event) throws IOException {
		if (!jtfPokladnicaNumber.getText().isEmpty() && !jtfPokladnicaDKP.getText().isEmpty() && okDKP && okID) {
			int pokladnicaNum = Integer.parseInt(jtfPokladnicaNumber.getText());
			String pokladnicaDKP = jtfPokladnicaDKP.getText();
			Pokladnica novaPokladnica = new Pokladnica(pokladnicaNum, pokladnicaDKP, false, null);
			pridanePokladnice.add(novaPokladnica);
			jlvPokladnice.getItems().add(novaPokladnica);
		}
		
	}
	
	@FXML
	private void jbDeleteAction(ActionEvent event) throws IOException {
		Pokladnica pokladnicaToRemove = jlvPokladnice.getSelectionModel().getSelectedItem();
		int idList = jlvPokladnice.getSelectionModel().getSelectedIndex();
		zmazanePokladnice.add(pokladnicaToRemove);
		jlvPokladnice.getItems().remove(idList);
	}
	
	@FXML
	private void jbEditAction(ActionEvent event) throws IOException {
		if (!jtfPokladnicaNumber.getText().isEmpty() && !jtfPokladnicaDKP.getText().isEmpty() && okDKP && okID) {
			Pokladnica pokladnicaToEdit = jlvPokladnice.getSelectionModel().getSelectedItem();
			int idList = jlvPokladnice.getSelectionModel().getSelectedIndex();
			int oldID = pokladnicaToEdit.getIdPokladnice();
			int newID = Integer.parseInt(jtfPokladnicaNumber.getText());
			String dkp = jtfPokladnicaDKP.getText();
			Pokladnica upravenaPokladnica = new Pokladnica(newID,dkp,false,null);
			upravenePokladnice.add(upravenaPokladnica);
			upravenePokladniceOldIDs.add(oldID);
			jlvPokladnice.getItems().set(idList, upravenaPokladnica);
		}
		
		
	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) throws IOException {
		if (pridanePokladnice != null && !pridanePokladnice.isEmpty()) {
			Database.addPokladnice(pridanePokladnice);
		}
		if (upravenePokladnice != null && !upravenePokladnice.isEmpty() && upravenePokladniceOldIDs != null && !upravenePokladniceOldIDs.isEmpty()) {
			Database.updatePokladnice(upravenePokladnice,upravenePokladniceOldIDs);
		}
		if (zmazanePokladnice != null && !zmazanePokladnice.isEmpty()) {
			Database.deletePokladnice(zmazanePokladnice);
		}
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

}
