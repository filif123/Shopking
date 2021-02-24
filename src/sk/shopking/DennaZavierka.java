package sk.shopking;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author filip
 *
 */
public class DennaZavierka implements Zavierka{
	private int idZavierky;
	private Date casZavierky;
	private UserPokladnik userPokladnik;
	private List<Nakup> nakupyDoZavierky;
	private List<NakupenyTovar> stornovaneNakupenyTovary;
	
	private final float obratSadzba10;
	private final float zakladSadzba10;
	private final float dphSadzba10;
	
	private final float obratSadzba20;
	private final float zakladSadzba20;
	private final float dphSadzba20;
	
	private final float zapornyObratSadzba10;
	private final float zapornyZakladSadzba10;
	private final float zaporneDPHSadzba10;
	
	private final float zapornyObratSadzba20;
	private final float zapornyZakladSadzba20;
	private final float zaporneDPHSadzba20;
	
	private final float sumaZapornyObrat;
	private final float sumaObrat;
	private final float sumaZakladDane;
	private final float sumaDPH;
	
	private final SimpleStringProperty datumProperty  = new SimpleStringProperty();
	private final SimpleStringProperty casProperty  = new SimpleStringProperty();
	private final SimpleStringProperty pokladnikProperty  = new SimpleStringProperty();
	private final SimpleStringProperty celkovyObratProperty = new SimpleStringProperty();
	private final SimpleStringProperty obrat10Property  = new SimpleStringProperty();
	private final SimpleStringProperty obrat20Property  = new SimpleStringProperty();
	private final SimpleStringProperty zapornyObratProperty = new SimpleStringProperty();
	
