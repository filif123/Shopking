/**
 * 
 */
package sk.shopking;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import sk.shopking.tools.ShopKingTools;

/**
 * Táto trieda je kontrolér pre okno šetriča obrazovky, keď je pokladnica v šetriacom režime.
 * @author Filip
 *
 */
public class CFXMLScreenSaver implements Initializable {
	
	
	@FXML private Label jlTime;
	@FXML private Label jlDate;
	
	@FXML
	private void wakeUp(MouseEvent event) throws IOException {
		Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        Stage stageLogin = new Stage();
		Scene sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("/resources/scenes/login.fxml")));
		new JMetro(sceneLogin,Style.LIGHT);
        stageLogin.setScene(sceneLogin);
        stageLogin.getIcons().add(new Image(ApplicationInfo.APP_ICON_SMALL));
        stageLogin.setResizable(false);
        stageLogin.initStyle(StageStyle.UNDECORATED);
        stageLogin.show();
	}
	
	public void showDate() {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		setLabelText(timeFormat.format(time),jlTime);
		setLabelText(ShopKingTools.getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)) + "  " + dateFormat.format(time),jlDate);
    }),
         new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
	}
	
	private void setLabelText(String text,Label label) {
		label.setText(text);
	}
	
	@Override
    public void initialize(URL url, ResourceBundle rb){
		showDate();
    }
}
