/**
 * 
 */
package sk.shopking.tools;

import sk.shopking.DPHType;

/**
 * @author Filip
 *
 */
public class DPH {
	
	public static float vypocetDPH(float cena, DPHType dphType) {
		float dphnum;
		if (dphType.equals(DPHType.DPH_10)) {
			dphnum = 0.1f;
		}
		else {
			dphnum = 0.2f;
		}
		float cenaBezDPH = cena / (1 + dphnum);
		return cena - cenaBezDPH;
	}
	
	public static float vypocetCenaBezDPH(float cena, DPHType dphType) {
		float dphnum;
		if (dphType.equals(DPHType.DPH_10)) {
			dphnum = 0.1f;
		}
		else {
			dphnum = 0.2f;
		}
		float cenaBezDPH = cena / (1 + dphnum);
		return cenaBezDPH;
	}
}
