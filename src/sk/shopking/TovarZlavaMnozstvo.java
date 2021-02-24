package sk.shopking;

/**
 * @author Filip
 *
 */
public class TovarZlavaMnozstvo extends Tovar {

	private int povodneMnozstvo;
	private int noveMnozstvo;
	private int minimalneMnozstvo; // je mnozstvo, od ktorého platí táto zľava
	
	public TovarZlavaMnozstvo(int tovarPLU,String tovarName,Category tovarCategory, JednotkaType jednotka, long tovarEAN, float tovarCena,DPHType dph,int povodneMnozstvo, int noveMnozstvo, int minimalneMnozstvo) {
		super(tovarPLU, tovarName, tovarCategory, jednotka, tovarEAN, tovarCena, dph);
		this.povodneMnozstvo = povodneMnozstvo;
		this.noveMnozstvo = noveMnozstvo;
		this.minimalneMnozstvo = minimalneMnozstvo;
		super.setAkciaProperty("" + povodneMnozstvo + "ks za cenu " + noveMnozstvo + "ks (od" + minimalneMnozstvo + "ks)");
	}

	public int getPovodneMnozstvo() {
		return povodneMnozstvo;
	}

	public void setPovodneMnozstvo(int povodneMnozsvo) {
		this.povodneMnozstvo = povodneMnozsvo;
	}

	public int getNoveMnozstvo() {
		return noveMnozstvo;
	}

	public void setNoveMnozstvo(int noveMnozstvo) {
		this.noveMnozstvo = noveMnozstvo;
	}

	public int getMinimalneMnozstvo() {
		return minimalneMnozstvo;
	}

	public void setMinimalneMnozstvo(int minimalneMnozstvo) {
		this.minimalneMnozstvo = minimalneMnozstvo;
	}
	
}
