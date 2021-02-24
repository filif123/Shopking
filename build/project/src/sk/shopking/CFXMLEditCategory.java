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
 * Táto trieda je kontrolér pre okno úpravy kategórie tovaru.
 * @author Filip
 *
 */
public class CFXMLEditCategory implements Initializable{
	
	@FXML private TextField jtName;
	@FXML private CheckBox jcPristupne;
	
	private Category povodnaCategory;
	private String categoryName;
	private boolean pristupneMladistvym;
	
	private List<Category> vsetkyKategorie;
	private Category upravenyTovar;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) throws IOException {
		categoryName = jtName.getText();
		pristupneMladistvym = jcPristupne.isSelected();
		
		if(categoryName == null || categoryName.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadny názov tovaru");
        	alert.setContentText("Zadajte názov tovaru!");
        	alert.show();
		}
		else {
			
			for (Category category : vsetkyKategorie) {
				if (categoryName.equals(category.getCategoryName()) && !categoryName.equals(povodnaCategory.getCategoryName())){
					Alert alert = new Alert(AlertType.ERROR);
					new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        	alert.setTitle("Duplicitná kategória");
		        	alert.setHeaderText("Zadaná kategória tovaru sa už v zozname nachádza");
		        	alert.setContentText("Opravte zadanú kategóriu!");
		        	alert.show();
		        	return;
				}
			}
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
	
	public Category getEditedCategory() {
		
		try {			
			upravenyTovar = new Category(this.povodnaCategory.getCategoryID(),categoryName, pristupneMladistvym);
		}catch (Exception e) {
			upravenyTovar = null;
		}
		return upravenyTovar;
	}
	
	public void setAktualnaKategoria(Category category) {
		this.povodnaCategory = category;
		this.categoryName = category.getCategoryName();
		this.pristupneMladistvym = category.getCategoryPristupnePreMladistvych();
		setVlastnosti();
	}
	
	public void setVsetkyKategorie(List<Category> vsetkyKategorie) {
		this.vsetkyKategorie = vsetkyKategorie;
	}
	
	private void setVlastnosti() {
		jtName.setText(categoryName);
		jcPristupne.setSelected(pristupneMladistvym);		
	}
}
