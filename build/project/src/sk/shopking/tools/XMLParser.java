/**
 * 
 */
package sk.shopking.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sk.shopking.Category;
import sk.shopking.DPHType;
import sk.shopking.JednotkaType;
import sk.shopking.Tovar;
import sk.shopking.User;
import sk.shopking.UserAdministrator;
import sk.shopking.UserPokladnik;
import sk.shopking.UserType;

/**
 * @author Filip
 * @deprecated
 */
public class XMLParser {
	
	public static String cestaKuServeru = System.getenv("APPDATA") + File.separator + "ShopKing";
	public static String cestaKuKlientovi = System.getenv("APPDATA") + File.separator + "ShopKing";
	
	/* users.xml - uchovava vsetkych pouzivatelov - ich mena,priezviska,nicky,hash a typ pouzivatela
	 * category.xml - uchovava vsetky kategorie tovaru
	 * tovar.xml - uchovava vsetky tovary - jeho kategoriu, obrazok,cenu,...
	 * objednavky.xml - uchovava vsetky aktualne objednavky
	 * XXsklad.xml - uchovava aktualny stav skladu, pocet vyrobkov v sklade a v predajniXX --- merged with tovar.xml
	 * akcia.xml - uchovava
	 * settings.xml - uchovava nastavenia obchodu
	 * clients.xml - uchovava zoznam vsetkych klientov - klienti sa pri instalacii zapisu ako na vratnici
	 * info.xml - uchovava informacie o obchode
	 * statistika.xml - uchova statistiku
	 * client.xml - uchova  informacie o kientovi (vyskytuje sa len v klientskom PC)
	 */
	public static final String FILE_LIST_USER = "users.xml";
	public static final String FILE_LIST_CATEGORY = "category.xml";
	public static final String FILE_LIST_TOVAR = "tovar.xml";
	public static final String FILE_LIST_ORDER = "objednavky.xml";
	public static final String FILE_LIST_AKCIA = "akcia.xml";
	public static final String FILE_SETTINGS_SHOP = "settings.xml";
	public static final String FILE_CLIENTS = "clients.xml";
	public static final String FILE_APP_INFO = "info.xml";
	public static final String FILE_STAT = "statistika.xml";
	public static final String FILE_INFO_CLIENT = "client.xml";
	
	/*
	 * kontrola, ktora sa prevedie pri starte programu
	 */
	
