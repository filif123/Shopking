/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.AppSettings;
import sk.shopking.tools.BarcodeScanner;
import sk.shopking.tools.BarcodeScannerListener;
import sk.shopking.tools.Database;
import sk.shopking.tools.ShopKingTools;

/**
 * Táto trieda je kontrolér pre okno domovskej obrazovky pokladníka.
 * @author Filip
 *
 */
public class CFXMLHomePokladnik implements Initializable {

	private UserPokladnik aktualnyUser = (UserPokladnik) CFXMLLogin.getZhoda();
	private List<Tovar> vsetkyTovary = Database.getTovary();
	private List<Pokladnica> vsetkyPokladnice = Database.getPokladnice();
	private Pokladnica tatoPokladnica;
	
	@FXML private Label jlUser;
	@FXML private Label jlPokladnicaNumber;
	@FXML private Label jlTime;
	@FXML private Label jlDate;
	@FXML private Label jlDay;
	
	@FXML private Label jlSuma;
	
	@FXML private TextField jtfInput;
	
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
        	return;
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
        	alert.setHeaderText("Číslo pokladne v súbore " + AppSettings.FILE_SETTINGS + " sa nezhoduje s databázov pokladní");
        	alert.setContentText("Skontrolujte obsahovú správnosť konfiguračného súboru!");
        	alert.showAndWait();
        	return;
		}
		else {
			jlPokladnicaNumber.setText("" + vybranaPokladnica);
			Database.setPokladnicaOpen(aktualnyUser.getId(), tatoPokladnica.getIdPokladnice());
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
		}
		else if (!BarcodeScanner.isPortAvailable(port)) {
			Alert alert = new Alert(AlertType.ERROR);
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
			bScanner.addListener(new BarcodeScannerListener() {
				
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
			});
		}
		
		vbHotovost.setDisable(true);
		vbStravneListky.setDisable(true);
		showDate();
		nameUser();
		setCellValueTable();
		jtNakup.getItems().addListener(new ListChangeListener<NakupenyTovar>(){

			@Override
			public void onChanged(Change<? extends NakupenyTovar> c) {	
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
			}	
		});
		
		jtNakup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<NakupenyTovar>() {

			@Override
			public void changed(ObservableValue<? extends NakupenyTovar> observable, NakupenyTovar oldValue, NakupenyTovar newValue) {
				try {
				
					jtNakup.getSelectionModel().select(newValue);
					//FIXME v tomto riadku dava NullPointerException
					if (newValue.getTovarJednotka().equals(JednotkaType.KS)) {
						vbMnozstvo.setDisable(false);
						vbHmotnost.setDisable(true);
					}
					else {
						vbMnozstvo.setDisable(true);
						vbHmotnost.setDisable(false);
					}
				} catch (NullPointerException e) {}
					
			}
		});
		this.tatoPokladnica.setPokladnikUser(aktualnyUser);
    }
	
	public void addPolozkaToNakupSHmotnostou(Tovar tovar) {
		try {
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklHmotnost.fxml"));
		    Parent root = (Parent) fxmlLoader.load();
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
	    		NakupenyTovar novyTovar = new NakupenyTovar(tovar.getTovarPLU(),tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), tovar.getTovarJednotkovaCena(), tovar.getTovarDPH(), hmotnost);
	    		jtNakup.getItems().add(novyTovar);
	    		jtNakup.getSelectionModel().selectLast();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPolozkaToNakup(Tovar tovar) {
		if (tovar.getTovarCategory().getCategoryPristupnePreMladistvych() == false && dospely == false) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			alert.setTitle("Upozornenie");
			alert.setHeaderText("Obmedzenie predaja nákupnej položky");
			alert.setContentText("Má občan viac ako 18 rokov ?");
			
			ButtonType buttonTypeYes = new ButtonType("Áno",ButtonData.YES);
			ButtonType buttonTypeNo = new ButtonType("Nie",ButtonData.NO);
			
			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			
			Optional<ButtonType> vysledok = alert.showAndWait();
			if(vysledok.get() == buttonTypeYes) {
				dospely = true;
				alert.close();
			}
			else {
				alert.close();
				return;
			}
		}
		
		for (int i = 0 ; i < jtNakup.getItems().size() ; i++) {
			//TODO miesto nazvu pouzit PLU
			if (jtNakup.getItems().get(i).getTovarName().equals(tovar.getTovarName())) {
				NakupenyTovar povodnaPolozka = jtNakup.getItems().get(i);
				if (povodnaPolozka.getTovarJednotka().equals(JednotkaType.KS)) {
					NakupenyTovar upravenaPolozka = new NakupenyTovar(povodnaPolozka.getTovarPLU(),povodnaPolozka.getTovarName(), povodnaPolozka.getTovarCategory(), povodnaPolozka.getTovarJednotka(), povodnaPolozka.getTovarEAN(), povodnaPolozka.getTovarJednotkovaCena(), povodnaPolozka.getTovarDPH(), povodnaPolozka.getNakupeneMnozstvo() + 1);
					jtNakup.getItems().set(i,upravenaPolozka);
					jtNakup.getSelectionModel().select(i);
					return;
				}
				else {
					try {
						Stage stage = new Stage();
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklHmotnost.fxml"));
					    Parent root = (Parent) fxmlLoader.load();
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
				    		NakupenyTovar novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(),vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), vybratyTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), hmotnost);
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
			jtNakup.getItems().add(new NakupenyTovar(tovar.getTovarPLU(),tovar.getTovarName(),tovar.getTovarCategory(),tovar.getTovarJednotka(),tovar.getTovarEAN(),tovar.getTovarJednotkovaCena(),tovar.getTovarDPH(),1));
			jtNakup.getSelectionModel().selectLast();
		}
		else {
			addPolozkaToNakupSHmotnostou(tovar);
		}
		
	}
	
	public Pokladnica getPokladnica() {
		return tatoPokladnica;
	}
	
	public void closePort() {
		if (bScanner != null) {
			bScanner.closePort();
		}
	}
	
	public boolean isEmptyList() {
		if (jtNakup.getItems().isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	private void setCellValueTable() {
		cNazovVyrobku.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("name"));
		cPLU.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("plu"));
		cEAN.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("ean"));
		cCena.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("cena"));
		cJednotkovaCena.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("jednotkovaCena"));
		cDPH.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("dph"));
		cMnozstvo.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("mnozstvo"));
		cJednotka.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("jednotka"));
		cAkcia.setCellValueFactory(new PropertyValueFactory<NakupenyTovar, String>("akcia"));
	}
	
	@FXML
	private void jbVypnutAction(MouseEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Ukončenie programu");
		alert.setHeaderText("Ukončenie programu");
		alert.setContentText("Chcete naozaj ukončiť program ?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.get() == ButtonType.OK) {
			alert.close();
			if (bScanner != null) {
				bScanner.closePort();
			}
			Node source = (Node) event.getSource();
	        Stage stage = (Stage) source.getScene().getWindow();
	        stage.close();
	        
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
		if(vysledok.get() == ButtonType.OK) {
			if (bScanner != null) {
				bScanner.closePort();
			}
			alert.close();
			Node source = (Node) event.getSource();
	        Stage stageLogin = (Stage) source.getScene().getWindow();
	        stageLogin.close();
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
		if(vysledok.get() == ButtonType.OK) {
			if (bScanner != null) {
				bScanner.closePort();
			}
			alert.close();
			Node source = (Node) event.getSource();
	        Stage stageLogin = (Stage) source.getScene().getWindow();
	        stageLogin.close();
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
							ShopKingTools.showExceptionDialog(e);
						}
					}	
				}
			});
	        stage.setScene(scene);
	        stage.setFullScreen(true);
	        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setResizable(false);
	        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        		public void handle(WindowEvent we) {
        			Alert alert = new Alert(AlertType.CONFIRMATION);
        			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
            		alert.setTitle("Ukončenie programu");
            		alert.setHeaderText("Ukončenie programu");
            		alert.setContentText("Chcete naozaj ukončiť program ?");
            		Optional<ButtonType> vysledok = alert.showAndWait();
            		if(vysledok.get() == ButtonType.OK) {
            			alert.close();
            	        stage.close();
            		}
            		else {
            			alert.close();
            			we.consume();
            		}
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
	private void jbMnozstvoAction(MouseEvent event) throws IOException {
		showMnozstvoInput();
	}
	
	@FXML
	private void jbHmotnostAction(MouseEvent event) throws IOException {
		showHmotnostInput();
	}
	
	@FXML
	private void jbAkciaAction(MouseEvent event) throws IOException {
		showAkciaInput();
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
	private void jbZrusitNakupAction(MouseEvent event) throws IOException {
		showZrusitNakup();
	}
	
	@FXML
	private void jbDeleteItemAction(MouseEvent event) throws IOException {
		showDeleteItem();
	}
	
	@FXML
	private void jbStornoAction(MouseEvent event) throws IOException {
		showStorno();
	}
	
	public void showZoznam() throws IOException {
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklZoznam.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
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
    		/*jtNakup.getSelectionModel().clearSelection();
			jtNakup.getSelectionModel().selectLast();*/
		}
	}
	
	public void showEANInput()  throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklEAN.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
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
	    Parent root = (Parent) fxmlLoader.load();
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
	
	public void showMnozstvoInput() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklMnozstvo.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
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
    		NakupenyTovar novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(),vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), vybratyTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), mnozstvo);
    		jtNakup.getItems().set(index, novyTovar);
		}
	}
	
	public void showHmotnostInput() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklHmotnost.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
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
    		NakupenyTovar novyTovar = new NakupenyTovar(vybratyTovar.getTovarPLU(),vybratyTovar.getTovarName(), vybratyTovar.getTovarCategory(), vybratyTovar.getTovarJednotka(), vybratyTovar.getTovarEAN(), vybratyTovar.getTovarJednotkovaCena(), vybratyTovar.getTovarDPH(), hmotnost);
    		jtNakup.getItems().set(index, novyTovar);
		}
	}
	
	public void showAkciaInput() throws IOException{
		Stage stage = new Stage();
    	Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/pokladnica/poklAkcia.fxml")));
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Zadať akciu na tovar");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.show();
	}
	
	public void showHotovostInput() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklHotovost.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
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
    	jtNakup.getItems().clear();
		cenaNakupu = 0;
		jlSuma.setText("0,00");
	}

	public void showStravneListkyInput() throws IOException{
		Stage stage = new Stage();
    	Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/pokladnica/poklStravneListky.fxml")));
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Stravné lístky");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.show();
	}
	
	public void showStavPokladnice() throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklStavPokladnice.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	CFXMLPoklStav cfxmlPoklStav = fxmlLoader.getController();
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Stav pokladnice");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.show();
    	cfxmlPoklStav.setUserSuma(aktualnyUser.getSumaPokladnica());
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
    	stage.show();
	}
	
	public void showVydajInput() throws IOException{
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
    	
    	cfxmlPoklVydaj.setUserSuma(aktualnyUser.getSumaPokladnica());
	}
	
	public void showUzavierka() throws IOException{
		
	}
	
	public void showZrusitNakup() throws IOException{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Zrušiť aktuálny nákup");
		alert.setHeaderText("Zrušenie nákupu");
		alert.setContentText("Chcete naozaj zrušiť aktuálny nákup?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.get() == ButtonType.OK) {
			alert.close();
			jtNakup.getItems().clear();
			cenaNakupu = 0;
			jlSuma.setText("0,00");
		}
		else {
			alert.close();
		}
	}
	
	public void showDeleteItem() throws IOException{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Stornovanie nákupnej položky");
		alert.setHeaderText("Stornovanie položky");
		alert.setContentText("Chcete naozaj stornovať označenú položku?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if(vysledok.get() == ButtonType.OK) {
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
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
    	new JMetro(scene,Style.LIGHT);
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    	stage.setTitle("Stornovanie položky nákupu");
    	stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
    	stage.show();
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
			setLabelText(ShopKingTools.getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)),jlDay);
		}),new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}

}
