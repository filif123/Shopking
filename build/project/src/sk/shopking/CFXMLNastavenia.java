/**
 * 
 */
package sk.shopking;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.AppSettings;
import sk.shopking.tools.Database;

/**
 * Táto trieda je kontrolér pre okno nastavení zobrazujúcich administrátorovi.
 * @author Filip
 *
 */
public class CFXMLNastavenia implements Initializable{
	
	private File selectedDirBloky = null;
	private File selectedFileLogo = null;
	private User aktualnyUser = CFXMLLogin.getZhoda();
	
	
	@FXML private HBox jpAdminPassword;
	@FXML private TextField jtfNazovSpol,jtfObchodnyNazov,jtfICO,jtfDIC,jtfICDPH,jtfUlicaSpol,jtfCisloSpol,jtfPSCSpol,jtfMestoSpol;
	@FXML private TextField jtfUlicaPrev,jtfMestoPrev,jtfCisloPrev,jtfPSCPrev;
	@FXML private TextField jtfPociatocnaSuma,jtfSumaLimit;
	@FXML private Label jlLogoPath;
	@FXML private Label jlBlokyPath;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		if(aktualnyUser.getUserNickname().equals("admin")) {
			jpAdminPassword.setDisable(false);
		}
		
		Obchod obchod = Database.infoObchod();
		selectedDirBloky = new File(AppSettings.loadReceiptPath());
		selectedFileLogo = obchod.getLogoSpolocnosti();	
		
		jtfNazovSpol.setText(obchod.getNazovFirmy());
		jtfObchodnyNazov.setText(obchod.getObchodnyNazovFirmy());
		jtfICO.setText(obchod.getICO());
		jtfDIC.setText(obchod.getDIC());
		jtfICDPH.setText(obchod.getICDPH());
		jtfUlicaSpol.setText(obchod.getUlicaFirmy());
		jtfCisloSpol.setText(obchod.getCisloPopisneFirmy());
		jtfPSCSpol.setText(obchod.getPSCFirmy());
		jtfMestoSpol.setText(obchod.getMestoFirmy());
		
		jtfUlicaPrev.setText(obchod.getUlicaPrevadzky());
		jtfCisloPrev.setText(obchod.getCisloPopisnePrevadzky());
		jtfPSCPrev.setText(obchod.getPSCPrevadzky());
		jtfMestoPrev.setText(obchod.getMestoPrevadzky());
		
		jtfPociatocnaSuma.setText("" + obchod.getPoklPociatocnaSuma());
		jtfSumaLimit.setText("" + obchod.getPoklMaxSuma());
		
		if (selectedFileLogo != null) {
			jlLogoPath.setText(selectedFileLogo.toString());
		}
		
