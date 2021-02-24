package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.ApplicationInfo;
import sk.shopking.User;
import sk.shopking.UserAdministrator;
import sk.shopking.UserPokladnik;
import sk.shopking.tools.Database;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.Utils;

/**
 * Táto trieda je kontrolér pre okno prihlasovania používateľov.
 * @author Filip
 *
 */
public class CFXMLLogin implements Initializable {
	
	private static User zhoda;
	
    @FXML
    private Button jbLogin;
    
    @FXML
    private Button jbExit;
    
    @FXML
    private TextField jtfUsername;
    
    @FXML
    private PasswordField jtfPassword;
    
    @FXML
    private void jbLoginAction (ActionEvent event) throws IOException {
    	CFXMLLogin.zhoda = null;
        String actualUsername = jtfUsername.getText();
        String actualPassword = jtfPassword.getText();
        
        if(actualUsername.isEmpty() || actualPassword.isEmpty()) {
        	Alert alert = new Alert(AlertType.ERROR);
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
        	alert.setTitle("Chyba prihlásenia");
        	alert.setHeaderText("Žiadne zadané prihlasovacie meno alebo heslo");
        	alert.setContentText("Zadajte prihlasovacie údaje!");
        	alert.show();
        }
        else {
			CFXMLLogin.zhoda = Database.loginUser(actualUsername,actualPassword);

        	if(CFXMLLogin.zhoda != null) {
        		if(zhoda.getClass().equals(UserAdministrator.class)) {
                	Stage stage = new Stage();
                	Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/homeAdmin.fxml")));
                	new JMetro(scene,Style.LIGHT);
                	stage.setScene(scene);
                	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
                	stage.setTitle(ApplicationInfo.APP_NAME + " - " + zhoda.getUserNickname() + " - Domov");
                	stage.setMaximized(true);
                	stage.setOnCloseRequest(we -> {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
						alert.setTitle("Ukončenie programu");
						alert.setHeaderText("Ukončenie programu");
						alert.setContentText("Chcete naozaj ukončiť program ?");
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
					FXMLTools.closeWindow(event);
                }
        		else if(zhoda.getClass().equals(UserPokladnik.class)) {
        			if (Utils.isSameDay(Database.pokladnikVykonalZavierku((UserPokladnik) zhoda), new Date())) {
        				Alert alert = new Alert(AlertType.ERROR);
        	        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	        	new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        	        	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
        	        	alert.setTitle("Používateľ už dnes vykonal dennú závierku");
        	        	alert.setHeaderText("Chystáte sa prihlásiť do konta pokladníka,\nktorý dnes vykonal dennú závierku");
        	        	alert.setContentText("Dnes už nemáte povolené prihlásiť sa pod toto konto.");
        	        	alert.show();
        	        	jtfPassword.setText("");
						return;
					}
        			Stage stage = new Stage();
        			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/pokladnica.fxml"));
        		    Parent root = fxmlLoader.load();
        		    Scene scene = new Scene(root);
                	CFXMLHomePokladnik cfxmlHomePokladnikController = fxmlLoader.getController();
                	new JMetro(scene,Style.LIGHT);
                	stage.setScene(scene);
                	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
                	stage.setTitle(ApplicationInfo.APP_NAME + " - " + zhoda.getUserNickname() + " - Domov");
                	stage.setMaximized(true);
                	stage.setOnCloseRequest(we -> {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
						alert.setTitle("Ukončenie programu");
						alert.setHeaderText("Ukončenie programu");
						alert.setContentText("Chcete naozaj ukončiť program ?");
						Optional<ButtonType> vysledok = alert.showAndWait();
						if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
							cfxmlHomePokladnikController.closePort();
							Database.setPokladnicaClose(zhoda.getId(), cfxmlHomePokladnikController.getPokladnica().getIdPokladnice());
							alert.close();
							stage.close();
						}
						else {
							alert.close();
							we.consume();
						}
					});
                	/*
                	 * pre jednoduche klavesove skratky
                	 */
                	scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                		
                		final KeyCode keyStavPokladnice = 	KeyCode.S;
                		final KeyCode keyPrijem1 = 			KeyCode.PLUS;
                		final KeyCode keyPrijem2 = 			KeyCode.ADD;
                		final KeyCode keyVydaj1 = 			KeyCode.MINUS;
                		final KeyCode keyVydaj2 = 			KeyCode.SUBTRACT;
                		final KeyCode keyUzavierka = 		KeyCode.U;
                		
                		final KeyCode keyZoznam = 			KeyCode.L;
                		final KeyCode keyEAN = 				KeyCode.E;
                		final KeyCode keyPLU = 				KeyCode.P;
						final KeyCode keyScanInfo = 		KeyCode.I;
                		
                		final KeyCode keyMnozstvo = 		KeyCode.M;
                		final KeyCode keyHmotnost = 		KeyCode.H;
                		
                		final KeyCode keyZrusitNakup = 		KeyCode.Z;
                		final KeyCode keyDeleteItem = 		KeyCode.DELETE;
                		final KeyCode keyStorno = 			KeyCode.Q;
						final KeyCode keyImport = 			KeyCode.T;
                		
                		final KeyCode keyHotovost = 		KeyCode.O;
                		final KeyCode keyStravneListky = 	KeyCode.C;

						final KeyCode keyFullscreen = 		KeyCode.F11;
                		
        				@Override
						public void handle(KeyEvent ke) {
        					
        					KeyCode key = ke.getCode();
        					if (key == keyStavPokladnice) {
        						try {
        							cfxmlHomePokladnikController.showStavPokladnice();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyPrijem1 || key == keyPrijem2) {
        						try {
        							cfxmlHomePokladnikController.showPrijemInput();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyVydaj1 || key == keyVydaj2) {
        						try {
        							cfxmlHomePokladnikController.showVydajInput();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyUzavierka) {
        						try {
        							cfxmlHomePokladnikController.showUzavierka();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyZoznam) {
        						try {
        							cfxmlHomePokladnikController.showZoznam();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyEAN) {
        						try {
        							cfxmlHomePokladnikController.showEANInput();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyPLU) {
        						try {
        							cfxmlHomePokladnikController.showPLUInput();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
							else if(key == keyScanInfo) {
								try {
									cfxmlHomePokladnikController.showScanInfo();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
        					else if(key == keyMnozstvo) {
        						try {
        							if (cfxmlHomePokladnikController.isNotEmptyList()) {
        								if (cfxmlHomePokladnikController.isTovarSMnozstvom()) {
        									cfxmlHomePokladnikController.showMnozstvoInput();
										}
        								
									}
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyHmotnost) {
        						try {
        							if (cfxmlHomePokladnikController.isNotEmptyList()) {
        								if (!cfxmlHomePokladnikController.isTovarSMnozstvom()) {
        									cfxmlHomePokladnikController.showHmotnostInput();
										}
									}
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyZrusitNakup) {
								cfxmlHomePokladnikController.showZrusitNakup();
							}
        					else if(key == keyDeleteItem) {
								if (cfxmlHomePokladnikController.isNotEmptyList()) {
									cfxmlHomePokladnikController.showDeleteItem();
								}
							}
        					else if(key == keyStorno) {
        						try {
        							cfxmlHomePokladnikController.showStorno();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
							else if(key == keyImport) {
								try {
									cfxmlHomePokladnikController.showImport();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
        					else if(key == keyHotovost) {
        						try {
        							if (cfxmlHomePokladnikController.isNotEmptyList()) {
        								cfxmlHomePokladnikController.showHotovostInput();
        							}
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyStravneListky) {
        						try {
        							if (cfxmlHomePokladnikController.isNotEmptyList()) {
        								cfxmlHomePokladnikController.showStravneListkyInput();
        							}
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
							else if(key == keyFullscreen) {
								if (stage.isFullScreen()) {
									stage.setFullScreen(false);
								}
								else{
									stage.setFullScreenExitHint("Stlačte ESC pre ukončenie zobrazenia celoobrazového režimu");
									stage.setFullScreen(true);
								}
							}
        				}
        			});
                	/*
                	 * pre kombinovane klavesove skratky
                	 */
                	scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                		
        				final KeyCombination keyOdhlasit = new KeyCodeCombination(KeyCode.F5, KeyCombination.ALT_DOWN);
        				final KeyCombination keyScreenSave = new KeyCodeCombination(KeyCode.F6, KeyCombination.ALT_DOWN);
        				
        				@Override
						public void handle(KeyEvent ke) {
        					//klavesova skratka pre odhlasenie 
        					if (keyOdhlasit.match(ke)) {
        						Alert alert = new Alert(AlertType.CONFIRMATION);
        						new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        						alert.setTitle("Odhlásenie");
        						alert.setHeaderText("Odhlásenie");
        						alert.setContentText("Chcete sa naozaj odhlásiť ?");
        						Optional<ButtonType> vysledok = alert.showAndWait();
        						if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
        							alert.close();
        					        stage.close();
        							try {
        								Stage stageLogin = new Stage();
        								Scene sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/login.fxml")));
        								new JMetro(sceneLogin,Style.LIGHT);
        								stageLogin.setScene(sceneLogin);
        								stageLogin.initStyle(StageStyle.UNDECORATED);
        								stageLogin.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
        								stageLogin.setResizable(false);
        								stageLogin.show();
        							} catch (IOException e) {
        								e.printStackTrace();
        							}
        					        
        						}
        						else {
        							alert.close();
        						}
        					}
        					//klavesova skratka pre setric obrazovky
        					else if (keyScreenSave.match(ke)) {
        						Alert alert = new Alert(AlertType.CONFIRMATION);
        						new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        						alert.setTitle("Odhlásenie");
        						alert.setHeaderText("Odhlásenie");
        						alert.setContentText("Chcete sa naozaj odhlásiť ?");
        						Optional<ButtonType> vysledok = alert.showAndWait();
        						if(vysledok.isPresent() && vysledok.get() == ButtonType.OK) {
        							cfxmlHomePokladnikController.closePort();
        							alert.close();
        							stage.close();
									try {
										Stage stageSaver = new Stage();
										Scene sceneSaver = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/screenSaver.fxml")));
										sceneSaver.addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>() {
	        								
	        								@Override
	        								public void handle(KeyEvent event) {
	        									if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
	        										stageSaver.close();
	        										try {
	        											Stage stageLogin = new Stage();
	        											Scene sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/login.fxml")));
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
										stageSaver.setScene(sceneSaver);
										stageSaver.setFullScreen(true);
										stageSaver.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
										stageSaver.initStyle(StageStyle.UNDECORATED);
										stageSaver.setResizable(false);
										stageSaver.setOnCloseRequest(we -> {
											Alert alert1 = new Alert(AlertType.CONFIRMATION);
											new JMetro(alert1.getDialogPane().getScene(),Style.LIGHT);
											alert1.setTitle("Ukončenie programu");
											alert1.setHeaderText("Ukončenie programu");
											alert1.setContentText("Chcete naozaj ukončiť program ?");
											Optional<ButtonType> vysledok1 = alert1.showAndWait();
											if(vysledok1.isPresent() && vysledok1.get() == ButtonType.OK) {
												cfxmlHomePokladnikController.closePort();
												alert1.close();
												stageSaver.close();
											}
											else {
												alert1.close();
												we.consume();
											}
										});
										stageSaver.show();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
        					        
        					        
        						}
        						else {
        							alert.close();
        						}
        					}
        				}
        			});
                	stage.show();
					FXMLTools.closeWindow(event);
                }
        	}
        	else {
        		jtfPassword.setText("");
        		Alert alert = new Alert(AlertType.ERROR);
        		new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
            	alert.setTitle("Chyba prihlásenia");
            	alert.setHeaderText("Zle zadané prihlasovacie meno alebo heslo");
            	alert.setContentText("Skontrolujte si zadané údaje!");
            	alert.show();
        	}	
        } 
    }
    
    public static User getUser() {
    	return getZhoda();
    }
    
    @FXML
    private void jbExitAction (ActionEvent event) {
		FXMLTools.closeWindow(event);
    }
    

	@Override
    public void initialize(URL url, ResourceBundle rb) {
		TextFormatter<?> formatUsername = new TextFormatter<>((TextFormatter.Change zmena) -> {
		    String text = zmena.getText();
		    if (!text.isEmpty()) {
		        String newText = text.replace(" ", "").toLowerCase();
		        int vynechanaPozicia = zmena.getCaretPosition() - text.length() + newText.length();
		        zmena.setText(newText);
		        zmena.selectRange( vynechanaPozicia,vynechanaPozicia);
		    }
		    return zmena;
		});
		TextFormatter<?> formatPassword = new TextFormatter<>((TextFormatter.Change zmena) -> {
		    String text = zmena.getText();
		    if (!text.isEmpty()) {
		        String newText = text.replace(" ", "").toLowerCase();
		        int vynechanaPozicia = zmena.getCaretPosition() - text.length() + newText.length();
		        zmena.setText(newText);
		        zmena.selectRange( vynechanaPozicia,vynechanaPozicia);
		    }
		    return zmena;
		});
		jtfUsername.setTextFormatter(formatUsername);
		jtfPassword.setTextFormatter(formatPassword);
    }

	public static User getZhoda() {
		return zhoda;
	}    
    
}
