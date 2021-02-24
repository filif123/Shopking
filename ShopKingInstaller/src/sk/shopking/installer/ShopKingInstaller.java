package sk.shopking.installer;

import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;

/**
 * @author Filip
 *
 */
public class ShopKingInstaller extends Application{
	
	public static void main(String[] args) {
		
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Scene sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/instalacia/insWelcome.fxml")));
		stage.setScene(sceneLogin);
		new JMetro(sceneLogin,Style.LIGHT);
		stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		stage.setTitle(ApplicationInfo.APP_NAME + " - Inštalácia");
		stage.setResizable(false);
		stage.setOnCloseRequest(we -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			alert.setTitle("Ukončenie inštalácie");
			alert.setHeaderText("Inštalácia nebola ukončená");
			alert.setContentText("Chcete naozaj ukončiť inštaláciu programu ?");
			Optional<ButtonType> vysledok = alert.showAndWait();
			if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
				alert.close();
				stage.close();
			}
			else {
				alert.close();
				we.consume();
			}
		});
		stage.show();
	}
}
