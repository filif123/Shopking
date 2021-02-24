package sk.shopking.installer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import sk.shopking.tools.FXMLTools;


/**
 * @author Filip
 *
 */
public class CFXMLInsDone implements Initializable{
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		
    }
	
	@FXML
    private void jbDoneAction (ActionEvent event){
		FXMLTools.closeWindow(event);
	}
}
