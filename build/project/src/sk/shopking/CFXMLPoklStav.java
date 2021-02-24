/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * @author Filip
 *
 */
public class CFXMLPoklStav implements Initializable{

	@FXML private Label jlSuma;
	
	private float sumaPokladnica;
	
	public void setUserSuma(float suma) {
		this.sumaPokladnica = suma;
		jlSuma.setText(new DecimalFormat("0.00").format(suma));
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
	}
	
	@FXML
   	private void jbPrijemAction(ActionEvent event) throws IOException {
    	   Node source = (Node) event.getSource();
           Stage stage = (Stage) source.getScene().getWindow();
           stage.close();
    }
	
	@FXML
   	private void jbVydajAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklVydaj.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	CFXMLPoklVydaj cfxmlPoklVydaj = fxmlLoader.getController();
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Výdaj z pokladnice");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.show();
    	
    	cfxmlPoklVydaj.setUserSuma(sumaPokladnica);
    	
    	Node source = (Node) event.getSource();
        Stage thisStage = (Stage) source.getScene().getWindow();
        thisStage.close();   
    }
	
	@FXML
   	private void jbOKAction(ActionEvent event) throws IOException {
    	   Node source = (Node) event.getSource();
           Stage stage = (Stage) source.getScene().getWindow();
           stage.close();
    }
	
	@FXML
   	private void jbStornoAction(ActionEvent event) throws IOException {
    	   Node source = (Node) event.getSource();
           Stage stage = (Stage) source.getScene().getWindow();
           stage.close();
    }
}
