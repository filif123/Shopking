/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Táto trieda je kontrolér pre okno riadenia akcií výrobkov.
 * @author Filip
 *
 */
public class CFXMLAkciaTovar implements Initializable{
	
	@FXML private TitledPane tpAkciaCena, tpAkciaMnozstvo;
	@FXML private RadioButton rbAkciaCena, rbAkciaMnozstvo;
	@FXML private Label jlPovodnaCena,jlNovaCena;
	@FXML private Label jlPovodneMnozstvo,jlNoveMnozstvo;
	@FXML private TextField jtfNovaCena,jtfZlavaPercenta,jtfRozdiel;
	@FXML private TextField jtfPovodneMnozstvo,jtfNoveMnozstvo,jtfPlatiOdMnozstva;
	
	private ToggleGroup buttonGroup = new ToggleGroup();
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		rbAkciaCena.setToggleGroup(buttonGroup);
		rbAkciaCena.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				tpAkciaCena.setDisable(false);
				tpAkciaMnozstvo.setDisable(true);
			}
			
		});
		rbAkciaMnozstvo.setToggleGroup(buttonGroup);
		rbAkciaMnozstvo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				tpAkciaCena.setDisable(true);
				tpAkciaMnozstvo.setDisable(false);
			}
			
		});
    }
	
	@FXML
	private void jbSaveAction(ActionEvent event) throws IOException {
		
	}
	
	@FXML
	private void jbDeleteAction(ActionEvent event) throws IOException {
		
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
}