		if (selectedDirBloky != null) {
			jlBlokyPath.setText(selectedDirBloky.toString());
		}
    }
	
	@FXML
	private void jbPrehladavatLogoAction(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Vyberte obrázkový súbor s logom spoločnosti");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		         //new ExtensionFilter("All Files", "*.*"));
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
			if (selectedFile != null) {
				selectedFileLogo = selectedFile;
				jlLogoPath.setText(selectedFileLogo.toString());
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
	private void jbPokladniceAction(ActionEvent event) throws IOException{
		Stage stage = new Stage();
	    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/pokladniceSettings.fxml")));
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL)); 
	    stage.setScene(scene);
	    new JMetro(scene,Style.LIGHT);
	    stage.setTitle("Nastavenia pokladníc");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.show();
	}
	
	@FXML
	private void jbPasswordChangeAction(ActionEvent event) throws IOException{
		Stage stage = new Stage();
	    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/passwordChange.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Zmena hesla");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.show();
	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) throws IOException{
		String nazovSpol = jtfNazovSpol.getText();
		String obchodnyNazov = jtfObchodnyNazov.getText();
		String ico = jtfICO.getText();
		String dic = jtfDIC.getText();
		String icdph = jtfICDPH.getText();
		String ulicaSpol = jtfUlicaSpol.getText();
		String cisloSpol = jtfCisloSpol.getText();
		String pscSpol = jtfPSCSpol.getText();
		String mestoSpol = jtfMestoSpol.getText();
		String ulicaPrev = jtfUlicaPrev.getText();
		String cisloPrev = jtfCisloPrev.getText();
		String pscPrev = jtfPSCPrev.getText();
		String mestoPrev = jtfMestoPrev.getText();
		
		String pocSuma = jtfPociatocnaSuma.getText();
		String limSuma = jtfSumaLimit.getText();
		
		if(nazovSpol == null || nazovSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadny názov spoločnosti");
        	alert.setContentText("Zadajte názov spoločnosti!");
        	alert.show();
		}
		else if(obchodnyNazov == null || obchodnyNazov.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnen obchodný názov spoločnosti");
        	alert.setContentText("Zadajte obchodný názov spoločnosti!");
        	alert.show();
		}
		else if(ico == null || ico.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne IČO spoločnosti");
        	alert.setContentText("Zadajte IČO spoločnosti!");
        	alert.show();
		}
		else if(dic == null || dic.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne DIČ spoločnosti");
        	alert.setContentText("Zadajte DIČ spoločnosti!");
        	alert.show();
		}
		else if(icdph == null || icdph.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne IČDPH spoločnosti");
        	alert.setContentText("Zadajte IČDPH spoločnosti!");
        	alert.show();
		}
		else if(ulicaSpol == null || ulicaSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnu ulicu sídla spoločnosti");
        	alert.setContentText("Zadajte ulicu sídla spoločnosti!");
        	alert.show();
		}
		else if(pscSpol == null || pscSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnu PSČ sídla spoločnosti");
        	alert.setContentText("Zadajte PSČ sídla spoločnosti!");
        	alert.show();
		}
		else if(cisloSpol == null || cisloSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne číslo popisné sídla spoločnosti");
        	alert.setContentText("Zadajte číslo popisné sídla spoločnosti!");
        	alert.show();
		}
		else if(mestoSpol == null || mestoSpol.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne mesto sídla spoločnosti");
        	alert.setContentText("Zadajte mesto sídla spoločnosti!");
        	alert.show();
		}
		else if(ulicaPrev == null || ulicaPrev.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnu ulicu prevádzky");
        	alert.setContentText("Zadajte ulicu prevádzky!");
        	alert.show();
		}
		else if(pscPrev == null || pscPrev.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadnu PSČ prevádzky");
        	alert.setContentText("Zadajte PSČ prevádzky!");
        	alert.show();
		}
		else if(cisloPrev == null || cisloPrev.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne číslo popisné prevádzky");
        	alert.setContentText("Zadajte číslo popisné prevádzky!");
        	alert.show();
		}
		else if(mestoPrev == null || mestoPrev.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol zadaný žiadne mesto prevádzky");
        	alert.setContentText("Zadajte mesto prevádzky!");
        	alert.show();
		}
		else if(pocSuma == null || pocSuma.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola zadaná žiadna počiatočná suma do pokladnice");
        	alert.setContentText("Zadajte počiatočnú sumu do pokladnice!");
        	alert.show();
		}
		else if(limSuma == null || limSuma.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebola zadaná žiadna maximálna suma v pokladnici");
        	alert.setContentText("Zadajte maximálnu sumu v pokladnici!");
        	alert.show();
		}
		else if(selectedDirBloky == null) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol vybratý priečinok pre uchovávanie pokladničných blokov");
        	alert.setContentText("Vyberte priečinok pre uchovávanie pokladničných blokov!");
        	alert.show();
		}
		/*else if(selectedFileLogo == null) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Zlé parametre");
        	alert.setHeaderText("Nebol vybratý obrázok loga spoločnosti");
        	alert.setContentText("Vyberte obrázok loga spoločnosti!");
        	alert.show();
		}*/
		else {
			float pocSumaNum,limSumaNum;
			try {
				pocSumaNum = Float.parseFloat(pocSuma);
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        	alert.setTitle("Zlé parametre");
	        	alert.setHeaderText("Počiatočná suma do pokladnice nie je číslo");
	        	alert.setContentText("Zadajte počiatočnú sumu do pokladnice!");
	        	alert.show();
	        	return;
			}
			
			try {
				limSumaNum = Float.parseFloat(limSuma);
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        	alert.setTitle("Zlé parametre");
	        	alert.setHeaderText("Maximálna suma v pokladnici nie je číslo");
	        	alert.setContentText("Zadajte maximálnu sumu v pokladnici!");
	        	alert.show();
	        	return;
			}
			
			
			Obchod obchod = new Obchod(obchodnyNazov, nazovSpol, mestoSpol, ulicaSpol, pscSpol, cisloSpol, ico, dic, icdph, mestoPrev, ulicaPrev, pscPrev, cisloPrev, selectedFileLogo,pocSumaNum,limSumaNum);
			if (!obchod.equals(Database.infoObchod())) {
				Database.editObchod(obchod);
			}
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
