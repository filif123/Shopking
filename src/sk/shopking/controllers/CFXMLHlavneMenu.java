package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.User;
import sk.shopking.tools.FXMLTools;

/**
 * Táto trieda je kontrolér pre podokno, ktoré sa zobrazuje na hlavnom okne pre administrátora.
 * @author Filip
 *
 */
public class CFXMLHlavneMenu implements Initializable{
	
	private User aktualnyUser;

	@FXML private VBox vbMain;
	
	@FXML private HBox jbHome;
	@FXML private HBox jbCategory;
	@FXML private HBox jbSklad;
	@FXML private HBox jbUsers;
	@FXML private HBox jbZavierky;
	@FXML private HBox jbPokladnice;
	@FXML private HBox jbStatistika;
	
	@FXML private HBox hbLoading;

	public void colorButton(String item) {
		switch (item) {
			case "jbHome":
				jbHome.setStyle("-fx-background-color: #000099");
				jbCategory.setStyle(null);
				jbSklad.setStyle(null);
				jbUsers.setStyle(null);
				jbZavierky.setStyle(null);
				jbPokladnice.setStyle(null);
				jbStatistika.setStyle(null);
				break;
			case "jbCategory":
				jbHome.setStyle(null);
				jbCategory.setStyle("-fx-background-color: #000099");
				jbSklad.setStyle(null);
				jbUsers.setStyle(null);
				jbZavierky.setStyle(null);
				jbPokladnice.setStyle(null);
				jbStatistika.setStyle(null);
				break;
			case "jbSklad":
				jbHome.setStyle(null);
				jbCategory.setStyle(null);
				jbSklad.setStyle("-fx-background-color: #000099");
				jbUsers.setStyle(null);
				jbZavierky.setStyle(null);
				jbPokladnice.setStyle(null);
				jbStatistika.setStyle(null);
				break;
			case "jbUsers":
				jbHome.setStyle(null);
				jbCategory.setStyle(null);
				jbSklad.setStyle(null);
				jbUsers.setStyle("-fx-background-color: #000099");
				jbZavierky.setStyle(null);
				jbPokladnice.setStyle(null);
				jbStatistika.setStyle(null);
				break;
			case "jbZavierky":
				jbHome.setStyle(null);
				jbCategory.setStyle(null);
				jbSklad.setStyle(null);
				jbUsers.setStyle(null);
				jbZavierky.setStyle("-fx-background-color: #000099");
				jbPokladnice.setStyle(null);
				jbStatistika.setStyle(null);
				break;
			case "jbPokladnice":
				jbHome.setStyle(null);
				jbCategory.setStyle(null);
				jbSklad.setStyle(null);
				jbUsers.setStyle(null);
				jbZavierky.setStyle(null);
				jbPokladnice.setStyle("-fx-background-color: #000099");
				jbStatistika.setStyle(null);
				break;
			case "jbStatistika":
				jbHome.setStyle(null);
				jbCategory.setStyle(null);
				jbSklad.setStyle(null);
				jbUsers.setStyle(null);
				jbZavierky.setStyle(null);
				jbPokladnice.setStyle(null);
				jbStatistika.setStyle("-fx-background-color: #000099");
				break;
			default:
				jbHome.setStyle(null);
				jbCategory.setStyle(null);
				jbSklad.setStyle(null);
				jbUsers.setStyle(null);
				jbZavierky.setStyle(null);
				jbPokladnice.setStyle(null);
				jbStatistika.setStyle(null);
				break;
		}
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		aktualnyUser = CFXMLLogin.getZhoda();
    }


	@FXML
	private void jbExitAction(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		alert.setTitle("Ukončenie programu");
		alert.setHeaderText("Ukončenie programu");
		alert.setContentText("Chcete naozaj ukončiť program ?");
		Optional<ButtonType> vysledok = alert.showAndWait();
		if( vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
			alert.close();
			FXMLTools.closeWindow(event);
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
			alert.close();
			Node source = (Node) event.getSource();
	        Stage stageLogin = (Stage) source.getScene().getWindow();
	        stageLogin.close();
	        Stage stage = new Stage();
	        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/login.fxml")));
	        new JMetro(scene,Style.LIGHT);
	        stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	        stage.setScene(scene);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setResizable(false);
	        stage.showAndWait();
		}
		else {
			alert.close();
		}
	}
	
	@FXML
	private void jbSettingsAction(MouseEvent event) throws IOException {
		Stage stage = new Stage();
	    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/nastaveniaAdmin.fxml")));
	    JMetro jm = new JMetro(scene,Style.LIGHT);
	    jm.setAutomaticallyColorPanes(true);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - Nastavenia");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.show();
	}
	
	@FXML
	private void jbInfoAction(MouseEvent event) throws IOException {
		Stage stage = new Stage();
	    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/information.fxml")));
	    JMetro jm = new JMetro(scene,Style.LIGHT);
	    jm.setAutomaticallyColorPanes(true);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - Informácie");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.show();
	}
	
	@FXML
	private void jbHomeAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/homeAdmin.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Domov");
	    stage.show();
	}
	
	@FXML
	private void jbCategoryAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/category.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Kategórie");
	    stage.show();
	}
	
	@FXML
	private void jbSkladAction (MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/sklad.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Tovary");
	    stage.show();
	}
	
	@FXML
	private void jbUsersAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/users.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Používatelia");
	    stage.show();
	}
	
	@FXML
	private void jbZavierkyAction(MouseEvent event) throws Exception {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();

		/*Stage stageLoading = new Stage();
		FXMLLoader fxmlLoaderLoading = new FXMLLoader(getClass().getResource("/resources/scenes/loading.fxml"));
		Parent rootLoading = fxmlLoaderLoading.load();
		Scene sceneLoading = new Scene(rootLoading);
		CFXMLLoadingWindowZavierky loadingWindowZavierkyController = fxmlLoaderLoading.getController();
		new JMetro(sceneLoading,Style.LIGHT);
		stageLoading.setScene(sceneLoading);
		stageLoading.initModality(Modality.APPLICATION_MODAL);
		stageLoading.setAlwaysOnTop(true);
		stageLoading.initOwner(stage);
		stageLoading.initStyle(StageStyle.UNDECORATED);
		stageLoading.show();*/

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/zavierky.fxml"));
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);
		new JMetro(scene,Style.LIGHT);
		stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		stage.setScene(scene);
		stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Závierky");
		CFXMLZavierky zavierkyController = fxmlLoader.getController();
		//zavierkyController.setZavierky(loadingWindowZavierkyController.getDenneZavierky(),loadingWindowZavierkyController.getMesacneZavierky(),loadingWindowZavierkyController.getRocneZavierky(),loadingWindowZavierkyController.getSkupinaDennychZavierok());
		stage.show();		
			  
	}
	
	@FXML
	private void jbPokladniceAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/pokladnice.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Pokladnice");
	    stage.show();
	}
	
	@FXML
	private void jbStatistikaAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/statistika.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Štatistika");
	    stage.show();
	}
}
