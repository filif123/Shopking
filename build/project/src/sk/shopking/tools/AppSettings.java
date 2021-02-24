/**
 * 
 */
package sk.shopking.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Obsahuje kľúče vlastností pre čítanie a zapisovanie do súboru settings.cfg
 * @author Filip
 *
 */
public class AppSettings{
	
	public static String cestaKuKlientovi = new File(AppSettings.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();
	public static final String FILE_SETTINGS = "settings.cfg";
	
	public static final String BARCODE_SCANNER_PORT_PROPERTY = "scanner_port";
	public static final String IPSQL_PROPERTY = "ip";
	public static final String PATH_RECEIPT_PROPERTY = "receipt_path";
	public static final String CASH_REGISTER_NUMBER_PROPERTY = "register_num";
	public static final String CASH_REGISTERS_AUTO_REFRESH_PROPERTY = "cash_registers_auto_refresh";
	//TODO komentar zapisany v samostatnom subore
	public static final String COMMENT = "";

	public static String loadScannerPort() {
		Properties prop = new Properties();
		String scannerPort;
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			scannerPort = prop.getProperty(BARCODE_SCANNER_PORT_PROPERTY);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return scannerPort;
	}
	
	public static void setScannerPort(String port) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			prop.setProperty(BARCODE_SCANNER_PORT_PROPERTY,port);
			prop.store(new FileOutputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS), "comment");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String loadReceiptPath() {
		Properties prop = new Properties();
		String dir;
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			dir = prop.getProperty(PATH_RECEIPT_PROPERTY);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dir;
	}
	
	public static void setReceiptPath(String path) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			prop.setProperty(PATH_RECEIPT_PROPERTY,path);
			prop.store(new FileOutputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS), "comment");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String loadIPSQL() {
		Properties prop = new Properties();
		String ip;
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			ip = prop.getProperty(IPSQL_PROPERTY);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ip;
	}
	
	public static void setIPSQL(String ip) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			prop.setProperty(IPSQL_PROPERTY,ip);
			prop.store(new FileOutputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS), "comment");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int loadCashRegisterNumber() {
		Properties prop = new Properties();
		int num;
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			num = Integer.parseInt(prop.getProperty(CASH_REGISTER_NUMBER_PROPERTY));
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return num;
	}
	
	public static void setCashRegisterNumber(String num) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			prop.setProperty(CASH_REGISTER_NUMBER_PROPERTY,num);
			prop.store(new FileOutputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS), "comment");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int loadCashRegistersAutoRefresh() {
		Properties prop = new Properties();
		int time;
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			time = Integer.parseInt(prop.getProperty(CASH_REGISTERS_AUTO_REFRESH_PROPERTY));
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return time;
	}
	
	public static void setCashRegistersAutoRefresh(String sec) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
			prop.setProperty(CASH_REGISTERS_AUTO_REFRESH_PROPERTY,sec);
			prop.store(new FileOutputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS), "comment");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean createPropFile(String scannerPort,String ipSQL,String pathReceipt,String poklNum,String autoRefresh) {
		Properties prop = new Properties();
		try {
			prop.setProperty(BARCODE_SCANNER_PORT_PROPERTY, scannerPort);
			prop.setProperty(IPSQL_PROPERTY, ipSQL);
			prop.setProperty(PATH_RECEIPT_PROPERTY, pathReceipt);
			prop.setProperty(CASH_REGISTER_NUMBER_PROPERTY, poklNum);
			prop.setProperty(CASH_REGISTERS_AUTO_REFRESH_PROPERTY, autoRefresh);
			prop.store(new FileOutputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS), "comment");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static int isInvalid() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + "\\" + FILE_SETTINGS));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
			return 1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 2;
		} catch (IOException e) {
			e.printStackTrace();
			return 3;
		}
		return 0;
	}
}
