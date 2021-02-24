package sk.shopking.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.Category;
import sk.shopking.DPHType;
import sk.shopking.JednotkaType;
import sk.shopking.Tovar;
import sk.shopking.tools.*;

/**
 * Táto trieda je kontrolér pre okno úpravy tovarov.
 * @author Filip
 *
 */
public class CFXMLEditTovar implements Initializable{
	
	private final List<Category> categoriesFromDB = Database.getCategories();
	private final ObservableList<Category> kategorie = FXCollections.observableArrayList();
	
	@FXML
	private TextField jtName;
	
	@FXML
	private TextField jtPLU;
	
	@FXML
	private TextField jtEAN;
	
	@FXML
	private TextField jtCena;

	@FXML
	private ChoiceBox<Category> jcbCategory;
	
	@FXML
	private ChoiceBox<JednotkaType> jcbJednotka;
	
	@FXML
	private ChoiceBox<DPHType> jcbDPH;
	
	private Tovar povodnyTovar;
	private Tovar editedTovar;
	
	private String tovarName,tovarCena,tovarEAN,tovarPLU;
	private DPHType tovarDPH;
	private JednotkaType tovarJednotka;
	private Category tovarCategory;
	
	private List<Tovar> vsetkyTovary;
	
	private BarcodeScanner bScanner;
	
	public void closePort() {
		bScanner.closePort();
	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) {
		tovarName = jtName.getText();
		tovarEAN = jtEAN.getText();
		tovarCena = jtCena.getText();
		if (tovarCena.contains(",")) {
			tovarCena = tovarCena.replace(',', '.');
		}
		
		if(tovarName == null || tovarName.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadny názov tovaru");
        	alert.setContentText("Zadajte názov tovaru!");
        	alert.show();
		}
		else if(tovarEAN == null || tovarEAN.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Nebol zadaný žiadny EAN tovaru");
	        alert.setContentText("Zadajte EAN tovaru!");
	        alert.show();	
		}
		else if(!Utils.isNumber(tovarEAN)) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Kód tovaru neobsahuje len čísla");
	        alert.setContentText("Skontrolujte kód tovaru!");
	        alert.show();	
		}
		else if(tovarCena == null || tovarCena.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadna cena tovaru");
        	alert.setContentText("Zadajte cenu tovaru!");
        	alert.show();
		}
		else if(!Utils.isNumber(tovarCena)) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Zadaná cena tovaru neobsahuje len čísla");
	        alert.setContentText("Skontrolujte cenu tovaru!");
	        alert.show();	
		}
		else if(jcbCategory.getSelectionModel().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola vybratá žiadna kategória tovaru");
        	alert.setContentText("Vyberte kategóriu tovaru!");
        	alert.show();
		}
		else if(jcbJednotka.getSelectionModel().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola vybratá žiadna jednotka tovaru");
        	alert.setContentText("Vyberte jednotku tovaru!");
        	alert.show();
		}
		
		else {
			tovarCategory = jcbCategory.getValue();
			tovarJednotka = jcbJednotka.getValue();
			tovarDPH = jcbDPH.getValue();
			
			Tovar testUpravenehoTovaru = new Tovar(povodnyTovar.getTovarPLU(),tovarName,tovarCategory,tovarJednotka,Long.parseLong(tovarEAN),Float.parseFloat(tovarCena),tovarDPH);
			for (Tovar tovar : vsetkyTovary) {
				if ((tovarName.equals(tovar.getTovarName()) || Long.parseLong(tovarEAN) == tovar.getTovarEAN()) && !povodnyTovar.equals(tovar)) {
					Alert alert = new Alert(AlertType.ERROR);
					new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        	alert.setTitle("Duplicitný tovar");
		        	alert.setHeaderText("Zadaný tovar sa už v zozname nachádza");
		        	alert.setContentText("Opravte zadaný tovar!");
		        	alert.show();
		        	return;
				}
			}
			editedTovar = testUpravenehoTovaru;
			bScanner.closePort();
			FXMLTools.closeWindow(event);
		}

	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		if (bScanner != null) {
			bScanner.removeListeners();
			bScanner.closePort();
		}
		FXMLTools.closeWindow(event);
	}
	
	/*private void changeCenaBezDPHValue() {
		if(!jtCena.getText().isEmpty() && !jcDPH.getValue().isEmpty()) {
			float cena = Float.parseFloat(jtCena.getText());
			int dph = Integer.parseInt(jcDPH.getValue());
			float dphnum = (float)dph/100;
			float cenaBezDPH = cena / (1 + dphnum);
			jtCenaBezDPH.setText("" + String.format("%.2f", cenaBezDPH));
		}	
	}*/
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		bScanner = new BarcodeScanner(AppSettings.loadScannerPort());
		bScanner.initPort();
		bScanner.addListener(barcode -> Platform.runLater(() -> jtEAN.setText(barcode)));
		fillCategoryBox();
		
		jcbJednotka.getItems().setAll(JednotkaType.values());
		jcbDPH.getItems().setAll(DPHType.values());
    }
	
	private void fillCategoryBox() {
		
		kategorie.addAll(categoriesFromDB);
		jcbCategory.setItems(kategorie);
	}
	
	public Tovar returnEditedTovar() {
		
		return editedTovar;
	}
	
	public void setAktualnyTovar(Tovar tovar) {
		povodnyTovar = tovar;
		
		this.tovarName = tovar.getTovarName();
		this.tovarCategory = tovar.getTovarCategory();
		this.tovarJednotka = tovar.getTovarJednotka();
		this.tovarEAN = "" + tovar.getTovarEAN();
		this.tovarCena = "" + tovar.getTovarJednotkovaCena();
		this.tovarDPH = tovar.getTovarDPH();
		this.tovarPLU = "" + tovar.getTovarPLU();
		setVlastnosti();
	}
	
	public void setVsetkyTovary(List<Tovar> vsetkyTovary) {
		this.vsetkyTovary = vsetkyTovary;
	}
	
	private void setVlastnosti() {
		jtName.setText(tovarName);
		jtCena.setText(new DecimalFormat("#.##").format(Float.parseFloat(tovarCena)));
		jtEAN.setText(tovarEAN);
		jtPLU.setText(tovarPLU);
		jcbJednotka.setValue(tovarJednotka);
		jcbDPH.setValue(tovarDPH);
		for (Category category : kategorie) {
			if (category.getCategoryName().equals(tovarCategory.getCategoryName())) {
				jcbCategory.setValue(category);
				break;
			}
		}		
	}
}	
