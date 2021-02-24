/**
 * 
 */
package sk.shopking;

import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;

/**
 * Trieda User určuje informácie o používateľovi.
 * @author Filip
 *
 */
public abstract class User{ 
	
	private String userMeno;
	private String userPriezvisko;
	private String userNickname;
	private int hashHeslo;
	protected UserType userType;
	private int id;
	
	private final SimpleStringProperty meno  = new SimpleStringProperty();
	private final SimpleStringProperty priezvisko = new SimpleStringProperty();
	private final SimpleStringProperty nickname = new SimpleStringProperty();
	private final SimpleStringProperty usertype = new SimpleStringProperty();
	

	public User(int id,String meno, String priezvisko, String nick, int hash) {
		this.setMenoProperty(meno);
		this.setPriezviskoProperty(priezvisko);
		this.setNicknameProperty(nick);
		this.setUsertypeProperty();
		
		this.setId(id);
		this.setUsertype();
		this.setUserMeno(meno);
		this.setUserPriezvisko(priezvisko);
		this.setUserNickname(nick);
		this.setHashHeslo(hash);
	}
	
	public static int hashPassword(String heslo) {
		return Objects.hash(heslo);
	}
	
	public String getUserMeno() {
		return userMeno;
	}

	public void setUserMeno(String meno) {
		this.userMeno = meno;
	}

	public String getUserPriezvisko() {
		return userPriezvisko;
	}

	public void setUserPriezvisko(String priezvisko) {
		this.userPriezvisko = priezvisko;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String nick) {
		this.userNickname = nick;
	}

	public int getHashHeslo() {
		return hashHeslo;
	}

	public void setHashHeslo(int hashHeslo) {
		this.hashHeslo = hashHeslo;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public abstract void setUsertype();
	
	public String toString() {
		return userMeno + " " + userPriezvisko;
	} 
	
	public SimpleStringProperty menoProperty() {
		return meno;
	}

	public SimpleStringProperty priezviskoProperty() {
		return priezvisko;
	}
	
	public SimpleStringProperty nicknameProperty() {
		return nickname;
	}
	
	public SimpleStringProperty usertypeProperty() {
		return usertype;
	}
	
	public final String getMenoProperty() {
        return menoProperty().get();
    }
	
	public final String getPriezviskoProperty() {
        return priezviskoProperty().get();
    }
	
	public final String getNicknameProperty() {
        return nicknameProperty().get();
    }
	
	public final String getUsertypeProperty() {
        return usertypeProperty().get();
    }
	
	public final void setMenoProperty(String name) {
		menoProperty().set(name);
    }
	
	public final void setPriezviskoProperty(String category) {
		priezviskoProperty().set(category);
    }
	
	public final void setNicknameProperty(String nickname) {
		nicknameProperty().set(nickname);
    }
	
	public abstract void setUsertypeProperty();



}
