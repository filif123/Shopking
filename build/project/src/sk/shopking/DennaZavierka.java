/**
 * 
 */
package sk.shopking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author filip
 *
 */
public class DennaZavierka {
	private int idZavierky;
	private Date casZavierky;
	private UserPokladnik userPokladnik;
	private List<Nakup> nakupyDoZavierky;
	
	private float celkovyObrat;
	private float zapornyObrat;
	
	private float obratSadzba10;
	private float zakladSadzba10;
	private float dphSadzba10;
	
	private float obratSadzba20;
	private float zakladSadzba20;
	private float dphSadzba20;
	
	private final SimpleStringProperty datum  = new SimpleStringProperty();
	private final SimpleStringProperty cas  = new SimpleStringProperty();
	private final SimpleStringProperty pokladnik  = new SimpleStringProperty();
	private final SimpleStringProperty obrat  = new SimpleStringProperty();
	
	public DennaZavierka(int id,List<Nakup> nakupyDoZavierky,Date casZavierky, UserPokladnik userPokladnik) {
		this.idZavierky = id;
		this.nakupyDoZavierky = nakupyDoZavierky;
		this.casZavierky = casZavierky;
		this.userPokladnik = userPokladnik;
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(casZavierky);
		
		float celkovyObrat = 0;
		float obrat20 = 0;
		float obrat10 = 0;
		
		for (Nakup nakup : nakupyDoZavierky) {
			List<NakupenyTovar> nakupeneTovary = nakup.getNakupenyTovar();
			for (NakupenyTovar nakupenyTovar : nakupeneTovary) {
				celkovyObrat = celkovyObrat + nakupenyTovar.getNakupenaCena();
				if (nakupenyTovar.getTovarDPH().equals(DPHType.DPH_10)) {
					obrat10 = obrat10 + nakupenyTovar.getNakupenaCena();
				}
				else {
					obrat20 = obrat20 + nakupenyTovar.getNakupenaCena();
				}
			}
		}
		
		this.celkovyObrat = celkovyObrat;
		
		this.obratSadzba10 = obrat10;
		this.zakladSadzba10 = obrat10 / 1.1f;
		this.dphSadzba10 = obrat10 - zakladSadzba10; 
				
		this.obratSadzba20 = obrat20;
		this.zakladSadzba20 = obrat20 / 1.2f;
		this.dphSadzba20 = obrat20 - zakladSadzba10; 
		
		this.setDatumProperty(dateFormat.format(casZavierky));
		this.setCasProperty(timeFormat.format(casZavierky));
		this.setPokladnikProperty(userPokladnik.toString());
		this.setObratProperty("" + celkovyObrat);
	}
	
	public int getIdZavierky() {
		return idZavierky;
	}

	public void setIdZavierky(int idZavierky) {
		this.idZavierky = idZavierky;
	}

	public Date getCasZavierky() {
		return casZavierky;
	}
	
	public void setCasZavierky(Date casZavierky) {
		this.casZavierky = casZavierky;
	}
	
	public List<Nakup> getNakupyDoZavierky() {
		return nakupyDoZavierky;
	}
	
	public void setNakupyDoZavierky(List<Nakup> nakupyDoZavierky) {
		this.nakupyDoZavierky = nakupyDoZavierky;
	}
	
	public UserPokladnik getUserPokladnik() {
		return userPokladnik;
	}

	public void setUserPokladnik(UserPokladnik userPokladnik) {
		this.userPokladnik = userPokladnik;
	}

	public float getCelkovyObrat() {
		return celkovyObrat;
	}

	public float getObratSadzba10() {
		return obratSadzba10;
	}

	public float getZapornyObrat() {
		return zapornyObrat;
	}

	public float getZakladSadzba10() {
		return zakladSadzba10;
	}

	public float getDphSadzba10() {
		return dphSadzba10;
	}

	public float getObratSadzba20() {
		return obratSadzba20;
	}

	public float getZakladSadzba20() {
		return zakladSadzba20;
	}

	public float getDphSadzba20() {
		return dphSadzba20;
	}

	public SimpleStringProperty datumProperty() {
		return datum;
	}

	public SimpleStringProperty casProperty() {
		return cas;
	}
	
	public SimpleStringProperty pokladnikProperty() {
		return pokladnik;
	}
	
	public SimpleStringProperty obratProperty() {
		return obrat;
	}
	
	public final String getDatumProperty() {
        return datumProperty().get();
    }
	
	public final String getCasProperty() {
        return casProperty().get();
    }
	
	public final String getPokladnikProperty() {
        return pokladnikProperty().get();
    }
	
	public final String getObratProperty() {
        return obratProperty().get();
    }
	
	public final void setDatumProperty(String datum) {
		datumProperty().set(datum);
    }
	
	public final void setCasProperty(String cas) {
		casProperty().set(cas);
    }
	
	public final void setPokladnikProperty(String pokladnik) {
		pokladnikProperty().set(pokladnik);
    }
	
	public final void setObratProperty(String obrat) {
		obratProperty().set(obrat);
    }
}
