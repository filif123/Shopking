package sk.shopking.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.User;
import sk.shopking.tools.FXMLTools;

/**
 * Táto trieda je kontrolér pre okno prehľadania používateľov.
 * @author filip
 *
 */
public class CFXMLSearchInUser implements Initializable{

	private List<User> searchInList = new ArrayList<>();
	private final List<Integer> indexTable = new ArrayList<>();
	
	@FXML private ChoiceBox<String> jcSearchType;
	@FXML private TextField jtSearchFor;
	
	public List<Integer> returnIndexy() {
		return indexTable;
	}
	
	public void setList(List<User> searchInList) {
		this.searchInList = searchInList;
	}
	private void fillChoiceBox() {
		List<String> searchTypeList = new ArrayList<>();
		searchTypeList.add("Meno");
		searchTypeList.add("Priezvisko");
		searchTypeList.add("Používateľské meno");
		jcSearchType.getItems().setAll(searchTypeList);
		jcSearchType.getSelectionModel().selectFirst();
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		fillChoiceBox();
    }
	
	@FXML
	private void jbSearchAction (ActionEvent event){
		String moznost = jcSearchType.getSelectionModel().getSelectedItem();
		String hladane = jtSearchFor.getText();
		if(hladane.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Nebol zadaný hľadaný reťazec");
	        alert.setContentText("Zadajte hľadaný reťazec!");
	        alert.show();
	        return;
		}
		if(moznost == null || moznost.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Nebol vybratý typ hľadania");
	        alert.setContentText("Vyberte typ hľadania!");
	        alert.show();
		}
		else {
			switch (moznost) {
				case "Meno": {
					for (int i = 0; i < searchInList.size(); i++) {
						if (searchInList.get(i).getUserMeno().contains(hladane)) {
							indexTable.add(i);
						}
					}
					if (!indexTable.isEmpty()) {
						FXMLTools.closeWindow(event);
						return;
					}

					Alert alert = new Alert(AlertType.INFORMATION);
					new JMetro(alert.getDialogPane().getScene(), Style.LIGHT);
					alert.setTitle("Zlé parametre");
					alert.setHeaderText("Zadaný reťazec nenájdený");
					alert.setContentText("Zadajte iný reťazec!");
					alert.show();
					return;
				}
				case "Priezvisko": {
					for (int i = 0; i < searchInList.size(); i++) {
						if (searchInList.get(i).getUserPriezvisko().contains(hladane)) {
							indexTable.add(i);
						}
					}
					if (!indexTable.isEmpty()) {
						FXMLTools.closeWindow(event);
						return;
					}

					Alert alert = new Alert(AlertType.INFORMATION);
					new JMetro(alert.getDialogPane().getScene(), Style.LIGHT);
					alert.setTitle("Zlé parametre");
					alert.setHeaderText("Zadaný reťazec nenájdený");
					alert.setContentText("Zadajte iný reťazec!");
					alert.show();
					return;
				}
				case "Používateľské meno": {
					for (int i = 0; i < searchInList.size(); i++) {
						if (searchInList.get(i).getUserNickname().contains(hladane)) {
							indexTable.add(i);
						}
					}
					if (!indexTable.isEmpty()) {
						FXMLTools.closeWindow(event);
						return;
					}

					Alert alert = new Alert(AlertType.INFORMATION);
					new JMetro(alert.getDialogPane().getScene(), Style.LIGHT);
					alert.setTitle("Zlé parametre");
					alert.setHeaderText("Zadaný reťazec nenájdený");
					alert.setContentText("Zadajte iný reťazec!");
					alert.show();
				}
			}
		}
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
}
