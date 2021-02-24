/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.AppSettings;
import sk.shopking.tools.Database;

/**
 * Táto trieda je kontrolér pre okno zoznamu pokladníc v obchode.
 * @author Filip
 *
 */
public class CFXMLPokladnice implements Initializable{
	
	private List<Pokladnica> pokladniceFromDB = Database.getPokladnice();
	private int refreshTime = AppSettings.loadCashRegistersAutoRefresh();
	private Timeline clock;
	//private static boolean consumeEvent = false;
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	@FXML private TableView<Pokladnica> jtPokladnice;
	
	@FXML private TableColumn<Pokladnica, String> cPokladnica;
	@FXML private TableColumn<Pokladnica, String> cDKP;
	@FXML private TableColumn<Pokladnica, String> cPripojena;
	@FXML private TableColumn<Pokladnica, Boolean> cOpenClose;
	@FXML private TableColumn<Pokladnica, String> cPokladnik;
	@FXML private TableColumn<Pokladnica, String> cSuma;
	
	/*public static boolean consumeEvent() {
		return consumeEvent;
	}*/
	private void fillTable() {
		jtPokladnice.getItems().addAll(pokladniceFromDB);
	}
	
	private void setCellValue() {
		cPokladnica.setCellValueFactory(new PropertyValueFactory<Pokladnica, String>("id"));
		cDKP.setCellValueFactory(new PropertyValueFactory<Pokladnica, String>("dkp"));
		cPripojena.setCellValueFactory(new PropertyValueFactory<Pokladnica, String>("pripojena"));
		cOpenClose.setCellValueFactory(new PropertyValueFactory<Pokladnica, Boolean>("open"));
		cOpenClose.setCellFactory(tc -> new CheckBoxTableCell<>());
		cPokladnik.setCellValueFactory(new PropertyValueFactory<Pokladnica, String>("pokladnik"));
		cSuma.setCellValueFactory(new PropertyValueFactory<Pokladnica, String>("suma"));
	}
	
	private void refreshTable() {
		pokladniceFromDB = Database.getPokladnice();
		jtPokladnice.getItems().setAll(pokladniceFromDB);
		
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		homeMenuController.colorButton("jbPokladnice");
		/*homeMenuController.addListener(new MainMenuListener() {

			@Override
			public void handle(MouseEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert.setTitle("Uložiť vykonané zmeny");
				alert.setHeaderText("Uložiť vykonané zmeny");
				alert.setContentText("Chcete uložiť vykonané zmeny?");
				
				ButtonType buttonTypeYes = new ButtonType("Áno",ButtonData.YES);
				ButtonType buttonTypeNo = new ButtonType("Nie",ButtonData.NO);
				ButtonType buttonTypeStorno = new ButtonType("Zrušiť",ButtonData.CANCEL_CLOSE);
				
				alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo,buttonTypeStorno);
				Optional<ButtonType> vysledok = alert.showAndWait();
				if(vysledok.get() == buttonTypeYes) {
					alert.close();
				}
				if(vysledok.get() == buttonTypeNo) {
					alert.close();
				}
				else {
					alert.close();
					consumeEvent = true;
				}
				
			}
			
		});*/
		setCellValue();
		fillTable();
		clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			refreshTable();
		}),new KeyFrame(Duration.seconds(refreshTime)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
    }
	
	@FXML
	private void jbSettingsAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
	    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/pokladniceSettings.fxml")));
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL)); 
	    stage.setScene(scene);
	    new JMetro(scene,Style.LIGHT);
	    stage.setTitle("Nastavenia pokladníc");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.showAndWait();
	    refreshTable();
	}
	
	@FXML
	private void jbRefreshAction(ActionEvent event) throws IOException {
		refreshTable();
		
	}
	
	@FXML
	private void jbAutoRefreshAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
	    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/autoRefreshPokladnice.fxml")));
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL)); 
	    stage.setScene(scene);
	    new JMetro(scene,Style.LIGHT);
	    stage.setTitle("Automatické obnovenie stavu pokladníc");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.showAndWait();
	    refreshTime = AppSettings.loadCashRegistersAutoRefresh();
		clock.stop();
	    clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			refreshTable();
		}),new KeyFrame(Duration.seconds(refreshTime)));
	    clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}
}
