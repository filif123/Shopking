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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * @author Filip
 *
 */
public class CFXMLPoklStornoInputDoklad implements Initializable{

	@FXML private TextField jtfDokladID;
	@FXML private Label jlStav;
	
	private boolean isFound = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jtfDokladID.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					jtfDokladID.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				else {
					
				}
			}
		});
		
	}
	
	@FXML
	private void jbOKAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklStornoVyber.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Stornovanie položky nákupu");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.show();
    	
    	Node source = (Node) event.getSource();
        Stage thisStage = (Stage) source.getScene().getWindow();
        thisStage.close();
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
	}

}
