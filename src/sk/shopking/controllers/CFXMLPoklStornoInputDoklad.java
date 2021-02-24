package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.Doklad;
import sk.shopking.UserPokladnik;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;

/**
 * @author Filip
 *
 */
public class CFXMLPoklStornoInputDoklad implements Initializable{

	@FXML private TextField jtfDokladID;
	@FXML private Label jlStav;
	
	private final List<Doklad> vsetkyDoklady = Database.getDoklady();
	private Doklad vybranyDoklad = null;
	private UserPokladnik pokladnik;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jtfDokladID.textProperty().addListener((observable, oldValue, newValue) -> {
			vybranyDoklad = null;
			if (!newValue.matches("\\d*")) {
				jtfDokladID.setText(newValue.replaceAll("[^\\d]", ""));
			}
			else if (newValue.isEmpty()) {
				jlStav.setText("Zadajte číslo dokladu");
				jlStav.setStyle("");
			}
			else {
				for (Doklad doklad : vsetkyDoklady) {
					if (doklad.getIdDoklad() == Integer.parseInt(newValue)) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
						jlStav.setText("Nájdené - " + dateFormat.format(doklad.getCasNakupu()));
						jlStav.setStyle("-fx-text-fill: green;");
						vybranyDoklad = doklad;
						break;
					}
				}
				if (vybranyDoklad == null) {
					jlStav.setText("Nenájdené");
					jlStav.setStyle("-fx-text-fill: red;");
				}
			}
		});
		
	}
	
	@FXML
	private void jbOKAction(ActionEvent event) throws IOException {
		if (vybranyDoklad != null) {
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/poklStornoVyber.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			CFXMLPoklStornoVyber stornoVyberController = fxmlLoader.getController();
			new JMetro(scene,Style.LIGHT);
			stage.setScene(scene);
			stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			stage.setTitle("Stornovanie položiek nákupu");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stornoVyberController.setInfoNaStorno(vybranyDoklad, pokladnik);
			scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			    final KeyCombination keyComb = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);
			    public void handle(KeyEvent ke) {
			    	if (keyComb.match(ke)) {
			    		stornoVyberController.prepareToStorno();
			    		ke.consume();
			    	}
			    }
			});
			stage.show();
		}

		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	public void setAktualnyPokladnik(UserPokladnik pokladnik) {
		this.pokladnik = pokladnik;
	}

}
