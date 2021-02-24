package sk.shopking.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.JednotkaType;
import sk.shopking.Tovar;
import sk.shopking.TovarZlavaCena;
import sk.shopking.TovarZlavaMnozstvo;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.Utils;

/**
 * Táto trieda je kontrolér pre okno riadenia akcií výrobkov.
 * @author Filip
 *
 */
public class CFXMLAkciaTovar implements Initializable{
	
	@FXML private TitledPane tpAkciaCena, tpAkciaMnozstvo;
	@FXML private RadioButton rbAkciaCena, rbAkciaMnozstvo;
	@FXML private TextField jtfPovodnaCena,jtfNovaCena,jtfZlavaPercenta,jtfRozdiel;
	@FXML private TextField jtfPovodneMnozstvo,jtfNoveMnozstvo,jtfPlatiOdMnozstva;
	
	private float povodnaCena,novaCena,zlavaPercenta,rozdiel;
	private int povodneMnozstvo,noveMnozstvo,minimalneMnozstvo;
	
	private Tovar tovarToEdit;
	
	private final DecimalFormat df = new DecimalFormat("0.00");
	
	
	private boolean zmenaTovaruVykonana;
	
	private final ToggleGroup buttonGroup = new ToggleGroup();
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		StringProperty novaCenaP = new SimpleStringProperty(jtfNovaCena.getText());
		StringProperty rozdielP = new SimpleStringProperty(jtfRozdiel.getText());
		StringProperty percentaZlavaP = new SimpleStringProperty(jtfZlavaPercenta.getText());

		DecimalFormatSymbols decimal = DecimalFormatSymbols.getInstance();
		decimal.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(decimal);
		
		rbAkciaCena.setToggleGroup(buttonGroup);
		rbAkciaCena.setOnAction(event -> {
			tpAkciaCena.setDisable(false);
			tpAkciaMnozstvo.setDisable(true);
		});
		rbAkciaMnozstvo.setToggleGroup(buttonGroup);
		rbAkciaMnozstvo.setOnAction(event -> {
			tpAkciaCena.setDisable(true);
			tpAkciaMnozstvo.setDisable(false);
		});

		novaCenaP.addListener((obs, oldValue, newValue) -> jtfNovaCena.setText(newValue));

		rozdielP.addListener((obs, oldValue, newValue) -> jtfRozdiel.setText(newValue));

		percentaZlavaP.addListener((obs, oldValue, newValue) -> jtfZlavaPercenta.setText(newValue));

		jtfNovaCena.textProperty().addListener((observable, oldValue, newValue) -> {
			if (! newValue.equals(novaCenaP.get())){
				try {
					novaCena = Float.parseFloat(newValue);
					rozdiel = povodnaCena - novaCena;
					zlavaPercenta = (1 - (novaCena/povodnaCena))*100;

					rozdielP.set(df.format(rozdiel));
					percentaZlavaP.set(df.format(zlavaPercenta));
				}
				catch (NumberFormatException ignored){
				}
				novaCenaP.set(newValue);
			}
		});

		jtfRozdiel.textProperty().addListener((observable, oldValue, newValue) -> {
			if (! newValue.equals(rozdielP.get())){
				try {
					rozdiel = Float.parseFloat(newValue);
					novaCena = povodnaCena - rozdiel;
					zlavaPercenta = (1 - (novaCena/povodnaCena))*100;

					novaCenaP.set(df.format(novaCena));
					percentaZlavaP.set(df.format(zlavaPercenta));
				}
				catch (NumberFormatException ignored){
				}
				rozdielP.set(newValue);
			}
		});

