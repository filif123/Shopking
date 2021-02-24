/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Filip
 *
 */
public class CFXMLPoklMnozstvo implements Initializable{
	
	private Integer mnozstvo;
	
	@FXML private TextField jtfMnozstvo;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		jtfMnozstvo.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					jtfMnozstvo.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				else {
					mnozstvo = Integer.parseInt(newValue);
				}
			}
		});
		
	}
	
	@FXML
	private void jbOKAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	@FXML
	private void jbStornoAction (ActionEvent event) throws IOException {
		mnozstvo = null;
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
	
	public Integer returnMnozstvo() {
		return mnozstvo;
	}

}
