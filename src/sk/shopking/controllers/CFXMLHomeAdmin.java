package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.User;

/**
 * Táto trieda je kontrolér pre okno domovskej obrazovky administrátora.
 * @author Filip
 *
 */
public class CFXMLHomeAdmin implements Initializable{
	
	private final User aktualnyUser = CFXMLLogin.getZhoda();

	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	@FXML private Label jlVersion;

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
	private void jbSkladAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/sklad.fxml")));
	    new JMetro(scene,Style.LIGHT);
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
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Používatelia");
	    stage.show();
	}
	
	@FXML
	private void jbZavierkyAction(MouseEvent event){

		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		Scene scene = source.getScene();
		try {
			scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/zavierky.fxml")));
			new JMetro(scene,Style.LIGHT);
			stage.setScene(scene);
			stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Závierky");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void jbPokladniceAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/pokladnice.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Pokladnice");
	    stage.show();
	}
	
	@FXML
	private void jbCategoryAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/category.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Kategórie");
	    stage.show();
	}
	
	@FXML
	private void jbStatistikaAction(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = source.getScene();
	    scene.setRoot(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/statistika.fxml")));
	    new JMetro(scene,Style.LIGHT);
	    stage.setScene(scene);
	    stage.setTitle(ApplicationInfo.APP_NAME + " - " + aktualnyUser.getUserNickname() + " - Štatistika");
	    stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jlVersion.setText(ApplicationInfo.APP_VERSION);
		homeMenuController.colorButton("jbHome");
	}

}
