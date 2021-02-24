package sk.shopking.controllers;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Handler;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.*;
import sk.shopking.tools.*;

/**
 * Táto trieda je kontrolér pre okno domovskej obrazovky pokladníka.
 * @author Filip
 *
 */
public class CFXMLHomePokladnik implements Initializable {

	private static final SKLog LOGGER = SKLog.getLogger();
	private final UserPokladnik aktualnyUser = (UserPokladnik) CFXMLLogin.getZhoda();
	private final List<Tovar> vsetkyTovary = Database.getTovary();
	private final List<Pokladnica> vsetkyPokladnice = Database.getPokladnice();
	private Pokladnica tatoPokladnica;
	
	@FXML private Label jlUser;
	@FXML private Label jlPokladnicaNumber;
	@FXML private Label jlTime;
	@FXML private Label jlDate;
	@FXML private Label jlDay;
	
	@FXML private Label jlSuma;
	
	@FXML private TableView<NakupenyTovar> jtNakup;
	
	@FXML private TableColumn<NakupenyTovar,String> cNazovVyrobku;
	@FXML private TableColumn<NakupenyTovar,String> cPLU;
	@FXML private TableColumn<NakupenyTovar,String> cEAN;
	@FXML private TableColumn<NakupenyTovar,String> cCena;
	@FXML private TableColumn<NakupenyTovar,String> cJednotkovaCena;
	@FXML private TableColumn<NakupenyTovar,String> cDPH;
	@FXML private TableColumn<NakupenyTovar,String> cMnozstvo;
	@FXML private TableColumn<NakupenyTovar,String> cJednotka;
	@FXML private TableColumn<NakupenyTovar,String> cAkcia;
	
	@FXML private VBox vbMnozstvo;
	@FXML private VBox vbHmotnost;
	@FXML private VBox vbHotovost;
	@FXML private VBox vbStravneListky;
	
	private float cenaNakupu = 0;
	private boolean dospely = false;
	private BarcodeScanner bScanner;
	private GetDataFromAndroidPokladnik getDataFromAndroidPokladnik;
	private float sumaVPokladnici = Database.getStavPokladnice(aktualnyUser.getId());

	private final AddTovarScannerListener addTovarScannerListener = new AddTovarScannerListener();
	private final AddTovarAppListener addTovarAppListener = new AddTovarAppListener();

