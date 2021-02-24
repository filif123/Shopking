
package sk.shopking.tools;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sk.shopking.*;

import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;


/**
 * Pracuje s SQL serverom
 * @author Filip
 *
 */
public class Database {

	private static final SKLog LOGGER = SKLog.getLogger();
	/**
	 * Adresa na server
	 */
	public static String addressToServer = AppSettings.loadIPSQL();
	/**
	 * Názov databázy
	 */
	public static final String DATABASE_NAME = "shopking";

	private static final String LOGIN_NAME = "app";
	private static final String LOGIN_PASSWORD = "BYGYNHckXzN7I4ad";
	private static final String ROOT_PASSWORD = "hesloroot";

	/**
	 * Zistí, či sa dá na daný server pripojiť
	 * @param url - adresa servera
	 * @param name - meno pouivatela
	 * @param password - heslo pouzivatela
	 * @return <strong>true</strong>, ak je spojenie s databázou úspešné
	 */
	public static boolean isServerExists(String url,String name, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.getConnection("jdbc:mysql://" + url, name, password);
		} catch (Exception ex) {
			return false;
		}
		return true;

	}

	
	/**
	 * Zistí, či sa dá na danú databázu pripojiť
	 * @return <strong>true</strong>, ak je spojenie s databázou úspešné
	 */
	public static boolean isConnectionAvailable() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.getConnection("jdbc:mysql://" + addressToServer + "/" + DATABASE_NAME + "?zeroDateTimeBehavior=convertToNull", LOGIN_NAME, LOGIN_PASSWORD);
        } catch (Exception ex) {
            return false;
        }
        return true;
        
	}
	/**
	 * Vráti komunikáciu so serverom
	 * @return komunikáciu
	 */
	public static Connection getConnectionToServer() {
		Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + addressToServer, LOGIN_NAME, LOGIN_PASSWORD);
        } catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.severe(ex.toString());
        }
        return con;
	}

	/**
	 * Vráti komunikáciu so serverom
	 * @param url - adresa servera
	 * @param name - meno pouivatela
	 * @param password - heslo pouzivatela
	 * @return komunikáciu
	 */
	public static Connection getConnectionToServer(String url, String name, String password) {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + url, name, password);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.severe(ex.toString());
		}
		return con;
	}

	/**
	 * Vráti komunikáciu s databázou
	 * @return komunikáciu
	 */
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + addressToServer + "/" + DATABASE_NAME + "?zeroDateTimeBehavior=convertToNull", LOGIN_NAME, LOGIN_PASSWORD);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.severe(ex.toString());
		}
		return con;
	}
	
	/**
	 * Vykoná zadaný script
	 * @param stream - súbor, obsahujúci script
	 * @return <strong>true</strong> v prípade úspechu alebo <strong>false</strong> v prípade výnimky alebo chyby
	 */
	public static boolean runScriptDB(InputStream stream) {
		
		try(Connection conn = getConnectionToServer()) {
			ScriptRunner sr = new ScriptRunner(conn, false, false);
			sr.runScript(new BufferedReader(new InputStreamReader(stream)));
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
			return false;
			
		}
	}

	/**
	 * Urobí potrebné úkony so serverom (pri instalácii servera)
	 *
	 */
	public static void prepareServer(){
		String query1 = "CREATE USER ? @'%' IDENTIFIED BY ?";
		String query2 = "GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, FILE, INDEX, ALTER, CREATE TEMPORARY TABLES, CREATE VIEW, EVENT, TRIGGER, SHOW VIEW, CREATE ROUTINE, ALTER ROUTINE, EXECUTE ON *.* TO 'app'@'%' REQUIRE NONE WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0";
		String query3 = "SET PASSWORD FOR 'root'@'localhost' = PASSWORD(?)";

		//1.krok - prihlasenie ako root a vytvorenie usera "app"
		try(Connection conn = getConnectionToServer("localhost","root","")) {
			PreparedStatement ps = conn.prepareStatement(query1);

			ps.setString(1, "app");
			ps.setString(2, "BYGYNHckXzN7I4ad");

			ps.execute();

		} catch (Exception e) {
			e.printStackTrace();

		}

		//2.krok - prihlasenie ako root a pridelenie prav userovi "app"
		try(Connection conn = getConnectionToServer("localhost","root","")) {
			Statement st = conn.createStatement();
			st.executeQuery(query2);

		} catch (Exception e) {
			e.printStackTrace();

		}

		//3.krok - prihlasenie ako root a zmena hesla userovi "root - localhost"
		try(Connection conn = getConnectionToServer("localhost","root","")) {
			PreparedStatement ps = conn.prepareStatement(query3);
			ps.setString(1, ROOT_PASSWORD);
			ps.execute();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * Zmeni heslo usera "root"
	 * @param url - adresa databazy
	 * @param password - nove heslo
	 */
	public static void changePassword(String url, String password){
		String query = "SET PASSWORD FOR 'root'@'localhost' = PASSWORD(?)";

		try(Connection conn = getConnectionToServer(url,"root",ROOT_PASSWORD)) {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, password);
			ps.execute();

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
	}

	/**
	 * Odstrani usera
	 * @param url - adresa databazy
	 * @param name - meno pouzivatela
	 */
	public static void deleteDBUser(String url, String name){
		String query = "DROP USER ? @'%'";

		try(Connection conn = getConnectionToServer(url,"root",ROOT_PASSWORD)) {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.execute();

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
	}

	/**
	 * Premaže celú databázu (okrem informácií o obchode)
	 * @return <strong>true</strong> v prípade úspechu alebo <strong>false</strong> v prípade výnimky alebo chyby
	 */
	public static boolean truncateDB() {
		UserAdministrator administrator = (UserAdministrator) getSpecificUser(0);
		String categories = "TRUNCATE `categories`";
		String denneZavierky = "TRUNCATE `denne_zavierky`";
		String mesacneZavierky = "TRUNCATE `mesacne_zavierky`";
		String rocneZavierky = "TRUNCATE `rocne_zavierky`";
		String doklady = "TRUNCATE `doklady`";
		String pokladnice = "TRUNCATE `pokladnice`";
		String tovary = "TRUNCATE `tovary`";
		String nakupenyTovar = "TRUNCATE `nakupeny_tovar`";
		String users = "TRUNCATE `users`";

		try(Connection conn = getConnectionToServer()) {
			conn.setAutoCommit(false);
			PreparedStatement[] preparedStatements = new PreparedStatement[9];

			preparedStatements[0] = conn.prepareStatement(categories);
			preparedStatements[1] = conn.prepareStatement(denneZavierky);
			preparedStatements[2] = conn.prepareStatement(mesacneZavierky);
			preparedStatements[3] = conn.prepareStatement(rocneZavierky);
			preparedStatements[4] = conn.prepareStatement(doklady);
			preparedStatements[5] = conn.prepareStatement(pokladnice);
			preparedStatements[6] = conn.prepareStatement(tovary);
			preparedStatements[7] = conn.prepareStatement(nakupenyTovar);
			preparedStatements[8] = conn.prepareStatement(users);

			for (int i = 0;i < 9;i++){
				preparedStatements[i].executeUpdate();
			}
			conn.commit();

			List<User> newUsers = new ArrayList<>();
			newUsers.add(administrator);
			addUsers(newUsers);

			return true;
		} catch (Exception e) {
			LOGGER.severe(e.toString());
			return false;
		}
	}

	/**
	 * Vymaže celú databázu
	 * @return <strong>true</strong> v prípade úspechu alebo <strong>false</strong> v prípade výnimky alebo chyby
	 */
	public static boolean deleteDB() {
		String query = "DROP DATABASE `shopking`";
		try(Connection conn = getConnectionToServer(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
			return false;
		}
	}
	
	/**
	 * Pridá kategórie tovaru v liste do databázy
	 * @param categories - nové kategórie tovaru
	 */
	public static void addCategories(List<Category> categories) {
		String query = "INSERT INTO categories SET nazov=?,pristupne_pre_mladistvych=?,povolene_stravne_listky=?";
		for (Category category : categories) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, category.getCategoryName());
				ps.setBoolean(2, category.getCategoryPristupnePreMladistvych());
                ps.setBoolean(3, category.getCategoryPovoleneStravneListky());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Vráti všetky kategórie tovaru, ktoré sa nachádzajú v databáze
	 * @return kategórie tovarov z databázy
	 */
	public static List<Category> getCategories() {
		List<Category> categoriesFromDBList = new ArrayList<>();
		ResultSet rSet;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM categories");
    		while (rSet.next()) {
        		int id = rSet.getInt("id");
    			String nazov = rSet.getString("nazov");
				boolean pristupnePreMladistvych =  rSet.getBoolean("pristupne_pre_mladistvych");
                boolean povoleneStravneListky =  rSet.getBoolean("povolene_stravne_listky");
				boolean deleted = rSet.getBoolean("deleted");
				if (!deleted) {
					Category category = new Category(id,nazov, pristupnePreMladistvych,povoleneStravneListky);
					categoriesFromDBList.add(category);
				}
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return categoriesFromDBList;
	}
	
	/**
	 * Vráti všetky kategórie tovaru, ktoré sa nachádzajú v databáze + aj tie, ktoré boli vymazané
	 * @return kategórie tovarov z databázy
	 */
	public static List<Category> getCategoriesPlusDeleted() {
		List<Category> categoriesFromDBList = new ArrayList<>();
		ResultSet rSet;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM categories");
    		while (rSet.next()) {
        		int id = rSet.getInt("id");
    			String nazov = rSet.getString("nazov");
				boolean pristupnePreMladistvych =  rSet.getBoolean("pristupne_pre_mladistvych");
                boolean povoleneStravneListky =  rSet.getBoolean("povolene_stravne_listky");
				Category category = new Category(id,nazov, pristupnePreMladistvych,povoleneStravneListky);
				categoriesFromDBList.add(category);
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return categoriesFromDBList;
	}
	
	
	/**
	 * Upraví zadané kategórie tovaru v databáze
	 * @param categories - všetky kategórie pre úpravu
	 */
	public static void updateCategories(List<Category> categories) {
		String query = "UPDATE categories SET nazov=?,pristupne_pre_mladistvych=?,povolene_stravne_listky=? WHERE id=?";
		for (Category category : categories) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, category.getCategoryName());
				ps.setBoolean(2, category.getCategoryPristupnePreMladistvych());
                ps.setBoolean(3, category.getCategoryPovoleneStravneListky());
				ps.setInt(4, category.getCategoryID());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Vymaže zadané kategórie z databázy (nastaví im hodnotu deleted = true)
	 * @param categories - všetky kategórie pre odstránenie
	 */
	public static void deleteCategories(List<Category> categories) {
		String query = "UPDATE categories SET deleted=? WHERE id=?";
		for (Category category : categories) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setBoolean(1, true);
				ps.setInt(2, category.getCategoryID());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
		
	
	/**
	 * Vráti kategóriu z databázy podľa id
	 * @param id kategórie 
	 * @return kategóriu tovarov na zadanom id
	 */
	public static Category getSpecificCategory(int id) {
		Category category= null;
		String query = "SELECT * FROM categories WHERE id=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,id);
		    
		    ResultSet rSet = ps.executeQuery();
			
    		while (rSet.next()) {
    			String nazov = rSet.getString("nazov");
				boolean pristupnePreMladistvych =  rSet.getBoolean("pristupne_pre_mladistvych");
                boolean povoleneStravneListky =  rSet.getBoolean("povolene_stravne_listky");
				boolean deleted = rSet.getBoolean("deleted");
				if (!deleted) {
					category = new Category(id,nazov, pristupnePreMladistvych,povoleneStravneListky);
				}
				
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return category;
	}
	
	/**
	 * Vráti kategóriu z databázy podľa id (pričom kategória mohla byť aj vymazaná)
	 * @param id kategórie 
	 * @return kategóriu tovarov na zadanom id
	 */
	public static Category getSpecificCategoryPlusDeleted(int id) {
		Category category= null;
		String query = "SELECT * FROM categories WHERE id=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,id);
		    
		    ResultSet rSet = ps.executeQuery();
			
    		while (rSet.next()) {
    			String nazov = rSet.getString("nazov");
				boolean pristupnePreMladistvych =  rSet.getBoolean("pristupne_pre_mladistvych");
                boolean povoleneStravneListky =  rSet.getBoolean("povolene_stravne_listky");
				category = new Category(id,nazov, pristupnePreMladistvych,povoleneStravneListky);
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return category;
	}
	
	/**
	 * Pridá tovary v liste do databázy
	 * @param tovary - nové tovary
	 */
	public static void addTovary(List<Tovar> tovary) {
		String query = "INSERT INTO tovary SET nazov=?,cena=?,dph=?,ean=?,jednotka=?,id_category=?,zlava_nova_cena=?,zlava_povodne_mnozstvo=?,zlava_nove_mnozstvo=?,zlava_min_mnozstvo=?";
		for (Tovar tovar : tovary) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, tovar.getTovarName());
				ps.setFloat(2, tovar.getTovarJednotkovaCena());
				if (tovar.getTovarDPH().equals(DPHType.DPH_10)) {
					ps.setString(3, "DPH_10");
				}
				else {
					ps.setString(3, "DPH_20");
				}
				ps.setString(4, "" + tovar.getTovarEAN());
				ps.setString(5, tovar.getTovarJednotka().toString());
				ps.setInt(6, tovar.getTovarCategory().getCategoryID());
				if (tovar instanceof TovarZlavaCena) {
					ps.setFloat(7, ((TovarZlavaCena) tovar).getNovaCena());
					ps.setNull(8, Types.INTEGER);
					ps.setNull(9, Types.INTEGER);
					ps.setNull(10, Types.INTEGER);
				}
				else if (tovar instanceof TovarZlavaMnozstvo) {
					ps.setNull(7, Types.FLOAT);
					ps.setFloat(8, ((TovarZlavaMnozstvo) tovar).getPovodneMnozstvo());
					ps.setFloat(9, ((TovarZlavaMnozstvo) tovar).getNoveMnozstvo());
					ps.setFloat(10, ((TovarZlavaMnozstvo) tovar).getMinimalneMnozstvo());
				}
				else {
					ps.setNull(7, Types.FLOAT);
					ps.setNull(8, Types.INTEGER);
					ps.setNull(9, Types.INTEGER);
					ps.setNull(10, Types.INTEGER);
				}
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
		
	}
	
	/**
	 * Vráti všetky tovary, ktoré sa nachádzajú v databáze
	 * @return tovary z databázy
	 */
	public static List<Tovar> getTovary() {
		List<Tovar> tovaryFromDBList = new ArrayList<>();
		
		ResultSet rSet;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM tovary");
    		while (rSet.next()) {
        		int plu = rSet.getInt("plu");
    			String nazov = rSet.getString("nazov");
    			float cena = rSet.getFloat("cena");
    			DPHType dphType = DPHType.valueOf(rSet.getString("dph"));
    			long ean = Long.parseLong(rSet.getString("ean"));
    			JednotkaType jednotkaType = JednotkaType.valueOf(rSet.getString("jednotka"));
				Category category = getSpecificCategory(rSet.getInt("id_category"));
				float zlavaNovaCena = rSet.getFloat("zlava_nova_cena");
				int zlavaPovodneMnozstvo = rSet.getInt("zlava_povodne_mnozstvo");
				int zlavaNoveMnozstvo = rSet.getInt("zlava_nove_mnozstvo");
				int zlavaMinimalneMnozstvo = rSet.getInt("zlava_min_mnozstvo");
				boolean deleted = rSet.getBoolean("deleted");
				if (!deleted) {
					if (zlavaNovaCena != 0) {
						TovarZlavaCena tovar = new TovarZlavaCena(plu,nazov,category,jednotkaType,ean,cena,dphType,zlavaNovaCena);
						tovaryFromDBList.add(tovar);
					}
					else if (zlavaPovodneMnozstvo != 0 && zlavaNoveMnozstvo != 0 && zlavaMinimalneMnozstvo != 0) {
						TovarZlavaMnozstvo tovar = new TovarZlavaMnozstvo(plu,nazov,category,jednotkaType,ean,cena,dphType, zlavaPovodneMnozstvo, zlavaNoveMnozstvo, zlavaMinimalneMnozstvo);
						tovaryFromDBList.add(tovar);
					}
					else {
						Tovar tovar = new Tovar(plu,nazov,category,jednotkaType,ean,cena,dphType);
						tovaryFromDBList.add(tovar);
					}
				}
				
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return tovaryFromDBList;
	}
	
	/**
	 * Vráti všetky tovary, ktoré sa nachádzajú v databáze + aj tie, ktoré boli vymazané
	 * @return tovary z databázy
	 */
	public static List<Tovar> getTovaryPlusDeleted() {
		List<Tovar> tovaryFromDBList = new ArrayList<Tovar>();
		
		ResultSet rSet;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM tovary");
    		while (rSet.next()) {
        		int plu = rSet.getInt("plu");
    			String nazov = rSet.getString("nazov");
    			float cena = rSet.getFloat("cena");
    			DPHType dphType = DPHType.valueOf(rSet.getString("dph"));
    			long ean = Long.parseLong(rSet.getString("ean"));
    			JednotkaType jednotkaType = JednotkaType.valueOf(rSet.getString("jednotka"));
				Category category = getSpecificCategory(rSet.getInt("id_category"));
				float zlavaNovaCena = rSet.getFloat("zlava_nova_cena");
				int zlavaPovodneMnozstvo = rSet.getInt("zlava_povodne_mnozstvo");
				int zlavaNoveMnozstvo = rSet.getInt("zlava_nove_mnozstvo");
				int zlavaMinimalneMnozstvo = rSet.getInt("zlava_min_mnozstvo");
				if (zlavaNovaCena != 0) {
					TovarZlavaCena tovar = new TovarZlavaCena(plu,nazov,category,jednotkaType,ean,cena,dphType,zlavaNovaCena);
					tovaryFromDBList.add(tovar);
				}
				else if (zlavaPovodneMnozstvo != 0 && zlavaNoveMnozstvo != 0 && zlavaMinimalneMnozstvo != 0) {
					TovarZlavaMnozstvo tovar = new TovarZlavaMnozstvo(plu,nazov,category,jednotkaType,ean,cena,dphType, zlavaPovodneMnozstvo, zlavaNoveMnozstvo, zlavaMinimalneMnozstvo);
					tovaryFromDBList.add(tovar);
				}
				else {
					Tovar tovar = new Tovar(plu,nazov,category,jednotkaType,ean,cena,dphType);
					tovaryFromDBList.add(tovar);
				}
				
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return tovaryFromDBList;
	}
	
	/**
	 * Upraví zadané tovary v databáze
	 * @param tovary - všetky tovary pre úpravu
	 */
	public static void updateTovary(List<Tovar> tovary) {
		String query = "UPDATE tovary SET nazov=?,cena=?,dph=?,ean=?,jednotka=?,id_category=?,zlava_nova_cena=?,zlava_povodne_mnozstvo=?,zlava_nove_mnozstvo=?,zlava_min_mnozstvo=? WHERE plu=?";
		for (Tovar tovar : tovary) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, tovar.getTovarName());
				ps.setFloat(2, tovar.getTovarJednotkovaCena());
				if (tovar.getTovarDPH().equals(DPHType.DPH_10)) {
					ps.setString(3, "DPH_10");
				}
				else {
					ps.setString(3, "DPH_20");
				}
				ps.setString(4, "" + tovar.getTovarEAN());
				ps.setString(5, tovar.getTovarJednotka().toString());
				ps.setInt(6, tovar.getTovarCategory().getCategoryID());
				if (tovar instanceof TovarZlavaCena) {
					ps.setFloat(7, ((TovarZlavaCena) tovar).getNovaCena());
					ps.setNull(8, Types.INTEGER);
					ps.setNull(9, Types.INTEGER);
					ps.setNull(10, Types.INTEGER);
				}
				else if (tovar instanceof TovarZlavaMnozstvo) {
					ps.setNull(7, Types.FLOAT);
					ps.setFloat(8, ((TovarZlavaMnozstvo) tovar).getPovodneMnozstvo());
					ps.setFloat(9, ((TovarZlavaMnozstvo) tovar).getNoveMnozstvo());
					ps.setFloat(10, ((TovarZlavaMnozstvo) tovar).getMinimalneMnozstvo());
				}
				else {
					ps.setNull(7, Types.FLOAT);
					ps.setNull(8, Types.INTEGER);
					ps.setNull(9, Types.INTEGER);
					ps.setNull(10, Types.INTEGER);
				}
				ps.setInt(11, tovar.getTovarPLU());
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Vymaže zadané tovary z databázy (nastaví im hodnotu deleted = true)
	 * @param tovary - všetky tovary pre odstránenie
	 */
	public static void deleteTovary(List<Tovar> tovary) {
		String query = "UPDATE tovary SET deleted=? WHERE plu=?";
		for (Tovar tovar : tovary) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setBoolean(1, true);
				ps.setInt(2, tovar.getTovarPLU());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Vráti tovar z databázy podľa id
	 * @param plu tovaru 
	 * @return tovar
	 */
	public static Tovar getSpecificTovar(int plu) {
		Tovar tovar = null;
		String query = "SELECT * FROM tovary WHERE plu=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,plu);
		    
		    ResultSet rSet = ps.executeQuery();
    		while (rSet.next()) {
    			String nazov = rSet.getString("nazov");
    			float cena = rSet.getFloat("cena");
    			DPHType dphType = DPHType.valueOf(rSet.getString("dph"));
    			long ean = Long.parseLong(rSet.getString("ean"));
    			JednotkaType jednotkaType = JednotkaType.valueOf(rSet.getString("jednotka"));
				Category category =  getSpecificCategory(rSet.getInt("id_category"));
				float zlavaNovaCena = rSet.getFloat("zlava_nova_cena");
				int zlavaPovodneMnozstvo = rSet.getInt("zlava_povodne_mnozstvo");
				int zlavaNoveMnozstvo = rSet.getInt("zlava_nove_mnozstvo");
				int zlavaMinimalneMnozstvo = rSet.getInt("zlava_min_mnozstvo");
				boolean deleted = rSet.getBoolean("deleted");
				if (!deleted) {
					if (zlavaNovaCena != 0) {
						tovar = new TovarZlavaCena(plu,nazov,category,jednotkaType,ean,cena,dphType,zlavaNovaCena);
					}
					else if (zlavaPovodneMnozstvo != 0 && zlavaNoveMnozstvo != 0 && zlavaMinimalneMnozstvo != 0) {
						tovar = new TovarZlavaMnozstvo(plu,nazov,category,jednotkaType,ean,cena,dphType, zlavaPovodneMnozstvo, zlavaNoveMnozstvo, zlavaMinimalneMnozstvo);
					}
					else {
						tovar = new Tovar(plu,nazov,category,jednotkaType,ean,cena,dphType);
					}
				}
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return tovar;
	}
	
	/**
	 * Vráti tovar z databázy podľa id (pričom tovar mohol byť aj vymazaný)
	 * @param plu tovaru 
	 * @return tovar
	 */
	public static Tovar getSpecificTovarPlusDeleted(int plu) {
		Tovar tovar= null;
		String query = "SELECT * FROM tovary WHERE plu=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,plu);
		    
		    ResultSet rSet = ps.executeQuery();
    		while (rSet.next()) {
    			String nazov = rSet.getString("nazov");
    			float cena = rSet.getFloat("cena");
    			DPHType dphType = DPHType.valueOf(rSet.getString("dph"));
    			long ean = Long.parseLong(rSet.getString("ean"));
    			JednotkaType jednotkaType = JednotkaType.valueOf(rSet.getString("jednotka"));
				Category category =  getSpecificCategory(rSet.getInt("id_category"));
				float zlavaNovaCena = rSet.getFloat("zlava_nova_cena");
				int zlavaPovodneMnozstvo = rSet.getInt("zlava_povodne_mnozstvo");
				int zlavaNoveMnozstvo = rSet.getInt("zlava_nove_mnozstvo");
				int zlavaMinimalneMnozstvo = rSet.getInt("zlava_min_mnozstvo");
				if (zlavaNovaCena != 0) {
					tovar = new TovarZlavaCena(plu,nazov,category,jednotkaType,ean,cena,dphType,zlavaNovaCena);
				}
				else if (zlavaPovodneMnozstvo != 0 && zlavaNoveMnozstvo != 0 && zlavaMinimalneMnozstvo != 0) {
					tovar = new TovarZlavaMnozstvo(plu,nazov,category,jednotkaType,ean,cena,dphType, zlavaPovodneMnozstvo, zlavaNoveMnozstvo, zlavaMinimalneMnozstvo);
				}
				else {
					tovar = new Tovar(plu,nazov,category,jednotkaType,ean,cena,dphType);
				}
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return tovar;
	}
	
	/**
	 * Pridá používateľov do databázy
	 * @param users - nový pouźívatelia
	 */
	public static void addUsers(List<User> users) {
		String query = "INSERT INTO users SET meno=?,priezvisko=?,nickname=?,hash=?,usertype=?,suma_v_pokladnici=?";
		for (User user : users) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, user.getUserMeno());
				ps.setString(2, user.getUserPriezvisko());
				ps.setString(3, user.getUserNickname());
				ps.setLong(4, user.getHashHeslo());
				if(user instanceof UserPokladnik) {
					ps.setString(5, "POKLADNIK");
					ps.setFloat(6, ((UserPokladnik) user).getSumaPokladnica());
				}
				else {
					ps.setString(5, "ADMIN");
					ps.setFloat(6, 0);
				}
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}

	/**
	 * Vráti používateľa z databázy podla jeho použivateľského mena a hesla
	 * @param username prihlasovacie meno
	 * @param password heslo pouzivatela
	 * @return používateľa z databázy, alebo NULL, ak neexistuje zhoda
	 */
	public static User loginUser(String username,String password) {
		String query = "SELECT * FROM users WHERE nickname=? AND hash=?";
		int hash = User.hashPassword(password);
		User prihlaseny = null;

		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1,username);
			ps.setInt(2,hash);

			ResultSet rSet = ps.executeQuery();

			while (rSet.next()) {
				int id = rSet.getInt("id");
				String meno = rSet.getString("meno");
				String priezvisko =  rSet.getString("priezvisko");
				String nickname =  rSet.getString("nickname");
				float sumaFloat = rSet.getFloat("suma_v_pokladnici");
				boolean deleted = rSet.getBoolean("deleted");
				UserType userType = UserType.valueOf(rSet.getString("usertype"));
				if(!deleted) {
					if (userType.equals(UserType.ADMIN)) {
						prihlaseny = new UserAdministrator(id,meno,priezvisko,nickname,hash);
					}
					else {
						prihlaseny = new UserPokladnik(id,meno,priezvisko,nickname,hash,sumaFloat);
					}
				}
			}
			rSet.close();

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return prihlaseny;
	}
	
	/**
	 * Vráti všetkých používateľov, ktorí sú uložený v databáze
	 * @return používateľov z databázy
	 */
	public static List<User> getUsers() {
		List<User> usersFromDBList = new ArrayList<User>();
		ResultSet rSet;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM users");
			while (rSet.next()) {
        		int id = rSet.getInt("id");
    			String meno = rSet.getString("meno");
    			String priezvisko =  rSet.getString("priezvisko");
    			String nickname =  rSet.getString("nickname");
    			int hash = rSet.getInt("hash");
    			float sumaFloat = rSet.getFloat("suma_v_pokladnici");
    			boolean deleted = rSet.getBoolean("deleted");
    			UserType userType = UserType.valueOf(rSet.getString("usertype"));
    			if(!deleted) {
    				if (userType.equals(UserType.ADMIN)) {
    					usersFromDBList.add(new UserAdministrator(id,meno,priezvisko,nickname,hash));
					}
    				else {
    					usersFromDBList.add(new UserPokladnik(id,meno,priezvisko,nickname,hash,sumaFloat));
    				}
    			}
    		}
    		rSet.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());

		}
		return usersFromDBList;
	}
	
	/**
	 * Vráti všetkých používateľov (aj vymazaných), ktorí sú uložený v databáze
	 * @return všetkých používateľov z databázy
	 */
	public static List<User> getUsersPlusDeleted() {
		List<User> usersFromDBList = new ArrayList<>();
		ResultSet rSet;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM users");
			while (rSet.next()) {
        		int id = rSet.getInt("id");
    			String meno = rSet.getString("meno");
    			String priezvisko =  rSet.getString("priezvisko");
    			String nickname =  rSet.getString("nickname");
    			int hash = rSet.getInt("hash");
    			float sumaFloat = rSet.getFloat("suma_v_pokladnici");
    			UserType userType = UserType.valueOf(rSet.getString("usertype"));
    			if (userType.equals(UserType.ADMIN)) {
    				usersFromDBList.add(new UserAdministrator(id,meno,priezvisko,nickname,hash));
				}
    			else {
    				usersFromDBList.add(new UserPokladnik(id,meno,priezvisko,nickname,hash,sumaFloat));
    			}
    		}
    		rSet.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return usersFromDBList;
	}
	
	/**
	 * Upraví zadaných používateľov v databáze
	 * @param users - všetci používatelia pre úpravu
	 */
	public static void updateUsers(List<User> users) {
		String query = "UPDATE users SET meno=?,priezvisko=?,nickname=?,hash=?,usertype=?,suma_v_pokladnici=? WHERE id=?";
		for (User user : users) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, user.getUserMeno());
				ps.setString(2, user.getUserPriezvisko());
				ps.setString(3, user.getUserNickname());
				ps.setLong(4, user.getHashHeslo());
				if(user instanceof UserPokladnik) {
					ps.setString(5, "POKLADNIK");
					ps.setFloat(6, ((UserPokladnik) user).getSumaPokladnica());
				}
				else {
					ps.setString(5, "ADMIN");
					ps.setFloat(6, 0);
				}
				ps.setInt(7, user.getId());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Vymaže zadaných používateľov z databázy (nastaví im hodnotu deleted = true)
	 * @param users - všetci používatelia pre odstránenie
	 */
	public static void deleteUsers(List<User> users) {
		String query = "UPDATE users SET deleted=? WHERE id=?";
		for (User user : users) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setBoolean(1, true);
				ps.setInt(2, user.getId());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Vráti používateľa z databázy podľa id
	 * @return používateľa na zadanom id
	 */
	public static User getSpecificUser(int id) {
		User user = null;
		String query = "SELECT * FROM users WHERE id=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,id);
		    
		    ResultSet rSet = ps.executeQuery();

    		while (rSet.next()) {
    			String meno = rSet.getString("meno");
    			String priezvisko =  rSet.getString("priezvisko");
    			String nickname =  rSet.getString("nickname");
    			int hash = rSet.getInt("hash");
    			float sumaFloat = rSet.getFloat("suma_v_pokladnici");
    			boolean deleted = rSet.getBoolean("deleted");
    			UserType userType = UserType.valueOf(rSet.getString("usertype"));
    			if(!deleted) {
    				if (userType.equals(UserType.ADMIN)) {
    					user = new UserAdministrator(id,meno,priezvisko,nickname,hash);
					}
    				else {
    					user = new UserPokladnik(id,meno,priezvisko,nickname,hash,sumaFloat);
    				}
    			}
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return user;
	}
	
	/**
	 * Vráti používateľa z databázy podľa id (pričom používateľ môže byť aj vymazaný)
	 * @return používateľa na zadanom id
	 */
	public static User getSpecificUserPlusDeleted(int id) {
		User user = null;
		String query = "SELECT * FROM users WHERE id=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,id);
		    
		    ResultSet rSet = ps.executeQuery();

    		while (rSet.next()) {
    			String meno = rSet.getString("meno");
    			String priezvisko =  rSet.getString("priezvisko");
    			String nickname =  rSet.getString("nickname");
    			int hash = rSet.getInt("hash");
    			float sumaFloat = rSet.getFloat("suma_v_pokladnici");
    			UserType userType = UserType.valueOf(rSet.getString("usertype"));
    			if (userType.equals(UserType.ADMIN)) {
    				user = new UserAdministrator(id,meno,priezvisko,nickname,hash);
				}
    			else {
    				user = new UserPokladnik(id,meno,priezvisko,nickname,hash,sumaFloat);
    			}
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return user;
	}
	
	/**
	 * Vráti informácie o obchode (o spoločnosti a prevádzke)
	 * @return informácie o obchode
	 */
	public static Obchod infoObchod() {
		Obchod obchod = null;
		ResultSet rSet;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM obchod");
    		while (rSet.next()) {
    			String obchodnyNazov = rSet.getString("obchodny_nazov");
    			String nazovObchodu = rSet.getString("nazov_obchodu");
    			String sidloMesto =  rSet.getString("sidlo_mesto");
    			String sidloUlica =  rSet.getString("sidlo_ulica");
    			String sidloPSC =  rSet.getString("sidlo_psc");
    			String sidloCislo =  rSet.getString("sidlo_cislo");
    			String ico =  rSet.getString("ico");
    			String dic =  rSet.getString("dic");
    			String icDPH =  rSet.getString("icdph");
    			String prevadzkaMesto =  rSet.getString("prevadzka_mesto");
    			String prevadzkaUlica =  rSet.getString("prevadzka_ulica");
    			String prevadzkaPSC =  rSet.getString("prevadzka_psc");
    			String prevadzkaCislo =  rSet.getString("prevadzka_cislo");
    			String logoPathString =  rSet.getString("logo_path");
    			
    			if (logoPathString != null) {
    				File logoFile = new File(logoPathString);
    				obchod = new Obchod(obchodnyNazov, nazovObchodu, sidloMesto, sidloUlica, sidloPSC, sidloCislo, ico, dic, icDPH, prevadzkaMesto, prevadzkaUlica, prevadzkaPSC, prevadzkaCislo,logoFile);
				}
    			else {
    				obchod = new Obchod(obchodnyNazov, nazovObchodu, sidloMesto, sidloUlica, sidloPSC, sidloCislo, ico, dic, icDPH, prevadzkaMesto, prevadzkaUlica, prevadzkaPSC, prevadzkaCislo,null);
				}
    			
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return obchod;
	}
	
	/**
	 * Uloží do databázy informácie o obchode
	 * @param obchod
	 */
	public static void createObchod(Obchod obchod) {
		String query = "INSERT INTO obchod SET obchodny_nazov=?,nazov_obchodu=?,sidlo_mesto=?,sidlo_ulica=?,sidlo_psc=?,sidlo_cislo=?,"
				+ "ico=?,dic=?,icdph=?,prevadzka_mesto=?,prevadzka_ulica=?,prevadzka_psc=?,prevadzka_cislo=?,logo_path=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, obchod.getObchodnyNazovFirmy());
			ps.setString(2, obchod.getNazovFirmy());
			ps.setString(3, obchod.getMestoFirmy());
			ps.setString(4, obchod.getUlicaFirmy());
			ps.setString(5, obchod.getPSCFirmy());
			ps.setString(6, obchod.getCisloPopisneFirmy());
			ps.setString(7, obchod.getICO());
			ps.setString(8, obchod.getDIC());
			ps.setString(9, obchod.getICDPH());
			ps.setString(10, obchod.getMestoPrevadzky());
			ps.setString(11, obchod.getUlicaPrevadzky());
			ps.setString(12, obchod.getPSCPrevadzky());
			ps.setString(13, obchod.getCisloPopisnePrevadzky());
			if(obchod.getLogoSpolocnosti() != null) {
				ps.setString(14, obchod.getLogoSpolocnosti().getAbsolutePath());
			}
			else {
				ps.setString(14, "");
			}
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Upraví v databáze informácie o obchode
	 * @param obchod so zmenou
	 */
	public static void editObchod(Obchod obchod) {
		String query = "UPDATE obchod SET obchodny_nazov=?,nazov_obchodu=?,sidlo_mesto=?,sidlo_ulica=?,sidlo_psc=?,sidlo_cislo=?,"
				+ "ico=?,dic=?,icdph=?,prevadzka_mesto=?,prevadzka_ulica=?,prevadzka_psc=?,prevadzka_cislo=?,logo_path=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, obchod.getObchodnyNazovFirmy());
			ps.setString(2, obchod.getNazovFirmy());
			ps.setString(3, obchod.getMestoFirmy());
			ps.setString(4, obchod.getUlicaFirmy());
			ps.setString(5, obchod.getPSCFirmy());
			ps.setString(6, obchod.getCisloPopisneFirmy());
			ps.setString(7, obchod.getICO());
			ps.setString(8, obchod.getDIC());
			ps.setString(9, obchod.getICDPH());
			ps.setString(10, obchod.getMestoPrevadzky());
			ps.setString(11, obchod.getUlicaPrevadzky());
			ps.setString(12, obchod.getPSCPrevadzky());
			ps.setString(13, obchod.getCisloPopisnePrevadzky());
			if(obchod.getLogoSpolocnosti() != null) {
				ps.setString(14, obchod.getLogoSpolocnosti().getAbsolutePath());
			}
			else {
				ps.setString(14, "");
			}
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
	}

	/**
	 * Vráti globálne nastavenia obchodu
	 * @return globálne nastavenia obchodu
	 */
	public static ShopSettings getShopSettings(){
		ShopSettings settings = new ShopSettings();
		ResultSet rSet;

		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM `global_settings`");
			while (rSet.next()) {
				float pocSuma = rSet.getFloat("pokl_poc_suma");
				float limSuma = rSet.getFloat("pokl_max_suma");
				settings.setPoklPociatocnaSuma(pocSuma);
				settings.setPoklMaxSuma(limSuma);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}

		return settings;
	}

	/**
	 * Uloží do databázy globálne nastavenia obchodu
	 * @param settings globálne nastavenia obchodu
	 */
	public static void createShopSettings(ShopSettings settings) {
		String query = "INSERT INTO global_settings SET pokl_poc_suma=?,pokl_max_suma=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setFloat(1, settings.getPoklPociatocnaSuma());
			ps.setFloat(2, settings.getPoklMaxSuma());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Upraví v databáze globálne nastavenia obchodu
	 * @param settings globálne nastavenia obchodu
	 */
	public static void editShopSettings(ShopSettings settings) {
		String query = "UPDATE global_settings SET pokl_poc_suma=?,pokl_max_suma=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setFloat(1, settings.getPoklPociatocnaSuma());
			ps.setFloat(2, settings.getPoklMaxSuma());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
	}

	/**
	 * Pridá záznamy o zadaných pokladniciach do databázy
	 * @param pokladnice - všetky pokladnice pre pridanie do databázy
	 */
	public static boolean addPokladnice(List<Pokladnica> pokladnice) {
		String query = "INSERT INTO pokladnice SET id=?,dkp=?";
		for (Pokladnica pokladnica : pokladnice) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setInt(1, pokladnica.getIdPokladnice());
				ps.setString(2, pokladnica.getDkpPokladnice());
				ps.executeUpdate();
			} catch (Exception e) {
				LOGGER.severe(e.toString());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Upraví zadané pokladnice v databáze
	 * @param pokladnice - pokladnice, ktoré sa má upraviť
	 * @param oldIDs - staré ID pokladníc pre úpravu
	 */
	public static void updatePokladnice(List<Pokladnica> pokladnice, List<Integer> oldIDs) {
		String query = "UPDATE pokladnice SET id=?,dkp=? WHERE id=?";
		for (int i = 0; i < pokladnice.size(); i++) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setInt(1, pokladnice.get(i).getIdPokladnice());
				ps.setString(2, pokladnice.get(i).getDkpPokladnice());
				ps.setInt(3, oldIDs.get(i));
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());

			}
		}

	}
	
	/**
	 * Vráti všetky pokladnice, ktorí sú uložený v databáze
	 * @return pokladnice z databázy
	 */
	public static List<Pokladnica> getPokladnice() {
		List<Pokladnica> pokladnice = new ArrayList<>();
		ResultSet rSet;
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM pokladnice");
    		while (rSet.next()) {
    			int id = rSet.getInt("id");
    			String dkp = rSet.getString("dkp");
    			boolean isOpen = rSet.getBoolean("otvorena");
    			UserPokladnik userPokladnik = (UserPokladnik) getSpecificUser(rSet.getInt("id_pokladnik"));
				String ip = rSet.getString("ip");
    			
    			Pokladnica tatoPokladnica = new Pokladnica(id,dkp,isOpen,userPokladnik,ip);
    			pokladnice.add(tatoPokladnica);
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return pokladnice;
	}
	
	/**
	 * Vymaže zadané pokladnice z databázy
	 * @param pokladnice - všetky pokladnice pre odstránenie
	 */
	public static void deletePokladnice(List<Pokladnica> pokladnice) {
		String query = "DELETE FROM pokladnice WHERE id=?";
		for (Pokladnica pokladnica : pokladnice) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setInt(1, pokladnica.getIdPokladnice());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Nastaví pokladnici v databáze tak, aby sa javila ako otvorená
	 * @param idPokladnik - id pokladníka
	 * @param idPokladnica - id pokladnic
	 * @param ip - ip adresa pokladnice
	 */
	public static void setPokladnicaOpen(int idPokladnik, int idPokladnica, String ip) {
		String query = "UPDATE pokladnice SET otvorena=?,id_pokladnik=?,ip=? WHERE id=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setBoolean(1, true);
			ps.setInt(2, idPokladnik);
			ps.setString(3, ip);
			ps.setInt(4, idPokladnica);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}	
	}
	
	/**
	 * Nastaví pokladnici v databáze tak, aby sa javila ako zatvorená
	 * @param idPokladnik - id pokladníka
	 * @param idPokladnica - id pokladnice
	 */
	public static void setPokladnicaClose(int idPokladnik,int idPokladnica) {
		String query = "UPDATE pokladnice SET otvorena=?,id_pokladnik=?,ip=? WHERE id=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setBoolean(1, false);
			ps.setNull(2, Types.INTEGER);
			ps.setNull(3, Types.VARCHAR);
			ps.setInt(4, idPokladnica);

			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}	
	}
	
	/**
	 * Nastaví pokladníkovu sumu v jeho pokladnici
	 * @param idPokladnik - id pokladníka
	 * @param suma - zmenená suma hotovosti v pokladnici
	 */
	public static void setStavPokladnice(int idPokladnik, float suma) {
		String query = "UPDATE users SET suma_v_pokladnici=? WHERE id=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setFloat(1, suma);
			ps.setInt(2, idPokladnik);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
	}
	
	/**
	 * Vráti sumu, ktorú má daný pokladník uloženú v pokladnici
	 * @param idPokladnik - id pokladníka
	 * @return sumu hotovosti pokladníka v pokladnici
	 */
	public static float getStavPokladnice(int idPokladnik) {
		float suma = 0f;
		String query = "SELECT suma_v_pokladnici FROM users WHERE id=?";
		
		try(Connection conn = getConnection()){
			
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,idPokladnik);
		    
		    ResultSet rs = ps.executeQuery();
			
    		while (rs.next()) {
    			suma = rs.getFloat("suma_v_pokladnici");
    		}
    		rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return suma;
	}
	
	/**
	 * Vytvorí záznam o novom nákupe do databázy
	 * @param nakup - nový nákup, ktorý má byť uložený do DB
	 */
	public static void setNewNakup(Nakup nakup) {
		List<NakupenyTovar> nakupeneTovary = nakup.getNakupenyTovar();
		String query = "INSERT INTO nakupeny_tovar SET mnozstvo=?,id_doklad=?,plu_tovar=?,jednotkova_cena=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			for (NakupenyTovar nakupenyTovar : nakupeneTovary) {
				ps.setFloat(1, nakupenyTovar.getNakupeneMnozstvo());
				ps.setInt(2, nakup.getDokladNakupu().getIdDoklad());
				ps.setInt(3, nakupenyTovar.getTovarPLU());
				ps.setFloat(4, nakupenyTovar.getTovarJednotkovaCena());
				
				ps.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
	}
	
	/**
	 * Stornuje nákupený tovar v danom nákupe podľa id dokladu, pričom sa NEstornuje celé množstvo stornovanej položky
	 * @param idDoklad - id dokladu
	 * @param nakupenyTovar - tovar, ktorý má byť stornovaný
	 * @param noveMnozstvo - mnozstvo, ktoré nebude stornované
	 * @param pokladnik - pokladník, ktorý vykonáva stornovanie
	 */
	public static void stornoNakupenyTovar(int idDoklad, NakupenyTovar nakupenyTovar, int noveMnozstvo, UserPokladnik pokladnik) {
		String query = "UPDATE nakupeny_tovar SET nove_mnozstvo=?,stornovane_kym=?,stornovane_kedy=? WHERE id_doklad=? && plu_tovar=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, noveMnozstvo);
			ps.setInt(2, pokladnik.getId());
			ps.setTimestamp(3, new Timestamp(new Date().getTime()));
			ps.setInt(4, idDoklad);
			ps.setInt(5, nakupenyTovar.getTovarPLU());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
	}
	
	/**
	 * Stornuje nákupený tovar v danom nákupe podľa id dokladu, pričom sa stornuje celé množstvo stornovanej položky
	 * @param idDoklad - id dokladu
	 * @param nakupenyTovar - tovar, ktorý má byť stornovaný
	 * @param pokladnik - pokladník, ktorý vykonáva stornovanie
	 */
	public static void stornoNakupenyTovar(int idDoklad, NakupenyTovar nakupenyTovar, UserPokladnik pokladnik) {
		String query = "UPDATE nakupeny_tovar SET stornovane_kym=?,stornovane_kedy=? WHERE id_doklad=? && plu_tovar=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, pokladnik.getId());
			ps.setTimestamp(2, new Timestamp(new Date().getTime()));
			ps.setInt(3, idDoklad);
			ps.setInt(4, nakupenyTovar.getTovarPLU());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}	
	}
	
	/**
	 * Vráti každý nakúpený tovar (VRATANE tých tovarov, ktoré boli vymazané a VRATANE tých nákupených tovarov, ktoré boli stornované)
	 * @return nákupy z databázy
	 */
	public static List<NakupenyTovar> getNakupenyTovar() {
		List<NakupenyTovar> nakupenyTovar = new ArrayList<>();
		String query = "SELECT * FROM nakupeny_tovar";
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    
		    ResultSet rs = ps.executeQuery();

    		while (rs.next()) {
    			float mnozstvo = rs.getFloat("mnozstvo");
    			int noveMnnozstvo = rs.getInt("nove_mnozstvo");
    			float jednotkovaCena = rs.getFloat("jednotkova_cena");
    			Tovar tovar = getSpecificTovarPlusDeleted(rs.getInt("plu_tovar"));
    			int stornovaneKym = rs.getInt("stornovane_kym");
    			//nestornovany tovar
    			if (stornovaneKym == 0) {
    				nakupenyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), mnozstvo));
				}
    			// stornovany tovar, ktory sa nestornoval cely
    			else if (noveMnnozstvo != 0) {
    				nakupenyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), noveMnnozstvo));
				}
				
    		}
    		rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return nakupenyTovar;
	}
	
	/**
	 * Vráti každý nakúpený tovar (OKREM tých tovarov, ktoré boli vymazané a OKREM tých nákupených tovarov, ktoré boli stornované) podľa id dokladu
	 * @param idDoklad - id dokladu
	 * @return nákupy z databázy
	 */
	public static List<NakupenyTovar> getNakupenyTovar(int idDoklad) {
		List<NakupenyTovar> nakupenyTovar = new ArrayList<>();
		String query = "SELECT * FROM nakupeny_tovar WHERE id_doklad=?";
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,idDoklad);
		    
		    ResultSet rs = ps.executeQuery();

    		while (rs.next()) {
    			float mnozstvo = rs.getFloat("mnozstvo");
    			int noveMnnozstvo = rs.getInt("nove_mnozstvo");
    			float jednotkovaCena = rs.getFloat("jednotkova_cena");
    			Tovar tovar = getSpecificTovar(rs.getInt("plu_tovar"));
    			int stornovaneKym = rs.getInt("stornovane_kym");
    			//ak dany tovar admini neodstranili z ponuky
    			if (tovar != null) {
        			//nestornovany tovar
        			if (stornovaneKym == 0) {
        				nakupenyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), mnozstvo));
    				}
        			// stornovany tovar, ktory sa nestornoval cely
        			else if (noveMnnozstvo != 0) {
        				nakupenyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), noveMnnozstvo));
					}
				}
    		}
    		rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return nakupenyTovar;
	}
	
	/**
	 * Vráti každý nakúpený tovar (VRATANE tých tovarov, ktoré boli vymazané a OKREM tých nákupených tovarov, ktoré boli stornované) podľa id dokladu
	 * @param idDoklad - id dokladu
	 * @return nákupy z databázy
	 */
	public static List<NakupenyTovar> getNakupenyTovarPlusDeletedTovar(int idDoklad) {
		List<NakupenyTovar> nakupenyTovar = new ArrayList<>();
		String query = "SELECT * FROM nakupeny_tovar WHERE id_doklad=?";
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,idDoklad);
		    
		    ResultSet rs = ps.executeQuery();

    		while (rs.next()) {
    			float mnozstvo = rs.getFloat("mnozstvo");
    			float jednotkovaCena = rs.getFloat("jednotkova_cena");
    			Tovar tovar = getSpecificTovarPlusDeleted(rs.getInt("plu_tovar"));
    			int stornovaneKym = rs.getInt("stornovane_kym");
    			//ak existuje len povodne mnozstvo a nikto ho nestornoval
    			if (stornovaneKym == 0) {
    				nakupenyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), mnozstvo));
				}	
    		}
    		rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return nakupenyTovar;
	}
	
	/**
	 * Vráti každý nakúpený tovar (VRATANE tých nakúpených tovarov, ktoré boli stornované a OKREM tovarov, ktoré boli vymazané) podľa id dokladu
	 * @param idDoklad - id dokladu
	 * @return nákupy z databázy
	 */
	public static List<NakupenyTovar> getNakupenyTovarPlusStorno(int idDoklad) {
		List<NakupenyTovar> nakupenyTovar = new ArrayList<>();
		String query = "SELECT * FROM nakupeny_tovar WHERE id_doklad=?";
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,idDoklad);
		    
		    ResultSet rs = ps.executeQuery();

    		while (rs.next()) {
    			float mnozstvo = rs.getFloat("mnozstvo");
    			float jednotkovaCena = rs.getFloat("jednotkova_cena");
    			Tovar tovar = getSpecificTovar(rs.getInt("plu_tovar"));
    			if (tovar != null) {
    				nakupenyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), mnozstvo));
				}
				
    		}
    		rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return nakupenyTovar;
	}
	
	/**
	 * Vráti každý nakúpený tovar (VRATANE tých nakúpených tovarov, ktoré boli stornované a VRATANE tovarov, ktoré boli vymazané) podľa id dokladu
	 * @param idDoklad - id dokladu
	 * @return nákupy z databázy
	 */
	public static List<NakupenyTovar> getNakupenyTovarPlusStornoAndDeleted(int idDoklad) {
		List<NakupenyTovar> nakupenyTovar = new ArrayList<>();
		String query = "SELECT * FROM nakupeny_tovar WHERE id_doklad=?";
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,idDoklad);
		    
		    ResultSet rs = ps.executeQuery();

    		while (rs.next()) {
    			float mnozstvo = rs.getFloat("mnozstvo");
    			float jednotkovaCena = rs.getFloat("jednotkova_cena");
    			Tovar tovar = getSpecificTovarPlusDeleted(rs.getInt("plu_tovar"));
        		nakupenyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), mnozstvo));

    		}
    		rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return nakupenyTovar;
	}
	
	/**
	 * Vráti nákup, ktorý bol vytvorení podľa daného dokladu
	 * @param idDoklad - id dokladu
	 * @return nákup z databázy podľa id dokladu
	 */
	public static Nakup getNakup(int idDoklad) {
		List<NakupenyTovar> nakupeneTovary = getNakupenyTovarPlusStornoAndDeleted(idDoklad);
		Doklad dokladNakupu = getDoklad(idDoklad);
		return new Nakup(nakupeneTovary, dokladNakupu);
	}
	
	/**
	 * Vytvorí nový nákupný doklad do databázy
	 * @return id nového dokladu
	 */
	public static int createNewDoklad(Date casNakupu,UserPokladnik pokladnik) {
		String query1 = "INSERT INTO doklady SET cas_nakupu=?,id_pokladnik=?";
		int generatedKey = 0;
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query1,Statement.RETURN_GENERATED_KEYS)) {
			ps.setTimestamp(1, new Timestamp(casNakupu.getTime()));
			ps.setInt(2, pokladnik.getId());
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			if (rs.next()) {
			    generatedKey = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return generatedKey;
	}
	
	/**
	 * Vráti všetky vytvorené nákupné doklady
	 * @return všetky doklady z databázy
	 */
	public static List<Doklad> getDoklady() {
		List<Doklad> vsetkyDoklady = new ArrayList<>();
		ResultSet rSet;
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM doklady");
    		while (rSet.next()) {
    			int id = rSet.getInt("id");
    			Date casNakupu = new Date(rSet.getTimestamp("cas_nakupu").getTime());
    			UserPokladnik pokladnik = (UserPokladnik) getSpecificUser(rSet.getInt("id_pokladnik"));
    			vsetkyDoklady.add(new Doklad(id, casNakupu, pokladnik));
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return vsetkyDoklady;
	}
	
	/**
	 * Vráti konkrétny nákupny doklad podľa id dokladu
	 * @param idDoklad - id dokladu
	 * @return doklad z databázy podľa jeho id
	 */
	public static Doklad getDoklad(int idDoklad) {
		Doklad doklad = null;
		String query = "SELECT * FROM doklady WHERE id=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,idDoklad);
		    
		    ResultSet rSet = ps.executeQuery();
   		while (rSet.next()) {
   			int id = rSet.getInt("id");
   			Date casNakupu = new Date(rSet.getTimestamp("cas_nakupu").getTime());
   			UserPokladnik pokladnik = (UserPokladnik) getSpecificUser(rSet.getInt("id_pokladnik"));
   			doklad = new Doklad(id, casNakupu, pokladnik);
   		}
   		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return doklad;
	}
	
	/**
	 * Vráti všetky nákupy, ktoré boli vykonané v danom dátume, zvoleným pokladníkom
	 * @param pokladnik - pokladník, ktorý robí závierku
	 * @param cas - čas, kedy robí pokladník závierku
	 * @return všetky nákupy, ktoré urobil daný pokladník v zadanom dátume
	 */
	public static List<Nakup> getNakupy(UserPokladnik pokladnik,Date cas) {
		List<Nakup> nakupyPokladnikaDnes = new ArrayList<>();
		List<Doklad> doklady = getDoklady();
		for (Doklad doklad : doklady) {
			if (doklad.getUserPokladnik().getId() == pokladnik.getId() && Utils.isSameDay(cas, doklad.getCasNakupu())) {
				Nakup nakup = getNakup(doklad.getIdDoklad());
				nakupyPokladnikaDnes.add(nakup);
			}
		}
		
		return nakupyPokladnikaDnes;
	}
	
	/**
	 * Vráti všetky nákupy, ktoré boli vykonané v danom dátume
	 * @param cas - čas, kedy robí pokladník závierku
	 * @return všetky nákupy, ktoré urobil daný pokladník v zadanom dátume
	 */
	public static List<Nakup> getNakupy(Date cas) {
		List<Nakup> nakupyDnes = new ArrayList<>();
		List<Doklad> doklady = getDoklady();
		for (Doklad doklad : doklady) {
			if (Utils.isSameDay(cas, doklad.getCasNakupu())) {
				Nakup nakup = getNakup(doklad.getIdDoklad());
				nakupyDnes.add(nakup);
			}
		}
		
		return nakupyDnes;
	}
	
	/**
	 * Vráti všetky vytvorené nákupy
	 * @return všetky nákupy
	 */
	public static List<Nakup> getNakupy() {
		List<Nakup> nakupy = new ArrayList<>();
		List<Doklad> doklady = getDoklady();
		for (Doklad doklad : doklady) {
			Nakup nakup = getNakup(doklad.getIdDoklad());
			nakupy.add(nakup);
		}
		
		return nakupy;
	}
	
	/**
	 * Urobí záznam dennej závierky do databázy
	 * @param pokladnik - pokladník, ktorý robí závierku
	 * @param cas - čas, kedy robí pokladník závierku
	 */
	public static void doDennaZavieka(UserPokladnik pokladnik, Date cas) {
		Timestamp casSQL = new Timestamp(cas.getTime());
		
		String query1 = "INSERT INTO denne_zavierky SET id_pokladnik=?,cas=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query1,Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, pokladnik.getId());
			ps.setTimestamp(2, casSQL);
			
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}

		
		String query2 = "UPDATE users SET vykonal_zavierku=? WHERE id=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query2)) {
			ps.setTimestamp(1, new Timestamp(cas.getTime()));
			ps.setInt(2, pokladnik.getId());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		
	}
	
	/**
	 * Vráti všetky denné závierky uložené v databáze
	 * @return denné závierky
	 */
	public static List<DennaZavierka> getDenneZavierky() {
		List<DennaZavierka> vsetkyDenneZavierky = new ArrayList<>();
		String query = "SELECT * FROM denne_zavierky";
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
		    while (rs.next()) {
		    	int id = rs.getInt("id");
		    	Date cas = new Date(rs.getTimestamp("cas").getTime());
		    	UserPokladnik pokladnik= (UserPokladnik) getSpecificUser(rs.getInt("id_pokladnik"));
		    	List<Nakup> nakupy = getNakupy(cas);
		    	vsetkyDenneZavierky.add(new DennaZavierka(id, nakupy, cas, pokladnik,getStornovanyTovarPokladnika(pokladnik, cas)));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return vsetkyDenneZavierky;
	}
	
	/**
	 * Vráti všetky denné závierky uložené v databáze podľa id dennej závierky
	 * @return denné závierky
	 */
	public static DennaZavierka getDennaZavierka(int id) {
		DennaZavierka dennaZavierka = null;
		String query = "SELECT * FROM denne_zavierky WHERE id=" + id;
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
		    while (rs.next()) {
		    	Date cas = new Date(rs.getTimestamp("cas").getTime());
		    	UserPokladnik pokladnik= (UserPokladnik) getSpecificUser(rs.getInt("id_pokladnik"));
		    	List<Nakup> nakupy = getNakupy(cas);
		    	dennaZavierka = new DennaZavierka(id, nakupy, cas, pokladnik,getStornovanyTovarPokladnika(pokladnik, cas));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return dennaZavierka;
	}
	
	/**
	 * Vráti všetky denné závierky uložené v databáze podľa id mesačnej závierky
	 * @return denné závierky
	 */
	public static List<DennaZavierka> getDenneZavierkyPodlaMesacnejZavierky(int id) {
		List<DennaZavierka> vsetkyDenneZavierky = new ArrayList<>();
		String query = "SELECT * FROM denne_zavierky WHERE id_mesacna_zavierka=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,id);
		    
		    ResultSet rs = ps.executeQuery();
		    while (rs.next()) {
		    	int idZ = rs.getInt("id");
		    	Date cas = new Date(rs.getTimestamp("cas").getTime());
		    	List<Nakup> nakupy = getNakupy(cas);
		    	UserPokladnik pokladnik= (UserPokladnik) getSpecificUser(rs.getInt("id_pokladnik"));
		    	vsetkyDenneZavierky.add(new DennaZavierka(idZ, nakupy, cas, pokladnik,getStornovanyTovarPokladnika(pokladnik, cas)));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return vsetkyDenneZavierky;
	}
	
	/**
	 * Vráti všetky denné závierky uložené v databáze v zadaný dátum
	 * @param datum - dátum hľadaných závierok
	 * @return denné závierky
	 */
	public static DenneZavierky getSkupinaDennychZavierok(Date datum) {
		List<DennaZavierka> vsetkyDenneZavierky = new ArrayList<>();
		String query = "SELECT * FROM denne_zavierky WHERE cas=?";
		Timestamp datumSQL = new Timestamp(datum.getTime());
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setTimestamp(1,datumSQL);
		    
		    ResultSet rs = ps.executeQuery();
		    while (rs.next()) {
		    	int id = rs.getInt("id");
		    	Date cas = new Date(rs.getTimestamp("cas").getTime());
		    	List<Nakup> nakupy = getNakupy(cas);
		    	UserPokladnik pokladnik= (UserPokladnik) getSpecificUser(rs.getInt("id_pokladnik"));
		    	vsetkyDenneZavierky.add(new DennaZavierka(id, nakupy, cas, pokladnik,getStornovanyTovarPokladnika(pokladnik, cas)));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return new DenneZavierky(vsetkyDenneZavierky);
	}
	
	/**
	 * Vráti všetky denné závierky uložené v databáze podľa dna
	 * @return denné závierky
	 */
	public static List<DenneZavierky> getSkupinyDennychZavierok() {
		List<DenneZavierky> vsetkySkupinyDennychZavierok = new ArrayList<>();
		List<DennaZavierka> vsetkyDenneZavierky = new ArrayList<>();
		Map<String, List<DennaZavierka>> skupinaZavierokMap = new TreeMap<>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		String query = "SELECT * FROM denne_zavierky";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    
		    ResultSet rs = ps.executeQuery();
		    while (rs.next()) {
		    	int id = rs.getInt("id");
		    	Date cas = new Date(rs.getTimestamp("cas").getTime());
		    	List<Nakup> nakupy = getNakupy(cas);
		    	UserPokladnik pokladnik= (UserPokladnik) getSpecificUser(rs.getInt("id_pokladnik"));
		    	vsetkyDenneZavierky.add(new DennaZavierka(id, nakupy, cas, pokladnik,getStornovanyTovarPokladnika(pokladnik, cas)));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		
		for (DennaZavierka dennaZavierka : vsetkyDenneZavierky) {
			skupinaZavierokMap.put(dateFormat.format(dennaZavierka.getCasZavierky()), new ArrayList<>());
		}
		
		for (DennaZavierka dennaZavierka : vsetkyDenneZavierky) {
			String dateString = dateFormat.format(dennaZavierka.getCasZavierky());
			List<DennaZavierka> getZavierky = skupinaZavierokMap.get(dateString);
			getZavierky.add(dennaZavierka);
			skupinaZavierokMap.put(dateString,getZavierky);
		}
		
		for (String key : skupinaZavierokMap.keySet()) {
			List<DennaZavierka> zavierkyVDanyDen = skupinaZavierokMap.get(key);
			vsetkySkupinyDennychZavierok.add(new DenneZavierky(zavierkyVDanyDen));
		}
		
		return vsetkySkupinyDennychZavierok;
	}
	
	/**
	 * Zapíše do databázy novú mesačnú závierku
	 * @param dateFrom - od ktorého dátumu sa robí mesačná závierka
	 * @param dateTo - do ktorého dátumu sa robí mesačná závierka
	 * @param now - dnešný dátum
	 */
	public static void doMesacnaZavierka(Date dateFrom, Date dateTo, Date now) {
		Timestamp casNow = new Timestamp(now.getTime());
		int idZavierky = -1;
		
		String query1 = "INSERT INTO mesacne_zavierky SET datum=?,interval_od=?,interval_do=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query1,Statement.RETURN_GENERATED_KEYS)) {
			ps.setTimestamp(1, casNow);
			ps.setDate(2, new java.sql.Date(dateFrom.getTime()));
			ps.setDate(3, new java.sql.Date(dateTo.getTime()));
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			if (rs.next()) {
			    idZavierky = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		
		List<DennaZavierka> vsetkyDenneZavierky = getDenneZavierky();
		List<DennaZavierka> vybraneZavierky = new ArrayList<>();
		for(DennaZavierka zavierka : vsetkyDenneZavierky) {
			if (dateFrom.compareTo(zavierka.getCasZavierky()) * zavierka.getCasZavierky().compareTo(dateTo) > 0) {
				vybraneZavierky.add(zavierka);
			}
		}
		
		for (DennaZavierka zavierka : vybraneZavierky) {
			String query2 = "UPDATE denne_zavierky SET id_mesacna_zavierka=? WHERE id=?";
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query2)) {
				ps.setInt(1, idZavierky);
				ps.setInt(2, zavierka.getIdZavierky());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Vráti všetky mesačné závierky uložené v databáze
	 * @return mesačné závierky
	 */
	public static List<MesacnaZavierka> getMesacneZavierky() {

		List<MesacnaZavierka> vsetkyMesacneZavierky = new ArrayList<>();
		String query = "SELECT * FROM mesacne_zavierky";
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
		    while (rs.next()) {
		    	int id = rs.getInt("id");
		    	Date cas = new Date(rs.getTimestamp("datum").getTime());
		    	Date intervalOd = new Date(rs.getDate("interval_od").getTime());
		    	Date intervalDo = new Date(rs.getDate("interval_do").getTime());
		    	List<DennaZavierka> denneZavierkyVTentoDen = getDenneZavierkyPodlaMesacnejZavierky(id);
		    	vsetkyMesacneZavierky.add(new MesacnaZavierka(id,denneZavierkyVTentoDen,cas,intervalOd,intervalDo));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return vsetkyMesacneZavierky;
	}
	
	/**
	 * Vráti všetky mesačné závierky uložené v databáze podľa id ročnej závierky
	 * @param id - id ročnej závierky
	 * @return mesačné závierky
	 */
	public static List<MesacnaZavierka> getMesacneZavierkyPodlaRocnejZavierky(int id) {
		List<MesacnaZavierka> vsetkyMesacneZavierky = new ArrayList<>();
		String query = "SELECT * FROM mesacne_zavierky WHERE id_rocna_zavierka=?";
		
		try(Connection conn = getConnection()){
			PreparedStatement ps = conn.prepareStatement(query);
		    ps.setInt(1,id);
		    
		    ResultSet rs = ps.executeQuery();
		    while (rs.next()) {
		    	int idZ = rs.getInt("id");
		    	Date cas = new Date(rs.getTimestamp("datum").getTime());
		    	Date intervalOd = new Date(rs.getDate("interval_od").getTime());
		    	Date intervalDo = new Date(rs.getDate("interval_do").getTime());
		    	List<DennaZavierka> denneZavierkyVDanyDen = getDenneZavierkyPodlaMesacnejZavierky(idZ);
		    	
		    	vsetkyMesacneZavierky.add(new MesacnaZavierka(idZ,denneZavierkyVDanyDen,cas,intervalOd,intervalDo));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return vsetkyMesacneZavierky;
	}
	
	/**
	 * Zapíše do databázy novú ročnú závierku
	 * @param dateFrom - od ktorého dátumu sa robí ročná závierka
	 * @param dateTo - do ktorého dátumu sa robí ročná závierka
	 * @param now - dnešný dátum
	 */
	public static void doRocnaZavierka(Date dateFrom, Date dateTo, Date now) {
		Timestamp casNow = new Timestamp(now.getTime());
		int idZavierky = -1;
		
		String query1 = "INSERT INTO rocne_zavierky SET datum=?,interval_od=?,interval_do=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query1,Statement.RETURN_GENERATED_KEYS)) {
			ps.setTimestamp(1, casNow);
			ps.setDate(2, new java.sql.Date(dateFrom.getTime()));
			ps.setDate(3, new java.sql.Date(dateTo.getTime()));
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			if (rs.next()) {
			    idZavierky = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());

		}
		
		List<DennaZavierka> vsetkyDenneZavierky = getDenneZavierky();
		List<DennaZavierka> vybraneZavierky = new ArrayList<>();
		for(DennaZavierka zavierka : vsetkyDenneZavierky) {
			if (dateFrom.compareTo(zavierka.getCasZavierky()) * zavierka.getCasZavierky().compareTo(dateTo) > 0) {
				vybraneZavierky.add(zavierka);
			}
		}
		
		for (DennaZavierka zavierka : vybraneZavierky) {
			String query2 = "UPDATE mesacne_zavierky SET id_rocna_zavierka=? WHERE id=?";
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query2)) {
				ps.setInt(1, idZavierky);
				ps.setInt(2, zavierka.getIdZavierky());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.severe(e.toString());
			}
		}
	}
	
	/**
	 * Vráti všetky ročné závierky uložené v databáze
	 * @return ročné závierky
	 */
	public static List<RocnaZavierka> getRocneZavierky() {

		List<RocnaZavierka> vsetkyRocneZavierky = new ArrayList<>();
		String query = "SELECT * FROM rocne_zavierky";
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
		    while (rs.next()) {
		    	int id = rs.getInt("id");
		    	Date cas = new Date(rs.getTimestamp("datum").getTime());
		    	Date intervalOd = new Date(rs.getDate("interval_od").getTime());
		    	Date intervalDo = new Date(rs.getDate("interval_do").getTime());
		    	List<MesacnaZavierka> mesacneZavierky = getMesacneZavierkyPodlaRocnejZavierky(id);
		    	vsetkyRocneZavierky.add(new RocnaZavierka(id, mesacneZavierky, cas,intervalOd,intervalDo));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return vsetkyRocneZavierky;
	}
	
	/**
	 * Zistí, či daný pokladnik vytvoril dennú závierku
	 * @param pokladnik - daný pokladník
	 * @return čas, kedy robil pokladnik naposledy dennú závierku
	 */
	public static Date pokladnikVykonalZavierku(UserPokladnik pokladnik) {
		Date vykonalZavierku = null;
		String query = "SELECT vykonal_zavierku FROM users WHERE id=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, pokladnik.getId());
			ResultSet rs = ps.executeQuery();
			
		    while (rs.next()) {
		    	Timestamp sTimestamp = rs.getTimestamp("vykonal_zavierku");
		    	if (sTimestamp != null) {
		    		vykonalZavierku = new Date(sTimestamp.getTime());
				}
		    	
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return vykonalZavierku;
	}
	
	/**
	 * Zistí, aké množstvo jednotlivých tovarov sa predalo
	 * @return predajnost každého výrobku v databáze
	 */
	public static List<Predajnost> getPredajnostTovarov() {
		List<Predajnost> vsetkyPredajnostiList = new ArrayList<>();
		
		Map<Integer,Float> vsetkyPredajnosti = new TreeMap<>();
		
		List<NakupenyTovar> vsetkyNakupenyTovary = getNakupenyTovar();
		
		for (NakupenyTovar nakupenyTovar : vsetkyNakupenyTovary) {
			vsetkyPredajnosti.put(nakupenyTovar.getTovarPLU(), 0f);
		}
		
		for (NakupenyTovar nakupenyTovar : vsetkyNakupenyTovary) {
			Float predaneMnozstvo = vsetkyPredajnosti.get(nakupenyTovar.getTovarPLU());
			vsetkyPredajnosti.replace(nakupenyTovar.getTovarPLU(), predaneMnozstvo + nakupenyTovar.getNakupeneMnozstvo());
		}
		
		for (Integer i : vsetkyPredajnosti.keySet()) {
			vsetkyPredajnostiList.add(new Predajnost(Database.getSpecificTovarPlusDeleted(i), vsetkyPredajnosti.get(i)));
		}
		
		return vsetkyPredajnostiList;
	}
	
	/**
	 * Vráti všetky nakúpené tovary, ktoré zadaný pokladník v zadanom dátume stornoval
	 * @param pokladnik - daný pokladník
	 * @param datum - daný dátum
	 * @return všetky nakúpené tovary, ktoré zadaný pokladník v zadanom dátume stornoval
	 */
	public static List<NakupenyTovar> getStornovanyTovarPokladnika(UserPokladnik pokladnik,Date datum) {
		List<NakupenyTovar> stornovanyTovar = new ArrayList<>();
		String query = "SELECT * FROM nakupeny_tovar WHERE stornovane_kym=? && stornovane_kedy>=? && stornovane_kedy<=?";
		Date casOd = Utils.atStartOfDay(datum);
		Date casDo = Utils.atEndOfDay(datum);
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, pokladnik.getId());
			ps.setTimestamp(2, new Timestamp(casOd.getTime()));
			ps.setTimestamp(3, new Timestamp(casDo.getTime()));
			
			ResultSet rs = ps.executeQuery();
			
		    while (rs.next()) {
		    	float mnozstvo = rs.getFloat("mnozstvo");
    			int noveMnozstvo = rs.getInt("nove_mnozstvo");
    			float jednotkovaCena = rs.getFloat("jednotkova_cena");
    			Tovar tovar = getSpecificTovarPlusDeleted(rs.getInt("plu_tovar"));
    			stornovanyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), mnozstvo - noveMnozstvo));
		    	
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return stornovanyTovar;
	}
	
	/**
	 * Vráti všetky nakúpené tovary, ktoré zadaný pokladník v zadanom intervale stornoval
	 * @param pokladnik - daný pokladník
	 * @param casOd - daný dátum
	 * @param casDo - daný dátum
	 * @return všetky nakúpené tovary, ktoré zadaný pokladník v zadanom dátume stornoval
	 */
	public static List<NakupenyTovar> getStornovanyTovarPokladnikaInterval(UserPokladnik pokladnik,Date casOd, Date casDo) {
		List<NakupenyTovar> stornovanyTovar = new ArrayList<>();
		String query = "SELECT * FROM nakupeny_tovar WHERE stornovane_kym=? && stornovane_kedy>=? && stornovane_kedy<=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, pokladnik.getId());
			ps.setTimestamp(2, new Timestamp(casOd.getTime()));
			ps.setTimestamp(3, new Timestamp(casDo.getTime()));
			
			ResultSet rs = ps.executeQuery();
			
		    while (rs.next()) {
		    	float mnozstvo = rs.getFloat("mnozstvo");
    			int noveMnozstvo = rs.getInt("nove_mnozstvo");
    			float jednotkovaCena = rs.getFloat("jednotkova_cena");
    			Tovar tovar = getSpecificTovarPlusDeleted(rs.getInt("plu_tovar"));
    			stornovanyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), jednotkovaCena, tovar.getTovarDPH(), mnozstvo - noveMnozstvo));
		    	
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return stornovanyTovar;
	}

	public static List<NakupenyTovar> getNakupAppZakaznika(int idZakaznika){
		List<NakupenyTovar> nakupeneTovary = new ArrayList<>();
		String query = "SELECT * FROM nakup_zakaznikov_online WHERE zakaznik_id=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, idZakaznika);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Tovar tovar = getSpecificTovar(rs.getInt("tovar_plu"));
				float mnozstvo = rs.getFloat("mnozstvo");

				if (tovar instanceof TovarZlavaCena){
					nakupeneTovary.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), tovar.getTovarJednotkovaCena(), tovar.getTovarDPH(), mnozstvo,((TovarZlavaCena) tovar).getNovaCena()));
				}
				else if (tovar instanceof TovarZlavaMnozstvo){
					nakupeneTovary.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), tovar.getTovarJednotkovaCena(), tovar.getTovarDPH(), mnozstvo,((TovarZlavaMnozstvo) tovar).getPovodneMnozstvo(),((TovarZlavaMnozstvo) tovar).getNoveMnozstvo(),((TovarZlavaMnozstvo) tovar).getMinimalneMnozstvo()));
				}
				else{
					nakupeneTovary.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), tovar.getTovarJednotkovaCena(), tovar.getTovarDPH(), mnozstvo));
				}
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}
		return nakupeneTovary;
	}

    public static boolean isNakupZakaznikaUkonceny(int idZakaznik){
        String query = "SELECT ukonceny FROM zakaznici WHERE id=?";
        try(Connection conn = getConnection()){
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1,idZakaznik);

            ResultSet rSet = ps.executeQuery();
            if (rSet.next()) {
                return rSet.getBoolean("ukonceny");
            }
            rSet.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe(e.toString());

        }
        return false;
    }

    public static void setUkoncenyNakupZakaznika(int idZakaznik){
        String query = "UPDATE zakaznici SET ukonceny=? WHERE id=?";
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBoolean(1, true);
            ps.setInt(2, idZakaznik);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.severe(e.toString());
        }
    }
}
