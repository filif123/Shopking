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
public class RocnaZavierka {
	private int idZavierky;
	private Date casZavierky;
	private List<MesacnaZavierka> mesacneZavierky;
	
	public RocnaZavierka(int id,List<MesacnaZavierka> mesacneZavierky,Date casZavierky) {
		this.idZavierky = id;
		this.mesacneZavierky = mesacneZavierky;
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
	
	public List<MesacnaZavierka> getDenneZavierky() {
		return mesacneZavierky;
	}
	
	public void setDenneZavierky(List<MesacnaZavierka> mesacneZavierky) {
		this.mesacneZavierky = mesacneZavierky;
	}
}

