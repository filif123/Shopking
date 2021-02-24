/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.shopking;

import java.io.IOException;
import java.util.logging.Handler;

import com.sun.deploy.util.SystemUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.*;


/**
 * Táto trieda slúži na spustenie aplikácie.
 * @author Filip
 *
 */
public class ShopKing extends Application {
	private static final SKLog LOGGER = SKLog.getLogger();
	private static boolean isDBConnectionAvailable;
	private static boolean isAplicationStartedBefore;
	private static int isPropInvalid;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

    	VysielanieInfoOServeri vysielanieInfoOServeri = new VysielanieInfoOServeri();
		vysielanieInfoOServeri.start();
    	//AppSettings.createPropFile("COM3", "localhost", "C:\\Users\\filip\\Documents", "1","20");
    	isDBConnectionAvailable = Database.isConnectionAvailable();
    	isAplicationStartedBefore = ControlInstances.isApplicationStarted();
    	isPropInvalid = AppSettings.isInvalid();
    	LOGGER.info("Program " + ApplicationInfo.APP_NAME + " sa spusil");
        launch(args);
        LOGGER.info("Program " + ApplicationInfo.APP_NAME + " sa ukončuje");
        /*
         * https://stackoverflow.com/questions/2723280/why-is-my-program-creating-empty-lck-files
         */
        for(Handler h:LOGGER.getHandlers()){
            h.close();
        }
        vysielanieInfoOServeri.terminate();
        System.exit(0);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	if(isAplicationStartedBefore) {
    		Alert alert = new Alert(AlertType.INFORMATION);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	        alert.setTitle("Duplicitná inštancia"); 
	        alert.setHeaderText("Program " + ApplicationInfo.APP_NAME + " je už spustený");
	        alert.setContentText("Duplicitná inštancia bude ukončená.");
	        alert.show();
	        LOGGER.info("Program " + ApplicationInfo.APP_NAME + " je už spustený");
	        LOGGER.info("Duplicitná inštancia bude ukončená.");
    	}
    	else if (!isDBConnectionAvailable) {
    		Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	        alert.setTitle("Chyba pri spojení s databázou"); 
	        alert.setHeaderText("Vyskytla sa chyba pri pripájaní k databáze");
	        alert.setContentText("Skontrolujte pripojenie k internetu a k databáze!");
	        alert.show();
	        LOGGER.severe("Vyskytla sa chyba pri pripájaní k databáze");
	        LOGGER.severe("Skontrolujte pripojenie k internetu a k databáze!");
		}
    	else {
    		if (isPropInvalid == 1) {
    			Alert alert = new Alert(AlertType.ERROR);
    			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
    	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
            	alert.setTitle("Súbor " + AppSettings.FILE_SETTINGS + " nenájdený");
            	alert.setHeaderText("Súbor " + AppSettings.FILE_SETTINGS + " bol nesprávne upravený");
            	alert.setContentText("Skontrolujte a opravte obsahovovosť súboru.");
            	alert.showAndWait();
            	LOGGER.severe("Súbor " + AppSettings.FILE_SETTINGS + " bol nesprávne upravený");
    	        LOGGER.severe("Skontrolujte a opravte obsahovovosť súboru.");
			}
    		else if (isPropInvalid == 2) {
    			Alert alert = new Alert(AlertType.ERROR);
    			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
    	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
            	alert.setTitle("Súbor " + AppSettings.FILE_SETTINGS + " nenájdený");
            	alert.setHeaderText("Súbor " + AppSettings.FILE_SETTINGS + " sa nenašiel");
            	alert.setContentText("Skontrolujte, či súbor existuje");
            	alert.showAndWait();
            	LOGGER.severe("Súbor " + AppSettings.FILE_SETTINGS + " sa nenašiel");
    	        LOGGER.severe("Skontrolujte, či súbor existuje");
			}
    		else if (isPropInvalid == 3) {
    			Alert alert = new Alert(AlertType.ERROR);
    			Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
    	        stageAlert.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
    			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
            	alert.setTitle("Súbor " + AppSettings.FILE_SETTINGS + " nenájdený");
            	alert.setHeaderText("Súbor " + AppSettings.FILE_SETTINGS + " sa nenašiel alebo bol nesprávne upravený");
            	alert.setContentText("Skontrolujte, či súbor existuje a či je obsahovo správny.");
            	alert.showAndWait();
            	LOGGER.severe("Súbor " + AppSettings.FILE_SETTINGS + " sa nenašiel alebo bol nesprávne upravený");
    	        LOGGER.severe("Skontrolujte, či súbor existuje a či je obsahovo správny.");
			}
    		else {
    			Scene sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/login.fxml")));
        		new JMetro(sceneLogin,Style.LIGHT);
                stage.setScene(sceneLogin);
                stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
                stage.setTitle(ApplicationInfo.APP_NAME + " - Prihlásenie");
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
			}
    			
    	}
    	
    }
}
