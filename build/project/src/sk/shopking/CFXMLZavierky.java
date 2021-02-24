/**
 * 
 */
package sk.shopking;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;


/**
 * Táto trieda je kontrolér pre okno zoznamu závierok.
 * @author Filip
 *
 */
public class CFXMLZavierky implements Initializable{
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		homeMenuController.colorButton("jbZavierky");
    }
	
}
