/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.Database;
import sk.shopking.tools.ShopKingTools;

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
        	List<User> allUsers = Database.getUsers();
    		for(User pokus : allUsers) {
    			if(Objects.equals(pokus.getUserNickname(),actualUsername)) {
    				if(Objects.equals(pokus.getHashHeslo(), User.hashPassword(actualPassword))) {
    					CFXMLLogin.zhoda = pokus;
    				}
    			}
    		}

        	if(CFXMLLogin.zhoda != null) {
        		if(zhoda.getClass().equals(UserAdministrator.class)) {
                	Stage stage = new Stage();
                	Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/admin/homeAdmin.fxml")));
                	new JMetro(scene,Style.LIGHT);
                	stage.setScene(scene);
                	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
                	stage.setTitle(ApplicationInfo.APP_NAME + " - " + zhoda.getUserNickname() + " - Domov");
                	stage.setMaximized(true);
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
                	final Node source = (Node) event.getSource();
                    final Stage stageA = (Stage) source.getScene().getWindow();
                    stageA.close();
                }
        		else if(zhoda.getClass().equals(UserPokladnik.class)) {
        			
        			Stage stage = new Stage();
        			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/scenes/pokladnica/pokladnica.fxml"));
        		    Parent root = (Parent) fxmlLoader.load();
        		    Scene scene = new Scene(root);
                	CFXMLHomePokladnik cfxmlHomePokladnikController = fxmlLoader.getController();
                	new JMetro(scene,Style.LIGHT);
                	stage.setScene(scene);
                	stage.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
                	stage.setTitle(ApplicationInfo.APP_NAME + " - " + zhoda.getUserNickname() + " - Domov");
                	stage.setMaximized(true);
                	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                		public void handle(WindowEvent we) {
                			Alert alert = new Alert(AlertType.CONFIRMATION);
                			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
                    		alert.setTitle("Ukončenie programu");
                    		alert.setHeaderText("Ukončenie programu");
                    		alert.setContentText("Chcete naozaj ukončiť program ?");
                    		Optional<ButtonType> vysledok = alert.showAndWait();
                    		if(vysledok.get() == ButtonType.OK) {
                    			cfxmlHomePokladnikController.closePort();
                    			Database.setPokladnicaClose(zhoda.getId(), cfxmlHomePokladnikController.getPokladnica().getIdPokladnice());
                    			alert.close();
                    	        stage.close();
                    		}
                    		else {
                    			alert.close();
                    			we.consume();
                    		}
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
                		
                		final KeyCode keyMnozstvo = 		KeyCode.M;
                		final KeyCode keyHmotnost = 		KeyCode.H;
                		final KeyCode keyAkcia = 			KeyCode.A;
                		
                		final KeyCode keyZrusitNakup = 		KeyCode.Z;
                		final KeyCode keyDeleteItem = 		KeyCode.DELETE;
                		final KeyCode keyStorno = 			KeyCode.Q;
                		
                		final KeyCode keyHotovost = 		KeyCode.O;
                		final KeyCode keyStravneListky = 	KeyCode.C;
                		
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
        					else if(key == keyMnozstvo) {
        						try {
        							if (!cfxmlHomePokladnikController.isEmptyList()) {
        								cfxmlHomePokladnikController.showMnozstvoInput();
									}
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyHmotnost) {
        						try {
        							if (!cfxmlHomePokladnikController.isEmptyList()) {
        								cfxmlHomePokladnikController.showHmotnostInput();
									}
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyAkcia) {
        						try {
        							cfxmlHomePokladnikController.showAkciaInput();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyZrusitNakup) {
        						try {
        							cfxmlHomePokladnikController.showZrusitNakup();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyDeleteItem) {
        						try {
        							if (!cfxmlHomePokladnikController.isEmptyList()) {
        								cfxmlHomePokladnikController.showDeleteItem();
									}
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyStorno) {
        						try {
        							cfxmlHomePokladnikController.showStorno();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyHotovost) {
        						try {
        							cfxmlHomePokladnikController.showHotovostInput();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        					else if(key == keyStravneListky) {
        						try {
        							cfxmlHomePokladnikController.showStravneListkyInput();
        						} catch (IOException e) {
        							e.printStackTrace();
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
        				
        				public void handle(KeyEvent ke) {
        					//klavesova skratka pre odhlasenie 
        					if (keyOdhlasit.match(ke)) {
        						Alert alert = new Alert(AlertType.CONFIRMATION);
        						new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
        						alert.setTitle("Odhlásenie");
        						alert.setHeaderText("Odhlásenie");
        						alert.setContentText("Chcete sa naozaj odhlásiť ?");
        						Optional<ButtonType> vysledok = alert.showAndWait();
        						if(vysledok.get() == ButtonType.OK) {
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
        						if(vysledok.get() == ButtonType.OK) {
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
	        											ShopKingTools.showExceptionDialog(e);
	        										}
	        									}	
	        								}
	        							});
										stageSaver.setScene(sceneSaver);
										stageSaver.setFullScreen(true);
										stageSaver.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
										stageSaver.initStyle(StageStyle.UNDECORATED);
										stageSaver.setResizable(false);
										stageSaver.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        				        		public void handle(WindowEvent we) {
	        				        			Alert alert = new Alert(AlertType.CONFIRMATION);
	        				        			new JMetro(alert.getDialogPane().getScene(),Style.LIGHT);
	        				            		alert.setTitle("Ukončenie programu");
	        				            		alert.setHeaderText("Ukončenie programu");
	        				            		alert.setContentText("Chcete naozaj ukončiť program ?");
	        				            		Optional<ButtonType> vysledok = alert.showAndWait();
	        				            		if(vysledok.get() == ButtonType.OK) {
	        				            			cfxmlHomePokladnikController.closePort();
	        				            			alert.close();
	        				            			stageSaver.close();
	        				            		}
	        				            		else {
	        				            			alert.close();
	        				            			we.consume();
	        				            		}
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
                	Node source = (Node) event.getSource();
                    Stage stageA = (Stage) source.getScene().getWindow();
                    stageA.close();
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
    private void jbExitAction (ActionEvent event) throws IOException {
    	Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
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
