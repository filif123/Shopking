package sk.shopking.controllers;

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.Tovar;
import sk.shopking.tools.Database;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;

/**
 * Táto trieda je kontrolér pre okno zoznamu všetkých výrobkov ponúkaných obchodom.
 * @author Filip
 *
 */
public class CFXMLTovary implements Initializable{
	
	private List<Tovar> skladFromDB = Database.getTovary();
	private final List<Tovar> newTovaryToExport = new ArrayList<>();
	private final List<Tovar> updatedTovaryToExport = new ArrayList<>();
	private final List<Tovar> deletedTovaryToExport = new ArrayList<>();
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	@FXML
	private TableView<Tovar> jtSklad;
	
	@FXML
	private TableColumn<Tovar, String> cNazov;
	
	@FXML
	private TableColumn<Tovar, String> cCategory;
	
	@FXML
	private TableColumn<Tovar, String> cJednotka;
	
	@FXML
	private TableColumn<Tovar, String> cPLU;
	
	@FXML
	private TableColumn<Tovar, String> cEAN;
	
	@FXML
	private TableColumn<Tovar, String> cCena;
	
	@FXML
	private TableColumn<Tovar, String> cDPH;
	
	@FXML
	private TableColumn<Tovar, String> cAkcia;

	private void fillTable() {
		jtSklad.getItems().addAll(skladFromDB);
	}
	
