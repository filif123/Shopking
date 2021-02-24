/**
 * 
 */
package sk.shopking.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import sk.shopking.ApplicationInfo;
import sk.shopking.DennaZavierka;
import sk.shopking.DenneZavierky;
import sk.shopking.MesacnaZavierka;
import sk.shopking.RocnaZavierka;

/**
 * @author filip
 *
 */
public class ZavierkaGenerator {

	public static String generateDennaZavierka(DennaZavierka dennaZavierka) {
		
		StringBuilder sBuilder = new StringBuilder();
		float sumaZapornyObrat = dennaZavierka.getSumaZapornyObrat();
		
		float zapornyObratSadzba10 = dennaZavierka.getZapornyObratSadzba10();
		float zapornyZakladSadzba10 = dennaZavierka.getZapornyZakladSadzba10();
		float zaporneDPHSadzba10 = dennaZavierka.getZaporneDPHSadzba10();
		
		float zapornyObratSadzba20 = dennaZavierka.getZapornyObratSadzba20();
		float zapornyZakladSadzba20 = dennaZavierka.getZapornyZakladSadzba20();
		float zaporneDPHSadzba20 = dennaZavierka.getZaporneDPHSadzba20();
		
		if (sumaZapornyObrat != 0) {
			sumaZapornyObrat=-sumaZapornyObrat;
		}
		
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
		
		sBuilder.append(ApplicationInfo.APP_NAME + "\n");
		sBuilder.append("*** D E N N A   Z A V I E R K A ***\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Doklad: " + dennaZavierka.getIdZavierky() + padRight(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(dennaZavierka.getCasZavierky()),30) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Obrat v sadzbe 20%:" + padRight(roundNum(dennaZavierka.getObratSadzba20()), 21) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(dennaZavierka.getZakladSadzba20()), 10) + "       " + "DPH:" + padRight(roundNum(dennaZavierka.getDphSadzba20()), 10) + "\n");
		sBuilder.append("Obrat v sadzbe 10%:" + padRight(roundNum(dennaZavierka.getObratSadzba10()), 21) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(dennaZavierka.getZakladSadzba10()), 10) + "       " + "DPH:" + padRight(roundNum(dennaZavierka.getDphSadzba10()), 10) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Zaporny obrat:" + padRight(roundNum(sumaZapornyObrat), 26) + "\n");
		sBuilder.append("Zaporny obrat v sadzbe 20%:" + padRight(roundNum(zapornyObratSadzba20), 13) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(zapornyZakladSadzba20), 10) + "       " + "DPH:" + padRight(roundNum(zaporneDPHSadzba20), 10) + "\n");
		sBuilder.append("Zaporny obrat v sadzbe 10%:" + padRight(roundNum(zapornyObratSadzba10), 13) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(zapornyZakladSadzba10), 10) + "       " + "DPH:" + padRight(roundNum(zaporneDPHSadzba10), 10) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Celkovy obrat:" + padRight(roundNum(dennaZavierka.getSumaObrat()), 26) + "\n");
		sBuilder.append("----------------------------------------\n");
		return sBuilder.toString();
	}
	
