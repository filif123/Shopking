package sk.shopking;

/**
 * Určuje výšku DPH u jednotlivých tovarov.
 * @author Filip
 *
 */
public enum DPHType {
	/**
	 * 10% DPH
	 */
	DPH_10("10"),
	/**
	 * 20% DPH
	 */
	DPH_20("20");
	
	private final String dph;
	
	DPHType(String dph) {
		this.dph = dph;
	}
	
	public String getDPHtype() {
        return dph;
    }
	
	@Override 
	public String toString() { 
		return dph; 
	}
}
