/**
 * 
 */
package sk.shopking;

import java.util.Date;
import java.util.List;

/**
 * @author filip
 *
 */
public class MesacnaZavierka {
	private int idZavierky;
	private Date casZavierky;
	private List<DennaZavierka> denneZavierky;
	
	public MesacnaZavierka(int id, List<DennaZavierka> denneZavierky,Date casZavierky) {
		this.idZavierky = id;
		this.denneZavierky = denneZavierky;
		this.casZavierky = casZavierky;
	}
	
	public int getIdZavierky() {
		return idZavierky;
	}

	public void setIdZavierky(int idZavierky) {
		this.idZavierky = idZavierky;
	}
	
	public Date getCasZavierky() {
		return casZavierky;
	}
	
	public void setCasZavierky(Date casZavierky) {
		this.casZavierky = casZavierky;
	}
	
	public List<DennaZavierka> getDenneZavierky() {
		return denneZavierky;
	}
	
	public void setDenneZavierky(List<DennaZavierka> denneZavierky) {
		this.denneZavierky = denneZavierky;
	}
}
