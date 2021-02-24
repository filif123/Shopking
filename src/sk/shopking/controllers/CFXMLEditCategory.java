package sk.shopking.controllers;

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
import sk.shopking.Category;
import sk.shopking.tools.FXMLTools;

/**
 * Táto trieda je kontrolér pre okno úpravy kategórie tovaru.
 * @author Filip
 *
 */
public class CFXMLEditCategory implements Initializable{
	
	@FXML private TextField jtName;
	@FXML private CheckBox jcPristupne;
	@FXML private CheckBox jcPovoleneStravneListky;
	
	private Category povodnaCategory;
	private String categoryName;
	private boolean pristupneMladistvym;
	private boolean povoleneStravneListky;
	
	private List<Category> vsetkyKategorie;

	@Override
    public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) {
		categoryName = jtName.getText();
		pristupneMladistvym = jcPristupne.isSelected();
		povoleneStravneListky = jcPovoleneStravneListky.isSelected();
		
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
		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	public Category getEditedCategory() {

		Category upravenyTovar;
		try {
			upravenyTovar = new Category(this.povodnaCategory.getCategoryID(),categoryName, pristupneMladistvym,povoleneStravneListky);
		}catch (Exception e) {
			upravenyTovar = null;
		}
		return upravenyTovar;
	}
	
	public void setAktualnaKategoria(Category category) {
		this.povodnaCategory = category;
		this.categoryName = category.getCategoryName();
		this.pristupneMladistvym = category.getCategoryPristupnePreMladistvych();
		this.povoleneStravneListky = category.getCategoryPovoleneStravneListky();
		setVlastnosti();
	}
	
	public void setVsetkyKategorie(List<Category> vsetkyKategorie) {
		this.vsetkyKategorie = vsetkyKategorie;
	}
	
	private void setVlastnosti() {
		jtName.setText(categoryName);
		jcPristupne.setSelected(pristupneMladistvym);
		jcPovoleneStravneListky.setSelected(povoleneStravneListky);
	}
}
