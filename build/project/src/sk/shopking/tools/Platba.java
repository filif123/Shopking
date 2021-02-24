/**
 * 
 */
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
	
	public Platba(float celkovaSuma, float zaplatene) {
		this.setCelkovaSuma(celkovaSuma);
		this.setZaplatene(zaplatene);
		this.setTypPlatby(PlatbaType.HOTOVOST);
	}
	
	public Platba(PlatbaType typPlatby,float celkovaSuma, float zaplatene, float doplatokHotovostou) {
		this.setCelkovaSuma(celkovaSuma);
		this.setTypPlatby(typPlatby);
		this.setZaplatene(zaplatene);
		this.setDoplatokHotovostou(doplatokHotovostou);
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

	
	
}