	private void setCellValue() {
		cNazov.setCellValueFactory(new PropertyValueFactory<>("name"));
		cCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
		cJednotka.setCellValueFactory(new PropertyValueFactory<>("jednotka"));
		cPLU.setCellValueFactory(new PropertyValueFactory<>("plu"));
		cEAN.setCellValueFactory(new PropertyValueFactory<>("ean"));
		cCena.setCellValueFactory(new PropertyValueFactory<>("jednotkovaCena"));
		cDPH.setCellValueFactory(new PropertyValueFactory<>("dph"));
		cAkcia.setCellValueFactory(new PropertyValueFactory<>("akcia"));
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		homeMenuController.colorButton("jbSklad");
		setCellValue();
		fillTable();
		jtSklad.setPlaceholder(new Label("Žiaden tovar na zobrazenie"));
		jtSklad.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		jtSklad.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
				if(mouseEvent.getClickCount() == 2){
					try {
						editJedenTovar();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
    }
	
	@FXML
	private void jbObnovitAction (ActionEvent event) {
		skladFromDB = Database.getTovary();
		jtSklad.getItems().setAll(skladFromDB);
	}
	
	@FXML
	private void jbSaveAction (ActionEvent event) {
		if (!newTovaryToExport.isEmpty()) {
			Database.addTovary(newTovaryToExport);
			newTovaryToExport.clear();
		}
		if (!updatedTovaryToExport.isEmpty()) {
			Database.updateTovary(updatedTovaryToExport);
			updatedTovaryToExport.clear();
		}
		if (!deletedTovaryToExport.isEmpty()) {
			Database.deleteTovary(deletedTovaryToExport);
			deletedTovaryToExport.clear();
		}
	}
	
	@FXML
	private void jbSearchAction(ActionEvent event) throws IOException {
		jtSklad.getSelectionModel().clearSelection();
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/searchTovar.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    CFXMLSearchInTovar searchTovarController = fxmlLoader.getController();
	    searchTovarController.setList(jtSklad.getItems());
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Hľadať");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.showAndWait();
	    List<Integer> indexy = searchTovarController.returnIndexy();
	    
	    if (!indexy.isEmpty()) {
			for (Integer integer : indexy) {
				jtSklad.getSelectionModel().select(integer);
			}
		}
	}
	
	@FXML
	private void jbAddTovarAction (ActionEvent event) throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/addTovar.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    CFXMLAddTovar addTovarController = fxmlLoader.getController();
	    
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Pridať tovar");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setOnCloseRequest(we -> addTovarController.closePort());
	    
	    addTovarController.setVsetkyTovary(jtSklad.getItems());
	    stage.showAndWait();
	    Tovar newTovar = addTovarController.getNewTovar();
	    if (newTovar != null) {
	    	newTovaryToExport.add(newTovar);
	    	jtSklad.getItems().add(newTovar);
	    	jtSklad.getSelectionModel().clearSelection();
	    	jtSklad.getSelectionModel().selectLast();
		}
	}
	
	@FXML
	private void jbDeleteTovarAction (ActionEvent event) {
		List<Tovar> vybrateTovary = jtSklad.getSelectionModel().getSelectedItems();
		for (Tovar tovar : vybrateTovary) {
			if (!newTovaryToExport.contains(tovar)) {
				deletedTovaryToExport.add(tovar);
			}
		}
		jtSklad.getItems().removeAll(vybrateTovary);
	}
	
	@FXML
	private void jbEditTovarAction (ActionEvent event) throws IOException{
		editViacTovarov();
	}
	
	@FXML
	private void jbAkciaTovarAction (ActionEvent event) throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/akciaTovar.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    CFXMLAkciaTovar addAkciaTovarController = fxmlLoader.getController();
	    
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Zadanie akcie na tovar");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    if (!jtSklad.getSelectionModel().isEmpty()) {
			Tovar tovarToEdit = jtSklad.getSelectionModel().getSelectedItem();
			addAkciaTovarController.setTovarToEdit(tovarToEdit);	
		}
	    else {
			return;
		}
	    stage.showAndWait();
	    if (addAkciaTovarController.isTovarEdited()) {
			updatedTovaryToExport.add(addAkciaTovarController.getEditedTovar());
			jtSklad.getItems().set(jtSklad.getSelectionModel().getSelectedIndex(), addAkciaTovarController.getEditedTovar());
			
		}
	}
	
	
	private void editJedenTovar() throws IOException{
		List<Tovar> vsetkyTovary = jtSklad.getItems();
		int indexUpravovanehoTovaru = jtSklad.getSelectionModel().getSelectedIndex();
		
		if (indexUpravovanehoTovaru == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiaden vybraný tovar");
        	alert.setHeaderText("Nebol vybratý žiaden tovar/tovary");
        	alert.setContentText("Vyberte aspon 1 tovar!");
        	alert.show();
        	return;
		}

		Tovar upravovanyTovar = vsetkyTovary.get(indexUpravovanehoTovaru);
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/editTovar.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    CFXMLEditTovar editTovarController = fxmlLoader.getController();
	    
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Upraviť tovar");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setOnCloseRequest(we -> editTovarController.closePort());

	    
	    editTovarController.setVsetkyTovary(vsetkyTovary);
	    editTovarController.setAktualnyTovar(upravovanyTovar);
	    
	    stage.showAndWait();
	    Tovar editedTovar = editTovarController.returnEditedTovar();
	    if (editedTovar != null) {
	    	updatedTovaryToExport.add(editedTovar);
	    	jtSklad.getItems().remove(indexUpravovanehoTovaru);
	    	jtSklad.getItems().add(indexUpravovanehoTovaru, editedTovar);
	    	jtSklad.getSelectionModel().clearSelection();
	    	jtSklad.getSelectionModel().select(indexUpravovanehoTovaru);
		}	
		
	}
	
	private void editViacTovarov() throws IOException{
		List<Tovar> vsetkyTovary = jtSklad.getItems();
		List<Integer> indexyUpravovanychTovarov = jtSklad.getSelectionModel().getSelectedIndices();
		ObservableList<Tovar> upraveneTovary = FXCollections.observableArrayList();
		upraveneTovary.addAll(vsetkyTovary);
		
		if (indexyUpravovanychTovarov.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiaden vybraný tovar");
        	alert.setHeaderText("Nebol vybratý žiaden tovar/tovary");
        	alert.setContentText("Vyberte aspon 1 tovar!");
        	alert.show();
        	return;
		}

		for (Integer indexUpravovanehoTovaru : indexyUpravovanychTovarov) {

			Tovar upravovanyTovar = jtSklad.getItems().get(indexUpravovanehoTovaru);
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/editTovar.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			new JMetro(scene, Style.LIGHT);
			CFXMLEditTovar cfxmlEditTovarController = fxmlLoader.getController();

			stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			stage.setScene(scene);
			stage.setTitle("Upraviť tovar");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setOnCloseRequest(we -> cfxmlEditTovarController.closePort());

			cfxmlEditTovarController.setVsetkyTovary(upraveneTovary);
			cfxmlEditTovarController.setAktualnyTovar(upravovanyTovar);
			stage.showAndWait();

			Tovar editedTovar = cfxmlEditTovarController.returnEditedTovar();
			if (editedTovar != null) {
				updatedTovaryToExport.add(editedTovar);
				upraveneTovary.set(indexUpravovanehoTovaru, editedTovar);
			}
		}

		jtSklad.setItems(upraveneTovary);
		for (Integer index : indexyUpravovanychTovarov) {
			jtSklad.getSelectionModel().select(index);
		}
	}
}
