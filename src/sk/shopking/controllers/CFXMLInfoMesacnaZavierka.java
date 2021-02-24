package sk.shopking.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sk.shopking.MesacnaZavierka;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.ZavierkaGenerator;

/**
 * @author filip
 *
 */
public class CFXMLInfoMesacnaZavierka implements Initializable{

	private MesacnaZavierka vybranaMesacnaZavierka;
	
	@FXML private TextField jtfCasZavierky,jtfInterval;
	
	@FXML private TextField jtfCelkovyObrat10,jtfCelkovyZaklad10,jtfCelkoveDPH10;
	
	@FXML private TextField jtfCelkovyObrat20,jtfCelkovyZaklad20,jtfCelkoveDPH20;
	
	@FXML private TextField jtfCelkovyObrat;
	
	@FXML private TextArea jtaOriginal;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	private void jbOKAction(ActionEvent event) {
		FXMLTools.closeWindow(event);
	}
	
	@FXML
	private void jbPrintAction(ActionEvent event) {
		PrinterJob job = PrinterJob.createPrinterJob();
		boolean isNotCancel = job.showPrintDialog(null);


		if (job != null) {
			if (isNotCancel){
				Text text = new Text();
				text.setFont(new Font("Courier New", 15));
				text.setY(15);
				text.setText(ZavierkaGenerator.generateMesacnaZavierka(vybranaMesacnaZavierka));

				boolean printed = job.printPage(text);
				if (printed) {
					job.endJob();
				}
			}

		}
	}
	
	public void setMesacnaZavierka(MesacnaZavierka zavierka) {
		this.vybranaMesacnaZavierka = zavierka;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		jtfCasZavierky.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(vybranaMesacnaZavierka.getCasZavierky()));
		jtfInterval.setText(dateFormat.format(vybranaMesacnaZavierka.getIntervalOd()) + " - " + dateFormat.format(vybranaMesacnaZavierka.getIntervalDo()));
		
		jtfCelkovyObrat10.setText(new DecimalFormat("#.##").format(vybranaMesacnaZavierka.getObratSadzba10()));
		jtfCelkovyZaklad10.setText(new DecimalFormat("#.##").format(vybranaMesacnaZavierka.getZakladSadzba10()));
		jtfCelkoveDPH10.setText(new DecimalFormat("#.##").format(vybranaMesacnaZavierka.getDphSadzba10()));
		
		jtfCelkovyObrat20.setText(new DecimalFormat("#.##").format(vybranaMesacnaZavierka.getObratSadzba20()));
		jtfCelkovyZaklad20.setText(new DecimalFormat("#.##").format(vybranaMesacnaZavierka.getZakladSadzba20()));
		jtfCelkoveDPH20.setText(new DecimalFormat("#.##").format(vybranaMesacnaZavierka.getDphSadzba20()));
		
		jtfCelkovyObrat.setText(new DecimalFormat("#.##").format(vybranaMesacnaZavierka.getSumaObrat()));
		
		jtaOriginal.setText(ZavierkaGenerator.generateMesacnaZavierka(vybranaMesacnaZavierka));
	}

}
