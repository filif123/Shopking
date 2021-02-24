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
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * Táto trieda je kontrolér pre okno zmeny hesla používateľov.
 * @author Filip
 *
 */
public class CFXMLPasswordChange implements Initializable{
	
	private String password;
	
	@FXML
	private PasswordField jtHeslo1;
	
	@FXML
	private PasswordField jtHeslo2;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		TextFormatter<?> formatPassword1 = new TextFormatter<>((TextFormatter.Change zmena) -> {
		    String text = zmena.getText();
		    if (!text.isEmpty()) {
		        String newText = text.replace(" ", "").toLowerCase();
		        int vynechanaPozicia = zmena.getCaretPosition() - text.length() + newText.length();
		        zmena.setText(newText);
		        zmena.selectRange( vynechanaPozicia,vynechanaPozicia);
		    }
		    return zmena;
		});
		TextFormatter<?> formatPassword2 = new TextFormatter<>((TextFormatter.Change zmena) -> {
		    String text = zmena.getText();
		    if (!text.isEmpty()) {
		        String newText = text.replace(" ", "").toLowerCase();
		        int vynechanaPozicia = zmena.getCaretPosition() - text.length() + newText.length();
		        zmena.setText(newText);
		        zmena.selectRange( vynechanaPozicia,vynechanaPozicia);
		    }
		    return zmena;
		});
		jtHeslo1.setTextFormatter(formatPassword1);
		jtHeslo2.setTextFormatter(formatPassword2);
    }
	
	public String getNewPassword() {
		return password;
	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) throws IOException{
		String heslo1 = jtHeslo1.getText();
		String heslo2 = jtHeslo2.getText();
		
		if(heslo1.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Nebolo zadané žiadne heslo používateľa");
	        alert.setContentText("Zadajte heslo používateľa!");
	        alert.show();	
		}
		else if(heslo2.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Nebolo zopakované heslo používateľa");
	        alert.setContentText("Zadajte heslo používateľa!");
	        alert.show();	
		}
		else if(!heslo1.equals(heslo2)) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        alert.setTitle("Zlé parametre");
	        alert.setHeaderText("Heslo sa líši");
	        alert.setContentText("Zadajte rovnaké heslo v oboch textových poliach!");
	        alert.show();	
		}
		else {
			password = heslo1;
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        stage.close();
		}
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) throws IOException{
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}
}
