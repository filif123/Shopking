/**
 * 
 */
package sk.shopking.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;
import sk.shopking.Obchod;
import sk.shopking.DPHType;
import sk.shopking.NakupenyTovar;
import sk.shopking.Pokladnica;

/**
 * Táto trieda sa stará vytvorenie pokladničného dokladu.
 * @author Filip
 *
 */
public class ReceiptCreator {

	private Obchod infoOPrevadzke;
	private String casNakupu; 
	private List<NakupenyTovar> nakupeneTovary;
	private Pokladnica pokladnica;
	private int doklad;
	private Platba platba;
	private Image logo;
	private String poznamka;
	private Canvas poklBlok;
	
	public ReceiptCreator(List<NakupenyTovar> nakupeneTovary,Pokladnica pokladnica,int doklad, Platba platba, Date casNakupu) {
		this.setNakupeneTovary(nakupeneTovary);
		this.setPokladnica(pokladnica);
		this.setDoklad(doklad);
		this.setPlatba(platba);
		this.setInfoOPrevadzke(getInfoObchod());
		File file = infoOPrevadzke.getLogoSpolocnosti();
		if (file != null) {
			InputStream ImageStream;
			try {
				ImageStream = new FileInputStream(file);
				logo = new Image(ImageStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}	
		}
		this.casNakupu = dateToString(casNakupu);
	}
	
	public ReceiptCreator(List<NakupenyTovar> nakupeneTovary,Pokladnica pokladnica,int doklad, Platba platba, Date casNakupu,String poznamka) {
		this.setNakupeneTovary(nakupeneTovary);
		this.setPokladnica(pokladnica);
		this.setDoklad(doklad);
		this.setPlatba(platba);
		this.setInfoOPrevadzke(getInfoObchod());
		File file = infoOPrevadzke.getLogoSpolocnosti();
		if (file != null) {
			InputStream ImageStream;
			try {
				ImageStream = new FileInputStream(file);
				logo = new Image(ImageStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}	
		}
		this.setPoznamka(poznamka);		
		this.casNakupu = dateToString(casNakupu);
	}
	
	private Obchod getInfoObchod() {
		Obchod obchod =  Database.infoObchod();
		return obchod; 
	}

	public Obchod getInfoOPrevadzke() {
		return infoOPrevadzke;
	}

	public void setInfoOPrevadzke(Obchod infoOPrevadzke) {
		this.infoOPrevadzke = infoOPrevadzke;
	}

	public String getCasNakupu() {
		return casNakupu;
	}

	public void setCasNakupu(String casNakupu) {
		this.casNakupu = casNakupu;
	}

	public List<NakupenyTovar> getNakupeneTovary() {
		return nakupeneTovary;
	}

	public void setNakupeneTovary(List<NakupenyTovar> nakupeneTovary) {
		this.nakupeneTovary = nakupeneTovary;
	}

	public Pokladnica getPokladnica() {
		return pokladnica;
	}

	public void setPokladnica(Pokladnica pokladnica) {
		this.pokladnica = pokladnica;
	}

	public int getDoklad() {
		return doklad;
	}

	public void setDoklad(int doklad) {
		this.doklad = doklad;
	}
	
	public Platba getPlatba() {
		return platba;
	}

	public void setPlatba(Platba platba) {
		this.platba = platba;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}
	
	private String dateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return formatter.format(date);
	}
	
	private float calculateSuma() {
		float suma = 0;
		for (NakupenyTovar nakup : nakupeneTovary) {
			suma+=nakup.getNakupenaCena();
		}
		return suma;
	}
	
	private String padRight(String string, int n) {
		return String.format("%" + n + "s", string);
	}
	
	public void generateReceipt() {
		int y = 60;
		int width = 510;
		float yShift = 24;
		float x = 30;
		int maxSize = 30;
		
		DecimalFormat df = new DecimalFormat("0.00");
		poklBlok = new Canvas(width,calculateHeight());
		
		GraphicsContext gc = poklBlok.getGraphicsContext2D();
		
		gc.setFont(new Font("Courier New", 18));
		if (logo != null) {
			float xPos = (float) (width / 2 - logo.getWidth() / 2);
			gc.drawImage(logo, xPos, y);y+=logo.getHeight()+(yShift*2);		
		}
		gc.fillText(infoOPrevadzke.getObchodnyNazovFirmy(), x, y);y+=yShift;
		gc.fillText(infoOPrevadzke.getUlicaFirmy() + " " + infoOPrevadzke.getCisloPopisneFirmy(), x, y);y+=yShift;
		gc.fillText(infoOPrevadzke.getPSCFirmy() + " " + infoOPrevadzke.getMestoFirmy(), x, y);y+=yShift;
		gc.fillText("Prevádzka: " + infoOPrevadzke.getUlicaPrevadzky() + " " + infoOPrevadzke.getCisloPopisnePrevadzky(), x, y);y+=yShift;
		gc.fillText(infoOPrevadzke.getPSCPrevadzky() + " " + infoOPrevadzke.getMestoPrevadzky(), x, y);y+=yShift;
		gc.fillText("IČO: " + infoOPrevadzke.getICO() + " " + "DIČ: " + infoOPrevadzke.getDIC(), x, y);y+=yShift;
		gc.fillText("IČDPH: " + infoOPrevadzke.getICDPH(), x, y);y+=yShift;
		gc.fillText("Kód pokladnice: " + pokladnica.getDkpPokladnice(), x, y);y+=yShift;
		gc.fillText("------------------------------------------", x, y);y+=yShift;
		gc.fillText("Pokladnica: " + pokladnica.getIdPokladnice(), x, y);y+=yShift;
		gc.fillText("Pokladník: " + pokladnica.getPokladnikUser().getId(), x, y);y+=yShift;
		gc.fillText("------------------------------------------", x, y);y+=yShift;
		for (NakupenyTovar nakup : nakupeneTovary) {
			String nazov = nakup.getTovarName();
			if (nazov.length() > maxSize) {
				nazov = nazov.substring(0, maxSize);
			}
			gc.fillText(padRight("" + nakup.getTovarPLU(), 7) + " " + nazov, x, y);y+=yShift;
			if(nakup.getTovarDPH().equals(DPHType.DPH_20)) {
				gc.fillText(padRight(nakup.getNakupeneMnozstvo() + " " + nakup.getTovarJednotka().toString(), 10)  + padRight(df.format(nakup.getTovarJednotkovaCena()), 11) + " EUR" + padRight(df.format(nakup.getNakupenaCena()), 15) + "A", x, y);y+=yShift;
			}
			else if(nakup.getTovarDPH().equals(DPHType.DPH_10)) {
				gc.fillText(padRight(nakup.getNakupeneMnozstvo() + " " + nakup.getTovarJednotka().toString(), 10) + padRight(df.format(nakup.getTovarJednotkovaCena()), 11) + " EUR" + padRight(df.format(nakup.getNakupenaCena()), 15) + "B", x, y);y+=yShift;
			}
			
		}
		gc.fillText("------------------------------------------", x, y);y+=yShift;
		gc.fillText("SPOLU:" + padRight(df.format(calculateSuma()), 32) + " EUR", x, y);y+=yShift;
		gc.fillText("Hotovosť:" + padRight(df.format(calculateSuma()), 29) + " EUR", x, y);y+=yShift;
		gc.fillText("------------------------------------------", x, y);y+=yShift;
		gc.fillText(padRight("Sadzba", 7) + padRight("Bez DPH", 11) + padRight("DPH", 11) + padRight("S DPH", 11), x, y);y+=yShift;
		float bezDPH20	= 0,bezDPH10	= 0;
		float dph10		= 0,dph20		= 0;
		float sDPH10	= 0,sDPH20		= 0;
		for (NakupenyTovar nakup : nakupeneTovary) {
			if (nakup.getTovarDPH().equals(DPHType.DPH_10)) {
				bezDPH10+=DPH.vypocetCenaBezDPH(nakup.getNakupenaCena(), DPHType.DPH_10);
				dph10+=DPH.vypocetDPH(nakup.getNakupenaCena(), DPHType.DPH_10);
				sDPH10+=DPH.vypocetCenaBezDPH(nakup.getNakupenaCena(), DPHType.DPH_10) + DPH.vypocetDPH(nakup.getNakupenaCena(), DPHType.DPH_10);
			}
			else {
				bezDPH20+=DPH.vypocetCenaBezDPH(nakup.getNakupenaCena(), DPHType.DPH_20);
				dph20+=DPH.vypocetDPH(nakup.getNakupenaCena(), DPHType.DPH_20);
				sDPH20+=DPH.vypocetCenaBezDPH(nakup.getNakupenaCena(), DPHType.DPH_20) + DPH.vypocetDPH(nakup.getNakupenaCena(), DPHType.DPH_20);
			}
		}
		gc.fillText(padRight("10% (B)", 7) + padRight(df.format(bezDPH10), 11) + padRight(df.format(dph10), 11) + padRight(df.format(sDPH10), 11), x, y);y+=yShift;
		gc.fillText(padRight("20% (A)", 7) + padRight(df.format(bezDPH20), 11) + padRight(df.format(dph20), 11) + padRight(df.format(sDPH20), 11) , x, y);y+=yShift;
		gc.fillText("------------------------------------------", x, y);y+=yShift;
		gc.fillText("Pokl. dokl. " + padRight(doklad + "/" + casNakupu, 30), x, y);y+=yShift;
		gc.fillText("------------------------------------------", x, y);y+=yShift;
		if (platba.getTypPlatby().equals(PlatbaType.HOTOVOST)) {
			gc.fillText("Zaplatené:" + padRight(df.format(platba.getZaplatene()),28) + " EUR", x, y);y+=yShift;
			gc.fillText("Vydané:" + padRight(df.format(platba.getZaplatene() - platba.getCelkovaSuma()),31) + " EUR", x, y);y+=yShift;
		}
		gc.fillText("------------------------------------------", x, y);y+=yShift;
		if (poznamka != null) {
			gc.fillText(poznamka, x, y);y+=yShift;
		}
		
	}
	
	private double calculateHeight() {
		int textSizeinPX = 24;
		double height = 60; //zaciatocna medzera
		
		if (logo != null) {
			height = height + logo.getHeight(); //velkost loga
		}
		height += textSizeinPX*2; //medzera medzi logom a hlavickou
		
		height += textSizeinPX * 8; //velkost textu v HLAVICKE
		//height += 4 * 7; //medzery medzi textom v HLAVICKE
		
		height += textSizeinPX; //medzera medzi HLAVICKOU a INFO O POKLADNICI
		
		height += textSizeinPX * 2; //velkost textu v INFO O POKLADNICI
		//height += 4 * 1; //medzery medzi textom v INFO O POKLADNICI
		
		height += textSizeinPX; //medzera medzi INFO O POKLADNICI a NAKUPOM
		
		height += textSizeinPX * (2 * nakupeneTovary.size()); //velkost textu v NAKUPE
		//height += 4 * (nakupeneTovary.size() - 1); //medzery medzi textom v NAKUPE
		
		height += textSizeinPX; //medzera medzi NAKUPOM a SUMAROM
		
		height += textSizeinPX * 2; //velkost textu v SUMARE
		//height += 4 * 1; //medzery medzi textom v SUMARE
		
		height += textSizeinPX; //medzera medzi SUMAROM a VYPOCTOM DPH
		
		height += textSizeinPX * 3; //velkost textu vo VYPOCTE DPH
		//height += 4 * 3; //medzery medzi textom vo VYPOCTE DPH
		
		height += textSizeinPX; //medzera medzi VYPOCTOM DPH a INFO O DOKLADE
		
		height += textSizeinPX; //velkost textu v INFO O DOKLADE
		
		height += textSizeinPX; //medzera medzi INFO O DOKLADE a HOTOVOST
		
		height += textSizeinPX; //velkost textu v HOTOVOST
		
		height += textSizeinPX; //medzera medzi HOTOVOST a POZNAMKOU
		
		if (poznamka != null) {
			height = height + poznamka.split("\r\n|\r|\n").length;
		}
		height+= 30; //medzera medzi POZNAMKOU a KONCOM DOKLADU
		
		return height;
	}
	
	public boolean printReceipt() {
		float scale = 1;
		WritableImage wImage = new WritableImage((int)Math.rint(scale * poklBlok.getWidth()),(int)Math.rint(scale * poklBlok.getHeight()));
		SnapshotParameters sParameters = new SnapshotParameters();
		sParameters.setTransform(Transform.scale(scale, scale));
		WritableImage vyslednyBlok = poklBlok.snapshot(sParameters,wImage);	
		BufferedImage bImage = SwingFXUtils.fromFXImage(vyslednyBlok,null);
		try {
			ImageIO.write(bImage,"png",new File(AppSettings.loadReceiptPath() + "\\" + generateNameReceipt() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}			
		return false;
	}
	
	private String generateNameReceipt() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		return doklad + "_" + formatter.format(date);
	}
}
