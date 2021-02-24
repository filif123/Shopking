package sk.shopking.tools;

import org.apache.poi.util.IOUtils;

import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
/**
 * Obsahuje kľúče vlastností pre čítanie a zapisovanie do súboru settings.cfg
 * @author Filip
 *
 */
public class AppSettings{
	
	public static final String FILE_SETTINGS = "settings.cfg";
	public static final String BARCODE_SCANNER_PORT_PROPERTY = "scanner_port";
	public static final String IPSQL_PROPERTY = "ip";
	public static final String PATH_RECEIPT_PROPERTY = "receipt_path";
	public static final String CASH_REGISTER_NUMBER_PROPERTY = "register_num";
	public static final String CASH_REGISTERS_AUTO_REFRESH_PROPERTY = "cash_registers_auto_refresh";

	//public static final File COMMENT_FILE = new File(AppSettings.class.getResource("/resources/comment-prop.txt").getFile());
	//public static String cestaKuKlientovi = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath()).getAbsolutePath();
	public static String cestaKuKlientovi = new File(System.getenv("APPDATA") + File.separator + "ShopKing").getAbsolutePath();
	
	private static String getCommentText() throws IOException{
		InputStream in = AppSettings.class.getResourceAsStream("/resources/comment-prop.txt");
		byte[] data = IOUtils.toByteArray(in);
		return new String(data);
	}

	private static String getPropertyValue(String name) {
		Properties prop = new Properties();
		String dir;
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + File.separator + FILE_SETTINGS));
			dir = prop.getProperty(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dir;
	}

	private static void setPropertyValue(String name, String value) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(cestaKuKlientovi + File.separator + FILE_SETTINGS));
			prop.setProperty(name, value);
			prop.store(new FileOutputStream(cestaKuKlientovi + File.separator + FILE_SETTINGS), getCommentText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String loadScannerPort() {
		return getPropertyValue(BARCODE_SCANNER_PORT_PROPERTY);
	}
	
	public static void setScannerPort(String port) {
		setPropertyValue(BARCODE_SCANNER_PORT_PROPERTY, port);
	}

	public static String loadReceiptPath() {
		return getPropertyValue(PATH_RECEIPT_PROPERTY);
	}

	public static void setReceiptPath(String path) {
		setPropertyValue(PATH_RECEIPT_PROPERTY, path);
	}
	
	public static String loadIPSQL() {
		return getPropertyValue(IPSQL_PROPERTY);
	}
	
	public static void setIPSQL(String ip) {
		setPropertyValue(IPSQL_PROPERTY, ip);
	}
	
	public static int loadCashRegisterNumber() {
		try {
			return Integer.parseInt(getPropertyValue(CASH_REGISTER_NUMBER_PROPERTY));
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void setCashRegisterNumber(String num) {
		setPropertyValue(CASH_REGISTER_NUMBER_PROPERTY, num);
	}

	public static int loadCashRegistersAutoRefresh() {
		try {
			return Integer.parseInt(getPropertyValue(CASH_REGISTERS_AUTO_REFRESH_PROPERTY));
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void setCashRegistersAutoRefresh(String sec) {
		setPropertyValue(CASH_REGISTERS_AUTO_REFRESH_PROPERTY, sec);
	}
	
	public static void createPropFile(File dir, String scannerPort, String ipSQL, String pathReceipt, String poklNum, String autoRefresh) {
		Properties prop = new Properties();
		try {
			prop.setProperty(BARCODE_SCANNER_PORT_PROPERTY, scannerPort);
			prop.setProperty(IPSQL_PROPERTY, ipSQL);
			prop.setProperty(PATH_RECEIPT_PROPERTY, pathReceipt);
			prop.setProperty(CASH_REGISTER_NUMBER_PROPERTY, poklNum);
			prop.setProperty(CASH_REGISTERS_AUTO_REFRESH_PROPERTY, autoRefresh);
			prop.store(new FileOutputStream(dir.getAbsolutePath() + File.separator + FILE_SETTINGS), getCommentText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int isInvalid() {
		try {
			new Properties().load(new FileInputStream(cestaKuKlientovi + File.separator + FILE_SETTINGS));
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
