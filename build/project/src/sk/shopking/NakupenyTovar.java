/**
 * 
 */
package sk.shopking;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleStringProperty;
import sk.shopking.tools.ShopKingTools;
/**
 * Táto trieda slúži na zobrazenie nákupov uskutočnených zákazníkmi.
 * @author Filip
 *
 */
public class NakupenyTovar extends Tovar{
	
	private float nakupeneMnozstvo;
	private float nakupenaCena;
	
	private final SimpleStringProperty mnozstvo  = new SimpleStringProperty();
	private final SimpleStringProperty cena  = new SimpleStringProperty();
	
	public NakupenyTovar(int tovarPLU,String tovarName, Category tovarCategory, JednotkaType jednotka, long tovarCode,float tovarCena, DPHType dph, float mnozstvo) {
		
		super(tovarPLU,tovarName, tovarCategory, jednotka, tovarCode, tovarCena, dph);
		
		this.setMnozstvoProperty("" + new DecimalFormat("#.##").format(mnozstvo));
		this.setNakupeneMnozstvo(mnozstvo);
		
		if(jednotka.equals(JednotkaType.KS) || jednotka.equals(JednotkaType.KG)) {
			this.setCenaProperty("" + new DecimalFormat("#.##").format(mnozstvo * tovarCena));
			this.setNakupenaCena(mnozstvo * tovarCena);
		}
		else if(jednotka.equals(JednotkaType.G)) {
			this.setCenaProperty("" + new DecimalFormat("#.##").format(ShopKingTools.roundNumber((double)(mnozstvo / 1000) * tovarCena,2)));
			this.setNakupenaCena((float)ShopKingTools.roundNumber((double)(mnozstvo / 1000) * tovarCena,2));
		}
	}
	
	public float getNakupeneMnozstvo() {
		return nakupeneMnozstvo;
	}

	public void setNakupeneMnozstvo(float nakupeneMnozstvo) {
		this.nakupeneMnozstvo = nakupeneMnozstvo;
	}
	

	public float getNakupenaCena() {
		return nakupenaCena;
	}

	public void setNakupenaCena(float nakupenaCena) {
		this.nakupenaCena = nakupenaCena;
	}
	
	public SimpleStringProperty mnozstvoProperty() {
		return mnozstvo;
	}
	
	public SimpleStringProperty cenaProperty() {
		return cena;
	}
	
	public final String getMnozstvoProperty() {
        return mnozstvoProperty().get();
    }
	
	public final String getCenaProperty() {
        return cenaProperty().get();
    }
	
	public final void setMnozstvoProperty(String mnozstvo) {
		mnozstvoProperty().set(mnozstvo);
    }
	
	public final void setCenaProperty(String cena) {
		cenaProperty().set(cena);
    }
}
