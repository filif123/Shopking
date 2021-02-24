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
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.Database;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;

/**
 * Táto trieda je kontrolér pre okno zoznamu všetkých výrobkov ponúkaných obchodom.
 * @author Filip
 *
 */
public class CFXMLSklad implements Initializable{
	
	private List<Tovar> skladFromDB = Database.getTovary();
	private List<Tovar> newTovaryToExport = new ArrayList<Tovar>();
	private List<Tovar> updatedTovaryToExport = new ArrayList<Tovar>();
	private List<Tovar> deletedTovaryToExport = new ArrayList<Tovar>();
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	@FXML
	private Button jbStorno;
	
	@FXML
	private Button jbSaveExit;
	
	@FXML
	private Button jbUkoncit;
	
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
	
	public void selectAtIndex(int index) {
		jtSklad.requestFocus();
		jtSklad.getSelectionModel().select(index);
		jtSklad.getFocusModel().focus(index);
	}
	
	private void fillTable() {
		jtSklad.getItems().addAll(skladFromDB);
	}
	
	private void setCellValue() {
		cNazov.setCellValueFactory(new PropertyValueFactory<Tovar, String>("name"));
		cCategory.setCellValueFactory(new PropertyValueFactory<Tovar, String>("category"));
		cJednotka.setCellValueFactory(new PropertyValueFactory<Tovar, String>("jednotka"));
		cPLU.setCellValueFactory(new PropertyValueFactory<Tovar, String>("plu"));
		cEAN.setCellValueFactory(new PropertyValueFactory<Tovar, String>("ean"));
		cCena.setCellValueFactory(new PropertyValueFactory<Tovar, String>("jednotkovaCena"));
		cDPH.setCellValueFactory(new PropertyValueFactory<Tovar, String>("dph"));
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		homeMenuController.colorButton("jbSklad");
		setCellValue();
		fillTable();
		jtSklad.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		jtSklad.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	try {
		            		editJedenTovar();
						} catch (IOException e) {
							e.printStackTrace();
						}
		            }
		        }
		    }
		});
    }
	
	@FXML
	private void jbObnovitAction (ActionEvent event) throws IOException{
		skladFromDB = Database.getTovary();
		jtSklad.getItems().setAll(skladFromDB);
	}
	
	@FXML
	private void jbSaveAction (ActionEvent event) throws IOException{
		if (newTovaryToExport != null && !newTovaryToExport.isEmpty()) {
			Database.addTovary(newTovaryToExport);
		}
		if (updatedTovaryToExport != null && !updatedTovaryToExport.isEmpty()) {
			Database.updateTovary(updatedTovaryToExport);
		}
		if (deletedTovaryToExport != null && !deletedTovaryToExport.isEmpty()) {
			Database.deleteTovary(deletedTovaryToExport);
		}
	}
	
	@FXML
	private void jbSearchAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
	    Scene sceneInfo = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/search.fxml")));
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(sceneInfo);
	    stage.setTitle("Hľadať");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.show();
	    
	    List<Tovar> tovaryAktList = new ArrayList<Tovar>();
		ObservableList<Tovar> tovaryAkt = jtSklad.getItems();
		for(int i = 0;i < tovaryAkt.size();i++) {
			tovaryAktList.add(i,tovaryAkt.get(i));	
		};
	}
	
	@FXML
	private void jbAddTovarAction (ActionEvent event) throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/addTovar.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    CFXMLAddTovar addTovarController = fxmlLoader.getController();
	    
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Pridať tovar");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		public void handle(WindowEvent we) {
    			addTovarController.closePort();
    		}	
    	});
	    
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
	private void jbDeleteTovarAction (ActionEvent event) throws IOException{
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
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    CFXMLAkciaTovar addAkciaTovarController = fxmlLoader.getController();
	    
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Zadanie akcie na tovar");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.show();
	}
	
	
	private void editJedenTovar() throws IOException{
		List<Tovar> vsetkyTovary = jtSklad.getItems();
		Integer indexUpravovanehoTovaru = jtSklad.getSelectionModel().getSelectedIndex();
		
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
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    CFXMLEditTovar editTovarController = fxmlLoader.getController();
	    
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Upraviť tovar");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		public void handle(WindowEvent we) {
    			editTovarController.closePort();
    		}	
    	});

	    
	    editTovarController.setVsetkyTovary(vsetkyTovary);
	    editTovarController.setAktualnyTovar(upravovanyTovar);
	    
	    stage.showAndWait();
	    Tovar editedTovar = editTovarController.returnEditedTovar();
	    if (editedTovar != null) {
	    	updatedTovaryToExport.add(editedTovar);
	    	jtSklad.getItems().remove((int)indexUpravovanehoTovaru);
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
		
		if (indexyUpravovanychTovarov.size() == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiaden vybraný tovar");
        	alert.setHeaderText("Nebol vybratý žiaden tovar/tovary");
        	alert.setContentText("Vyberte aspon 1 tovar!");
        	alert.show();
        	return;
		}
		
		for (int i = 0 ; i < indexyUpravovanychTovarov.size() ; i++) {
			
			Integer indexUpravovanehoTovaru = indexyUpravovanychTovarov.get(i);
			Tovar upravovanyTovar = jtSklad.getItems().get(indexUpravovanehoTovaru);
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/editTovar.fxml"));
		    Parent root = (Parent) fxmlLoader.load();
		    Scene scene = new Scene(root);
		    new JMetro(scene,Style.LIGHT);
		    CFXMLEditTovar cfxmlEditTovarController = fxmlLoader.getController();
		    
		    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		    stage.setScene(scene);
		    stage.setTitle("Upraviť tovar");
		    stage.setResizable(false);
		    stage.initModality(Modality.APPLICATION_MODAL);
		    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    		public void handle(WindowEvent we) {
	    			cfxmlEditTovarController.closePort();
	    		}	
	    	});

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
