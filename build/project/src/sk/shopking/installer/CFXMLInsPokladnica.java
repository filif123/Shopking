/**
 * 
 */
package sk.shopking.installer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.fazecast.jSerialComm.SerialPort;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * @author Filip
 *
 */
public class CFXMLInsPokladnica implements Initializable{
	
	@FXML private TextField jtfDKP,jtfCisloPokl;
	@FXML private Label jlBlokyPath;
	@FXML private ChoiceBox<SerialPort> jcbPorty;
	
	private File selectedDirBloky;
	//private String comPort;
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		List<SerialPort> portyList = Arrays.asList(SerialPort.getCommPorts());
		jcbPorty.getItems().addAll(portyList);
		
		jtfDKP.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.isEmpty()) {
					if (!newValue.matches("\\d*")) {
						jtfDKP.setText(newValue.replaceAll("[^\\d]", ""));
			        } 
				}
			}
		});
		
		jtfCisloPokl.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.isEmpty()) {
					if (!newValue.matches("\\d*")) {
						jtfCisloPokl.setText(newValue.replaceAll("[^\\d]", ""));
			        } 
				}
			}
		});
		
		if (InstallPokladnica.dokladyPath != null && InstallPokladnica.cisloPokladnice != 0 && InstallPokladnica.dkp != null) {
			if (InstallPokladnica.citackaPort != null) {
				jcbPorty.setValue(SerialPort.getCommPort(InstallPokladnica.citackaPort));
			}
			jtfCisloPokl.setText("" + InstallPokladnica.cisloPokladnice);
			jtfDKP.setText(InstallPokladnica.dkp);
			selectedDirBloky = InstallPokladnica.dokladyPath;
			jlBlokyPath.setText(selectedDirBloky.toString());
		}
    }
	
	@FXML
	private void jbPrehladavatBlokyAction(ActionEvent event) throws IOException {
		DirectoryChooser dir = new DirectoryChooser();
		dir.setTitle("Vyberte priečinok pre ukladanie pokladničných blokov");
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        File file = dir.showDialog(stage); 
		if (file != null) {
			selectedDirBloky = file;
			jlBlokyPath.setText(selectedDirBloky.toString());
		}
	}
	
	@FXML
    private void jbBackAction (ActionEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/scenes/instalacia/insKlientPokladnica.fxml")));
 	    stage.setScene(scene);
	}
	
	@FXML
    private void jbNextAction (ActionEvent event) throws IOException {
		String dkp = jtfDKP.getText();
		String cisloPokl = jtfCisloPokl.getText();
		
		if(dkp == null || dkp.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola zadaná žiadna DKP pokladnice");
        	alert.setContentText("Zadajte DKP pokladnice!");
        	alert.show();
		}
		if(cisloPokl == null || cisloPokl.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebolo zadané žiadne číslo pokladnice");
        	alert.setContentText("Zadajte číslo pokladnice!");
        	alert.show();
		}
		else if(selectedDirBloky == null){
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol vybratý priečinok pre ukladanie pokladničných dokladov");
        	alert.setContentText("Vyberte priečinok pre ukladanie pokladničných dokladov!");
        	alert.show();
		}
		else {
			SerialPort port = null;
			if (!jcbPorty.getSelectionModel().isEmpty()) {
				port = jcbPorty.getSelectionModel().getSelectedItem();
			}
			InstallPokladnica.cisloPokladnice = Integer.parseInt(cisloPokl);
			InstallPokladnica.dkp = dkp;
			InstallPokladnica.dokladyPath = selectedDirBloky;
			InstallPokladnica.citackaPort = port.getSystemPortName();
			
			Node source = (Node) event.getSource();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/instalacia/insInstalaciaUkazovatelPokladnice.fxml"));
		    Parent root = (Parent) fxmlLoader.load();
	        Stage stage = (Stage) source.getScene().getWindow();
	        Scene scene = source.getScene();
		    scene.setRoot(root);
	 	    stage.setScene(scene);
	 	    CFXMLInsUkazovatelPokladnice installPokladnicaController = fxmlLoader.getController(); 
	 	    installPokladnicaController.installing();
	 	   /* stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new  EventHandler<WindowEvent>(){
	 	        @Override
	 	        public void handle(WindowEvent window) {
	 	        	
	 	        }
	 	    });*/
		}
		
		
	}
	
	@FXML
    private void jbStornoAction (ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
   		alert.setTitle("Ukončenie inštalácie");
   		alert.setHeaderText("Inštalácia nebola ukončená");
   		alert.setContentText("Chcete naozaj ukončiť inštaláciu programu ?");
   		Optional<ButtonType> vysledok = alert.showAndWait();
   		if(vysledok.get() == ButtonType.OK) {
   			alert.close();
   			Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
   			stage.close();
   		}
   		else {
   			alert.close();
   		}
	}
}
