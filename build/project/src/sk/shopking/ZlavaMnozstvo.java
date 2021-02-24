/**
 * 
 */
package sk.shopking;

/**
 * @author Filip
 *
 */
public class ZlavaMnozstvo implements Akcia {

	private float povodneMnozsvo;
	private float noveMnozstvo;
	private float minimalneMnozstvo;
	
	public ZlavaMnozstvo(int povodneMnozsvo, int noveMnozstvo, int minimalneMnozstvo) {
		this.povodneMnozsvo = povodneMnozsvo;
		this.noveMnozstvo = noveMnozstvo;
		this.minimalneMnozstvo = minimalneMnozstvo;
	}

	public float getPovodneMnozsvo() {
		return povodneMnozsvo;
	}

	public void setPovodneMnozsvo(float povodneMnozsvo) {
		this.povodneMnozsvo = povodneMnozsvo;
	}

	public float getNoveMnozstvo() {
		return noveMnozstvo;
	}

	public void setNoveMnozstvo(float noveMnozstvo) {
		this.noveMnozstvo = noveMnozstvo;
	}

	public float getMinimalneMnozstvo() {
		return minimalneMnozstvo;
	}

	public void setMinimalneMnozstvo(float minimalneMnozstvo) {
		this.minimalneMnozstvo = minimalneMnozstvo;
	}
	
}
