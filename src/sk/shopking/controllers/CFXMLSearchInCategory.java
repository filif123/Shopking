package sk.shopking.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.Category;
import sk.shopking.tools.FXMLTools;

/**
 * Táto trieda je kontrolér pre okno prehľadania kategórií tovarov.
 * @author filip
 *
 */
public class CFXMLSearchInCategory implements Initializable {

	private List<Category> searchInList = new ArrayList<>();
	private final List<Integer> indexTable = new ArrayList<>();
	
	@FXML private ChoiceBox<String> jcSearchType;
	@FXML private TextField jtSearchFor;
	
	public List<Integer> returnIndexy() {
		return indexTable;
	}
	
	public void setList(List<Category> searchInList) {
		this.searchInList = searchInList;
	}
	private void fillChoiceBox() {
		List<String> searchTypeList = new ArrayList<>();
		searchTypeList.add("Názov kategórie");
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
		}
		else {
			if(moznost.equals("Názov kategórie")) {
				for(int i = 0; i < searchInList.size();i++) {
					if(searchInList.get(i).getCategoryName().contains(hladane)) {
				        indexTable.add(i);
					}	
				}
				if(!indexTable.isEmpty()) {
					Node source = (Node) event.getSource();
			        Stage stage = (Stage) source.getScene().getWindow();
			        stage.close();
			        return;
				}
				
				Alert alert = new Alert(AlertType.INFORMATION);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        alert.setTitle("Zlé parametre");
		        alert.setHeaderText("Zadaný reťazec nenájdený");
		        alert.setContentText("Zadajte iný reťazec!");
		        alert.show();
			}
		}

	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
}
