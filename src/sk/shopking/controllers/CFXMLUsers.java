package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.User;
import sk.shopking.tools.Database;

/**
 * Táto trieda je kontrolér pre okno zoznamu používateľov.
 * @author Filip
 *
 */
public class CFXMLUsers implements Initializable{
	
	@FXML private Parent homeMenu;
	@FXML private CFXMLHlavneMenu homeMenuController;
	
	private List<User> usersFromDB = Database.getUsers();
	private final List<User> newUsersToExport = new ArrayList<>();
	private final List<User> updatedUsersToExport = new ArrayList<>();
	private final List<User> deletedUsersToExport = new ArrayList<>();
	
	@FXML private TableView<User> jtUsers;
	@FXML private TableColumn<User,String> cMeno;
	@FXML private TableColumn<User,String> cPriezvisko;
	@FXML private TableColumn<User,String> cNickname;
	@FXML private TableColumn<User,String> cUserType;
	
	private void setCellValueTable() {
		cMeno.setCellValueFactory(new PropertyValueFactory<>("meno"));
		cPriezvisko.setCellValueFactory(new PropertyValueFactory<>("priezvisko"));
		cNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
		cUserType.setCellValueFactory(new PropertyValueFactory<>("usertype"));
	}
	
	private void fillTable() {
		
		for (int i = 0 ; i < usersFromDB.size() ; i++) {
			if(usersFromDB.get(i).getUserMeno().equals("Administrátor")){
				usersFromDB.remove(i);
			}	
		}
		jtUsers.getItems().addAll(usersFromDB);
		
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		setCellValueTable();
		fillTable();
		homeMenuController.colorButton("jbUsers");
		jtUsers.setPlaceholder(new Label("Žiadni používatelia na zobrazenie"));
		jtUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		jtUsers.setOnMouseClicked(mouseEvent -> {
			if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
				if(mouseEvent.getClickCount() == 2){
					try {
						editJedenUser();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
    }
	
	@FXML
	private void jbAddUserAction(ActionEvent event) throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/addUser.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Pridať používateľa");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    
	    CFXMLAddUser addUserController = fxmlLoader.getController();
	    addUserController.setAllUses(jtUsers.getItems());
	    stage.showAndWait();
	    User newUser = addUserController.getNewUser();
	    if (newUser != null) {
	    	jtUsers.getItems().add(newUser);
	    	jtUsers.getSelectionModel().clearSelection();
	    	jtUsers.getSelectionModel().selectLast();
	    	newUsersToExport.add(newUser);
		}

	}
	
	@FXML
	private void jbDeleteUserAction(ActionEvent event) {
		int index = jtUsers.getSelectionModel().getSelectedIndex();
		if(!(index == -1)) {
			deletedUsersToExport.add(jtUsers.getItems().get(index));
			jtUsers.getItems().remove(index);
			
		}
		
	}
	
	@FXML
	private void jbEditUserAction(ActionEvent event) throws IOException{
		editViacUsers();

	}
	
	@FXML
	private void jbSearchAction(ActionEvent event) throws IOException{
		jtUsers.getSelectionModel().clearSelection();
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/searchUser.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    CFXMLSearchInUser searchUserController = fxmlLoader.getController();
	    searchUserController.setList(jtUsers.getItems());
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Hľadať");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.showAndWait();
	    List<Integer> indexy = searchUserController.returnIndexy();
	    
	    if (!indexy.isEmpty()) {
			for (Integer integer : indexy) {
				jtUsers.getSelectionModel().select(integer);
			}
		}

	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) {
		if (!newUsersToExport.isEmpty()) {
			Database.addUsers(newUsersToExport);
			newUsersToExport.clear();
		}
		if (!updatedUsersToExport.isEmpty()) {
			Database.updateUsers(updatedUsersToExport);
			updatedUsersToExport.clear();
		}
		if (!deletedUsersToExport.isEmpty()) {
			Database.deleteUsers(deletedUsersToExport);
			deletedUsersToExport.clear();
		}
		
	}
	
	@FXML
	private void jbObnovitAction(ActionEvent event) {
		usersFromDB = Database.getUsers();
		for (int i = 0; i < usersFromDB.size(); i++) {
			if (usersFromDB.get(i).getUserNickname().equals("admin")) {
				usersFromDB.remove(i);
				break;
			}
		}
		jtUsers.getItems().setAll(usersFromDB);
	}
	
	private void editJedenUser() throws IOException{
		List<User> allUsers = jtUsers.getItems();
		int indexUpravovanehoUsera = jtUsers.getSelectionModel().getSelectedIndex();
		
		if (indexUpravovanehoUsera == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiaden vybraný používateľ");
        	alert.setHeaderText("Nebol vybratý žiaden používateľ/používatelia");
        	alert.setContentText("Vyberte min. 1 používateľa!");
        	alert.show();
        	return;
		}

		User upravovanyUser = allUsers.get(indexUpravovanehoUsera);
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/editUser.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Upraviť používateľa");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);

	    CFXMLEditUser editUserController = fxmlLoader.getController();
	    editUserController.setAllUsers(allUsers);
	    editUserController.setAktualnyUser(upravovanyUser);
	    
	    stage.showAndWait();
	    User editedUser = editUserController.getEditedUser();
	    if (editedUser != null) {
	    	updatedUsersToExport.add(editedUser);
	    	jtUsers.getItems().remove(indexUpravovanehoUsera);
	    	jtUsers.getItems().add(indexUpravovanehoUsera, editedUser);
	    	jtUsers.getSelectionModel().clearSelection();
	    	jtUsers.getSelectionModel().select(indexUpravovanehoUsera);
		}
	}
	
	private void editViacUsers() throws IOException{
		
		List<User> allUsers = jtUsers.getItems();
		List<Integer> indexyUpravovanychUserov = jtUsers.getSelectionModel().getSelectedIndices();
		ObservableList<User> upraveneUsers = FXCollections.observableArrayList();
		upraveneUsers.addAll(allUsers);
		
		if (indexyUpravovanychUserov.size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiaden vybraný používateľ");
        	alert.setHeaderText("Nebol vybratý žiaden používateľ/používatelia");
        	alert.setContentText("Vyberte min. 1 používateľa!");
        	alert.show();
        	return;
		}

		for (Integer indexUpravovanehoTovaru : indexyUpravovanychUserov) {

			User upravovanyUser = jtUsers.getItems().get(indexUpravovanehoTovaru);
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/editUser.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			new JMetro(scene, Style.LIGHT);
			stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
			stage.setScene(scene);
			stage.setTitle("Upraviť používateľa");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);

			CFXMLEditUser editUserController = fxmlLoader.getController();
			editUserController.setAllUsers(upraveneUsers);
			editUserController.setAktualnyUser(upravovanyUser);
			stage.showAndWait();

			User editedUser = editUserController.getEditedUser();
			if (editedUser != null) {
				updatedUsersToExport.add(editedUser);
				upraveneUsers.set(indexUpravovanehoTovaru, editedUser);
			}
		}

		jtUsers.setItems(upraveneUsers);
		for (Integer index : indexyUpravovanychUserov) {
			jtUsers.getSelectionModel().select(index);
		}
	}
}
