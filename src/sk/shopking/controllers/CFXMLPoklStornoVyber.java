package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.*;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.Utils;

/**
 * @author Filip
 *
 */
public class CFXMLPoklStornoVyber implements Initializable{

	@FXML private TableView<NakupenyTovar> jtPolozkyNakupu;
	
	@FXML private TableColumn<NakupenyTovar,String> cNazovVyrobku;
	@FXML private TableColumn<NakupenyTovar,String> cPLU;
	@FXML private TableColumn<NakupenyTovar,String> cEAN;
	@FXML private TableColumn<NakupenyTovar,String> cCena;
	@FXML private TableColumn<NakupenyTovar,String> cJednotkovaCena;
	@FXML private TableColumn<NakupenyTovar,String> cMnozstvo;
	@FXML private TableColumn<NakupenyTovar,String> cJednotka;
	@FXML private TableColumn<NakupenyTovar,String> cStornoMnozstvo;
	
	private Doklad doklad;
	private UserPokladnik pokladnik;
	private final List<NakupenyTovar> polozkyNaStornovanie = new ArrayList<>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setCellValueTable();
		
	}
	
	private void setCellValueTable() {
		cNazovVyrobku.setCellValueFactory(new PropertyValueFactory<>("name"));
		cPLU.setCellValueFactory(new PropertyValueFactory<>("plu"));
		cEAN.setCellValueFactory(new PropertyValueFactory<>("ean"));
		cCena.setCellValueFactory(new PropertyValueFactory<>("cena"));
		cJednotkovaCena.setCellValueFactory(new PropertyValueFactory<>("jednotkovaCena"));
		cMnozstvo.setCellValueFactory(new PropertyValueFactory<>("mnozstvo"));
		cJednotka.setCellValueFactory(new PropertyValueFactory<>("jednotka"));
		cStornoMnozstvo.setCellValueFactory(new PropertyValueFactory<>("storno"));
		cStornoMnozstvo.setCellFactory(TextFieldTableCell.forTableColumn());
		cStornoMnozstvo.setOnEditCommit((TableColumn.CellEditEvent<NakupenyTovar, String> evt) -> {
			if (Utils.isNumber(evt.getNewValue())) {
				if (Integer.parseInt(evt.getNewValue()) < jtPolozkyNakupu.getSelectionModel().getSelectedItem().getNakupeneMnozstvo()) {
					if (jtPolozkyNakupu.getSelectionModel().getSelectedItem().getTovarJednotka().equals(JednotkaType.KS)) {
						NakupenyTovar editovanyNakupenyTovar = jtPolozkyNakupu.getSelectionModel().getSelectedItem();
						editovanyNakupenyTovar.setStornoProperty(evt.getNewValue());
						editovanyNakupenyTovar.setNoveMnozstvoPoStorne(Integer.parseInt(evt.getNewValue()));
						polozkyNaStornovanie.add(editovanyNakupenyTovar);
						if (Integer.parseInt(evt.getNewValue())==0) {
							editovanyNakupenyTovar.setStornoProperty("celý");
						}
						jtPolozkyNakupu.refresh();
					}
					else {
						NakupenyTovar editovanyNakupenyTovar = jtPolozkyNakupu.getSelectionModel().getSelectedItem();
						editovanyNakupenyTovar.setStornoProperty("celý");
						polozkyNaStornovanie.add(editovanyNakupenyTovar);
						jtPolozkyNakupu.refresh();
					}
					
				}
				else {
					evt.consume();
					jtPolozkyNakupu.refresh();
				}
				
			}
			else {
				evt.consume();
				jtPolozkyNakupu.refresh();
			}
			
        });
		jtPolozkyNakupu.setOnKeyReleased(keyEvent -> {
			if(keyEvent.getCode().equals(KeyCode.SPACE)){
				int row = jtPolozkyNakupu.getSelectionModel().getSelectedIndex();
				jtPolozkyNakupu.edit(row, cStornoMnozstvo);
			}
		});
	}
	
	public void setInfoNaStorno(Doklad doklad,UserPokladnik pokladnik) {
		this.doklad = doklad;
		this.pokladnik = pokladnik;
		jtPolozkyNakupu.getItems().addAll(Database.getNakupenyTovarPlusDeletedTovar(doklad.getIdDoklad()));
	}
	
	@FXML
	private void jbDeleteAction(ActionEvent event) {
		prepareToStorno();
	
	}
	
	public void prepareToStorno() {
		if (!polozkyNaStornovanie.isEmpty()) {
			Collections.reverse(polozkyNaStornovanie);
			List<NakupenyTovar> polozkyBezDuplicit = Utils.removeDuplicates(polozkyNaStornovanie);
			
			try {
				Stage stage = new Stage();
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklStornoSumarum.fxml"));
				Parent root = fxmlLoader.load();
				Scene scene = new Scene(root);
				CFXMLPoklStornoSumarum stornoSumarumController = fxmlLoader.getController();
				new JMetro(scene,Style.LIGHT);
				stage.setScene(scene);
				stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
				stage.setTitle("Stornovanie položiek nákupu");
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stornoSumarumController.setInfoNaStorno(doklad, polozkyBezDuplicit,pokladnik);
				stage.showAndWait();
				if (stornoSumarumController.stornoVykonane()) {
			        Stage thisStage = (Stage) jtPolozkyNakupu.getScene().getWindow();
			        thisStage.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiadna zmena");
        	alert.setHeaderText("Nevykonali ste žiadnu zmenu");
        	alert.setContentText("Nič sa neuložilo!");
        	alert.show();
		}
		
	}
	
	@FXML
	private void jbZrusitAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
}
