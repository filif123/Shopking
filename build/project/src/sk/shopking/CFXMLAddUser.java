/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * Táto trieda je kontrolér pre okno pridania používateľa.
 * @author Filip
 *
 */
public class CFXMLAddUser implements Initializable{
	
	@FXML private TextField jtfMeno;
	@FXML private TextField jtfPriezvisko;
	@FXML private TextField jtfUsername;
	@FXML private ChoiceBox<UserType> jcbUsertype;
	
	private String userMeno,userPriezvisko,username,userHeslo;
	private UserType userType;
	
	private List<User> allUsers;
	private User novyUser;

    public void initialize(URL url, ResourceBundle rb){
    	jcbUsertype.getItems().setAll(UserType.values());
    }
    
    @FXML
   	private void jbHesloAction(ActionEvent event) throws IOException {
    	Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/passwordChange.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Scene scene = new Scene(root);
	    CFXMLPasswordChange passwordChangeController = fxmlLoader.getController();
	    new JMetro(scene,Style.LIGHT);
	    stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
	    stage.setScene(scene);
	    stage.setTitle("Zmena hesla");
	    stage.setResizable(false);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.showAndWait();
	    
	    userHeslo = passwordChangeController.getNewPassword();
    }
       
    @FXML
   	private void jbSaveAction(ActionEvent event) throws IOException {
    	userMeno = jtfMeno.getText();
    	userPriezvisko= jtfPriezvisko.getText();
    	username = jtfUsername.getText();
    	
   		
   		if(userMeno == null || userMeno.isEmpty()) {
   			Alert alert = new Alert(AlertType.ERROR);
   			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
           	alert.setTitle("Zlé parametre");
           	alert.setHeaderText("Nebolo zadané žiadne meno používateľa");
           	alert.setContentText("Zadajte meno používateľa!");
           	alert.show();
   		}
   		else if(userPriezvisko == null || userPriezvisko.isEmpty()) {
   			Alert alert = new Alert(AlertType.ERROR);
   			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
           	alert.setTitle("Zlé parametre");
           	alert.setHeaderText("Nebolo zadané žiadne priezvisko používateľa");
           	alert.setContentText("Zadajte priezvisko používateľa!");
           	alert.show();
   		}
   		else if(username == null || username.isEmpty()) {
   			Alert alert = new Alert(AlertType.ERROR);
   			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
           	alert.setTitle("Zlé parametre");
           	alert.setHeaderText("Nebolo zadané žiadne meno používateľa");
           	alert.setContentText("Zadajte používateľské meno používateľa!");
           	alert.show();
   		}
   		else if(userHeslo == null) {
   			Alert alert = new Alert(AlertType.ERROR);
   			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
           	alert.setTitle("Zlé parametre");
           	alert.setHeaderText("Nebolo zadané žiadne heslo používateľa");
           	alert.setContentText("Zadajte heslo používateľa!");
           	alert.show();
   		}
   		else {
   			userType = jcbUsertype.getValue();
   			
   			for (User user : allUsers) {

				if (username.equals(user.getUserNickname())) {
					Alert alert = new Alert(AlertType.ERROR);
					new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
		        	alert.setTitle("Duplicitný používateľ");
		        	alert.setHeaderText("Zadaný používateľ sa už v zozname nachádza");
		        	alert.setContentText("Opravte zadaného používateľa (zmente použ. meno)!");
		        	alert.show();
		        	return;
				}
			}
   			
   			Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
		}
    }
       
    @FXML
   	private void jbStornoAction(ActionEvent event) throws IOException {
    	   Node source = (Node) event.getSource();
           Stage stage = (Stage) source.getScene().getWindow();
           stage.close();
    }
       
       public User getNewUser(){
   		try {
   			if (userType.equals(UserType.ADMIN)) {
   				novyUser = new UserAdministrator(0,userMeno,userPriezvisko,username,User.hashPassword(userHeslo));
			}
   			else {
   				novyUser = new UserPokladnik(0,userMeno,userPriezvisko,username,User.hashPassword(userHeslo),0);
			}
   			
   		}catch (Exception e) {
   			novyUser = null;
   		}
   		
   		return novyUser;
   	}
   	
   	public void setAllUses(List<User> allUsers) {
   		this.allUsers = allUsers;
   	}
}