	public static boolean kontrolaSuborov(){

		boolean kontrola;
		if(isDirectoryExists(cestaKuKlientovi) == false) {
			createNewClientDirectory();
			kontrola = false;
		}
		
		if(isDirectoryExists(cestaKuServeru) == false) {
			createNewServerDirectory();
			kontrola = false;
		}
		
		if(isFileExists(cestaKuServeru,FILE_LIST_USER) == true) {
			/*if(validateFile(cestaKuServeru,FILE_LIST_USER) == false) {
				showErrorValidateDialog(FILE_LIST_USER);
			}*/
			kontrola = true;
		}
		else {
			createNewUserFile();
			kontrola = true;
		}
		
		if(isFileExists(cestaKuServeru,FILE_LIST_CATEGORY) == true) {
			/*if(validateFile(cestaKuServeru,FILE_LIST_CATEGORY) == false) {
				showErrorValidateDialog(FILE_LIST_CATEGORY);
			}*/
			kontrola = true;
		}
		else {
			createNewCategoryFile();
			kontrola = true;
		}
		
		if(isFileExists(cestaKuServeru,FILE_LIST_TOVAR) == true) {
			/*if(validateFile(cestaKuServeru,FILE_LIST_TOVAR) == false) {
				showErrorValidateDialog(FILE_LIST_TOVAR);
			}*/
			kontrola = true;
		}
		else {
			createNewTovarFile();
			kontrola = true;
		}
		
		if(isFileExists(cestaKuServeru,FILE_LIST_ORDER) == true) {
			/*if(validateFile(cestaKuServeru,FILE_LIST_ORDER) == false) {
				showErrorValidateDialog(FILE_LIST_ORDER);
			}*/
			kontrola = true;
		}
		else {
			createNewOrderFile();
			kontrola = true;
		}

		if(isFileExists(cestaKuServeru,FILE_LIST_AKCIA) == true) {
			/*if(validateFile(cestaKuServeru,FILE_LIST_AKCIA) == false) {
				showErrorValidateDialog(FILE_LIST_AKCIA);
			}*/
			kontrola = true;
		}
		else {
			createNewAkciaFile();
			kontrola = true;
		}
		
		if(isFileExists(cestaKuServeru,FILE_SETTINGS_SHOP) == true) {
			/*if(validateFile(cestaKuServeru,FILE_SETTINGS_SHOP) == false) {
				showErrorValidateDialog(FILE_SETTINGS_SHOP);
			}*/
			kontrola = true;
		}
		else {
			createNewSettingsShopFile();
			kontrola = true;
		}
		
		if(isFileExists(cestaKuServeru,FILE_CLIENTS) == true) {
			/*if(validateFile(cestaKuServeru,FILE_SETTINGS_SHOP) == false) {
				showErrorValidateDialog(FILE_SETTINGS_SHOP);
			}*/
			kontrola = true;
		}
		else {
			createNewClientsFile();
			kontrola = true;
		}
		
		if(isFileExists(cestaKuServeru,FILE_STAT) == true) {
			/*if(validateFile(cestaKuServeru,FILE_SETTINGS_SHOP) == false) {
				showErrorValidateDialog(FILE_SETTINGS_SHOP);
			}*/
			kontrola = true;
		}
		else {
			createNewStatsFile();
			kontrola = true;
		}
		
		if(isFileExists(cestaKuKlientovi,FILE_INFO_CLIENT) == true) {
			/*if(validateFile(cestaKuServeru,FILE_SETTINGS_SHOP) == false) {
				showErrorValidateDialog(FILE_SETTINGS_SHOP);
			}*/
			kontrola = true;
		}
		else {
			createNewClientFile();
			kontrola = true;
		}
		return kontrola;
	}
	/**
	 * zobrazenie chyby nespravnosti xml suborov
	 * @deprecated
	 */
	public static void showErrorValidateDialog(String file) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ShopKing Chyba");
		alert.setHeaderText("Chyba pri kontrole súborov");
		alert.setContentText("Súbor " + file + " je chybný. Pre správny štart programu ho musíte opraviť !");
		alert.show();
	}
	
	
	/*
	 * kontroly ci priecinky a subory exsistuju
	 */
	public static boolean isDirectoryExists(String cesta) {
		if(new File(cesta).exists() == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public static boolean isFileExists(String cesta, String file) {
		if(new File(cesta + File.separator + file).exists() == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * zistenie ci ma kolekcia duplikaty
	 * @deprecated
	 */
	public static <T> boolean maDuplikat(T object,List<T> list) {
		for(T ob : list) {
			if(object.equals(ob)) {
				return false;
			}
		}
		return false;
	}
	
	/*
	 * vytvaranie novych suborov ak neexistuju
	 */
	
	public static void createNewServerDirectory() {
		try {
			new File(cestaKuServeru).mkdir();
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		}
	}
	
	public static void createNewClientDirectory() {
		try {
			new File(cestaKuKlientovi + File.separator + "logs").mkdir();
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	public static void createNewCategoryFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("categories");
            document.appendChild(root);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_CATEGORY));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	public static void createNewUserFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            
            User defaultUser = new UserAdministrator(1,"Administrátor","","admin",User.hashPassword("heslo1"));

            Element root = document.createElement("users");
            document.appendChild(root);
            Element user = document.createElement("user");
            
            user.setAttribute("meno", defaultUser.getUserMeno());
            user.setAttribute("priezvisko", defaultUser.getUserPriezvisko());
            user.setAttribute("nick", defaultUser.getUserNickname());
            user.setAttribute("hash", "" + defaultUser.getHashHeslo());
            //user.setAttribute("usertype", defaultUser.getUsertype().toString());
            
            root.appendChild(user);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_USER));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	public static void createNewTovarFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("tovary");
            document.appendChild(root);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_TOVAR));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
		
	}
		
	public static void createNewOrderFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("orders");
            document.appendChild(root);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_ORDER));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	public static void createNewAkciaFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("akcia");
            document.appendChild(root);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_AKCIA));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	public static void createNewSettingsShopFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("shopsettings");
            document.appendChild(root);
            Element shopname = document.createElement("shopname"); 
            shopname.appendChild(document.createTextNode("demo"));
            root.appendChild(shopname);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_SETTINGS_SHOP));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	public static void createNewClientsFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("clients");
            document.appendChild(root);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_CLIENTS));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	public static void createNewStatsFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("statistics");
            document.appendChild(root);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_STAT));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	public static void createNewClientFile() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("info");
            document.appendChild(root);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_INFO_CLIENT));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	/*
	 * citanie XML suborov
	 */
	
	 /* 
	 * USER
	 */
	
	/*public static void appendUserFile(User user) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			Utils.showExceptionDialog(e);
		}
        Document document = null;
		try {
			document = documentBuilder.parse(new File(cestaKuServeru + File.separator + FILE_LIST_USER));
		} catch (SAXException | IOException e) {
			Utils.showExceptionDialog(e);
		}
        Element root = document.getDocumentElement();

        User novyUser = new User(meno,priezvisko,nick,hash);
        List<User> zaznamenanyUsers = readUserFile();   
        
        zaznamenanyUsers.add(novyUser);
        
        List<User> users = new ArrayList<User>();
        users.add(novyUser);

        for (int i = 0; i < users.size(); i++) {
			Element newUser = document.createElement("user");
            newUser.setAttribute("meno", meno);
            newUser.setAttribute("priezvisko", priezvisko);
            newUser.setAttribute("nick", nick);
            newUser.setAttribute("hash", "" + hash);
            newUser.setAttribute("suma", usertype);
            root.appendChild(newUser);
		}

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		} catch (TransformerConfigurationException e) {
			Utils.showExceptionDialog(e);
			
		}
        StreamResult result = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_USER));
        try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			Utils.showExceptionDialog(e);
			
		}
	}*/
	
	public static List<User> readUserFile(){
		
		 List<User> users = new ArrayList<User>();
	       
	     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder builder = null;
	     
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			ShopKingTools.showExceptionDialog(e1);
			
		}
		
	     Document document = null;
	     
		try {
			document = builder.parse(new File(cestaKuServeru + File.separator + FILE_LIST_USER));
		} catch (SAXException | IOException e) {
			ShopKingTools.showExceptionDialog(e);
			
		}
		
	     document.getDocumentElement().normalize();
	     NodeList nList = document.getElementsByTagName("user");
	     
	     for (int temp = 0; temp < nList.getLength(); temp++){
	         Node node = nList.item(temp);
	         if (node.getNodeType() == Node.ELEMENT_NODE){
	            Element eElement = (Element) node;
	            
	            int userID = Integer.parseInt(eElement.getAttribute("id"));
	            String userMeno = eElement.getAttribute("meno");
	            String userPriezvisko = eElement.getAttribute("priezvisko");
	            String userNick = eElement.getAttribute("nick");
	            int userHash = Integer.parseInt(eElement.getAttribute("hash"));
	            Float suma;
	            try {
	            	suma = Float.parseFloat(eElement.getAttribute("suma"));
				} catch (NumberFormatException e) {
					suma = null;
				}
	            User user;
	            if (suma == null) {
	            	user = new UserAdministrator(userID,userMeno,userPriezvisko,userNick,userHash);
				}
	            else {
	            	user = new UserPokladnik(userID,userMeno,userPriezvisko,userNick,userHash,suma);
				}
	            
	            users.add(user);
	         }
	      }
	      return users;
	}
	
	public static void editUserFile(List<User> users) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            
            Element root = document.createElement("users");
            document.appendChild(root);
            
            for(User user : users) {
            	Element userE = document.createElement("user");
            	userE.setAttribute("meno", user.getUserMeno());
            	userE.setAttribute("priezvisko", user.getUserPriezvisko());
            	userE.setAttribute("nick", user.getUserNickname());
            	userE.setAttribute("hash", "" + user.getHashHeslo());
            	if(user instanceof UserPokladnik) {
            		userE.setAttribute("suma", "" + ((UserPokladnik) user).getSumaPokladnica());
            	}
            	root.appendChild(userE);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_USER));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	/*
	 * CATEGORY
	 */
	
	/*public static void appendCategoryFile(String name, boolean preMladistvych) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			Utils.showExceptionDialog(e);
		}
        Document document = null;
		try {
			document = documentBuilder.parse(new File(cestaKuServeru + File.separator + FILE_LIST_CATEGORY));
		} catch (SAXException | IOException e) {
			Utils.showExceptionDialog(e);
		}
        Element root = document.getDocumentElement();

        Category novaKategoria = new Category(name,preMladistvych);
        List<Category> zaznamenaneKategorie = readCategoryFile(); 
        zaznamenaneKategorie.add(novaKategoria);
        
        List<Category> categories = new ArrayList<Category>();
        categories.add(novaKategoria);

        for (int i = 0; i < categories.size(); i++) {
			Element newCategory = document.createElement("category");
            newCategory.setAttribute("name", name);
            newCategory.setAttribute("image", "" + preMladistvych);
            root.appendChild(newCategory);
		}

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		} catch (TransformerConfigurationException e) {
			Utils.showExceptionDialog(e);
			
		}
        StreamResult result = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_CATEGORY));
        try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			Utils.showExceptionDialog(e);
			
		}
	}*/
	
	public static List<Category> readCategoryFile(){
		
		 List<Category> categories = new ArrayList<Category>();
	       
	     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder builder = null;
	     
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			ShopKingTools.showExceptionDialog(e1);
			
		}
		
	     Document document = null;
	     
		try {
			document = builder.parse(new File(cestaKuServeru + File.separator + FILE_LIST_CATEGORY));
		} catch (SAXException | IOException e) {
			ShopKingTools.showExceptionDialog(e);
		}
		
	     document.getDocumentElement().normalize();
	     NodeList nList = document.getElementsByTagName("category");
	     
	     for (int temp = 0; temp < nList.getLength(); temp++){
	         Node node = nList.item(temp);
	         if (node.getNodeType() == Node.ELEMENT_NODE){
	            Element eElement = (Element) node;
	            
	            int id = Integer.parseInt(eElement.getAttribute("id"));
	            String name = eElement.getAttribute("name");
	            boolean preMladistvych = Boolean.parseBoolean(eElement.getAttribute("pre-mladistvych"));
	            
	            Category category = new Category(id,name,preMladistvych);
	            categories.add(category);
	         }
	      }
	      return categories;
	}
	
	public static void editCategoryFile(List<Category> categories) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            
            Element root = document.createElement("categories");
            document.appendChild(root);
            
            for(Category category : categories) {
            	Element categoryE = document.createElement("category");
            	categoryE.setAttribute("name", category.getCategoryName());
            	categoryE.setAttribute("pre-mladistvych", "" + category.getCategoryPristupnePreMladistvych());
            	root.appendChild(categoryE);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_CATEGORY));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	
	/*
	 * TOVAR
	 */
	
	public static void appendTovarFile(Integer tovarPLU,String tovarName,Category tovarCategory,JednotkaType tovarJednotka, String tovarCode, float tovarCena,DPHType DPH) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			ShopKingTools.showExceptionDialog(e);
		}
        Document document = null;
		try {
			document = documentBuilder.parse(new File(cestaKuServeru + File.separator + FILE_LIST_TOVAR));
		} catch (SAXException | IOException e) {
			ShopKingTools.showExceptionDialog(e);
		}
        Element root = document.getDocumentElement();

        Tovar novyTovar = new Tovar(tovarPLU,tovarName,tovarCategory,tovarJednotka,Long.parseLong(tovarCode),tovarCena,DPH);
        List<Tovar> zaznamenaneTovary = readTovarFile();
        zaznamenaneTovary.add(novyTovar);
        
        List<Tovar> tovary = new ArrayList<Tovar>();
        tovary.add(novyTovar);

        for (int i = 0; i < tovary.size(); i++) {
			Element newTovar = document.createElement("tovar");
			newTovar.setAttribute("plu","" + tovarPLU);
            newTovar.setAttribute("name", tovarName);
            newTovar.setAttribute("category", tovarCategory.toString());
            newTovar.setAttribute("code", tovarCode);
            newTovar.setAttribute("cena", "" + tovarCena);
            newTovar.setAttribute("dph", "" + DPH);
            newTovar.setAttribute("jednotka", tovarJednotka.toString());
            root.appendChild(newTovar);
		}

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		} catch (TransformerConfigurationException e) {
			ShopKingTools.showExceptionDialog(e);	
		}
        StreamResult result = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_TOVAR));
        try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			ShopKingTools.showExceptionDialog(e);	
		}
	}
	
	public static List<Tovar> readTovarFile(){
		
		 List<Tovar> tovary = new ArrayList<Tovar>();
		 List<Category> kategorie = readCategoryFile();
	       
	     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder builder = null;
	     
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			ShopKingTools.showExceptionDialog(e1);	
		}
		
	     Document document = null;
	     
		try {
			document = builder.parse(new File(cestaKuServeru + File.separator + FILE_LIST_TOVAR));
		} catch (SAXException | IOException e) {
			ShopKingTools.showExceptionDialog(e);
		}
		
	     document.getDocumentElement().normalize();
	     NodeList nList = document.getElementsByTagName("tovar");
	     
	     for (int temp = 0; temp < nList.getLength(); temp++){
	         Node node = nList.item(temp);
	         if (node.getNodeType() == Node.ELEMENT_NODE){
	            Element eElement = (Element) node;
	            
	            int tovarPLU = Integer.parseInt(eElement.getAttribute("plu"));
	            String tovarName = eElement.getAttribute("name");
	            String tovarCategoryString = eElement.getAttribute("category");
	            Category tovarCategory = null;
	            for (Category category : kategorie) {
					if (tovarCategoryString.equals(category.getCategoryName())) {
						tovarCategory = category;
					}
				}
	            long tovarCodeLong = Long.parseLong(eElement.getAttribute("code"));
	            float tovarCena = Float.parseFloat(eElement.getAttribute("cena"));
	            DPHType tovarDPH = DPHType.valueOf(eElement.getAttribute("dph"));
	            JednotkaType tovarJednotka = JednotkaType.valueOf(eElement.getAttribute("jednotka"));
	            Tovar tovar = new Tovar(tovarPLU,tovarName,tovarCategory,tovarJednotka,tovarCodeLong,tovarCena,tovarDPH);
	            tovary.add(tovar);
	         }
	      }
	      return tovary;
	}
	
	public static void editTovarFile(List<Tovar> tovary) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            
            Element root = document.createElement("tovary");
            document.appendChild(root);
            
            for(Tovar tovar : tovary) {
            	Element tovarE = document.createElement("tovar");
            	tovarE.setAttribute("plu", "" + tovar.getTovarPLU());
            	tovarE.setAttribute("name", tovar.getTovarName());
            	tovarE.setAttribute("category", tovar.getTovarCategory().toString());
            	tovarE.setAttribute("code", "" + tovar.getTovarEAN());
            	tovarE.setAttribute("cena", "" + tovar.getTovarJednotkovaCena());
            	DPHType dph = tovar.getTovarDPH();
            	if (dph == DPHType.DPH_10) {
            		tovarE.setAttribute("dph", "DPH_10");
				}
            	else {
            		tovarE.setAttribute("dph", "DPH_20");
				}
            	tovarE.setAttribute("jednotka", tovar.getTovarJednotka().toString());
            	root.appendChild(tovarE);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(cestaKuServeru + File.separator + FILE_LIST_TOVAR));
            transformer.transform(domSource, streamResult);
		}
		catch(Exception e) {
			ShopKingTools.showExceptionDialog(e);
		};
	}
	

	/*
	 * SETTINGS
	 */
	public static void readSettingsFile(){
		
	}
}
