/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;


/**
 * Táto trieda je kontrolér pre okno pridania kategórie tovaru.
 * @author Filip
 *
 */
public class CFXMLAddCategory implements Initializable {
	
	@FXML private TextField jtName;
	@FXML private CheckBox jcPristupne;
	
	private String categoryName;
	private boolean pristupneMladistvym; 
	
	private List<Category> vsetkyKategorie;
	private Category newCategory = null;

	@Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
	
	@FXML
	private void jbSaveAction(ActionEvent event) throws IOException {
		categoryName = jtName.getText();
		pristupneMladistvym = jcPristupne.isSelected();
		
		if(categoryName.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadny názov kategórie tovaru");
        	alert.setContentText("Zadajte názov kategórie!");
        	alert.show();
		}
		else {
			for (Category category : vsetkyKategorie) {
				if (categoryName.equals(category.getCategoryName())){
					Alert alert = new Alert(AlertType.ERROR);
					new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        	alert.setTitle("Duplicitná kategória");
		        	alert.setHeaderText("Zadaná kategória tovaru sa už v zozname nachádza");
		        	alert.setContentText("Opravte zadanú kategóriu!");
		        	alert.show();
		        	return;
				}
			}
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        stage.close();
		}
		
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	public Category getNewCategory() {
		try {
			if (categoryName == null) {
				return null;
			}
			newCategory = new Category(0,categoryName,pristupneMladistvym);
		}catch (Exception e) {
			newCategory = null;
		}
		
		return newCategory;
	}
	
	public void setVsetkyKategorie(List<Category> vsetkyKategorie) {
		this.vsetkyKategorie = vsetkyKategorie;
	}
}