	public static String generateSkupinaDennychZavierok(DenneZavierky dennaZavierka) {
		
		StringBuilder sBuilder = new StringBuilder();
		float sumaZapornyObrat = dennaZavierka.getSumaZapornyObrat();
		
		float zapornyObratSadzba10 = dennaZavierka.getZapornyObratSadzba10();
		float zapornyZakladSadzba10 = dennaZavierka.getZapornyZakladSadzba10();
		float zaporneDPHSadzba10 = dennaZavierka.getZaporneDPHSadzba10();
		
		float zapornyObratSadzba20 = dennaZavierka.getZapornyObratSadzba20();
		float zapornyZakladSadzba20 = dennaZavierka.getZapornyZakladSadzba20();
		float zaporneDPHSadzba20 = dennaZavierka.getZaporneDPHSadzba20();
		
		if (sumaZapornyObrat != 0) {
			sumaZapornyObrat=-sumaZapornyObrat;
		}
		
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
		
		sBuilder.append(ApplicationInfo.APP_NAME + "\n");
		sBuilder.append("*** D E N N A   Z A V I E R K A ***\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append(new SimpleDateFormat("dd.MM.yyyy").format(dennaZavierka.getDatumZavierok()) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Obrat v sadzbe 20%:" + padRight(roundNum(dennaZavierka.getObratSadzba20()), 21) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(dennaZavierka.getZakladSadzba20()), 10) + "       " + "DPH:" + padRight(roundNum(dennaZavierka.getDphSadzba20()), 10) + "\n");
		sBuilder.append("Obrat v sadzbe 10%:" + padRight(roundNum(dennaZavierka.getObratSadzba10()), 21) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(dennaZavierka.getZakladSadzba10()), 10) + "       " + "DPH:" + padRight(roundNum(dennaZavierka.getDphSadzba10()), 10) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Zaporny obrat:" + padRight(roundNum(sumaZapornyObrat), 26) + "\n");
		sBuilder.append("Zaporny obrat v sadzbe 20%:" + padRight(roundNum(zapornyObratSadzba20), 13) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(zapornyZakladSadzba20), 10) + "       " + "DPH:" + padRight(roundNum(zaporneDPHSadzba20), 10) + "\n");
		sBuilder.append("Zaporny obrat v sadzbe 10%:" + padRight(roundNum(zapornyObratSadzba10), 13) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(zapornyZakladSadzba10), 10) + "       " + "DPH:" + padRight(roundNum(zaporneDPHSadzba10), 10) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Celkovy obrat:" + padRight(roundNum(dennaZavierka.getSumaObrat()), 26) + "\n");
		sBuilder.append("----------------------------------------\n");
		return sBuilder.toString();
	}
	
	public static String generateMesacnaZavierka(MesacnaZavierka mesacnaZavierka) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(ApplicationInfo.APP_NAME + "\n");
		sBuilder.append("*** M E S A C N A   Z A V I E R K A ***\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Doklad: " + mesacnaZavierka.getIdZavierky() + padRight(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(mesacnaZavierka.getCasZavierky()),30) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Obdobie: " + padRight(new SimpleDateFormat("dd.MM.yyyy").format(mesacnaZavierka.getIntervalOd()) + " - " + new SimpleDateFormat("dd.MM.yyyy").format(mesacnaZavierka.getIntervalDo()),29) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Obrat v sadzbe 20%:" + padRight(roundNum(mesacnaZavierka.getObratSadzba20()), 21) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(mesacnaZavierka.getZakladSadzba20()), 10) + "       " + "DPH:" + padRight(roundNum(mesacnaZavierka.getDphSadzba20()), 10) + "\n");
		sBuilder.append("Obrat v sadzbe 10%:" + padRight(roundNum(mesacnaZavierka.getObratSadzba10()), 21) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(mesacnaZavierka.getZakladSadzba10()), 10) + "       " + "DPH:" + padRight(roundNum(mesacnaZavierka.getDphSadzba10()), 10) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Celkovy obrat:" + padRight(roundNum(mesacnaZavierka.getSumaObrat()), 26) + "\n");
		sBuilder.append("----------------------------------------\n");
		return sBuilder.toString();
	}
	
	public static String generateRocnaZavierka(RocnaZavierka rocnaZavierka) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(ApplicationInfo.APP_NAME + "\n");
		sBuilder.append("*** R O C N A   Z A V I E R K A ***\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Doklad: " + rocnaZavierka.getIdZavierky() + padRight(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rocnaZavierka.getCasZavierky()),30) + "\n");
		sBuilder.append("----------------------------------------\n");
		//sBuilder.append("Obdobie: " + padRight(new SimpleDateFormat("dd.MM.yyyy").format(rocnaZavierka.getCasPrvejZavierky()),29) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Obrat v sadzbe 20%:" + padRight(roundNum(rocnaZavierka.getObratSadzba20()), 21) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(rocnaZavierka.getZakladSadzba20()), 10) + "       " + "DPH:" + padRight(roundNum(rocnaZavierka.getDphSadzba20()), 10) + "\n");
		sBuilder.append("Obrat v sadzbe 10%:" + padRight(roundNum(rocnaZavierka.getObratSadzba10()), 21) + "\n");
		sBuilder.append("  Zaklad:" + padRight(roundNum(rocnaZavierka.getZakladSadzba10()), 10) + "       " + "DPH:" + padRight(roundNum(rocnaZavierka.getDphSadzba10()), 10) + "\n");
		sBuilder.append("----------------------------------------\n");
		sBuilder.append("Celkovy obrat:" + padRight(roundNum(rocnaZavierka.getSumaObrat()), 26) + "\n");
		sBuilder.append("----------------------------------------\n");
		return sBuilder.toString();
	}
	
	private static String padRight(String string, int n) {
		return String.format("%" + n + "s", string);
	}
	
	private static String roundNum(float num) {
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormatSymbols decimal = DecimalFormatSymbols.getInstance();
		decimal.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(decimal);
		return df.format(num);
	}
}
