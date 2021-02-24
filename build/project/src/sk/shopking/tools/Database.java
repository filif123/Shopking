/**
 * 
 */
package sk.shopking.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import sk.shopking.Category;
import sk.shopking.DPHType;
import sk.shopking.Doklad;
import sk.shopking.Obchod;
import sk.shopking.Pokladnica;
import sk.shopking.JednotkaType;
import sk.shopking.Nakup;
import sk.shopking.NakupenyTovar;
import sk.shopking.Tovar;
import sk.shopking.User;
import sk.shopking.UserAdministrator;
import sk.shopking.UserPokladnik;
import sk.shopking.UserType;
import sk.shopking.DennaZavierka;
import sk.shopking.tools.ShopKingTools;

import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;


/**
 * Pracuje s SQL serverom
 * @author Filip
 *
 */
/**
 * @author Filip
 *
 */
public class Database {
	
	public static String ADDRESS_TO_SERVER = AppSettings.loadIPSQL();
	public static final String DATABASE_NAME = "shopking";
	private static final String LOGIN_NAME = "root";
	private static final String LOGIN_PASSWORD = "";
	
	/**
	 * 
	 * @return <strong>true</strong>, ak je spojenie s databázou úspešné
	 */
	public static boolean isServerExists() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.getConnection("jdbc:mysql://" + ADDRESS_TO_SERVER, LOGIN_NAME, LOGIN_PASSWORD);
        } catch (Exception ex) {
            return false;
        }
        return true;
        
	}
	
	/**
	 * 
	 * @return <strong>true</strong>, ak je spojenie s databázou úspešné
	 */
	public static boolean isConnectionAvailable() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.getConnection("jdbc:mysql://" + ADDRESS_TO_SERVER + "/" + DATABASE_NAME, LOGIN_NAME, LOGIN_PASSWORD);
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
            con = DriverManager.getConnection("jdbc:mysql://" + ADDRESS_TO_SERVER, LOGIN_NAME, LOGIN_PASSWORD);
        } catch (Exception ex) {
            ShopKingTools.showExceptionDialog(ex);
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
            con = DriverManager.getConnection("jdbc:mysql://" + ADDRESS_TO_SERVER + "/" + DATABASE_NAME, LOGIN_NAME, LOGIN_PASSWORD);
        } catch (Exception ex) {
            ShopKingTools.showExceptionDialog(ex);
        }
        return con;
	}
	
	/**
	 * Zmení obsah databázy (INSERT,UPDATE,DELETE)
	 * @param query - príkaz, ktorý sa má vykonať
	 * @param input - list dátových typov a ich hodnoty
	 * @return buď <strong>počet riadkov pre výkaz SQL Data Manipulation Language (DML)</strong> alebo <strong>0</strong> pre SQL statements, ktoré nevracajú nič
	 */
	public static int updateDB(String query, List<Object> input) {
		
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
	
			for (int i = 0; i < input.size(); i++) {
				
				if (input.get(i) instanceof String) {
					ps.setString(i + 1, (String) input.get(i));
					
				} else if (input.get(i) instanceof Integer){
					ps.setInt(i + 1, (Integer) input.get(i));
					
				} else if (input.get(i) instanceof Float){
					ps.setFloat(i + 1, (Float) input.get(i));
					
				} else if (input.get(i) instanceof Double){
					ps.setDouble(i + 1, (Double) input.get(i));
					
				} else if (input.get(i) instanceof Boolean){
					ps.setBoolean(i + 1, (Boolean) input.get(i));
					
				} else if (input.get(i) instanceof Long){
					ps.setLong(i + 1, (Long) input.get(i));
					
				}else {
					return 0;
				}	
			}
			
			int output = ps.executeUpdate();
			return output;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	

	/**
	 * Zmení obsah databázy (INSERT,UPDATE,DELETE)
	 * @param query - príkaz, ktorý sa má vykonať
	 * @return buď <strong>počet riadkov pre výkaz SQL Data Manipulation Language (DML)</strong> alebo <strong>0</strong> pre SQL statements, ktoré nevracajú nič
	 */
	public static int updateDB(String query) {
		
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			
			int output = ps.executeUpdate();
			return output;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Prečíta obsah databázy podľa príkazu (SELECT)
	 * @param query - príkaz, ktorý sa má vykonať
	 * @return <strong>list výsledkov</strong> alebo <strong>null</strong> v prípade výnimky alebo chyby
	 */
	public static ResultSet readDB(String query) {
		
		ResultSet rs = null;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rs = statement.executeQuery(query);
			return rs;
			
		} catch (Exception e) {
			e.printStackTrace();
			return rs;
		}
	}
	
	/**
	 * Vykoná zadaný script
	 * @param file - súbor, obsahujúci script
	 * @return <strong>true</strong> v prípade úspechu alebo <strong>false</strong> v prípade výnimky alebo chyby
	 */
	public static boolean runScriptDB(String file) {
		
		try(Connection conn = getConnectionToServer()) {
			ScriptRunner sr = new ScriptRunner(conn, false, false);
			sr.runScript(new BufferedReader(new FileReader(file)));
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
	}
	
	/**
	 * @param categories - nové kategórie tovaru
	 */
	public static void addCategories(List<Category> categories) {
		String query = "INSERT INTO categories SET nazov=?,pristupne_pre_mladistvych=?";
		for (Category category : categories) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, category.getCategoryName());
				ps.setBoolean(2, category.getCategoryPristupnePreMladistvych());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 
	 * @return kategórie tovarov z databázy
	 */
	public static List<Category> getCategories() {
		List<Category> categoriesFromDBList = new ArrayList<Category>();
		ResultSet rSet = null;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM categories");
    		while (rSet.next()) {
        		Integer id = rSet.getInt("id");
    			String nazov = rSet.getString("nazov");
				Boolean pristupnePreMladistvych =  rSet.getBoolean("pristupne_pre_mladistvych");
				Boolean deleted = rSet.getBoolean("deleted");
				if (deleted == false) {
					Category category = new Category(id,nazov, pristupnePreMladistvych);
					categoriesFromDBList.add(category);
				}
				
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoriesFromDBList;
	}
	
	
	/**
	 * @param categories - všetky kategórie pre úpravu
	 */
	public static void updateCategories(List<Category> categories) {
		String query = "UPDATE categories SET nazov=?,pristupne_pre_mladistvych=? WHERE id=?";
		for (Category category : categories) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, category.getCategoryName());
				ps.setBoolean(2, category.getCategoryPristupnePreMladistvych());
				ps.setInt(3, category.getCategoryID());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
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
			}
		}
	}
		
	
	/**
	 * @param id kategórie 
	 * @return kategóriu tovarov na zadanom id
	 */
	public static Category getSpecificCategory(int id) {
		Category category= null;
		ResultSet rSet = null;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM categories WHERE id=" + id);
			
    		while (rSet.next()) {
    			String nazov = rSet.getString("nazov");
				Boolean pristupnePreMladistvych =  rSet.getBoolean("pristupne_pre_mladistvych");
				Boolean deleted = rSet.getBoolean("deleted");
				if (deleted == false) {
					category = new Category(id,nazov, pristupnePreMladistvych);
				}
				
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return category;
	}
	
	/**
	 * @param tovary - nové tovary
	 */
	public static void addTovary(List<Tovar> tovary) {
		String query = "INSERT INTO tovary SET nazov=?,cena=?,dph=?,ean=?,jednotka=?,id_category=?";
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
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 
	 * @return tovary z databázy
	 */
	public static List<Tovar> getTovary() {
		List<Tovar> tovaryFromDBList = new ArrayList<Tovar>();
		
		ResultSet rSet = null;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM tovary");
    		while (rSet.next()) {
        		Integer plu = rSet.getInt("plu");
    			String nazov = rSet.getString("nazov");
    			Float cena = rSet.getFloat("cena");
    			DPHType dphType = DPHType.valueOf(rSet.getString("dph"));
    			Long ean = Long.parseLong(rSet.getString("ean"));
    			JednotkaType jednotkaType = JednotkaType.valueOf(rSet.getString("jednotka"));
				Category category =  getSpecificCategory(rSet.getInt("id_category"));
				Boolean deleted = rSet.getBoolean("deleted");
				if (deleted == false) {
					Tovar tovar = new Tovar(plu,nazov,category,jednotkaType,ean,cena,dphType);
					tovaryFromDBList.add(tovar);
				}
				
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tovaryFromDBList;
	}
	
	/**
	 * @param tovary - všetky tovary pre úpravu
	 */
	public static void updateTovary(List<Tovar> tovary) {
		String query = "UPDATE tovary SET nazov=?,cena=?,dph=?,ean=?,jednotka=?,id_category=? WHERE plu=?";
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
				ps.setInt(7, tovar.getTovarPLU());
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
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
			}
		}
	}
	
	/**
	 * @param plu tovaru 
	 * @return tovar
	 */
	public static Tovar getSpecificTovar(int plu) {
		Tovar tovar= null;
		ResultSet rSet = null;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM tovary WHERE id=" + plu);
    		while (rSet.next()) {
    			String nazov = rSet.getString("nazov");
    			Float cena = rSet.getFloat("cena");
    			DPHType dphType = DPHType.valueOf(rSet.getString("dph"));
    			Long ean = Long.parseLong(rSet.getString("ean"));
    			JednotkaType jednotkaType = JednotkaType.valueOf(rSet.getString("jednotka"));
				Category category =  getSpecificCategory(rSet.getInt("id_category"));
				Boolean deleted = rSet.getBoolean("deleted");
				if (deleted == false) {
					tovar = new Tovar(plu,nazov,category,jednotkaType,ean,cena,dphType);
				}
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tovar;
	}
	
	/**
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
					ps.setString(5, UserType.POKLADNIK.toString());
					ps.setFloat(6, ((UserPokladnik) user).getSumaPokladnica());
				}
				else {
					ps.setString(5, UserType.ADMIN.toString());
					ps.setFloat(6, 0);
				}
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @return používateľov z databázy
	 */
	public static List<User> getUsers() {
		List<User> usersFromDBList = new ArrayList<User>();
		ResultSet rSet = null;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM users");
			while (rSet.next()) {
        		Integer id = rSet.getInt("id");
    			String meno = rSet.getString("meno");
    			String priezvisko =  rSet.getString("priezvisko");
    			String nickname =  rSet.getString("nickname");
    			Integer hash = rSet.getInt("hash");
    			Float sumaFloat = rSet.getFloat("suma_v_pokladnici");
    			Boolean deleted = rSet.getBoolean("deleted");
    			UserType userType = UserType.valueOf(rSet.getString("usertype"));
    			if(deleted == false) {
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
		}
		return usersFromDBList;
	}
	
	/**
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
					ps.setString(5, UserType.POKLADNIK.toString());
					ps.setFloat(6, ((UserPokladnik) user).getSumaPokladnica());
				}
				else {
					ps.setString(6, UserType.ADMIN.toString());
					ps.setFloat(6, 0);
				}
				ps.setInt(7, user.getId());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
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
			}
		}
	}
	
	/**
	 * 
	 * @return používateľa na zadanom id
	 */
	public static User getSpecificUser(int id) {
		User user = null;
		ResultSet rSet = null;
		
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM users WHERE id=" + id);
    		while (rSet.next()) {
    			String meno = rSet.getString("meno");
    			String priezvisko =  rSet.getString("priezvisko");
    			String nickname =  rSet.getString("nickname");
    			Integer hash = rSet.getInt("hash");
    			Float sumaFloat = rSet.getFloat("suma_v_pokladnici");
    			Boolean deleted = rSet.getBoolean("deleted");
    			UserType userType = UserType.valueOf(rSet.getString("usertype"));
    			if(deleted == false) {
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
		}
		return user;
	}
	
	/**
	 * 
	 * @return informácie o obchode
	 */
	public static Obchod infoObchod() {
		Obchod obchod = null;
		ResultSet rSet = null;
		
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
    			Float pocSuma = rSet.getFloat("pokl_poc_suma");
    			Float limSuma = rSet.getFloat("pokl_max_suma");
    			
    			if (logoPathString != null) {
    				File logoFile = new File(logoPathString);
    				obchod = new Obchod(obchodnyNazov, nazovObchodu, sidloMesto, sidloUlica, sidloPSC, sidloCislo, ico, dic, icDPH, prevadzkaMesto, prevadzkaUlica, prevadzkaPSC, prevadzkaCislo,logoFile,pocSuma,limSuma);
				}
    			else {
    				obchod = new Obchod(obchodnyNazov, nazovObchodu, sidloMesto, sidloUlica, sidloPSC, sidloCislo, ico, dic, icDPH, prevadzkaMesto, prevadzkaUlica, prevadzkaPSC, prevadzkaCislo,null,pocSuma,limSuma);
				}
    			
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obchod;
	}
	
	/**
	 * @param obchod
	 */
	public static void createObchod(Obchod obchod) {
		String query = "INSERT INTO obchod SET obchodny_nazov=?,nazov_obchodu=?,sidlo_mesto=?,sidlo_ulica=?,sidlo_psc=?,sidlo_cislo=?,"
				+ "ico=?,dic=?,icdph=?,prevadzka_mesto=?,prevadzka_ulica=?,prevadzka_psc=?,prevadzka_cislo=?,logo_path=?,pokl_poc_suma=?,pokl_max_suma=?";
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
			ps.setString(14, obchod.getLogoSpolocnosti().getAbsolutePath());
			ps.setFloat(15, obchod.getPoklPociatocnaSuma());
			ps.setFloat(16, obchod.getPoklMaxSuma());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param obchod so zmenou
	 */
	public static void editObchod(Obchod obchod) {
		String query = "UPDATE obchod SET obchodny_nazov=?,nazov_obchodu=?,sidlo_mesto=?,sidlo_ulica=?,sidlo_psc=?,sidlo_cislo=?,"
				+ "ico=?,dic=?,icdph=?,prevadzka_mesto=?,prevadzka_ulica=?,prevadzka_psc=?,prevadzka_cislo=?,logo_path=?,pokl_poc_suma=?,pokl_max_suma=?";
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
			ps.setString(14, obchod.getLogoSpolocnosti().getAbsolutePath());
			ps.setFloat(15, obchod.getPoklPociatocnaSuma());
			ps.setFloat(16, obchod.getPoklMaxSuma());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param pokladnice - všetky pokladnice pre pridanie do databázy
	 */
	public static void addPokladnice(List<Pokladnica> pokladnice) {
		String query = "INSERT INTO pokladnice SET id=?,dkp=?";
		for (Pokladnica pokladnica : pokladnice) {
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setInt(1, pokladnica.getIdPokladnice());
				ps.setString(2, pokladnica.getDkpPokladnice());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
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
			}
		}
			
	}
	
	/**
	 * 
	 * @return pokladnice z databázy
	 */
	public static List<Pokladnica> getPokladnice() {
		List<Pokladnica> pokladnice = new ArrayList<Pokladnica>();
		ResultSet rSet = null;
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM pokladnice");
    		while (rSet.next()) {
    			Integer id = rSet.getInt("id");
    			String dkp = rSet.getString("dkp");
    			boolean isOpen = rSet.getBoolean("otvorena");
    			UserPokladnik userPokladnik = (UserPokladnik) getSpecificUser(rSet.getInt("id_pokladnik"));
    			
    			Pokladnica tatoPokladnica = new Pokladnica(id,dkp,isOpen,userPokladnik);
    			pokladnice.add(tatoPokladnica);
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pokladnice;
	}
	
	/**
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
			}
		}
	}
	
	/**
	 * @param idPokladnik - id pokladníka
	 * @param idPokladnica - id pokladnice
	 */
	public static void setPokladnicaOpen(int idPokladnik,int idPokladnica) {
		String query = "UPDATE pokladnice SET otvorena=?,id_pokladnik=? WHERE id=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setBoolean(1, true);
			ps.setInt(2, idPokladnik);
			ps.setInt(3, idPokladnica);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * @param idPokladnik - id pokladníka
	 * @param idPokladnica - id pokladnice
	 */
	public static void setPokladnicaClose(int idPokladnik,int idPokladnica) {
		String query = "UPDATE pokladnice SET otvorena=?,id_pokladnik=? WHERE id=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setBoolean(1, false);
			ps.setNull(2, Types.INTEGER);
			ps.setInt(3, idPokladnica);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * @param nakup - nový nákup, ktorý má byť uložený do DB
	 */
	public static void setNewNakup(Nakup nakup) {
		List<NakupenyTovar> nakupeneTovary = nakup.getNakupenyTovar();
		String query = "INSERT INTO nakupeny_tovar SET mnozstvo=?,id_doklad=?,plu_tovar=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			for (NakupenyTovar nakupenyTovar : nakupeneTovary) {
				ps.setFloat(1, nakupenyTovar.getNakupeneMnozstvo());
				ps.setInt(2, nakup.getDokladNakupu().getIdDoklad());
				ps.setInt(3, nakupenyTovar.getTovarPLU());
				
				ps.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param idDoklad - id dokladu
	 * @param nakupenyTovar - tovar, ktorý má byť stornovaný
	 * @param noveMnozstvo - mnozstvo, ktoré nebude stornované
	 */
	public static void stornoNakupenyTovar(int idDoklad, NakupenyTovar nakupenyTovar, int noveMnozstvo) {
		String query = "UPDATE nakupeny_tovar SET stornovane=? WHERE id_doklad=? && plu_tovar=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setBoolean(1, true);
			ps.setInt(2, idDoklad);
			ps.setInt(3, nakupenyTovar.getTovarPLU());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String query2 = "INSERT INTO nakupeny_tovar SET mnozstvo=?,id_doklad=?,plu_tovar=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query2)) {
			ps.setFloat(1, noveMnozstvo);
			ps.setInt(2, idDoklad);
			ps.setInt(3, nakupenyTovar.getTovarPLU());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * @param idDoklad - id dokladu
	 * @param nakupenyTovar - tovar, ktorý má byť stornovaný
	 */
	public static void stornoNakupenyTovar(int idDoklad, NakupenyTovar nakupenyTovar) {
		String query = "UPDATE nakupeny_tovar SET stornovane=? WHERE id_doklad=? && plu_tovar=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setBoolean(1, true);
			ps.setInt(2, idDoklad);
			ps.setInt(3, nakupenyTovar.getTovarPLU());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * @param idDoklad - id dokladu
	 * @return nákupy z databázy
	 */
	public static List<NakupenyTovar> getNakupenyTovar(int idDoklad) {
		List<NakupenyTovar> nakupenyTovar = new ArrayList<NakupenyTovar>();
		ResultSet rSet = null;
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM nakupeny_tovar WHERE id_doklad=" + idDoklad);
    		while (rSet.next()) {
    			Float mnozstvo = rSet.getFloat("mnozstvo");
    			Tovar tovar = getSpecificTovar(rSet.getInt("plu_tovar"));
    			Boolean stornovane = rSet.getBoolean("stornovane");
    			if (!stornovane) {
					nakupenyTovar.add(new NakupenyTovar(tovar.getTovarPLU(), tovar.getTovarName(), tovar.getTovarCategory(), tovar.getTovarJednotka(), tovar.getTovarEAN(), tovar.getTovarJednotkovaCena(), tovar.getTovarDPH(), mnozstvo));
				}
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nakupenyTovar;
	}
	
	/**
	 * @param idDoklad - id dokladu
	 * @return nákup z databázy podľa id dokladu
	 */
	public static Nakup getNakup(int idDoklad) {
		List<NakupenyTovar> nakupeneTovary = getNakupenyTovar(idDoklad);
		Doklad dokladNakupu = getDoklad(idDoklad);
		return new Nakup(nakupeneTovary, dokladNakupu);
	}
	
	/**
	 *
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
		}
		return generatedKey;
	}
	
	/**
	 *
	 * @return všetky doklady z databázy
	 */
	public static List<Doklad> getDoklady() {
		List<Doklad> vsetkyDoklady = new ArrayList<Doklad>();
		ResultSet rSet = null;
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM doklady");
    		while (rSet.next()) {
    			Integer id = rSet.getInt("id");
    			Date casNakupu = new Date(rSet.getTimestamp("cas_nakupu").getTime());
    			UserPokladnik pokladnik = (UserPokladnik) getSpecificUser(rSet.getInt("id_pokladnik"));
    			vsetkyDoklady.add(new Doklad(id, casNakupu, pokladnik));
    		}
    		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vsetkyDoklady;
	}
	
	/**
	 * @param idDoklad - id dokladu
	 * @return doklad z databázy podľa jeho id
	 */
	public static Doklad getDoklad(int idDoklad) {
		Doklad doklad = null;
		ResultSet rSet = null;
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			rSet = statement.executeQuery("SELECT * FROM doklady WHERE id=" + idDoklad);
   		while (rSet.next()) {
   			Integer id = rSet.getInt("id");
   			Date casNakupu = new Date(rSet.getTimestamp("cas_nakupu").getTime());
   			UserPokladnik pokladnik = (UserPokladnik) getSpecificUser(rSet.getInt("id_pokladnik"));
   			doklad = new Doklad(id, casNakupu, pokladnik);
   		}
   		rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doklad;
	}
	
	/**
	 * @param pokladnik - pokladník, ktorý robí závierku
	 * @param cas - čas, kedy robí pokladník závierku
	 * @return všetky nákupy, ktoré urobil daný pokladník v zadanom dátume
	 */
	public static List<Nakup> getNakupy(UserPokladnik pokladnik,Date cas) {
		List<Nakup> nakupyPokladnikaDnes = new ArrayList<Nakup>();
		List<Doklad> doklady = getDoklady();
		for (Doklad doklad : doklady) {
			if (doklad.getUserPokladnik().equals(pokladnik) && ShopKingTools.isSameDay(cas, doklad.getCasNakupu())) {
				Nakup nakup = getNakup(doklad.getIdDoklad());
				nakupyPokladnikaDnes.add(nakup);
			}
		}
		
		return nakupyPokladnikaDnes;
	}
	
	/**
	 * @param cas - čas, kedy robí pokladník závierku
	 * @return všetky nákupy, ktoré urobil daný pokladník v zadanom dátume
	 */
	public static List<Nakup> getNakupy(Date cas) {
		List<Nakup> nakupyDnes = new ArrayList<Nakup>();
		List<Doklad> doklady = getDoklady();
		for (Doklad doklad : doklady) {
			if (ShopKingTools.isSameDay(cas, doklad.getCasNakupu())) {
				Nakup nakup = getNakup(doklad.getIdDoklad());
				nakupyDnes.add(nakup);
			}
		}
		
		return nakupyDnes;
	}
	
	/**
	 * Urobí záznam dennej závierky do databázy
	 * @param pokladnik - pokladník, ktorý robí závierku
	 * @param cas - čas, kedy robí pokladník závierku
	 */
	public static void doDennaZavieka(UserPokladnik pokladnik, Date cas) {
		List<Nakup> nakupyPokladnika = getNakupy(pokladnik, cas);
		List<Doklad> doklady = new ArrayList<Doklad>();
		for (Nakup nakup : nakupyPokladnika) {
			doklady.add(nakup.getDokladNakupu());
		}
		
		Timestamp casSQL = new Timestamp(cas.getTime());
		int idZavierky = -1;
		
		String query1 = "INSERT INTO denne_zavierky SET cas=?";
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query1)) {
			ps.setTimestamp(1, casSQL);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String query2 = "SELECT id FROM denne_zavierky WHERE cas=?";
		try(Connection conn = getConnection()){
			PreparedStatement ps2 = conn.prepareStatement(query2);
		    ps2.setTimestamp(1,casSQL);
		    
		    ResultSet rs = ps2.executeQuery();
		    while (rs.next()) {
	   			idZavierky = rs.getInt("id");
	   		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (idZavierky == -1) {
			return;
		}
		for (Doklad doklad : doklady) {
			String query3 = "UPDATE doklady SET id_zavierka=? WHERE id=?";
			try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query3)) {
				ps.setInt(1, idZavierky);
				ps.setInt(2, doklad.getIdDoklad());
				
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Vráti všetky denné závierky uložené v databáze
	 * @return denné závierky
	 */
	public static List<DennaZavierka> getDenneZavierky() {
		List<DennaZavierka> vsetkyDenneZavierky = new ArrayList<DennaZavierka>();
		String query = "SELECT * FROM denne_zavierky";
		try(Connection conn = getConnection()){
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM denne_zavierky");
		    while (rs.next()) {
		    	int id = rs.getInt("id");
		    	Date cas = new Date(rs.getTimestamp("cas").getTime());
		    	UserPokladnik pokladnik= (UserPokladnik) getSpecificUser(rs.getInt("id_pokladnik"));
		    	List<Nakup> nakupy = getNakupy(cas);
		    	vsetkyDenneZavierky.add(new DennaZavierka(id, nakupy, cas, pokladnik));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return vsetkyDenneZavierky;
	}
	
	/**
	 * Vráti všetky denné závierky uložené v databáze v zadaný dátum
	 * @param datum - dátum hľadaných závierok
	 * @return denné závierky
	 */
	public static List<DennaZavierka> getDenneZavierky(Date datum) {
		List<DennaZavierka> vsetkyDenneZavierky = new ArrayList<DennaZavierka>();
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
		    	vsetkyDenneZavierky.add(new DennaZavierka(id, nakupy, cas, pokladnik));
    		}
		    rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return vsetkyDenneZavierky;
	}
}
