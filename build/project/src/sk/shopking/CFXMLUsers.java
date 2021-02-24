/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
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
	private List<User> newUsersToExport = new ArrayList<User>();
	private List<User> updatedUsersToExport = new ArrayList<User>();
	private List<User> deletedUsersToExport = new ArrayList<User>();
	
	@FXML
	private Button jbDeleteUser;
	
	@FXML
	private Button jbEditUser;
	
	@FXML
	private Button jbAddUser;
	
	@FXML
	private TableView<User> jtUsers;
	
	@FXML
	private TableColumn<User,String> cMeno;
	
	@FXML
	private TableColumn<User,String> cPriezvisko;
	
	@FXML
	private TableColumn<User,String> cNickname;
	
	@FXML
	private TableColumn<User,String> cUserType;
	
	//private User admin;
	
	private void setCellValueTable() {
		cMeno.setCellValueFactory(new PropertyValueFactory<User, String>("meno"));
		cPriezvisko.setCellValueFactory(new PropertyValueFactory<User, String>("priezvisko"));
		cNickname.setCellValueFactory(new PropertyValueFactory<User, String>("nickname"));
		cUserType.setCellValueFactory(new PropertyValueFactory<User, String>("usertype"));
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
		jtUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		jtUsers.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	try {
		            		editJedenUser();
						} catch (IOException e) {
							e.printStackTrace();
						}
		            }
		        }
		    }
		});
    }
	
	@FXML
	private void jbAddUserAction(ActionEvent event) throws IOException{
		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/addUser.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
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
	private void jbDeleteUserAction(ActionEvent event) throws IOException{
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
		

	}
	
	@FXML
	private void jbSaveAction(ActionEvent event) throws IOException{
		if (newUsersToExport != null && !newUsersToExport.isEmpty()) {
			Database.addUsers(newUsersToExport);
		}
		if (updatedUsersToExport != null && !updatedUsersToExport.isEmpty()) {
			Database.updateUsers(updatedUsersToExport);
		}
		if (deletedUsersToExport != null && !deletedUsersToExport.isEmpty()) {
			Database.deleteUsers(deletedUsersToExport);
		}
		
	}
	
	@FXML
	private void jbObnovitAction(ActionEvent event) throws IOException{
		usersFromDB = Database.getUsers();
		jtUsers.getItems().setAll(usersFromDB);
	}
	
	private void editJedenUser() throws IOException{
		List<User> allUsers = jtUsers.getItems();
		Integer indexUpravovanehoUsera = jtUsers.getSelectionModel().getSelectedIndex();
		
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
	    Parent root = (Parent) fxmlLoader.load();
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
	    	jtUsers.getItems().remove((int)indexUpravovanehoUsera);
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
		
		if (indexyUpravovanychUserov.size() == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	alert.setTitle("Žiaden vybraný používateľ");
        	alert.setHeaderText("Nebol vybratý žiaden používateľ/používatelia");
        	alert.setContentText("Vyberte min. 1 používateľa!");
        	alert.show();
        	return;
		}
		
		for (int i = 0 ; i < indexyUpravovanychUserov.size() ; i++) {
			
			Integer indexUpravovanehoTovaru = indexyUpravovanychUserov.get(i);
			User upravovanyUser = jtUsers.getItems().get(indexUpravovanehoTovaru);
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/admin/editUser.fxml"));
		    Parent root = (Parent) fxmlLoader.load();
		    Scene scene = new Scene(root);
		    new JMetro(scene,Style.LIGHT);
		    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
		    stage.setScene(scene);
		    stage.setTitle("Upraviť používateľa");
		    stage.setResizable(false);
		    stage.initModality(Modality.APPLICATION_MODAL);

		    CFXMLEditUser editUserController = fxmlLoader.getController();
		    editUserController.setAllUsers(upraveneUsers);
		    editUserController.setAktualnyUser(upravovanyUser);		    
		    stage.showAndWait();
		    
		    User editedUser =  editUserController.getEditedUser();
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
