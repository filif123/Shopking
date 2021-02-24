package sk.shopking.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Handler;

import com.fazecast.jSerialComm.SerialPort;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.*;
import sk.shopking.tools.*;

/**
 * Táto trieda je kontrolér pre okno nastavení zobrazujúcich administrátorovi.
 * @author Filip
 *
 */
public class CFXMLNastavenia implements Initializable{

	private static final SKLog LOGGER = SKLog.getLogger();

	private File selectedDirBloky = null;
	private File selectedFileLogo = null;
	private String newPassword = null;
	private final User aktualnyUser = CFXMLLogin.getZhoda();
	private final List<SerialPort> portyList = Arrays.asList(SerialPort.getCommPorts());
	
	
	@FXML private HBox jpAdminPassword;
	@FXML private TextField jtfNazovSpol,jtfObchodnyNazov,jtfICO,jtfDIC,jtfICDPH,jtfUlicaSpol,jtfCisloSpol,jtfPSCSpol,jtfMestoSpol;
	@FXML private TextField jtfUlicaPrev,jtfMestoPrev,jtfCisloPrev,jtfPSCPrev;
	@FXML private TextField jtfPociatocnaSuma,jtfSumaLimit;
	@FXML private TextField jtfLogoPath;
	@FXML private TextField jtfBlokyPath;
	@FXML private ChoiceBox<String> jcbPorty;
	@FXML private TextField jtfIP;
	@FXML private ChoiceBox<String> jcbPokladnicaNumber;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		if(aktualnyUser.getUserNickname().equals("admin")) {
			jpAdminPassword.setDisable(false);
		}
		
		Obchod obchod = Database.infoObchod();
		ShopSettings shopSettings = Database.getShopSettings();
		selectedDirBloky = new File(Objects.requireNonNull(AppSettings.loadReceiptPath()));
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
		
		jtfPociatocnaSuma.setText("" + shopSettings.getPoklPociatocnaSuma());
		jtfSumaLimit.setText("" + shopSettings.getPoklMaxSuma());
		
		if (selectedFileLogo != null) {
			jtfLogoPath.setText(selectedFileLogo.toString());
		}
		
		if (selectedDirBloky != null) {
			jtfBlokyPath.setText(selectedDirBloky.toString());
		}
		
		
		ObservableList<String> portyString = FXCollections.observableArrayList();
		portyString.add("Žiadny");
		for (SerialPort serialPort : portyList) {
			portyString.add(serialPort.getPortDescription() + " (" + serialPort.getSystemPortName() + ")");
		}
		
		jcbPorty.setItems(portyString);
		
		String portName = AppSettings.loadScannerPort();
		if (portName == null || portName.isEmpty()) {
			jcbPorty.getSelectionModel().selectFirst();
		}
		else if (BarcodeScanner.isPortNotAvailable(portName)) {
			jcbPorty.getSelectionModel().selectFirst();
		}
		else{
			SerialPort actualSerialPort = SerialPort.getCommPort(portName);

			for (int i = 0; i < portyList.size(); i++) {
				if (portyList.get(i).getSystemPortName().equals(actualSerialPort.getSystemPortName())) {
					jcbPorty.getSelectionModel().select(i + 1);
				}
			}
		}

		jtfIP.setText(AppSettings.loadIPSQL());

