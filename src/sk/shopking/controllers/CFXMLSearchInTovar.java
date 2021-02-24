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
import sk.shopking.Tovar;
import sk.shopking.tools.FXMLTools;

/**
 * Táto trieda je kontrolér pre okno prehľadania tovarov.
 * @author Filip
 *
 */
public class CFXMLSearchInTovar implements Initializable {
	
	private List<Tovar> searchInList = new ArrayList<>();
	private final List<Integer> indexTable = new ArrayList<>();
	
	@FXML private ChoiceBox<String> jcSearchType;
	@FXML private TextField jtSearchFor;
	
	public List<Integer> returnIndexy() {
		return indexTable;
	}
	
	public void setList(List<Tovar> searchInList) {
		this.searchInList = searchInList;
	}
	private void fillChoiceBox() {
		List<String> searchTypeList = new ArrayList<>();
		searchTypeList.add("Názov výrobku");
		searchTypeList.add("EAN");
		searchTypeList.add("PLU");
		jcSearchType.getItems().setAll(searchTypeList);
		jcSearchType.getSelectionModel().selectFirst();
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		fillChoiceBox();
    }
	
	@FXML
	private void jbSearchAction (ActionEvent event) {
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
				case "Názov výrobku": {
					for (int i = 0; i < searchInList.size(); i++) {
						if (searchInList.get(i).getTovarName().contains(hladane)) {
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
					break;
				}
				case "EAN": {
					for (int i = 0; i < searchInList.size(); i++) {
						long ean = searchInList.get(i).getTovarEAN();
						long hladaneEan = Long.parseLong(hladane);
						if (ean == hladaneEan) {
							indexTable.add(i);
							FXMLTools.closeWindow(event);
							return;
						}

					}
					Alert alert = new Alert(AlertType.INFORMATION);
					new JMetro(alert.getDialogPane().getScene(), Style.LIGHT);
					alert.setTitle("Zlé parametre");
					alert.setHeaderText("Zadaný reťazec nenájdený");
					alert.setContentText("Zadajte iný reťazec!");
					alert.show();
					return;
				}
				case "PLU": {
					for (int i = 0; i < searchInList.size(); i++) {
						int kod = searchInList.get(i).getTovarPLU();
						int hladanyKod = Integer.parseInt(hladane);
						if (kod == hladanyKod) {
							indexTable.add(i);
							FXMLTools.closeWindow(event);
							return;
						}
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
