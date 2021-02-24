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
import sk.shopking.DennaZavierka;
import sk.shopking.tools.FXMLTools;
import sk.shopking.tools.ZavierkaGenerator;

/**
 * @author filip
 *
 */
public class CFXMLInfoDennaZavierka implements Initializable{

	private DennaZavierka vybranaDennaZavierka;
	
	@FXML private TextField jtfCasZavierky,jtfPokladnik;
	
	@FXML private TextField jtfCelkovyObrat10,jtfCelkovyZaklad10,jtfCelkoveDPH10;
	@FXML private TextField jtfObrat10,jtfZaklad10,jtfDPH10;
	@FXML private TextField jtfZapornyObrat10,jtfZapornyZaklad10,jtfZaporneDPH10;
	
	@FXML private TextField jtfCelkovyObrat20,jtfCelkovyZaklad20,jtfCelkoveDPH20;
	@FXML private TextField jtfObrat20,jtfZaklad20,jtfDPH20;
	@FXML private TextField jtfZapornyObrat20,jtfZapornyZaklad20,jtfZaporneDPH20;
	
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

		if (job != null){
			if (isNotCancel){
				Text text = new Text();
				text.setFont(new Font("Courier New", 15));
				text.setY(15);
				text.setText(ZavierkaGenerator.generateDennaZavierka(vybranaDennaZavierka));

				boolean printed = job.printPage(text);
				if (printed) {
					job.endJob();
				}
			}
		}
	}
	
	public void setDennaZavierka(DennaZavierka zavierka) {
		this.vybranaDennaZavierka = zavierka;
		
		float zapornyObratSadzba10 = vybranaDennaZavierka.getZapornyObratSadzba10();
		float zapornyZakladSadzba10 = vybranaDennaZavierka.getZapornyZakladSadzba10();
		float zaporneDPHSadzba10 = vybranaDennaZavierka.getZaporneDPHSadzba10();
		
		float zapornyObratSadzba20 = vybranaDennaZavierka.getZapornyObratSadzba20();
		float zapornyZakladSadzba20 = vybranaDennaZavierka.getZapornyZakladSadzba20();
		float zaporneDPHSadzba20 = vybranaDennaZavierka.getZaporneDPHSadzba20();
		
		if (zapornyObratSadzba10 != 0) {
			zapornyObratSadzba10=-zapornyObratSadzba10;
		}
		if (zapornyZakladSadzba10 != 0) {
			zapornyZakladSadzba10=-zapornyZakladSadzba10;
		}
		if (zaporneDPHSadzba10 != 0) {
			zaporneDPHSadzba10=-zaporneDPHSadzba10;
		}
		
		if (zapornyObratSadzba20 != 0) {
			zapornyObratSadzba20=-zapornyObratSadzba20;
		}
		if (zapornyZakladSadzba20 != 0) {
			zapornyZakladSadzba20=-zapornyZakladSadzba20;
		}
		if (zaporneDPHSadzba20 != 0) {
			zaporneDPHSadzba20=-zaporneDPHSadzba20;
		}
		
		jtfCasZavierky.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(vybranaDennaZavierka.getCasZavierky()));
		jtfPokladnik.setText(vybranaDennaZavierka.getUserPokladnik().toString());
		
		jtfCelkovyObrat10.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getObratSadzba10() - vybranaDennaZavierka.getZapornyObratSadzba10()));
		jtfCelkovyZaklad10.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getZakladSadzba10() - vybranaDennaZavierka.getZapornyZakladSadzba10()));
		jtfCelkoveDPH10.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getDphSadzba10() - vybranaDennaZavierka.getZaporneDPHSadzba10()));
		
		jtfObrat10.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getObratSadzba10()));
		jtfZaklad10.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getZakladSadzba10()));
		jtfDPH10.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getDphSadzba10()));
		
		jtfZapornyObrat10.setText(new DecimalFormat("#.##;-#.##").format(zapornyObratSadzba10));
		jtfZapornyZaklad10.setText(new DecimalFormat("#.##;-#.##").format(zapornyZakladSadzba10));
		jtfZaporneDPH10.setText(new DecimalFormat("#.##;-#.##").format(zaporneDPHSadzba10));
		
		jtfCelkovyObrat20.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getObratSadzba20() - vybranaDennaZavierka.getZapornyObratSadzba20()));
		jtfCelkovyZaklad20.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getZakladSadzba20() - vybranaDennaZavierka.getZapornyZakladSadzba20()));
		jtfCelkoveDPH20.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getDphSadzba20() - vybranaDennaZavierka.getZaporneDPHSadzba20()));
		
		jtfObrat20.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getObratSadzba20()));
		jtfZaklad20.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getZakladSadzba20()));
		jtfDPH20.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getDphSadzba20()));
		
		jtfZapornyObrat20.setText(new DecimalFormat("#.##;-#.##").format(zapornyObratSadzba20));
		jtfZapornyZaklad20.setText(new DecimalFormat("#.##;-#.##").format(zapornyZakladSadzba20));
		jtfZaporneDPH20.setText(new DecimalFormat("#.##;-#.##").format(zaporneDPHSadzba20));
		
		jtfCelkovyObrat.setText(new DecimalFormat("#.##").format(vybranaDennaZavierka.getSumaObrat()));
		
		jtaOriginal.setText(ZavierkaGenerator.generateDennaZavierka(vybranaDennaZavierka));
	}

}
