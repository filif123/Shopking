package sk.shopking;

import java.text.DecimalFormat;

/**
 * @author Filip
 *
 */
public class TovarZlavaCena extends Tovar{
	
	private float novaCena;
	private final float rozdiel;
	private float zlavaVPercentach;
	
	public TovarZlavaCena(int tovarPLU,String tovarName,Category tovarCategory, JednotkaType jednotka, long tovarEAN, float tovarCena,DPHType dph,float novaCena) {
		super(tovarPLU, tovarName, tovarCategory, jednotka, tovarEAN, tovarCena, dph);
		this.novaCena = novaCena;
		this.rozdiel = tovarCena - novaCena;
		this.zlavaVPercentach = 100 - ((novaCena / tovarCena)*100);
		DecimalFormat df = new DecimalFormat("#.#");
		super.setAkciaProperty("(" + tovarCena + "€) -> " + novaCena + "€ (" + df.format(zlavaVPercentach) + "%)");
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

	public float getRozdiel() {
		return rozdiel;
	}
}
