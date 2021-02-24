/**
 * 
 */
package sk.shopking;

import java.io.File;

/**
 * Táto trieda slúži na zobrazenie informácií o obchode.
 * @author Filip
 *
 */
public class Obchod {
	
	private String obchodnyNazovFirmy;
	private String nazovFirmy;
	
	private String mestoFirmy;
	private String ulicaFirmy;
	private String pscFirmy;
	private String cisloPopisneFirmy;
	
	private String ico;
	private String dic;
	private String icdph;
	
	private String mestoPrevadzky;
	private String ulicaPrevadzky;
	private String pscPrevadzky;
	private String cisloPopisnePrevadzky;
	
	private File logoSpolocnosti;
	
	private float poklPociatocnaSuma;
	private float poklMaxSuma;
	
	public Obchod(String obchodnyNazovFirmy,String nazovFirmy,
			String mestoFirmy, String ulicaFirmy, String pscFirmy, String cisloPopisneFirmy,
			String ico, String dic, String icdph,
			String mestoPrevadzky, String ulicaPrevadzky, String pscPrevadzky, String cisloPopisnePrevadzky,
			File logoSpolocnosti,
			float poklPociatocnaSuma,float poklMaxSuma){
		
		this.obchodnyNazovFirmy = obchodnyNazovFirmy;
		this.nazovFirmy = nazovFirmy;
		
		this.mestoFirmy = mestoFirmy;
		this.ulicaFirmy = ulicaFirmy;
		this.pscFirmy = pscFirmy;
		this.cisloPopisneFirmy = cisloPopisneFirmy;
		
		this.ico = ico;
		this.dic = dic;
		this.icdph = icdph;
		
		this.mestoPrevadzky = mestoPrevadzky;
		this.ulicaPrevadzky = ulicaPrevadzky;
		this.pscPrevadzky = pscPrevadzky;
		this.cisloPopisnePrevadzky = cisloPopisnePrevadzky;
		
		this.logoSpolocnosti = logoSpolocnosti;
		
		this.poklPociatocnaSuma = poklPociatocnaSuma;
		this.poklMaxSuma = poklMaxSuma;
	}
	
	public String getObchodnyNazovFirmy() {
		return obchodnyNazovFirmy;
	}
	
	public void setObchodnyNazovFirmy(String obchodnyNazovFirmy) {
		this.obchodnyNazovFirmy = obchodnyNazovFirmy;
	}
	
	public String getNazovFirmy() {
		return nazovFirmy;
	}
	
	public void setNazovFirmy(String nazovFirmy) {
		this.nazovFirmy = nazovFirmy;
	}
	
	public String getUlicaFirmy() {
		return ulicaFirmy;
	}
	
	public void setUlicaFirmy(String ulicaFirmy) {
		this.ulicaFirmy = ulicaFirmy;
	}
	
	public String getMestoFirmy() {
		return mestoFirmy;
	}
	
	public void setMestoFirmy(String mestoFirmy) {
		this.mestoFirmy = mestoFirmy;
	}
	
	public String getCisloPopisneFirmy() {
		return cisloPopisneFirmy;
	}
	
	public void setCisloPopisneFirmy(String cisloPopisneFirmy) {
		this.cisloPopisneFirmy = cisloPopisneFirmy;
	}
	
	public String getPSCFirmy() {
		return pscFirmy;
	}
	
	public void setPSCFirmy(String pscFirmy) {
		this.pscFirmy = pscFirmy;
	}
	
	public String getICO() {
		return ico;
	}
	
	public void setICO(String ico) {
		this.ico = ico;
	}
	
	public String getDIC() {
		return dic;
	}
	
	public void setDIC(String dic) {
		this.dic = dic;
	}
	
	public String getICDPH() {
		return icdph;
	}
	
	public void setICDPH(String icdph) {
		this.icdph = icdph;
	}	
	
	public String getMestoPrevadzky() {
		return mestoPrevadzky;
	}

	public void setMestoPrevadzky(String mestoPrevadzky) {
		this.mestoPrevadzky = mestoPrevadzky;
	}

	public String getUlicaPrevadzky() {
		return ulicaPrevadzky;
	}

	public void setUlicaPrevadzky(String ulicaPrevadzky) {
		this.ulicaPrevadzky = ulicaPrevadzky;
	}

	public String getPSCPrevadzky() {
		return pscPrevadzky;
	}

	public void setPSCPrevadzky(String pscPrevadzky) {
		this.pscPrevadzky = pscPrevadzky;
	}

	public String getCisloPopisnePrevadzky() {
		return cisloPopisnePrevadzky;
	}

	public void setCisloPopisnePrevadzky(String cisloPrevadzky) {
		this.cisloPopisnePrevadzky = cisloPrevadzky;
	}

	public File getLogoSpolocnosti() {
		return logoSpolocnosti;
	}

	public void setLogoSpolocnosti(File logoSpolocnosti) {
		this.logoSpolocnosti = logoSpolocnosti;
	}

	public float getPoklPociatocnaSuma() {
		return poklPociatocnaSuma;
	}

	public void setPoklPociatocnaSuma(float poklPociatocnaSuma) {
		this.poklPociatocnaSuma = poklPociatocnaSuma;
	}

	public float getPoklMaxSuma() {
		return poklMaxSuma;
	}

	public void setPoklMaxSuma(float poklMaxSuma) {
		this.poklMaxSuma = poklMaxSuma;
	}
}