	@Override
    public void initialize(URL url, ResourceBundle rb){
		int cisloPokladniceFromXML = AppSettings.loadCashRegisterNumber();
		if(cisloPokladniceFromXML == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Nesprávny formát konfiguračného súboru");
        	alert.setHeaderText("Číslo pokladne v súbore " + AppSettings.FILE_SETTINGS + " má nesprávny formát");
        	alert.setContentText("Skontrolujte obsahovú správnosť konfiguračného súboru!");
        	alert.showAndWait();
        	LOGGER.info("Program " + ApplicationInfo.APP_NAME + " sa ukončuje");
			for(Handler h:LOGGER.getHandlers()){
				h.close();
			}
        	Platform.exit();
        	System.exit(0);
		}
		int vybranaPokladnica = -1;
		for (Pokladnica pokladnica : vsetkyPokladnice) {
			if (pokladnica.getIdPokladnice() == cisloPokladniceFromXML) {
				vybranaPokladnica = pokladnica.getIdPokladnice();
				this.tatoPokladnica = pokladnica;
				break;
			}
		}
		if(vybranaPokladnica == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Pokladnica neexistuje");
        	alert.setHeaderText("Číslo pokladne v súbore " + AppSettings.FILE_SETTINGS + " sa nezhoduje s databázou pokladní");
        	alert.setContentText("Skontrolujte obsahovú správnosť konfiguračného súboru!");
        	alert.showAndWait();
        	LOGGER.info("Program " + ApplicationInfo.APP_NAME + " sa ukončuje");
			for(Handler h:LOGGER.getHandlers()){
				h.close();
			}
        	Platform.exit();
        	System.exit(0);
		}
		else {
			jlPokladnicaNumber.setText("" + vybranaPokladnica);

			String ip;
			try {
				ip = InternetTools.getLocalIP();
				if (ip == null){
					ip="";
				}
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
				ip = "";
			}

			Database.setPokladnicaOpen(aktualnyUser.getId(), tatoPokladnica.getIdPokladnice(),ip);
		}
		
		String port = AppSettings.loadScannerPort();
		if (port == null) {
			Alert alert = new Alert(AlertType.ERROR);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Súbor " + AppSettings.FILE_SETTINGS + " nenájdený");
        	alert.setHeaderText("Súbor " + AppSettings.FILE_SETTINGS + " sa nenašiel alebo bol nesprávne upravený");
        	alert.setContentText("Skontrolujte, či súbor existuje a či je obsahovo správny.");
        	alert.showAndWait();
        	LOGGER.info("Program " + ApplicationInfo.APP_NAME + " sa ukončuje");
			for(Handler h:LOGGER.getHandlers()){
				h.close();
			}
        	Platform.exit();
        	System.exit(0);
		}
		else if (BarcodeScanner.isPortNotAvailable(port)) {
			Alert alert = new Alert(AlertType.WARNING);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Čítačka čiarových kódov nenájdená");
        	alert.setHeaderText("Nebola nájdená čítačka čiar. kódov na zadanom porte");
        	alert.setContentText("Skontrolujte port, na ktorom by mala byť čítačka zapojená!");
        	alert.showAndWait();
		}
		else {
			bScanner = new BarcodeScanner(port);
			bScanner.initPort();
			bScanner.addListener(addTovarScannerListener);
		}
		try {
			getDataFromAndroidPokladnik = new GetDataFromAndroidPokladnik();
			getDataFromAndroidPokladnik.addListener(addTovarAppListener);
			getDataFromAndroidPokladnik.start();
		} catch (IOException e) {
			e.printStackTrace();
		}


		vbHotovost.setDisable(true);
		vbStravneListky.setDisable(true);
		showDate();
		nameUser();
		setCellValueTable();
		jtNakup.getItems().addListener((ListChangeListener<NakupenyTovar>) c -> {
			cenaNakupu = 0;
			for (NakupenyTovar polozka : jtNakup.getItems()) {
				cenaNakupu = cenaNakupu + polozka.getNakupenaCena();
			}
			jlSuma.setText("" + new DecimalFormat("0.00").format(cenaNakupu));

			if (cenaNakupu == 0) {
				vbHotovost.setDisable(true);
				vbStravneListky.setDisable(true);
			}
			else {
				vbHotovost.setDisable(false);
				vbStravneListky.setDisable(false);
			}
		});
		
		jtNakup.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			jtNakup.getSelectionModel().select(newValue);
			try {
				if (newValue.getTovarJednotka().equals(JednotkaType.KS)) {
					vbMnozstvo.setDisable(false);
					vbHmotnost.setDisable(true);
				}
				else {
					vbMnozstvo.setDisable(true);
					vbHmotnost.setDisable(false);
				}
			}catch (NullPointerException ignored){}


		});
		this.tatoPokladnica.setPokladnikUser(aktualnyUser);
		jtNakup.setPlaceholder(new Label("Nebol zadaný žiaden nákup"));
		
    }

    class AddTovarScannerListener implements BarcodeScannerListener {

		@Override
		public void barcodeEvent(String barcode) {
			Platform.runLater(() -> {
				for (Tovar tovar : vsetkyTovary) {
					if (Long.parseLong(barcode) == tovar.getTovarEAN()) {
						addPolozkaToNakup(tovar);
						return;
					}
				}
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert.setTitle("Skenovaný tovar nenájdený");
				alert.setHeaderText("Nebol nájdený oskenovaný tovar");
				alert.setContentText("Skontrolujte databázu ponúkaného tovaru!");
				alert.show();
			});
		}
	}

	class AddTovarAppListener implements GetDataFromAndroidPokladnikListener{

		@Override
		public void receivedBarcodeEvent(String barcode) {
			Platform.runLater(() -> {
				for (Tovar tovar : vsetkyTovary) {
					if (Long.parseLong(barcode) == tovar.getTovarEAN()) {
						addPolozkaToNakup(tovar);
						return;
					}
				}
				Alert alert = new Alert(AlertType.ERROR);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert.setTitle("Skenovaný tovar nenájdený");
				alert.setHeaderText("Nebol nájdený oskenovaný tovar");
				alert.setContentText("Skontrolujte databázu ponúkaného tovaru!");
				alert.show();
			});
		}
	}
	
	public void addPolozkaToNakupSHmotnostou(Tovar tovar) {
		try {
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklHmotnost.fxml"));
		    Parent root = fxmlLoader.load();
		    Scene scene = new Scene(root);
	    	new JMetro(scene,Style.LIGHT);
	    	stage.setScene(scene);
	    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    	stage.setTitle("Zadať hmotnosť tovaru");
	    	stage.setResizable(false);
		    stage.initModality(Modality.APPLICATION_MODAL);
	    	stage.showAndWait();
	    	CFXMLPoklHmotnost cfxmlPoklHmotnostController = fxmlLoader.getController();
	    	Integer hmotnost = cfxmlPoklHmotnostController.returnHmotnost();
	    	if (hmotnost != null) {
				NakupenyTovar novyTovar;
				if (tovar instanceof TovarZlavaCena) {
					novyTovar = new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), tovar.getTovarJednotkovaCena(), tovar.getTovarDPH(), hmotnost, ((TovarZlavaCena) tovar).getNovaCena());
				}
	    		else {
					novyTovar = new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), tovar.getTovarJednotkovaCena(), tovar.getTovarDPH(), hmotnost);
				}
				jtNakup.getItems().add(novyTovar);
				jtNakup.getSelectionModel().selectLast();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPolozkaToNakup(Tovar tovar) {
		if (!tovar.getTovarCategory().getCategoryPristupnePreMladistvych() && !dospely) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			alert.setTitle("Upozornenie");
			alert.setHeaderText("Obmedzenie predaja nákupnej položky");
			alert.setContentText("Má občan viac ako 18 rokov ?");
			
			ButtonType buttonTypeYes = new ButtonType("Ano",ButtonData.YES);
			ButtonType buttonTypeNo = new ButtonType("Nie",ButtonData.NO);
			
			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			
			Optional<ButtonType> vysledok = alert.showAndWait();
			if(vysledok.isPresent() && vysledok.get() == buttonTypeYes) {
				dospely = true;
				alert.close();
			}
			else {
				alert.close();
				return;
			}
		}
		
		for (int i = 0 ; i < jtNakup.getItems().size() ; i++) {
			if (jtNakup.getItems().get(i).getTovarName().equals(tovar.getTovarName())) {
				NakupenyTovar povodnaPolozka = jtNakup.getItems().get(i);
				if (povodnaPolozka.getTovarJednotka().equals(JednotkaType.KS)) {
					if (tovar instanceof TovarZlavaCena) {
						NakupenyTovar upravenaPolozka = new NakupenyTovar(povodnaPolozka.getTovarPLU(),povodnaPolozka.getTovarName(), povodnaPolozka.getTovarCategory(), povodnaPolozka.getTovarJednotka(), povodnaPolozka.getTovarEAN(), tovar.getTovarJednotkovaCena(), povodnaPolozka.getTovarDPH(), povodnaPolozka.getNakupeneMnozstvo() + 1,((TovarZlavaCena)tovar).getNovaCena());
						jtNakup.getItems().set(i,upravenaPolozka);
						jtNakup.getSelectionModel().select(i);
						return;
					}
					else if (tovar instanceof TovarZlavaMnozstvo) {
						NakupenyTovar upravenaPolozka = new NakupenyTovar(povodnaPolozka.getTovarPLU(),povodnaPolozka.getTovarName(), povodnaPolozka.getTovarCategory(), povodnaPolozka.getTovarJednotka(), povodnaPolozka.getTovarEAN(), tovar.getTovarJednotkovaCena(), povodnaPolozka.getTovarDPH(), povodnaPolozka.getNakupeneMnozstvo() + 1,((TovarZlavaMnozstvo)tovar).getPovodneMnozstvo(),((TovarZlavaMnozstvo)tovar).getNoveMnozstvo(),((TovarZlavaMnozstvo)tovar).getMinimalneMnozstvo());
						jtNakup.getItems().set(i,upravenaPolozka);
						jtNakup.getSelectionModel().select(i);
						return;
					}
					else {
						NakupenyTovar upravenaPolozka = new NakupenyTovar(povodnaPolozka.getTovarPLU(),povodnaPolozka.getTovarName(), povodnaPolozka.getTovarCategory(), povodnaPolozka.getTovarJednotka(), povodnaPolozka.getTovarEAN(), tovar.getTovarJednotkovaCena(), povodnaPolozka.getTovarDPH(), povodnaPolozka.getNakupeneMnozstvo() + 1);
						jtNakup.getItems().set(i,upravenaPolozka);
						jtNakup.getSelectionModel().select(i);
						return;
					}
					
				}
				else {
					try {
						Stage stage = new Stage();
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklHmotnost.fxml"));
					    Parent root = fxmlLoader.load();
					    Scene scene = new Scene(root);
				    	new JMetro(scene,Style.LIGHT);
				    	stage.setScene(scene);
				    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
				    	stage.setTitle("Zadať hmotnosť tovaru");
				    	stage.setResizable(false);
					    stage.initModality(Modality.APPLICATION_MODAL);
				    	stage.showAndWait();
				    	CFXMLPoklHmotnost cfxmlPoklHmotnostController = fxmlLoader.getController();
				    	Integer hmotnost = cfxmlPoklHmotnostController.returnHmotnost();
				    	if (hmotnost != null) {
							NakupenyTovar vybratyTovar = jtNakup.getItems().get(i);
							NakupenyTovar novyTovar;
							if (tovar instanceof TovarZlavaCena) {
								novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(), vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), vybratyTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), hmotnost, ((TovarZlavaCena) tovar).getNovaCena());
							}
				    		else {
								novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(), vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), vybratyTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), hmotnost);
							}
							jtNakup.getItems().set(i, novyTovar);

						}
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
							
			}
		}
		if (tovar.getTovarJednotka().equals(JednotkaType.KS)) {
			if (tovar instanceof TovarZlavaCena) {
				jtNakup.getItems().add(new NakupenyTovar(tovar.getTovarPLU(),tovar.getTovarName(),tovar.getTovarCategory(),tovar.getTovarJednotka(),tovar.getTovarEAN(),tovar.getTovarJednotkovaCena(),tovar.getTovarDPH(),1,((TovarZlavaCena)tovar).getNovaCena()));
				jtNakup.getSelectionModel().selectLast();
			}
			else if (tovar instanceof TovarZlavaMnozstvo) {
				jtNakup.getItems().add(new NakupenyTovar(tovar.getTovarPLU(),tovar.getTovarName(),tovar.getTovarCategory(),tovar.getTovarJednotka(),tovar.getTovarEAN(),tovar.getTovarJednotkovaCena(),tovar.getTovarDPH(),1,((TovarZlavaMnozstvo)tovar).getPovodneMnozstvo(),((TovarZlavaMnozstvo)tovar).getNoveMnozstvo(),((TovarZlavaMnozstvo)tovar).getMinimalneMnozstvo()));
				jtNakup.getSelectionModel().selectLast();
			}
			else {
				jtNakup.getItems().add(new NakupenyTovar(tovar.getTovarPLU(),tovar.getTovarName(),tovar.getTovarCategory(),tovar.getTovarJednotka(),tovar.getTovarEAN(),tovar.getTovarJednotkovaCena(),tovar.getTovarDPH(),1));
				jtNakup.getSelectionModel().selectLast();
			}
			
		}
		else {
			addPolozkaToNakupSHmotnostou(tovar);
		}
		
	}

	public void addPolozkyToNakup(List<NakupenyTovar> nakupeneTovary) {
		for (NakupenyTovar nakupenyTovar : nakupeneTovary) {
			if (!nakupenyTovar.getTovarCategory().getCategoryPristupnePreMladistvych() && !dospely) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert.setTitle("Upozornenie");
				alert.setHeaderText("Obmedzenie predaja nákupnej položky");
				alert.setContentText("Má občan viac ako 18 rokov ?");

				ButtonType buttonTypeYes = new ButtonType("Ano",ButtonData.YES);
				ButtonType buttonTypeNo = new ButtonType("Nie",ButtonData.NO);

				alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

				Optional<ButtonType> vysledok = alert.showAndWait();
				if(vysledok.isPresent() && vysledok.get() == buttonTypeYes) {
					dospely = true;
					alert.close();
				}
				else {
					alert.close();
					continue;
				}
			}

			for (int i = 0 ; i < jtNakup.getItems().size() ; i++) {
				if (jtNakup.getItems().get(i).getTovarName().equals(nakupenyTovar.getTovarName())) {
					NakupenyTovar povodnaPolozka = jtNakup.getItems().get(i);
					if (povodnaPolozka.getTovarJednotka().equals(JednotkaType.KS)) {
						if ((Tovar)nakupenyTovar instanceof TovarZlavaCena) {
							NakupenyTovar upravenaPolozka = new NakupenyTovar(povodnaPolozka.getTovarPLU(),povodnaPolozka.getTovarName(), povodnaPolozka.getTovarCategory(), povodnaPolozka.getTovarJednotka(), povodnaPolozka.getTovarEAN(), nakupenyTovar.getTovarJednotkovaCena(), povodnaPolozka.getTovarDPH(), povodnaPolozka.getNakupeneMnozstvo() + nakupenyTovar.getNakupeneMnozstvo(),((TovarZlavaCena)(Tovar)nakupenyTovar).getNovaCena());
							jtNakup.getItems().set(i,upravenaPolozka);
							jtNakup.getSelectionModel().select(i);
						}
						else if ((Tovar)nakupenyTovar instanceof TovarZlavaMnozstvo) {
							NakupenyTovar upravenaPolozka = new NakupenyTovar(povodnaPolozka.getTovarPLU(),povodnaPolozka.getTovarName(), povodnaPolozka.getTovarCategory(), povodnaPolozka.getTovarJednotka(), povodnaPolozka.getTovarEAN(), nakupenyTovar.getTovarJednotkovaCena(), povodnaPolozka.getTovarDPH(), povodnaPolozka.getNakupeneMnozstvo() + nakupenyTovar.getNakupeneMnozstvo(),((TovarZlavaMnozstvo)(Tovar)nakupenyTovar).getPovodneMnozstvo(),((TovarZlavaMnozstvo)(Tovar)nakupenyTovar).getNoveMnozstvo(),((TovarZlavaMnozstvo)(Tovar)nakupenyTovar).getMinimalneMnozstvo());
							jtNakup.getItems().set(i,upravenaPolozka);
							jtNakup.getSelectionModel().select(i);
						}
						else {
							NakupenyTovar upravenaPolozka = new NakupenyTovar(povodnaPolozka.getTovarPLU(),povodnaPolozka.getTovarName(), povodnaPolozka.getTovarCategory(), povodnaPolozka.getTovarJednotka(), povodnaPolozka.getTovarEAN(), nakupenyTovar.getTovarJednotkovaCena(), povodnaPolozka.getTovarDPH(), povodnaPolozka.getNakupeneMnozstvo() + nakupenyTovar.getNakupeneMnozstvo());
							jtNakup.getItems().set(i,upravenaPolozka);
							jtNakup.getSelectionModel().select(i);
						}
					}
					else {
						jtNakup.getItems().set(i, nakupenyTovar);
					}
					return;
				}
			}
			jtNakup.getItems().add(nakupenyTovar);
			jtNakup.getSelectionModel().selectLast();
		}
	}
	
	private void notifyMaxSumaPokladnica() {
		Alert alert = new Alert(AlertType.WARNING);
		Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
    	alert.setTitle("Prekročený limit hotovosti v pokladnici");
    	alert.setHeaderText("Vo vašej pokladnici sa nachádza väčšie\nmnožstvo hotovosti ako je povolené");
    	alert.setContentText("Vydajte nadbytočnú hotovosť správcovi.");
    	alert.show();
	}
	
	private void notifyMinSumaPokladnica() {
		Alert alert = new Alert(AlertType.WARNING);
		Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
    	alert.setTitle("Prekročené minimum hotovosti v pokladnici");
    	alert.setHeaderText("Vo vašej pokladnici sa nachádza menšie\nmnožstvo hotovosti ako je povolené");
    	alert.setContentText("Pridajte do pokladnice potrebnú hotovosť.");
    	alert.show();
	}
	
	public Pokladnica getPokladnica() {
		return tatoPokladnica;
	}
	
	public void closePort() {
		if (bScanner != null) {
            bScanner.removeListeners();
			bScanner.closePort();
		}
	}
	
	public boolean isNotEmptyList() {
		return !jtNakup.getItems().isEmpty();
	}
	
	public boolean isTovarSMnozstvom() {
		return jtNakup.getSelectionModel().getSelectedItem().getTovarJednotka().equals(JednotkaType.KS);
	}
	
	private void setCellValueTable() {
		cNazovVyrobku.setCellValueFactory(new PropertyValueFactory<>("name"));
		cPLU.setCellValueFactory(new PropertyValueFactory<>("plu"));
		cEAN.setCellValueFactory(new PropertyValueFactory<>("ean"));
		cCena.setCellValueFactory(new PropertyValueFactory<>("cena"));
		cJednotkovaCena.setCellValueFactory(new PropertyValueFactory<>("jednotkovaCena"));
		cDPH.setCellValueFactory(new PropertyValueFactory<>("dph"));
		cMnozstvo.setCellValueFactory(new PropertyValueFactory<>("mnozstvo"));
		cJednotka.setCellValueFactory(new PropertyValueFactory<>("jednotka"));
		cAkcia.setCellValueFactory(new PropertyValueFactory<>("akcia"));
	}
	
	@FXML
	private void jbVypnutAction(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Ukončenie programu");
		alert.setHeaderText("Ukončenie programu");
		alert.setContentText("Chcete naozaj ukončiť program ?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
			alert.close();
            closePort();
			getDataFromAndroidPokladnik.terminate();
			FXMLTools.closeWindow(event);
	        
	        Database.setPokladnicaClose(aktualnyUser.getId(), tatoPokladnica.getIdPokladnice());
		}
		else {
			alert.close();
		}
	}
	
	@FXML
	private void jbOdhlasitAction(MouseEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Odhlásenie");
		alert.setHeaderText("Odhlásenie");
		alert.setContentText("Chcete sa naozaj odhlásiť ?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
            closePort();
			getDataFromAndroidPokladnik.terminate();
			alert.close();
			FXMLTools.closeWindow(event);
	        Database.setPokladnicaClose(aktualnyUser.getId(), tatoPokladnica.getIdPokladnice());
	        Stage stage = new Stage();
	        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/login.fxml")));
	        new JMetro(scene,Style.LIGHT);
	        stage.setScene(scene);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	        stage.setResizable(false);
	        stage.show();
		}
		else {
			alert.close();
		}
	}
	
	@FXML
	private void jbCloseAction(MouseEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Odhlásenie");
		alert.setHeaderText("Odhlásenie");
		alert.setContentText("Chcete sa naozaj odhlásiť ?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
            closePort();
			getDataFromAndroidPokladnik.terminate();
			alert.close();
			FXMLTools.closeWindow(event);
	        Database.setPokladnicaClose(aktualnyUser.getId(), tatoPokladnica.getIdPokladnice());
	        
	        Stage stage = new Stage();
	        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/screenSaver.fxml")));
	        scene.addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>() {
				
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
				        stage.close();
				        Stage stageLogin = new Stage();
						Scene sceneLogin;
						try {
							sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/login.fxml")));
							new JMetro(sceneLogin,Style.LIGHT);
					        stageLogin.setScene(sceneLogin);
					        stageLogin.setResizable(false);
					        stageLogin.initStyle(StageStyle.UNDECORATED);
					        stageLogin.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
					        stageLogin.show();
						} catch (IOException e) {
							FXMLTools.showExceptionAlert(e);
						}
					}	
				}
			});
	        stage.setScene(scene);
	        stage.setFullScreen(true);
	        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setResizable(false);
	        stage.setOnCloseRequest(we -> {
				Alert alert1 = new Alert(AlertType.CONFIRMATION);
				new JMetro(alert1.getDialogPane().getScene(),Style.LIGHT);
				alert1.setTitle("Ukončenie programu");
				alert1.setHeaderText("Ukončenie programu");
				alert1.setContentText("Chcete naozaj ukončiť program ?");
				Optional<ButtonType> vysledok1 = alert1.showAndWait();
				if(vysledok1.isPresent() && vysledok1.get() == ButtonType.OK) {
					alert1.close();
					stage.close();
				}
				else {
					alert1.close();
					we.consume();
				}
			});
	        stage.show();
		}
		else {
			alert.close();
		}
	}
	
	@FXML
	private void jbZoznamAction(MouseEvent event) throws IOException {
		showZoznam();
	}
	
	@FXML
	private void jbEANAction(MouseEvent event) throws IOException {
		showEANInput();
	}
	
	@FXML
	private void jbPLUAction(MouseEvent event) throws IOException {
		showPLUInput();
	}

	@FXML
	private void jbScanInfoAction(MouseEvent event) throws IOException {
		showScanInfo();
	}
	
	@FXML
	private void jbMnozstvoAction(MouseEvent event) throws IOException {
		showMnozstvoInput();
	}
	
	@FXML
	private void jbHmotnostAction(MouseEvent event) throws IOException {
		showHmotnostInput();
	}
	
	@FXML
	private void jbHotovostAction(MouseEvent event) throws IOException {
		showHotovostInput();
	}
	
	@FXML
	private void jbStravneListkyAction(MouseEvent event) throws IOException {
		showStravneListkyInput();
	}
	
	@FXML
	private void jbStavPokladniceAction(MouseEvent event) throws IOException {
		showStavPokladnice();
	}
	
	@FXML
	private void jbPrijemAction(MouseEvent event) throws IOException {
		showPrijemInput();
	}
	
	@FXML
	private void jbVydajAction(MouseEvent event) throws IOException {
		showVydajInput();
	}
	
	@FXML
	private void jbUzavierkaAction(MouseEvent event) throws IOException {
		showUzavierka();
	}
	
	@FXML
	private void jbZrusitNakupAction(MouseEvent event) {
		showZrusitNakup();
	}
	
	@FXML
	private void jbDeleteItemAction(MouseEvent event) {
		showDeleteItem();
	}
	
	@FXML
	private void jbStornoAction(MouseEvent event) throws IOException {
		showStorno();
	}

	@FXML
	private void jbImportAction(MouseEvent event) throws IOException {
		showImport();
	}
	
	public void showZoznam() throws IOException {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklZoznam.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ESCAPE){
				stage.close();
			}
		});
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Zoznam produktov");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.showAndWait();
    	CFXMLPoklZoznam cfxmlPoklZoznamController = fxmlLoader.getController();
    	Tovar tovar = cfxmlPoklZoznamController.returnTovar();
    	if (tovar != null) {
    		addPolozkaToNakup(tovar);
		}
	}
	
	public void showEANInput()  throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklEAN.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Zadať EAN");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.showAndWait();
    	CFXMLPoklEAN cfxmlPoklEANController = fxmlLoader.getController();
    	Tovar tovar = cfxmlPoklEANController.returnTovar();
    	if (tovar != null) {
			addPolozkaToNakup(tovar);
			/*jtNakup.getSelectionModel().clearSelection();
			jtNakup.getSelectionModel().selectLast();*/
		}
	}
	
	public void showPLUInput()  throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklPLU.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Zadať PLU");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.showAndWait();
    	CFXMLPoklPLU cfxmlPoklPLUController = fxmlLoader.getController();
    	Tovar tovar = cfxmlPoklPLUController.returnTovar();
    	if (tovar != null) {
			addPolozkaToNakup(tovar);
		}
	}

	public void showScanInfo()  throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklScanInfo.fxml"));
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);
		new JMetro(scene,Style.LIGHT);
		stage.setScene(scene);
		stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		stage.setTitle("Získať informácie oskenovaním tovaru");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		CFXMLPoklScanInfo cfxmlPoklScanInfo = fxmlLoader.getController();
		if (bScanner != null){
			bScanner.removeListener(addTovarScannerListener);
			cfxmlPoklScanInfo.setScanner(bScanner);
		}
		/*else{
			Alert alert = new Alert(AlertType.WARNING);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
			stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			alert.setTitle("Čítačka čiarových kódov nenájdená");
			alert.setHeaderText("Čítačka čiarových kódov nie je pripojená");
			alert.setContentText("Pripojte čítačku a reštartujte program alebo sa odhláste a prihláste");
			alert.show();
		}*/
		getDataFromAndroidPokladnik.removeListener(addTovarAppListener);
		cfxmlPoklScanInfo.setApp(getDataFromAndroidPokladnik);
		stage.showAndWait();
		if (bScanner != null){
			bScanner.addListener(addTovarScannerListener);
		}
		getDataFromAndroidPokladnik.addListener(addTovarAppListener);

	}
	
	public void showMnozstvoInput() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklMnozstvo.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Zadať množstvo tovaru");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.showAndWait();
    	CFXMLPoklMnozstvo cfxmlPoklMnozstvoController = fxmlLoader.getController();
    	Integer mnozstvo = cfxmlPoklMnozstvoController.returnMnozstvo();
    	if (mnozstvo != null) {
    		int index = jtNakup.getSelectionModel().getSelectedIndex();
    		NakupenyTovar vybratyTovar = jtNakup.getItems().get(index);
    		Tovar thisTovar = Database.getSpecificTovar(vybratyTovar.getTovarPLU());
    		if (thisTovar instanceof TovarZlavaCena) {
    			NakupenyTovar novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(),vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), thisTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), mnozstvo,((TovarZlavaCena)thisTovar).getNovaCena());
        		jtNakup.getItems().set(index, novyTovar);
			}
    		else if (thisTovar instanceof TovarZlavaMnozstvo) {
    			NakupenyTovar novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(),vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), thisTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), mnozstvo,((TovarZlavaMnozstvo)thisTovar).getPovodneMnozstvo(),((TovarZlavaMnozstvo)thisTovar).getNoveMnozstvo(),((TovarZlavaMnozstvo)thisTovar).getMinimalneMnozstvo());
        		jtNakup.getItems().set(index, novyTovar);
			}
    		else {
    			NakupenyTovar novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(),vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), thisTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), mnozstvo);
        		jtNakup.getItems().set(index, novyTovar);
			}
    		
		}
	}
	
	public void showHmotnostInput() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklHmotnost.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Zadať hmotnosť tovaru");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.showAndWait();
    	CFXMLPoklHmotnost cfxmlPoklHmotnostController = fxmlLoader.getController();
    	Integer hmotnost = cfxmlPoklHmotnostController.returnHmotnost();
    	if (hmotnost != null) {
    		int index = jtNakup.getSelectionModel().getSelectedIndex();
    		NakupenyTovar vybratyTovar = jtNakup.getItems().get(index);
    		Tovar thisTovar = Database.getSpecificTovar(vybratyTovar.getTovarPLU());
			NakupenyTovar novyTovar;
			if (thisTovar instanceof TovarZlavaCena) {
				novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(), vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), thisTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), hmotnost, ((TovarZlavaCena) thisTovar).getNovaCena());
			}
    		else {
				novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(), vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), thisTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), hmotnost);
			}
			jtNakup.getItems().set(index, novyTovar);

		}
	}
	
	public void showHotovostInput() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklHotovost.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Hotovosť");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	CFXMLPoklHotovost cfxmlPoklHotovostController = fxmlLoader.getController();
    	cfxmlPoklHotovostController.setNakupeneTovary(jtNakup.getItems());
    	cfxmlPoklHotovostController.setPokladnica(tatoPokladnica);
    	stage.showAndWait();
    	
    	sumaVPokladnici = Database.getStavPokladnice(aktualnyUser.getId());
    	
    	if (sumaVPokladnici > Database.getShopSettings().getPoklMaxSuma()) {
			notifyMaxSumaPokladnica();
		}
    	
    	if (!cfxmlPoklHotovostController.isPlatbaZrusena()) {
    		jtNakup.getItems().clear();
    		cenaNakupu = 0;
    		jlSuma.setText("0,00");
    		vbHmotnost.setDisable(true);
    		vbMnozstvo.setDisable(true);
		}
	}

	public void showStravneListkyInput() throws IOException {
		List<NakupenyTovar> nakupeneTovary = jtNakup.getItems();
		for (NakupenyTovar nakupenyTovar : nakupeneTovary) {
			if (!nakupenyTovar.getTovarCategory().getCategoryPovoleneStravneListky()){
				Alert alert = new Alert(AlertType.WARNING);
				Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
				stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
				new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
				alert.setTitle("Nákup obsahuje tovar, ktorý nie je povolené zaplatiť stravnými lístkami");
				alert.setHeaderText("Nákup obsahuje tovar, ktorý nie je povolené zaplatiť stravnými lístkami");
				alert.setContentText("Nákup je možné platiť iba hotovosťou alebo kartou.");
				alert.show();
				return;
			}
		}
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklStravneListky.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Stravné lístky");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	CFXMLPoklStravneListky cfxmlPoklStravneListkyController = fxmlLoader.getController();
    	cfxmlPoklStravneListkyController.setNakupeneTovary(jtNakup.getItems());
    	cfxmlPoklStravneListkyController.setPokladnica(tatoPokladnica);
    	stage.showAndWait();
    	
    	sumaVPokladnici = Database.getStavPokladnice(aktualnyUser.getId());
    	
    	if (sumaVPokladnici > Database.getShopSettings().getPoklMaxSuma()) {
			notifyMaxSumaPokladnica();
		}
    	
    	if (!cfxmlPoklStravneListkyController.isPlatbaZrusena()) {
    		jtNakup.getItems().clear();
    		cenaNakupu = 0;
    		jlSuma.setText("0,00");
    		vbHmotnost.setDisable(true);
    		vbMnozstvo.setDisable(true);
		}
    	
	}
	
	public void showStavPokladnice() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklStavPokladnice.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Stav pokladnice");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.showAndWait();
    	
    	sumaVPokladnici = Database.getStavPokladnice(aktualnyUser.getId());
    	ShopSettings settings = Database.getShopSettings();
    	if (sumaVPokladnici > settings.getPoklMaxSuma()) {
			notifyMaxSumaPokladnica();
		}
    	
    	if (sumaVPokladnici < settings.getPoklPociatocnaSuma()) {
			notifyMinSumaPokladnica();
		}
	}
	
	public void showPrijemInput() throws IOException{
		Stage stage = new Stage();
    	Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/pokladnica/poklPrijem.fxml")));
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Príjem do pokladnice");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.showAndWait();
    	
    	sumaVPokladnici = Database.getStavPokladnice(aktualnyUser.getId());
    	
    	if (sumaVPokladnici > Database.getShopSettings().getPoklMaxSuma()) {
			notifyMaxSumaPokladnica();
		}
	}
	
	public void showVydajInput() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklVydaj.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Výdaj z pokladnice");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.showAndWait();
    	
    	sumaVPokladnici = Database.getStavPokladnice(aktualnyUser.getId());
    	
    	if (sumaVPokladnici < Database.getShopSettings().getPoklPociatocnaSuma()) {
			notifyMinSumaPokladnica();
		}
	}
	
	public void showUzavierka() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklUzavierkaVyber.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Výber závierky");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	CFXMLPoklZavierkaVyber cfxmlPoklYavierkaVyberController = fxmlLoader.getController();
    	cfxmlPoklYavierkaVyberController.setInfoNaZavierku(aktualnyUser, Database.getNakupy(aktualnyUser, new Date()));
    	stage.showAndWait();
    	if (cfxmlPoklYavierkaVyberController.isZavierkaVykonana()) {
    		if (bScanner != null) {
				bScanner.closePort();
			}
			getDataFromAndroidPokladnik.terminate();
	        Stage thisStage = (Stage) jlDate.getScene().getWindow();
	        thisStage.close();
	        Database.setPokladnicaClose(aktualnyUser.getId(), tatoPokladnica.getIdPokladnice());
	        Stage stageLogin = new Stage();
	        Scene sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/login.fxml")));
	        new JMetro(sceneLogin,Style.LIGHT);
	        stageLogin.setScene(sceneLogin);
	        stageLogin.initStyle(StageStyle.UNDECORATED);
	        stageLogin.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	        stageLogin.setResizable(false);
	        stageLogin.show();
		}
	}
	
	public void showZrusitNakup(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Zrušiť aktuálny nákup");
		alert.setHeaderText("Zrušenie nákupu");
		alert.setContentText("Chcete naozaj zrušiť aktuálny nákup?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
			alert.close();
			jtNakup.getItems().clear();
			cenaNakupu = 0;
			jlSuma.setText("0,00");
			vbHmotnost.setDisable(true);
			vbMnozstvo.setDisable(true);
		}
		else {
			alert.close();
		}
	}
	
	public void showDeleteItem(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Stornovanie nákupnej položky");
		alert.setHeaderText("Stornovanie položky");
		alert.setContentText("Chcete naozaj stornovať označenú\npoložku?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
			alert.close();
			if (jtNakup.getSelectionModel().getSelectedIndex() != -1) {
				jtNakup.getItems().remove(jtNakup.getSelectionModel().getSelectedIndex());
			}
		}
		else {
			alert.close();
		}
	}
	
	public void showStorno() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklStornoInputDoklad.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Stornovanie položky nákupu");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    CFXMLPoklStornoInputDoklad cfxmlPoklStornoInputDokladController = fxmlLoader.getController();
	    cfxmlPoklStornoInputDokladController.setAktualnyPokladnik(aktualnyUser);
    	stage.show();
	}

	public void showImport() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklImportNakupu.fxml"));
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);
		new JMetro(scene,Style.LIGHT);
		stage.setScene(scene);
		stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		stage.setTitle("Import nákupu z telefónu");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		CFXMLPoklImportNakupu cfxmlPoklImportNakupu = fxmlLoader.getController();
		if (bScanner != null){
			bScanner.removeListener(addTovarScannerListener);
			cfxmlPoklImportNakupu.setScanner(bScanner);
		}
		getDataFromAndroidPokladnik.removeListener(addTovarAppListener);
		cfxmlPoklImportNakupu.setApp(getDataFromAndroidPokladnik);
		stage.showAndWait();
		if (bScanner != null){
			bScanner.addListener(addTovarScannerListener);
		}
		getDataFromAndroidPokladnik.addListener(addTovarAppListener);
		List<NakupenyTovar> nakupeneTovary = cfxmlPoklImportNakupu.getNakup();
		if (!nakupeneTovary.isEmpty()){
			addPolozkyToNakup(nakupeneTovary);
		}
	}
	
	private void setLabelText(String text,Label label) {
		label.setText(text);
	}
	
	private void nameUser() {
		 setLabelText(aktualnyUser.getUserMeno() + " " + aktualnyUser.getUserPriezvisko(),jlUser);
	}
	
	public void showDate() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			Date time = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(time);
			setLabelText(timeFormat.format(time),jlTime);
			setLabelText(dateFormat.format(time),jlDate);
			setLabelText(Utils.getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)),jlDay);
		}),new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}

}