	public DennaZavierka(int id,List<Nakup> nakupyDoZavierky,Date casZavierky, UserPokladnik userPokladnik, List<NakupenyTovar> stornovaneNakupenyTovary) {
		this.idZavierky = id;
		this.nakupyDoZavierky = nakupyDoZavierky;
		this.casZavierky = casZavierky;
		this.userPokladnik = userPokladnik;
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		float celkovyObrat = 0;
		float obrat20 = 0;
		float obrat10 = 0;
		
		for (Nakup nakup : nakupyDoZavierky) {
			List<NakupenyTovar> nakupeneTovary = nakup.getNakupenyTovar();
			for (NakupenyTovar nakupenyTovar : nakupeneTovary) {
				celkovyObrat = celkovyObrat + nakupenyTovar.getNakupenaCena();
				if (nakupenyTovar.getTovarDPH().equals(DPHType.DPH_10)) {
					obrat10 += nakupenyTovar.getNakupenaCena();
				}
				else {
					obrat20 += nakupenyTovar.getNakupenaCena();
				}
			}
		}
		
		this.stornovaneNakupenyTovary = stornovaneNakupenyTovary;
		
		float celkovyZapornyObrat = 0;
		float zapObrat20 = 0;
		float zapObrat10 = 0;
		for (NakupenyTovar nakupenyTovar : stornovaneNakupenyTovary) {
			celkovyZapornyObrat = celkovyZapornyObrat + (nakupenyTovar.getNakupeneMnozstvo() - nakupenyTovar.getNoveMnozstvoPoStorne()) * nakupenyTovar.getTovarJednotkovaCena();
			if (nakupenyTovar.getTovarDPH().equals(DPHType.DPH_10)) {
				zapObrat10 += (nakupenyTovar.getNakupeneMnozstvo() - nakupenyTovar.getNoveMnozstvoPoStorne()) * nakupenyTovar.getTovarJednotkovaCena();
			}
			else {
				zapObrat20 += (nakupenyTovar.getNakupeneMnozstvo() - nakupenyTovar.getNoveMnozstvoPoStorne()) * nakupenyTovar.getTovarJednotkovaCena();
			}
		}
		
		this.zapornyObratSadzba10 = zapObrat10;
		this.zapornyZakladSadzba10 = zapObrat10 / 1.1f;
		this.zaporneDPHSadzba10 = zapObrat10 - zapornyZakladSadzba10;
		
		this.zapornyObratSadzba20 = zapObrat20;
		this.zapornyZakladSadzba20 = zapObrat20 / 1.2f;
		this.zaporneDPHSadzba20 = zapObrat20 - zapornyZakladSadzba20;

		this.obratSadzba10 = obrat10;
		this.zakladSadzba10 = obrat10 / 1.1f;
		this.dphSadzba10 = obrat10 - zakladSadzba10; 
				
		this.obratSadzba20 = obrat20;
		this.zakladSadzba20 = obrat20 / 1.2f;
		this.dphSadzba20 = obrat20 - zakladSadzba20;
		
		this.sumaZapornyObrat = zapornyObratSadzba10 + zapornyObratSadzba20;
		this.sumaObrat = obratSadzba10 + obratSadzba20 - zapornyObratSadzba10 - zapornyObratSadzba20;
		this.sumaZakladDane = zakladSadzba10 + zakladSadzba20 - zapornyZakladSadzba10 - zapornyZakladSadzba20;
		this.sumaDPH = dphSadzba10 + dphSadzba20 - zaporneDPHSadzba10 - zaporneDPHSadzba20;
		
		this.setDatumProperty(dateFormat.format(casZavierky));
		this.setCasProperty(timeFormat.format(casZavierky));
		this.setPokladnikProperty(userPokladnik.toString());
		this.setCelkovyObratProperty(new DecimalFormat("#.##").format(this.sumaObrat));
		this.setObrat10Property(new DecimalFormat("#.##").format(obratSadzba10));
		this.setObrat20Property(new DecimalFormat("#.##").format(obratSadzba20));
		if (this.sumaZapornyObrat != 0) {
			this.setZapornyObratProperty(new DecimalFormat("#.##").format(-this.sumaZapornyObrat));
		}
		else {
			this.setZapornyObratProperty(new DecimalFormat("#.##").format(this.sumaZapornyObrat));
		}
		
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

	public float getObratSadzba10() {
		return obratSadzba10;
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

	public float getZapornyObratSadzba10() {
		return zapornyObratSadzba10;
	}

	public float getZapornyZakladSadzba10() {
		return zapornyZakladSadzba10;
	}

	public float getZaporneDPHSadzba10() {
		return zaporneDPHSadzba10;
	}

	public float getZapornyObratSadzba20() {
		return zapornyObratSadzba20;
	}
	
	public float getZapornyZakladSadzba20() {
		return zapornyZakladSadzba20;
	}

	public float getZaporneDPHSadzba20() {
		return zaporneDPHSadzba20;
	}
	
	public float getSumaZapornyObrat() {
		return sumaZapornyObrat;
	}

	public float getSumaObrat() {
		return sumaObrat;
	}

	public float getSumaZakladDane() {
		return sumaZakladDane;
	}

	public float getSumaDPH() {
		return sumaDPH;
	}

	public List<NakupenyTovar> getStornovaneNakupenyTovary() {
		return stornovaneNakupenyTovary;
	}

	public void setStornovaneNakupenyTovary(List<NakupenyTovar> stornovaneNakupenyTovary) {
		this.stornovaneNakupenyTovary = stornovaneNakupenyTovary;
	}

	public SimpleStringProperty datumProperty() {
		return datumProperty;
	}

	public SimpleStringProperty casProperty() {
		return casProperty;
	}
	
	public SimpleStringProperty pokladnikProperty() {
		return pokladnikProperty;
	}
	
	public SimpleStringProperty celkovyObratProperty() {
		return celkovyObratProperty;
	}
	
	public SimpleStringProperty obrat10Property() {
		return obrat10Property;
	}
	
	public SimpleStringProperty obrat20Property() {
		return obrat20Property;
	}
	
	public SimpleStringProperty zapornyObratProperty() {
		return zapornyObratProperty;
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
	
	public final String getCelkovyObratProperty() {
        return celkovyObratProperty().get();
    }
	
	public final String getObrat10Property() {
        return obrat10Property().get();
    }
	
	public final String getObrat20Property() {
        return obrat20Property().get();
    }
	
	public final String getZapornyObratProperty() {
        return zapornyObratProperty().get();
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
	
	public final void setCelkovyObratProperty(String obrat) {
		celkovyObratProperty().set(obrat);
    }
	
	public final void setObrat10Property(String obrat) {
		obrat10Property().set(obrat);
    }
	
	public final void setObrat20Property(String obrat) {
		obrat20Property().set(obrat);
    }
	
	public final void setZapornyObratProperty(String obrat) {
		zapornyObratProperty().set(obrat);
    }
}
