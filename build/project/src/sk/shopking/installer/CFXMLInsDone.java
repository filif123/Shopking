/**
 * 
 */
package sk.shopking.installer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;


/**
 * @author Filip
 *
 */
public class CFXMLInsDone implements Initializable{
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		
    }
	
	@FXML
    private void jbDoneAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
 	    stage.close();
		
	}
}
