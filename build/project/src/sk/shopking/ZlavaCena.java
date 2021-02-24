/**
 * 
 */
package sk.shopking;

/**
 * @author Filip
 *
 */
public class ZlavaCena implements Akcia{
	
	private float povodnaCena;
	private float novaCena;
	private float rozdiel;
	private float zlavaVPercentach;
	
	public ZlavaCena(float povodnaCena, float novaCena) {
		this.povodnaCena = povodnaCena;
		this.novaCena = novaCena;
		this.rozdiel = povodnaCena - novaCena;
	}
	
	public float getNovaCena() {
		return novaCena;
	}

	public void setNovaCena(float novaCena) {
		this.novaCena = novaCena;
	}

	public float getZlavaVPercentach() {
		return zlavaVPercentach;
	}

	public void setZlavaVPercentach(float zlavaVPercentach) {
		this.zlavaVPercentach = zlavaVPercentach;
	}

	public float getPovodnaCena() {
		return povodnaCena;
	}

	public void setPovodnaCena(float povodnaCena) {
		this.povodnaCena = povodnaCena;
	}

	public float getRozdiel() {
		this.rozdiel = povodnaCena - novaCena;
		return rozdiel;
	}
}
