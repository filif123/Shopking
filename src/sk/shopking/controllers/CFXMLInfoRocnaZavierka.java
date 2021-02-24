package sk.shopking.controllers;

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
import sk.shopking.RocnaZavierka;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.ZavierkaGenerator;

/**
 * @author filip
 *
 */
public class CFXMLInfoRocnaZavierka implements Initializable{

	private RocnaZavierka vybranaRocnaZavierka;
	
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
			if(isNotCancel){
				Text text = new Text();
				text.setFont(new Font("Courier New", 15));
				text.setY(15);
				text.setText(ZavierkaGenerator.generateRocnaZavierka(vybranaRocnaZavierka));

				boolean printed = job.printPage(text);
				if (printed) {
					job.endJob();
				}
			}
		}
	}
	
	public void setRocnaZavierka(RocnaZavierka zavierka) {
		this.vybranaRocnaZavierka = zavierka;

		jtfCasZavierky.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(vybranaRocnaZavierka.getCasZavierky()));

		jtfCelkovyObrat10.setText(new DecimalFormat("#.##").format(vybranaRocnaZavierka.getObratSadzba10()));
		jtfCelkovyZaklad10.setText(new DecimalFormat("#.##").format(vybranaRocnaZavierka.getZakladSadzba10()));
		jtfCelkoveDPH10.setText(new DecimalFormat("#.##").format(vybranaRocnaZavierka.getDphSadzba10()));
		
		jtfCelkovyObrat20.setText(new DecimalFormat("#.##").format(vybranaRocnaZavierka.getObratSadzba20()));
		jtfCelkovyZaklad20.setText(new DecimalFormat("#.##").format(vybranaRocnaZavierka.getZakladSadzba20()));
		jtfCelkoveDPH20.setText(new DecimalFormat("#.##").format(vybranaRocnaZavierka.getDphSadzba20()));
		
		jtfCelkovyObrat.setText(new DecimalFormat("#.##").format(vybranaRocnaZavierka.getSumaObrat()));
		
		jtaOriginal.setText(ZavierkaGenerator.generateRocnaZavierka(vybranaRocnaZavierka));
	}

}
