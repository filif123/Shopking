/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.ShopKingTools;

/**
 * Táto trieda je kontrolér pre okno prehľadania tovarov.
 * @author Filip
 *
 */
public class CFXMLSearchTovar implements Initializable {
	
	private ObservableList<String> searchType = FXCollections.observableArrayList();
	private List<Tovar> aktualneTovary;
	private FXMLLoader loader;
	private Parent root;
	private CFXMLEditTovar cfxmlEditTovar;
	private static List<Integer> indexTable = new ArrayList<Integer>();
	
	@FXML
	private ChoiceBox<String> jcSearchType;
	
	@FXML
	private TextField jtSearchFor;
	
	public static List<Integer> returnIndexy() {
		return indexTable;
		
	}
	private void fillChoiceBox() {
		List<String> searchTypeList = new ArrayList<String>();
		searchTypeList.add("Názov výrobku");
		searchTypeList.add("EAN");
		searchTypeList.add("ID výrobku");
		searchType.setAll(searchTypeList);
		jcSearchType.setItems(searchType);
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		fillChoiceBox();
		loader = new FXMLLoader(getClass().getResource("/resources/scenes/editTovar.fxml"));
        try {
			root = loader.load();
		} catch (IOException e) {
			ShopKingTools.showExceptionDialog(e);
		}
        cfxmlEditTovar = loader.getController();
        //aktualneTovary = cfxmlEditTovar.getTovary();
    }
	
	@FXML
	private void jbSearchAction (ActionEvent event) throws IOException{
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
	        return;
		}
		else {
			if(moznost.equals("Názov výrobku")) {
				for(int i = 0; i < aktualneTovary.size();i++) {
					if(aktualneTovary.get(i).getTovarName().contains(hladane)) {
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
		        return;
			}

			else if(moznost.equals("EAN")) {
				for(int i = 0; i < aktualneTovary.size();i++) {
					long ean = aktualneTovary.get(i).getTovarEAN();
					long hladaneEan = Long.parseLong(hladane);
					if(ean == hladaneEan) {
						Node source = (Node) event.getSource();
				        Stage stage = (Stage) source.getScene().getWindow();
				        indexTable.add(i);
				        stage.close();
				        return;
					}
					
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        alert.setTitle("Zlé parametre");
		        alert.setHeaderText("Zadaný reťazec nenájdený");
		        alert.setContentText("Zadajte iný reťazec!");
		        alert.show();
		        return;	
			}
			else if(moznost.equals("ID výrobku")) {
				for(int i = 0; i < aktualneTovary.size();i++) {
					int kod = aktualneTovary.get(i).getTovarPLU();
					int hladanyKod = Integer.parseInt(hladane);
					if(kod == hladanyKod) {
						Node source = (Node) event.getSource();
				        Stage stage = (Stage) source.getScene().getWindow();
				        indexTable.add(i);
				        stage.close();
				        return;
					}
					
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        alert.setTitle("Zlé parametre");
		        alert.setHeaderText("Zadaný reťazec nenájdený");
		        alert.setContentText("Zadajte iný reťazec!");
		        alert.show();
		        return;	
			}
		}

	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) throws IOException{
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
}
