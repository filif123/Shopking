package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import sk.shopking.tools.FXMLTools;

/**
 * @author filip
 *
 */
public class CFXMLDenneZavierkyPodlaDna implements Initializable{
	
	private Date vybranyDatum;
	
	@FXML private DatePicker jdpDatum;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	private void jbShowAction(ActionEvent event) {
		if (jdpDatum.getValue() != null) {
			vybranyDatum = Date.from(jdpDatum.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			FXMLTools.closeWindow(event);
		}
		
	}
	
	@FXML
	private void jbStornoAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	public Date getSelectedDate() {
		return this.vybranyDatum;
	}

}