		jtfZlavaPercenta.textProperty().addListener((observable, oldValue, newValue) -> {
			if (! newValue.equals(percentaZlavaP.get())){
				try {
					zlavaPercenta = Float.parseFloat(newValue);
					novaCena = (1 - zlavaPercenta/100) * povodnaCena;
					rozdiel = povodnaCena - novaCena;

					novaCenaP.set(df.format(novaCena));
					rozdielP.set(df.format(rozdiel));
				}
				catch (NumberFormatException ignored){
				}
				percentaZlavaP.set(newValue);
			}
		});
    }
	
	@FXML
	private void jbSaveAction(ActionEvent event) {
		if (rbAkciaCena.isSelected()) {
			if (jtfNovaCena.getText().isEmpty() || jtfZlavaPercenta.getText().isEmpty() || jtfRozdiel.getText().isEmpty() || !Utils.isNumber(jtfNovaCena.getText()) || !Utils.isNumber(jtfZlavaPercenta.getText()) || !Utils.isNumber(jtfRozdiel.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        alert.setTitle("Zlé parametre");
		        alert.setHeaderText("Neboli zadané všetky požadované údaje");
		        alert.setContentText("Skontrolujte zadané údaje!");
		        alert.show();
		        return;	
			}
			else {
				novaCena = Float.parseFloat(jtfNovaCena.getText());
				zlavaPercenta = Float.parseFloat(jtfZlavaPercenta.getText());
				rozdiel = Float.parseFloat(jtfRozdiel.getText());
				tovarToEdit = new TovarZlavaCena(tovarToEdit.getTovarPLU(), tovarToEdit.getTovarName(), tovarToEdit.getTovarCategory(), tovarToEdit.getTovarJednotka(), tovarToEdit.getTovarEAN(), tovarToEdit.getTovarJednotkovaCena(), tovarToEdit.getTovarDPH(), novaCena);
			}
			
		}
		else if (rbAkciaMnozstvo.isSelected()) {
			if (jtfPovodneMnozstvo.getText().isEmpty() || jtfNoveMnozstvo.getText().isEmpty() || jtfPlatiOdMnozstva.getText().isEmpty() || !Utils.isNumber(jtfPovodneMnozstvo.getText()) || !Utils.isNumber(jtfNoveMnozstvo.getText()) || !Utils.isNumber(jtfPlatiOdMnozstva.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        alert.setTitle("Zlé parametre");
		        alert.setHeaderText("Neboli zadané všetky požadované údaje");
		        alert.setContentText("Skontrolujte zadané údaje!");
		        alert.show();
		        return;
			}
			else {
				povodneMnozstvo = Integer.parseInt(jtfPovodneMnozstvo.getText());
				noveMnozstvo = Integer.parseInt(jtfNoveMnozstvo.getText());
				minimalneMnozstvo = Integer.parseInt(jtfPlatiOdMnozstva.getText());
				tovarToEdit = new TovarZlavaMnozstvo(tovarToEdit.getTovarPLU(), tovarToEdit.getTovarName(), tovarToEdit.getTovarCategory(), tovarToEdit.getTovarJednotka(), tovarToEdit.getTovarEAN(), tovarToEdit.getTovarJednotkovaCena(), tovarToEdit.getTovarDPH(), povodneMnozstvo,noveMnozstvo,minimalneMnozstvo);
			}
			
		}
		this.zmenaTovaruVykonana = true;
		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbDeleteAction(ActionEvent event) {
		this.zmenaTovaruVykonana = true;
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Odstránenenie akcie na tovar");
		alert.setHeaderText("Odstránenie akcie");
		alert.setContentText("Naozaj chcete odstrániť akciu na tento tovar ?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
			tovarToEdit = new Tovar(tovarToEdit.getTovarPLU(), tovarToEdit.getTovarName(), tovarToEdit.getTovarCategory(), tovarToEdit.getTovarJednotka(), tovarToEdit.getTovarEAN(), tovarToEdit.getTovarJednotkovaCena(), tovarToEdit.getTovarDPH());
			FXMLTools.closeWindow(event);
		}
		else {
			alert.close();
			event.consume();
		}
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		this.zmenaTovaruVykonana = false;
		FXMLTools.closeWindow(event);
	}
	
	public void setTovarToEdit(Tovar tovar) {
		this.tovarToEdit = tovar;
		jtfPovodnaCena.setText(df.format(tovarToEdit.getTovarJednotkovaCena()));
		povodnaCena = tovarToEdit.getTovarJednotkovaCena();

		if (tovarToEdit.getTovarJednotka().equals(JednotkaType.KS)) {
			if (tovarToEdit instanceof TovarZlavaCena) {
				tpAkciaCena.setDisable(false);
				tpAkciaMnozstvo.setDisable(true);
				
				TovarZlavaCena thisTovar = (TovarZlavaCena)tovarToEdit;
				novaCena = thisTovar.getNovaCena();
				zlavaPercenta = thisTovar.getZlavaVPercentach();
				rozdiel = thisTovar.getRozdiel();
				
				jtfNovaCena.setText(df.format(novaCena));
				jtfZlavaPercenta.setText(df.format(zlavaPercenta));
				jtfRozdiel.setText(df.format(rozdiel));
			}
			else if (tovarToEdit instanceof TovarZlavaMnozstvo) {
				tpAkciaCena.setDisable(true);
				tpAkciaMnozstvo.setDisable(false);
				rbAkciaMnozstvo.setSelected(true);
				
				TovarZlavaMnozstvo thisTovar = (TovarZlavaMnozstvo)tovarToEdit;
				povodneMnozstvo = thisTovar.getPovodneMnozstvo();
				noveMnozstvo = thisTovar.getNoveMnozstvo();
				minimalneMnozstvo = thisTovar.getMinimalneMnozstvo();
				jtfPovodneMnozstvo.setText("" + povodneMnozstvo);
				jtfNoveMnozstvo.setText("" + noveMnozstvo);
				jtfPlatiOdMnozstva.setText("" + minimalneMnozstvo);
			}
		}
		else {
			tpAkciaCena.setDisable(false);
			tpAkciaMnozstvo.setDisable(true);
			rbAkciaMnozstvo.setDisable(true);
		}
		
	}
	
	public Tovar getEditedTovar() {
		return this.tovarToEdit;
	}
	
	public boolean isTovarEdited() {
		return this.zmenaTovaruVykonana;
	}
}
