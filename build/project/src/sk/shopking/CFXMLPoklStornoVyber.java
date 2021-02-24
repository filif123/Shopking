/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * @author Filip
 *
 */
public class CFXMLPoklStornoVyber implements Initializable{

	@FXML private TableView<NakupenyTovar> jtPolozkyNakupu;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	private void jbDeleteAction(ActionEvent event) throws IOException {
		
	}
	
	@FXML
	private void jbZrusitAction(ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}

}
