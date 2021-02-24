package sk.shopking.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.*;
import sk.shopking.tools.Database;
import sk.shopking.tools.ExportToExcel;
import sk.shopking.tools.Utils;


/**
 * Táto trieda je kontrolér pre okno zoznamu závierok.
 * @author Filip
 *
 */
public class CFXMLZavierky implements Initializable{
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	@FXML private StackPane jspVyberDennejZavierky;
	
	@FXML private TableView<DennaZavierka> jtDenneZavierky;
	@FXML private TableView<DenneZavierky> jtDenneZavierkySpolu;
	@FXML private TableView<MesacnaZavierka> jtMesacneZavierky;
	@FXML private TableView<RocnaZavierka> jtRocneZavierky;
	
	@FXML private TableColumn<DennaZavierka, String> cDatumD;
	@FXML private TableColumn<DennaZavierka, String> cCasD;
	@FXML private TableColumn<DennaZavierka, String> cPokladnikD;
	@FXML private TableColumn<DennaZavierka, String> cCelkovyObratD;
	@FXML private TableColumn<DennaZavierka, String> cObrat10D;
	@FXML private TableColumn<DennaZavierka, String> cObrat20D;
	@FXML private TableColumn<DennaZavierka, String> cZapornyObratD;
	
	@FXML private TableColumn<DenneZavierky, String> cDatumDS;
	@FXML private TableColumn<DenneZavierky, String> cCelkovyObratDS;
	@FXML private TableColumn<DenneZavierky, String> cObrat10DS;
	@FXML private TableColumn<DenneZavierky, String> cObrat20DS;
	@FXML private TableColumn<DenneZavierky, String> cZapornyObratDS;
	
	@FXML private TableColumn<MesacnaZavierka, String> cDatumM;
	@FXML private TableColumn<MesacnaZavierka, String> cOdKedyM;
	@FXML private TableColumn<MesacnaZavierka, String> cDoKedyM;
	@FXML private TableColumn<MesacnaZavierka, String> cCelkovyObratM;
	
	@FXML private TableColumn<RocnaZavierka, String> cDatumR;
	@FXML private TableColumn<RocnaZavierka, String> cOdKedyR;
	@FXML private TableColumn<RocnaZavierka, String> cDoKedyR;
	@FXML private TableColumn<RocnaZavierka, String> cCelkovyObratR;
	@FXML private ToggleButton jtbZavierkyPokladnikov;
	@FXML private Button jbVybratDatum;
	