		setChoiceBoxPokladnice();
    }

    private void setChoiceBoxPokladnice(){
		ObservableList<String> pokladniceList = FXCollections.observableArrayList();
		int thisPokl = AppSettings.loadCashRegisterNumber();
		int selectNum = 0;
		pokladniceList.add("Žiadne");
		for (Pokladnica pokladnica : Database.getPokladnice()){
			pokladniceList.add("" + pokladnica.getIdPokladnice());
			if (pokladnica.getIdPokladnice() == thisPokl){
				selectNum = pokladnica.getIdPokladnice();
			}
		}

		jcbPokladnicaNumber.setItems(pokladniceList);
		if (selectNum != 0){
			jcbPokladnicaNumber.getSelectionModel().select("" + selectNum);
		}
		else {
			jcbPokladnicaNumber.getSelectionModel().select(0);
		}
	}

	@FXML
	private void jbPrehladavatLogoAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Vyberte obrázkový súbor s logom spoločnosti");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
			if (selectedFile != null) {
				selectedFileLogo = selectedFile;
				jtfLogoPath.setText(selectedFileLogo.toString());
			} 
	}
	
	@FXML
	private void jbDeleteLogoAction(ActionEvent event) {
		selectedFileLogo = null;
		jtfLogoPath.setText("Nevybraté");
	}
	
	@FXML
	private void jbPrehladavatBlokyAction(ActionEvent event) {
		DirectoryChooser dir = new DirectoryChooser();
		dir.setTitle("Vyberte priečinok pre ukladanie pokladničných blokov");
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        File file = dir.showDialog(stage); 
		if (file != null) {
			selectedDirBloky = file;
			jtfBlokyPath.setText(selectedDirBloky.toString());
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
	    stage.showAndWait();

	    setChoiceBoxPokladnice();
	}
	
	@FXML
	private void jbPasswordChangeAction(ActionEvent event) throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/passwordChange.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    CFXMLPasswordChange passwordChangeController = fxmlLoader.getController();
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Zmena hesla");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.showAndWait();
	    
	    newPassword = passwordChangeController.getNewPassword();
	}
	
	@FXML
	private void jbSaveAction(ActionEvent event){
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

		String ip = jtfIP.getText();
		
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
		else if(ip == null || ip.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			alert.setTitle("Zlé parametre");
			alert.setHeaderText("Nebola zadaná žiadna adresa ku serveru");
			alert.setContentText("Zadajte adresu serveru!");
			alert.show();
		}
		else {
			String pattern = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
			if (!ip.matches(pattern) && !ip.equals("localhost")) {
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert.setTitle("Zlé parametre");
				alert.setHeaderText("Zadaná IP adresa má nesprávny tvar");
				alert.setContentText("Opravte adresu serveru!");
				alert.show();
				return;
			}

			AppSettings.setIPSQL(ip);

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
			
			if (newPassword != null) {
				List<User> users = new ArrayList<>();
				User oldAdministrator = Database.getSpecificUser(1);
				users.add(new UserAdministrator(1, oldAdministrator.getUserMeno(), null, oldAdministrator.getUserNickname(), User.hashPassword(newPassword)));
				Database.updateUsers(users);
			}
			
			if (!jcbPorty.getSelectionModel().isEmpty()) {
				int index = jcbPorty.getSelectionModel().getSelectedIndex();
				if (index != 0) {
					SerialPort port = portyList.get(index - 1);
					AppSettings.setScannerPort(port.getSystemPortName());
				}
				else if (Objects.equals(AppSettings.loadScannerPort(), "")) {
					AppSettings.setScannerPort("");
				}
			}
			
			Obchod obchod = new Obchod(obchodnyNazov, nazovSpol, mestoSpol, ulicaSpol, pscSpol, cisloSpol, ico, dic, icdph, mestoPrev, ulicaPrev, pscPrev, cisloPrev, selectedFileLogo);
			if (!obchod.equals(Database.infoObchod())) {
				Database.editObchod(obchod);
			}

			ShopSettings shopSettings = new ShopSettings();
			shopSettings.setPoklPociatocnaSuma(pocSumaNum);
			shopSettings.setPoklMaxSuma(limSumaNum);
			if (!shopSettings.equals(Database.getShopSettings())){
				Database.editShopSettings(shopSettings);
			}
			
			if (selectedDirBloky != null) {
				AppSettings.setReceiptPath(selectedDirBloky.getPath());
			}

			String vybrateCisloPokladne = jcbPokladnicaNumber.getValue();
			if (vybrateCisloPokladne.equals("Žiadne")){
				AppSettings.setCashRegisterNumber("" + 0);
			}
			else {
				AppSettings.setCashRegisterNumber(vybrateCisloPokladne);
			}

			FXMLTools.closeWindow(event);
		}
		
	}

	@FXML
	private void jbPremazatDBAction(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Upozornenie");
		alert.setHeaderText("Naozaj chcete premazať databázu?");
		alert.setContentText("Všetky uložené dáta v databáze (okrem informácií o obchode) sa vymažú! Heslo hlavného administrára ostáva zachované.");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
			boolean success = Database.truncateDB();
			if (!success){
				Alert alert2 = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert2.setTitle("Chyba");
				alert2.setHeaderText("Nastala chyba pri premazávaní databázy!");
				alert2.setContentText("Databáza sa nepremazala celá!");
				alert2.show();

				alert.close();
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();
				return;
			}
			alert.close();
			FXMLTools.closeWindow(event);

			LOGGER.info("Program " + ApplicationInfo.APP_NAME + " sa ukončuje");
			for(Handler h:LOGGER.getHandlers()){
				h.close();
			}
			Platform.exit();
			System.exit(0);
		}
		else {
			alert.close();
		}
	}

	@FXML
	private void jbVymazatDBAction(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Upozornenie");
		alert.setHeaderText("Naozaj chcete vymazať databázu?");
		alert.setContentText("Celá databáza sa na serveri vymaže!");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
			Database.deleteDBUser(Database.addressToServer,"app");
			Database.changePassword(Database.addressToServer,"");
			boolean success = Database.deleteDB();
			if (!success){
				Alert alert2 = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert2.setTitle("Chyba");
				alert2.setHeaderText("Nastala chyba pri mazaní databázy!");
				alert2.setContentText("Databáza sa nevymazala!");
				alert2.show();

				alert.close();
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();
				return;
			}
			alert.close();
			FXMLTools.closeWindow(event);

			LOGGER.info("Program " + ApplicationInfo.APP_NAME + " sa ukončuje");
			for(Handler h:LOGGER.getHandlers()){
				h.close();
			}
			Platform.exit();
			System.exit(0);
		}
		else {
			alert.close();
		}
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event){
		FXMLTools.closeWindow(event);
	}
}
