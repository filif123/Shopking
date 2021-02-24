package sk.shopking.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import sk.shopking.Category;
import sk.shopking.Tovar;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * Táto trieda je kontrolér pre okno zoznamu ponukaných obchodom pre pokladníka na výber.
 * @author Filip
 *
 */
public class CFXMLPoklZoznam implements Initializable{

	@FXML private TextField jtfSearch;
	@FXML private ChoiceBox<String> jcbCategory;
	@FXML private TableView<Tovar> jtZoznam;
	@FXML private TableColumn<Tovar, String> cNazov;
	@FXML private TableColumn<Tovar, Integer> cPLU;
	@FXML private TableColumn<Tovar, Long> cEAN;
	@FXML private TableColumn<Tovar, String> cCena;
	@FXML private TableColumn<Tovar, String> cAkcia;
	
	//tovar, ktory je vybraty
	private Tovar vybratyTovar;
	
	private final List<Tovar> tovaryFromDB = Database.getTovary();
	private final List<Category> kategorieFromDB = Database.getCategories();
	
	private final ObservableList<Tovar> tovary = FXCollections.observableArrayList();
	private final ObservableList<String> kategorie = FXCollections.observableArrayList();

	
	private void fillTable() {
		kategorie.add("Všetko");
		for (Category category : kategorieFromDB){
			kategorie.add(category.getCategoryName());
		}
		tovary.addAll(tovaryFromDB);
		
		jcbCategory.setItems(kategorie);
		jtZoznam.setItems(tovary);
	}
	
	private void setCellValueTable() {
		cNazov.setCellValueFactory(new PropertyValueFactory<>("name"));
		cPLU.setCellValueFactory(new PropertyValueFactory<>("plu"));
		cEAN.setCellValueFactory(new PropertyValueFactory<>("ean"));
		cCena.setCellValueFactory(new PropertyValueFactory<>("jednotkovaCena"));
		cAkcia.setCellValueFactory(new PropertyValueFactory<>("akcia"));
	}
	
	public Tovar returnTovar() {
		return vybratyTovar;
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		setCellValueTable();
		fillTable();
		jcbCategory.getSelectionModel().selectFirst();
		
		jtZoznam.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
		 	if (ke.getCode() == KeyCode.ENTER) {
		 		vybratyTovar = jtZoznam.getSelectionModel().getSelectedItem();
				FXMLTools.closeWindow(ke);
		 	}
		 	else if (ke.getCode() == KeyCode.UP && jtZoznam.getSelectionModel().isSelected(0)){
		 		jtfSearch.requestFocus();
			}
		 });

		jtZoznam.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
				if(mouseEvent.getClickCount() == 2){
					vybratyTovar = jtZoznam.getSelectionModel().getSelectedItem();
					FXMLTools.closeWindow(mouseEvent);
				}
			}
		});

		jtfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()){
				jtZoznam.getItems().clear();
				for (Tovar tovar : tovaryFromDB){
					if (tovar.getTovarName().toLowerCase().contains(newValue.toLowerCase())){
						jtZoznam.getItems().add(tovar);
					}
				}
			}
			else{
				jtZoznam.getItems().setAll(tovaryFromDB);
			}
		});

		jtfSearch.addEventFilter(KeyEvent.KEY_PRESSED, event ->{
			if (event.getCode() == KeyCode.DOWN){
				jtZoznam.requestFocus();
				jtZoznam.getSelectionModel().selectFirst();
			}
			else if (event.getCode() == KeyCode.RIGHT){
				jcbCategory.requestFocus();
				jcbCategory.show();
			}
		});

		jcbCategory.addEventFilter(KeyEvent.KEY_PRESSED, event ->{
			if (event.getCode() == KeyCode.LEFT){
				jtfSearch.requestFocus();
			}
		});
    }

	@FXML
	private void jtfSearchAction(ActionEvent event) {
		jtZoznam.requestFocus();
		jtZoznam.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void jcbCategoryAction(ActionEvent event) {
		String categoryName = jcbCategory.getValue();
		if (categoryName.equals("Všetko")){
			jtZoznam.getItems().setAll(tovaryFromDB);
			jtZoznam.requestFocus();
			jtZoznam.getSelectionModel().selectFirst();
			return;
		}
  	  	ObservableList<Tovar> tovaryPodlaKategorie = FXCollections.observableArrayList();
  	  	for (Tovar tovar : tovary) {
			if (tovar.getCategoryProperty().equals(categoryName)) {
				tovaryPodlaKategorie.add(tovar);
			}
  	  	}
  	  	jtZoznam.setItems(tovaryPodlaKategorie);
		jtZoznam.requestFocus();
		jtZoznam.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
}
