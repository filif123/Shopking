package sk.shopking.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sk.shopking.Pokladnica;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLPokladniceSettings implements Initializable{
	private final List<Pokladnica> pokladniceFromDB = Database.getPokladnice();
	private final List<Pokladnica> pridanePokladnice = new ArrayList<>();
	private final List<Pokladnica> upravenePokladnice = new ArrayList<>();
	private final List<Integer> upravenePokladniceOldIDs = new ArrayList<>();
	private final List<Pokladnica> zmazanePokladnice = new ArrayList<>();
	private boolean okID = true,okDKP = true;
	
	@FXML private TextField jtfPokladnicaNumber;
	@FXML private TextField jtfPokladnicaDKP;
	@FXML private ListView<Pokladnica> jlvPokladnice;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jlvPokladnice.getItems().addAll(pokladniceFromDB);
		jlvPokladnice.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			Pokladnica vybranaPokladnica = jlvPokladnice.getSelectionModel().getSelectedItem();
			if (!jlvPokladnice.getSelectionModel().isEmpty()) {
				jtfPokladnicaNumber.setText("" + vybranaPokladnica.getIdPokladnice());
				jtfPokladnicaDKP.setText("" + vybranaPokladnica.getDkpPokladnice());
			}
		});
		jtfPokladnicaNumber.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				jtfPokladnicaNumber.setText(newValue.replaceAll("[^\\d]", ""));
			}
			else if (!newValue.isEmpty()) {
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
		});
		jtfPokladnicaDKP.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				jtfPokladnicaDKP.setText(newValue.replaceAll("[^\\d]", ""));
			}
			else if (!newValue.isEmpty()) {
				for (int i = 0; i < pokladniceFromDB.size();i++) {
					if (newValue.equals(pokladniceFromDB.get(i).getDkpPokladnice()) && jlvPokladnice.getSelectionModel().getSelectedIndex() != i) {
						jtfPokladnicaDKP.setStyle("-fx-text-fill: red;");
						okDKP = false;
						return;
					}
				}
				jtfPokladnicaDKP.setStyle("");
				okDKP = true;
			}
		});
	}
	
	@FXML
	private void jbAddAction(ActionEvent event) {
		if (!jtfPokladnicaNumber.getText().isEmpty() && !jtfPokladnicaDKP.getText().isEmpty() && okDKP && okID) {
			int pokladnicaNum = Integer.parseInt(jtfPokladnicaNumber.getText());
			String pokladnicaDKP = jtfPokladnicaDKP.getText();
			Pokladnica novaPokladnica = new Pokladnica(pokladnicaNum, pokladnicaDKP, false, null,null);
			pridanePokladnice.add(novaPokladnica);
			jlvPokladnice.getItems().add(novaPokladnica);
		}
		
	}
	
	@FXML
	private void jbDeleteAction(ActionEvent event) {
		Pokladnica pokladnicaToRemove = jlvPokladnice.getSelectionModel().getSelectedItem();
		int idList = jlvPokladnice.getSelectionModel().getSelectedIndex();
		zmazanePokladnice.add(pokladnicaToRemove);
		jlvPokladnice.getItems().remove(idList);
	}
	
	@FXML
	private void jbEditAction(ActionEvent event) {
		if (!jtfPokladnicaNumber.getText().isEmpty() && !jtfPokladnicaDKP.getText().isEmpty() && okDKP && okID) {
			Pokladnica pokladnicaToEdit = jlvPokladnice.getSelectionModel().getSelectedItem();
			int idList = jlvPokladnice.getSelectionModel().getSelectedIndex();
			int oldID = pokladnicaToEdit.getIdPokladnice();
			int newID = Integer.parseInt(jtfPokladnicaNumber.getText());
			String dkp = jtfPokladnicaDKP.getText();
			Pokladnica upravenaPokladnica = new Pokladnica(newID,dkp,false,null,null);
			upravenePokladnice.add(upravenaPokladnica);
			upravenePokladniceOldIDs.add(oldID);
			jlvPokladnice.getItems().set(idList, upravenaPokladnica);
		}
		
		
	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) {
		if (!pridanePokladnice.isEmpty()) {
			Database.addPokladnice(pridanePokladnice);
		}
		if (!upravenePokladnice.isEmpty() && !upravenePokladniceOldIDs.isEmpty()) {
			Database.updatePokladnice(upravenePokladnice,upravenePokladniceOldIDs);
		}
		if (!zmazanePokladnice.isEmpty()) {
			Database.deletePokladnice(zmazanePokladnice);
		}
		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
}
