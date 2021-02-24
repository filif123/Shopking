package sk.shopking;

import java.util.List;

/**
 * 
 * @author Filip
 *
 */
public class Nakup {
	
	private List<NakupenyTovar> nakupenyTovar;
	private Doklad dokladNakupu;
	
	public Nakup(List<NakupenyTovar> nakupenyTovar, Doklad dokladNakupu) {
		this.setNakupenyTovar(nakupenyTovar);
		this.setDokladNakupu(dokladNakupu);
	}

	public List<NakupenyTovar> getNakupenyTovar() {
		return nakupenyTovar;
	}

	public void setNakupenyTovar(List<NakupenyTovar> nakupenyTovar) {
		this.nakupenyTovar = nakupenyTovar;
	}

	public Doklad getDokladNakupu() {
		return dokladNakupu;
	}

	public void setDokladNakupu(Doklad dokladNakupu) {
		this.dokladNakupu = dokladNakupu;
	}
}
