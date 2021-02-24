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
 * Táto trieda je kontrolér pre okno zobrazenia štatistiky o výkonnosti obchodu.
 * @author Filip
 *
 */
public class CFXMLStatistika implements Initializable{
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		homeMenuController.colorButton("jbStatistika");
    }
	
}
