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
public class MesacnaZavierka implements Zavierka{
	private int idZavierky;
	private Date casZavierky;
	private List<DennaZavierka> denneZavierky;
	private final Date intervalOd;
	private final Date intervalDo;
	
	private float obratSadzba10;
	private float zakladSadzba10;
	private float dphSadzba10;
	
	private float obratSadzba20;
	private float zakladSadzba20;
	private float dphSadzba20;
	
	private float sumaObrat;
	private float sumaZakladDane;
	private float sumaDPH;
	
	private final SimpleStringProperty datumZavierkyProperty  = new SimpleStringProperty();
	private final SimpleStringProperty datumOdProperty  = new SimpleStringProperty();
	private final SimpleStringProperty datumDoProperty  = new SimpleStringProperty();
	private final SimpleStringProperty celkovyObratProperty = new SimpleStringProperty();
	
	public MesacnaZavierka(int id, List<DennaZavierka> denneZavierky,Date casZavierky,Date intervalOd,Date intervalDo) {
		this.idZavierky = id;
		this.denneZavierky = denneZavierky;
		this.casZavierky = casZavierky;
		this.intervalOd = intervalOd;
		this.intervalDo = intervalDo;
		
		for (DennaZavierka zavierka : denneZavierky) {
			sumaObrat += zavierka.getSumaObrat();
			sumaZakladDane += zavierka.getSumaZakladDane();
			sumaDPH += zavierka.getSumaDPH();
			
			obratSadzba10 += zavierka.getObratSadzba10();
			zakladSadzba10 += zavierka.getZakladSadzba10();
			dphSadzba10 += zavierka.getDphSadzba10();
			
			obratSadzba20 += zavierka.getObratSadzba20();
			zakladSadzba20 += zavierka.getZakladSadzba20();
			dphSadzba20 += zavierka.getDphSadzba20();
		}
		
		this.setDatumZavierkyProperty(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(casZavierky));
		this.setDatumOdProperty(new SimpleDateFormat("dd.MM.yyyy").format(intervalOd));
		this.setDatumDoProperty(new SimpleDateFormat("dd.MM.yyyy").format(intervalDo));
		this.setCelkovyObratProperty(new DecimalFormat("#.##;-#.##").format(sumaObrat));
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
	
	public List<DennaZavierka> getDenneZavierky() {
		return denneZavierky;
	}
	
	public void setDenneZavierky(List<DennaZavierka> denneZavierky) {
		this.denneZavierky = denneZavierky;
	}
	
	public Date getIntervalOd() {
		return intervalOd;
	}

	public Date getIntervalDo() {
		return intervalDo;
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

	public float getSumaObrat() {
		return sumaObrat;
	}

	public float getSumaZakladDane() {
		return sumaZakladDane;
	}

	public float getSumaDPH() {
		return sumaDPH;
	}

	public SimpleStringProperty datumZavierkyProperty() {
		return datumZavierkyProperty;
	}

	public SimpleStringProperty datumOdProperty() {
		return datumOdProperty;
	}
	
	public SimpleStringProperty datumDoProperty() {
		return datumDoProperty;
	}
	
	public SimpleStringProperty celkovyObratProperty() {
		return celkovyObratProperty;
	}
	
	public final String getDatumZavierkyProperty() {
        return datumZavierkyProperty().get();
    }
	
	public final String getDatumOdProperty() {
        return datumOdProperty().get();
    }
	
	public final String getDatumDoProperty() {
        return datumDoProperty().get();
    }
	
	public final String getCelkovyObratProperty() {
        return celkovyObratProperty().get();
    }
	
	public final void setDatumZavierkyProperty(String datum) {
		datumZavierkyProperty().set(datum);
    }
	
	public final void setDatumOdProperty(String datum) {
		datumOdProperty().set(datum);
    }
	
	public final void setDatumDoProperty(String datum) {
		datumDoProperty().set(datum);
    }
	
	public final void setCelkovyObratProperty(String obrat) {
		celkovyObratProperty().set(obrat);
    }
}	
