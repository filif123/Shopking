/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sk.shopking.tools.Database;

/**
 * Táto trieda je kontrolér pre okno zoznamu ponukaných obchodom pre pokladníka na výber.
 * @author Filip
 *
 */
public class CFXMLPoklZoznam implements Initializable{
	
	@FXML private ChoiceBox<Category> jcbCategory;
	@FXML private TableView<Tovar> jtZoznam;
	
	@FXML private TableColumn<Tovar, String> cNazov;
	@FXML private TableColumn<Tovar, String> cPLU;
	@FXML private TableColumn<Tovar, String> cEAN;
	@FXML private TableColumn<Tovar, String> cCena;
	
	//tovar, ktory je vybraty
	private Tovar vybratyTovar;
	
	private List<Tovar> tovaryFromXML = Database.getTovary();
	private List<Category> kategorieFromXML = Database.getCategories();
	
	private ObservableList<Tovar> tovary = FXCollections.observableArrayList();
	private ObservableList<Category> kategorie = FXCollections.observableArrayList();

	
	private void fillTable() {
		kategorie.addAll(kategorieFromXML);
		tovary.addAll(tovaryFromXML);
		
		jcbCategory.setItems(kategorie);
		jtZoznam.setItems(tovary);
	}
	
	private void setCellValueTable() {
		cNazov.setCellValueFactory(new PropertyValueFactory<Tovar, String>("name"));
		cPLU.setCellValueFactory(new PropertyValueFactory<Tovar, String>("plu"));
		cEAN.setCellValueFactory(new PropertyValueFactory<Tovar, String>("ean"));
		cCena.setCellValueFactory(new PropertyValueFactory<Tovar, String>("jednotkovaCena"));
	}
	
	public Tovar returnTovar() {
		return vybratyTovar;
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		setCellValueTable();
		fillTable();
		
		 jtZoznam.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ENTER) {
					Tovar oznacenyTovar = jtZoznam.getSelectionModel().getSelectedItem();
	            	vybratyTovar = oznacenyTovar;
	            	Node source = (Node) ke.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
				}
			}
		});
		jtZoznam.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	Tovar oznacenyTovar = jtZoznam.getSelectionModel().getSelectedItem();
		            	vybratyTovar = oznacenyTovar;
		            	Node source = (Node) mouseEvent.getSource();
	                    Stage stage = (Stage) source.getScene().getWindow();
	                    stage.close();
		            }
		        }
		    }
		});
    }
	
	@FXML
	private void jcbCategoryAction(ActionEvent event) throws IOException {
		String categoryName = jcbCategory.getValue().toString();
  	  	ObservableList<Tovar> tovaryPodlaKategorie = FXCollections.observableArrayList();
  	  	for (Tovar tovar : tovary) {
			if (tovar.getCategoryProperty().equals(categoryName)) {
				tovaryPodlaKategorie.add(tovar);
			}
  	  	}
  	  	jtZoznam.setItems(tovaryPodlaKategorie);
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
}
