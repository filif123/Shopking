package sk.shopking.tools;

/**
 * @author Filip
 *
 */
public class Platba {
	
	private PlatbaType typPlatby;
	private float celkovaSuma;
	private float zaplatene;
	private float doplatokHotovostou;
	private int pocetStravnychListkov;
	private float hodnotaStravnehoListka;
	
	public Platba(float celkovaSuma, float zaplatene) {
		this.setCelkovaSuma(celkovaSuma);
		this.setZaplatene(zaplatene);
		this.setTypPlatby(PlatbaType.HOTOVOST);
	}
	
	public Platba(float celkovaSuma,float doplatokHotovostou, int pocetStravnychListkov, float hodnotaListka) {
		this.setTypPlatby(PlatbaType.STRAVNE_LISTKY);
		this.setCelkovaSuma(celkovaSuma);
		this.setZaplatene(pocetStravnychListkov * hodnotaListka);
		this.setDoplatokHotovostou(doplatokHotovostou);
		this.setPocetStravnychListkov(pocetStravnychListkov);
		this.setHodnotaStravnehoListka(hodnotaListka);
	}
	
	public void setTypPlatby(PlatbaType typPlatby) {
		this.typPlatby = typPlatby;
	}
	
	public PlatbaType getTypPlatby() {
		return typPlatby;
	}

	public float getCelkovaSuma() {
		return celkovaSuma;
	}

	public void setCelkovaSuma(float celkovaSuma) {
		this.celkovaSuma = celkovaSuma;
	}

	public float getDoplatokHotovostou() {
		return doplatokHotovostou;
	}

	public void setDoplatokHotovostou(float doplatokHotovostou) {
		this.doplatokHotovostou = doplatokHotovostou;
	}

	public float getZaplatene() {
		return zaplatene;
	}

	public void setZaplatene(float zaplatene) {
		this.zaplatene = zaplatene;
	}

	public int getPocetStravnychListkov() {
		return pocetStravnychListkov;
	}

	public void setPocetStravnychListkov(int pocetStravnychListkov) {
		this.pocetStravnychListkov = pocetStravnychListkov;
	}

	public float getHodnotaStravnehoListka() {
		return hodnotaStravnehoListka;
	}

	public void setHodnotaStravnehoListka(float hodnotaStravnehoListka) {
		this.hodnotaStravnehoListka = hodnotaStravnehoListka;
	}

	
	
}
