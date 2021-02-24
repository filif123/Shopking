package sk.shopking.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sk.shopking.ApplicationInfo;
import sk.shopking.Obchod;
import sk.shopking.tools.Database;

/**
 * Táto trieda je kontrolér pre okno informácií o obchode a výrobcovi tohto softvéru.
 * @author Filip
 *
 */
public class CFXMLInformation implements Initializable{
	
	private final Obchod tentoObchod = Database.infoObchod();
	
	
	@FXML private Label jlAppName;
	@FXML private Label jlAppVersion;
	@FXML private Label jlAppManufacturer;
	@FXML private Label jlReleaseDate;
	@FXML private Label jlUpdateDate;
	@FXML private Label jlShopName;
	@FXML private Label jlCompanyName;
	@FXML private Label jlSidloSpol;
	@FXML private Label jlSidloPrev;
	@FXML private Label jlICO;
	@FXML private Label jlDIC;
	
	@FXML private ImageView jiLogo;
	

	@Override
    public void initialize(URL url, ResourceBundle rb) {
        showApplicationInfo();
        showShopInfo();
    }
	
	private void setLabelText(String text,Label label) {
		label.setText(text);
	}
	
	private void showApplicationInfo() {
		setLabelText(ApplicationInfo.APP_NAME,jlAppName);
		setLabelText(ApplicationInfo.APP_VERSION,jlAppVersion);
		setLabelText(ApplicationInfo.APP_MANUFACTURER,jlAppManufacturer);
		setLabelText(ApplicationInfo.APP_RELEASE_DATE,jlReleaseDate);
		setLabelText(ApplicationInfo.APP_UPDATE_DATE,jlUpdateDate);
	}
	
	private void showShopInfo() {
		setLabelText(tentoObchod.getNazovFirmy(),jlShopName);
		setLabelText(tentoObchod.getObchodnyNazovFirmy(),jlCompanyName);
		setLabelText(tentoObchod.getUlicaFirmy() + " " + tentoObchod.getCisloPopisneFirmy() + "\n"
				+ tentoObchod.getPSCFirmy() + "\n"
				+ tentoObchod.getMestoFirmy(),jlSidloSpol);
		setLabelText(tentoObchod.getUlicaPrevadzky() + " " + tentoObchod.getCisloPopisnePrevadzky() + "\n"
				+ tentoObchod.getPSCPrevadzky() + "\n"
				+ tentoObchod.getMestoPrevadzky(),jlSidloPrev);
		setLabelText(tentoObchod.getICO(),jlICO);
		setLabelText(tentoObchod.getDIC(),jlDIC);
		if (tentoObchod.getLogoSpolocnosti() != null) {
			Image image = new Image(tentoObchod.getLogoSpolocnosti().toURI().toString());
		    jiLogo.setImage(image);
		}
	    
	}
}
