/**
 * 
 */
package sk.shopking;

import java.util.Date;

/**
 * @author filip
 *
 */
public class Doklad {
	private int idDoklad;
	private Date casNakupu;
	private UserPokladnik userPokladnik;
	
	public Doklad(int idDoklad,Date casNakupu,UserPokladnik userPokladnik){
		this.idDoklad = idDoklad;
		this.casNakupu = casNakupu;
		this.userPokladnik = userPokladnik;
	}
	public int getIdDoklad() {
		return idDoklad;
	}
	public void setIdDoklad(int idDoklad) {
		this.idDoklad = idDoklad;
	}
	public Date getCasNakupu() {
		return casNakupu;
	}
	public void setCasNakupu(Date casNakupu) {
		this.casNakupu = casNakupu;
	}
	public UserPokladnik getUserPokladnik() {
		return userPokladnik;
	}
	public void setUserPokladnik(UserPokladnik userPokladnik) {
		this.userPokladnik = userPokladnik;
	}
}
