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
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.Database;

/**
 * Táto trieda je kontrolér pre okno zoznamu kategórií tovaru.
 * @author Filip
 *
 */
public class CFXMLCategory implements Initializable{
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	private List<Category> categoriesFromDB = Database.getCategories();
	private List<Category> newCategoriesToExport = new ArrayList<Category>();
	private List<Category> updatedCategoriesToExport = new ArrayList<Category>();
	private List<Category> deletedCategoriesToExport = new ArrayList<Category>();
	
	@FXML
	private TableView<Category> jtCategories;
	
	@FXML
	private TableColumn<Category, String> cName;
	
	@FXML
	private TableColumn<Category, Boolean> cPreMladistvych;
	
	private void fillTable() {
		jtCategories.getItems().addAll(categoriesFromDB);
	}
	
	private void setCellValue() {
		cName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
		cPreMladistvych.setCellValueFactory(new PropertyValueFactory<Category, Boolean>("preMladistvych"));
		cPreMladistvych.setCellFactory(tc -> new CheckBoxTableCell<>());
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		setCellValue();
		fillTable();
		homeMenuController.colorButton("jbCategory");
		jtCategories.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		jtCategories.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	try {
		            		editJednuKategoriu();
						} catch (IOException e) {
							e.printStackTrace();
						}
		            }
		        }
		    }
		});
    }
	
	@FXML
	private void jbSaveAction(ActionEvent event) throws IOException {
		if (newCategoriesToExport != null && !newCategoriesToExport.isEmpty()) {
			Database.addCategories(newCategoriesToExport);
		}
		if (updatedCategoriesToExport != null && !updatedCategoriesToExport.isEmpty()) {
			Database.updateCategories(updatedCategoriesToExport);
		}
		if (deletedCategoriesToExport != null && !deletedCategoriesToExport.isEmpty()) {
			Database.deleteCategories(deletedCategoriesToExport);
		}
	}
	
	@FXML
	private void jbObnovitAction(ActionEvent event) throws IOException {
		categoriesFromDB = Database.getCategories();
		jtCategories.getItems().setAll(categoriesFromDB);
	}
	
	@FXML
	private void jbAddCategoryAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/addCategory.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Pridať kategóriu tovaru");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    CFXMLAddCategory addCategoryController = fxmlLoader.getController();
	    addCategoryController.setVsetkyKategorie(jtCategories.getItems());
	    stage.showAndWait();
	    Category newCategory = addCategoryController.getNewCategory();
	    if (newCategory != null) {
	    	newCategoriesToExport.add(newCategory);
	    	jtCategories.getItems().add(newCategory);
	    	jtCategories.getSelectionModel().clearSelection();
	    	jtCategories.getSelectionModel().selectLast();
		}
	}
	
	@FXML
	private void jbDeleteCategoryAction(ActionEvent event) throws IOException {
		int index = jtCategories.getSelectionModel().getSelectedIndex();
		if(index != -1) {
			deletedCategoriesToExport.add(jtCategories.getItems().get(index));
			jtCategories.getItems().remove(index);
		}
	}
	
	@FXML
	private void jbEditCategoryAction(ActionEvent event) throws IOException {
		editViacKategorii();
	}
	
	@FXML
	private void jbSearchAction(ActionEvent event) throws IOException {
		
	}
	
	private void editJednuKategoriu() throws IOException{
		List<Category> vsetkyKategorie = jtCategories.getItems();
		Integer indexUpravovanejKategorie = jtCategories.getSelectionModel().getSelectedIndex();
		
		if (indexUpravovanejKategorie == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiaden vybraný tovar");
        	alert.setHeaderText("Nebol vybratý žiaden tovar/tovary");
        	alert.setContentText("Vyberte min. 1 tovar!");
        	alert.show();
        	return;
		}

		Category upravovanaKategoria = vsetkyKategorie.get(indexUpravovanejKategorie);
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/editCategory.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Upraviť tovar");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);

	    CFXMLEditCategory editCategoryController = fxmlLoader.getController();
	    editCategoryController.setVsetkyKategorie(vsetkyKategorie);
	    editCategoryController.setAktualnaKategoria(upravovanaKategoria);
	    
	    stage.showAndWait();
	    Category editedCategory = editCategoryController.getEditedCategory();
	    if (editedCategory != null) {
	    	updatedCategoriesToExport.add(editedCategory);
	    	jtCategories.getItems().remove((int)indexUpravovanejKategorie);
	    	jtCategories.getItems().add(indexUpravovanejKategorie, editedCategory);
	    	jtCategories.getSelectionModel().clearSelection();
	    	jtCategories.getSelectionModel().select(indexUpravovanejKategorie);
		}	
		
	}
	
	private void editViacKategorii() throws IOException{
		
		List<Category> allCategories = jtCategories.getItems();
		List<Integer> indexyUpravovanychKategorii = jtCategories.getSelectionModel().getSelectedIndices();
		ObservableList<Category> upraveneKategorie = FXCollections.observableArrayList();
		upraveneKategorie.addAll(allCategories);
		
		if (indexyUpravovanychKategorii.size() == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiaden vybraná kategória tovaru");
        	alert.setHeaderText("Nebola vybratá žiadna kategória tovaru");
        	alert.setContentText("Vyberte min. 1 kategóriu tovaru!");
        	alert.show();
        	return;
		}
		
		for (int i = 0 ; i < indexyUpravovanychKategorii.size() ; i++) {
			
			Integer indexUpravovanejKategorie = indexyUpravovanychKategorii.get(i);
			Category upravovanaKategoria = jtCategories.getItems().get(indexUpravovanejKategorie);
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/editCategory.fxml"));
		    Parent root = (Parent) fxmlLoader.load();
		    Scene scene = new Scene(root);
		    new JMetro(scene,Style.LIGHT);
		    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		    stage.setScene(scene);
		    stage.setTitle("Upraviť používateľa");
		    stage.setResizable(false);
		    stage.initModality(Modality.APPLICATION_MODAL);

		    CFXMLEditCategory editCategoryController = fxmlLoader.getController();
		    editCategoryController.setVsetkyKategorie(upraveneKategorie);
		    editCategoryController.setAktualnaKategoria(upravovanaKategoria);		    
		    stage.showAndWait();
		    
		    Category editedCategory =  editCategoryController.getEditedCategory();
		    if (editedCategory != null) {
		    	updatedCategoriesToExport.add(editedCategory);
		    	upraveneKategorie.set(indexUpravovanejKategorie, editedCategory);
			}
		}

		jtCategories.setItems(upraveneKategorie);
		for (Integer index : indexyUpravovanychKategorii) {
			jtCategories.getSelectionModel().select(index);
		}
	}
	
}