	private List<DenneZavierky> vsetkyDenneZavierkySpolu;
	private List<DennaZavierka> vsetkyDenneZavierky;
	private List<MesacnaZavierka> vsetkyMesacneZavierky;
	private List<RocnaZavierka> vsetkyRocneZavierky;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {

		vsetkyDenneZavierkySpolu = Database.getSkupinyDennychZavierok();
		vsetkyDenneZavierky = Database.getDenneZavierky();
		vsetkyMesacneZavierky = Database.getMesacneZavierky();
		vsetkyRocneZavierky = Database.getRocneZavierky();
		
		homeMenuController.colorButton("jbZavierky");
		
		setCellValueTable();
		
		
		jtDenneZavierkySpolu.getItems().addAll(vsetkyDenneZavierkySpolu);
		jtDenneZavierkySpolu.setPlaceholder(new Label("Žiadne závierky na zobrazenie"));
		jtDenneZavierkySpolu.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
				if(mouseEvent.getClickCount() == 2){
					showInfoDenneZavierky();
				}
			}
		});
		
		jtDenneZavierky.getItems().addAll(vsetkyDenneZavierky);
		jtDenneZavierky.setPlaceholder(new Label("Žiadne závierky na zobrazenie"));
		jtDenneZavierky.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
				if(mouseEvent.getClickCount() == 2){
					showInfoDennaZavierka();
				}
			}
		});
		
		//-----------------------------------2.STRANA---------------------------------------------
		
		jtMesacneZavierky.getItems().addAll(vsetkyMesacneZavierky);
		jtMesacneZavierky.setPlaceholder(new Label("Žiadne závierky na zobrazenie"));
		jtMesacneZavierky.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
				if(mouseEvent.getClickCount() == 2){
					showInfoMesacnaZavierka();
				}
			}
		});
		
		//-----------------------------------3.STRANA---------------------------------------------
		
		jtRocneZavierky.getItems().addAll(vsetkyRocneZavierky);
		jtRocneZavierky.setPlaceholder(new Label("Žiadne závierky na zobrazenie"));
		jtRocneZavierky.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
				if(mouseEvent.getClickCount() == 2){
					showInfoRocnaZavierka();
				}
			}
		});
    }
	
	private void setCellValueTable() {
		cDatumDS.setCellValueFactory(new PropertyValueFactory<>("datumProperty"));
		cCelkovyObratDS.setCellValueFactory(new PropertyValueFactory<>("celkovyObratProperty"));
		cObrat10DS.setCellValueFactory(new PropertyValueFactory<>("obrat10Property"));
		cObrat20DS.setCellValueFactory(new PropertyValueFactory<>("obrat20Property"));
		cZapornyObratDS.setCellValueFactory(new PropertyValueFactory<>("zapornyObratProperty"));
		cDatumDS.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		
		cDatumD.setCellValueFactory(new PropertyValueFactory<>("datumProperty"));
		cCasD.setCellValueFactory(new PropertyValueFactory<>("casProperty"));
		cPokladnikD.setCellValueFactory(new PropertyValueFactory<>("pokladnikProperty"));
		cCelkovyObratD.setCellValueFactory(new PropertyValueFactory<>("celkovyObratProperty"));
		cObrat10D.setCellValueFactory(new PropertyValueFactory<>("obrat10Property"));
		cObrat20D.setCellValueFactory(new PropertyValueFactory<>("obrat20Property"));
		cZapornyObratD.setCellValueFactory(new PropertyValueFactory<>("zapornyObratProperty"));
		cDatumD.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		cCasD.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		
		cDatumM.setCellValueFactory(new PropertyValueFactory<>("datumZavierkyProperty"));
		cOdKedyM.setCellValueFactory(new PropertyValueFactory<>("datumOdProperty"));
		cDoKedyM.setCellValueFactory(new PropertyValueFactory<>("datumDoProperty"));
		cCelkovyObratM.setCellValueFactory(new PropertyValueFactory<>("celkovyObratProperty"));
		cDatumM.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		cOdKedyM.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		cDoKedyM.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		
		cDatumR.setCellValueFactory(new PropertyValueFactory<>("datumZavierkyProperty"));
		cOdKedyR.setCellValueFactory(new PropertyValueFactory<>("datumOdProperty"));
		cDoKedyR.setCellValueFactory(new PropertyValueFactory<>("datumDoProperty"));
		cCelkovyObratR.setCellValueFactory(new PropertyValueFactory<>("celkovyObratProperty"));
		cDatumR.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		cOdKedyR.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
		cDoKedyR.setComparator((o1, o2) -> {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				return formatter.parse(o1).compareTo(formatter.parse(o2));
			} catch (ParseException e) {
				return 0;
			}
		});
	}
	
	@FXML
    private void jbShowDennaZavierkaAction (ActionEvent event) {
		if (jtbZavierkyPokladnikov.isSelected()) {
			showInfoDennaZavierka();
		}
		else {
			showInfoDenneZavierky();
		}
		
	}
	
	private void showInfoDennaZavierka() {
		if (!jtDenneZavierky.getSelectionModel().isEmpty()) {
			DennaZavierka vybranaZavierka = jtDenneZavierky.getSelectionModel().getSelectedItem();
			
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/infoDennaZavierka.fxml"));
			try {
				Parent root = fxmlLoader.load();
				Scene scene = new Scene(root);
			    new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
			    CFXMLInfoDennaZavierka infoDennaZavierkaController = fxmlLoader.getController();
			    infoDennaZavierkaController.setDennaZavierka(vybranaZavierka);		    
			    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			    stage.setScene(scene);
			    stage.setTitle("Informácie o dennej závierke");
			    stage.setResizable(false);
			    stage.initModality(Modality.APPLICATION_MODAL);
			    stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void showInfoDenneZavierky() {
		if (!jtDenneZavierkySpolu.getSelectionModel().isEmpty()) {
			DenneZavierky vybranaZavierka = jtDenneZavierkySpolu.getSelectionModel().getSelectedItem();
			
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/infoDenneZavierky.fxml"));
			try {
				Parent root = fxmlLoader.load();
				Scene scene = new Scene(root);
			    new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
			    CFXMLInfoDenneZavierky infoDenneZavierkyController = fxmlLoader.getController();
			    infoDenneZavierkyController.setDennaZavierka(vybranaZavierka);		    
			    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			    stage.setScene(scene);
			    stage.setTitle("Informácie o dennej závierke");
			    stage.setResizable(false);
			    stage.initModality(Modality.APPLICATION_MODAL);
			    stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
    private void jbExportDenneAction (ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
	    FileChooser.ExtensionFilter xlsx = new FileChooser.ExtensionFilter("Zošit programu Excel", "*.xlsx");
	    fileChooser.getExtensionFilters().add(xlsx);
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
        	ExportToExcel.exportDenneZavierky(selectedFile, vsetkyDenneZavierkySpolu);
		}
	}
	
	@FXML
    private void jbObnovitDenneZavierky (ActionEvent event) {
		vsetkyDenneZavierky = Database.getDenneZavierky();
		jtDenneZavierky.getItems().setAll(vsetkyDenneZavierky);
		
		vsetkyDenneZavierkySpolu = Database.getSkupinyDennychZavierok();
		jtDenneZavierkySpolu.getItems().setAll(vsetkyDenneZavierkySpolu);
	}
	
	@FXML
    private void jbVybratDatumAction (ActionEvent event) {
		try {
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/denneZavierkyPodlaDna.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
		    new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
		    CFXMLDenneZavierkyPodlaDna zavierkyDatumController = fxmlLoader.getController();		    
		    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		    stage.setScene(scene);
		    stage.setTitle("Denné závierky podľa dna");
		    stage.setResizable(false);
		    stage.initModality(Modality.APPLICATION_MODAL);
		    stage.showAndWait();
		    Date vybranyDatum = zavierkyDatumController.getSelectedDate();
		    if (vybranyDatum != null) {
		    	List<DennaZavierka> zavierkyVDanyDatum = new ArrayList<>();
			    for (DennaZavierka dennaZavierka : vsetkyDenneZavierky) {
					if (Utils.isSameDay(dennaZavierka.getCasZavierky(), vybranyDatum)) {
						zavierkyVDanyDatum.add(dennaZavierka);
					}
				}
			    jtDenneZavierky.getItems().setAll(zavierkyVDanyDatum);
			}
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
    private void jtbZavierkyPokladnikovAction(ActionEvent event) {
		if (jtbZavierkyPokladnikov.isSelected()) {
			jtDenneZavierky.setVisible(true);
			jtDenneZavierkySpolu.setVisible(false);
			jtDenneZavierkySpolu.getSelectionModel().clearSelection();
			jbVybratDatum.setDisable(false);
			
		}
		else {
			jtDenneZavierky.setVisible(false);
			jtDenneZavierky.getSelectionModel().clearSelection();
			jtDenneZavierkySpolu.setVisible(true);
			jbVybratDatum.setDisable(true);
		}
	}
	//-----------------------------------2.STRANA---------------------------------------------
	
	@FXML
    private void jbNewMesacnaZavierkaAction (ActionEvent event) {
		
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/newMesacnaZavierka.fxml"));
		try {
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
		    new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
		    CFXMLNewMesacnaZavierka newMesacnaZavierkaController = fxmlLoader.getController();		    
		    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		    stage.setScene(scene);
		    stage.setTitle("Vytvoriť novú mesačnú závierku");
		    stage.setResizable(false);
		    stage.initModality(Modality.APPLICATION_MODAL);
		    stage.showAndWait();
		    if (newMesacnaZavierkaController.isZavierkaVykonana()) {
				jtMesacneZavierky.getItems().add(newMesacnaZavierkaController.getMesacnaZavierka());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
    private void jbShowMesacnaZavierkaAction (ActionEvent event) {
		showInfoMesacnaZavierka();
	}
	
	@FXML
    private void jbExportMesacneAction (ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
	    FileChooser.ExtensionFilter xlsx = new FileChooser.ExtensionFilter("Zošit programu Excel", "*.xlsx");
	    fileChooser.getExtensionFilters().add(xlsx);
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
        	ExportToExcel.exportMesacneZavierky(selectedFile, vsetkyMesacneZavierky);
		}
	}
	
	private void showInfoMesacnaZavierka() {
		if (!jtMesacneZavierky.getSelectionModel().isEmpty()) {
			MesacnaZavierka vybranaZavierka = jtMesacneZavierky.getSelectionModel().getSelectedItem();
			
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/infoMesacnaZavierka.fxml"));
			try {
				Parent root = fxmlLoader.load();
				Scene scene = new Scene(root);
			    new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
			    CFXMLInfoMesacnaZavierka infoMesacnaZavierkaController = fxmlLoader.getController();
			    infoMesacnaZavierkaController.setMesacnaZavierka(vybranaZavierka);		    
			    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			    stage.setScene(scene);
			    stage.setTitle("Informácie o mesačnej závierke");
			    stage.setResizable(false);
			    stage.initModality(Modality.APPLICATION_MODAL);
			    stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//-----------------------------------3.STRANA---------------------------------------------
	
	@FXML
    private void jbNewRocnaZavierkaAction (ActionEvent event) {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/newRocnaZavierka.fxml"));
		try {
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
		    new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
		    CFXMLNewRocnaZavierka newRocnaZavierkaController = fxmlLoader.getController();		    
		    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		    stage.setScene(scene);
		    stage.setTitle("Vytvoriť novú ročnú závierku");
		    stage.setResizable(false);
		    stage.initModality(Modality.APPLICATION_MODAL);
		    stage.showAndWait();
		    if (newRocnaZavierkaController.isZavierkaVykonana()) {
				jtRocneZavierky.getItems().add(newRocnaZavierkaController.getRocnaZavierka());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
    private void jbShowRocnaZavierkaAction (ActionEvent event) {
		showInfoRocnaZavierka();
	}
	
	@FXML
    private void jbExportRocneAction (ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
	    FileChooser.ExtensionFilter xlsx = new FileChooser.ExtensionFilter("Zošit programu Excel", "*.xlsx");
	    fileChooser.getExtensionFilters().add(xlsx);
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
        	ExportToExcel.exportRocneZavierky(selectedFile, vsetkyRocneZavierky);
		}
	}
	
	private void showInfoRocnaZavierka() {
		if (!jtRocneZavierky.getSelectionModel().isEmpty()) {
			RocnaZavierka vybranaZavierka = jtRocneZavierky.getSelectionModel().getSelectedItem();
			
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/infoRocnaZavierka.fxml"));
			try {
				Parent root = fxmlLoader.load();
				Scene scene = new Scene(root);
			    new JMetro(scene,Style.LIGHT).setAutomaticallyColorPanes(true);
			    CFXMLInfoRocnaZavierka infoRocnaZavierkaController = fxmlLoader.getController();
			    infoRocnaZavierkaController.setRocnaZavierka(vybranaZavierka);		    
			    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			    stage.setScene(scene);
			    stage.setTitle("Informácie o ročnej závierke");
			    stage.setResizable(false);
			    stage.initModality(Modality.APPLICATION_MODAL);
			    stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setZavierky(List<DennaZavierka> denneZavierky, List<MesacnaZavierka> mesacneZavierky, List<RocnaZavierka> rocneZavierky, List<DenneZavierky> skupinaDennychZavierok){
		this.vsetkyDenneZavierky = denneZavierky;
		this.vsetkyMesacneZavierky = mesacneZavierky;
		this.vsetkyRocneZavierky = rocneZavierky;
		this.vsetkyDenneZavierkySpolu = skupinaDennychZavierok;
	}
}