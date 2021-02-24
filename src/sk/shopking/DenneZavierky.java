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
public class DenneZavierky{
	
	private List<DennaZavierka> denneZavierky;
	
	private final Date datumZavierok;
	
	private float obratSadzba10;
	private float zakladSadzba10;
	private float dphSadzba10;
	
	private float obratSadzba20;
	private float zakladSadzba20;
	private float dphSadzba20;
	
	private float zapornyObratSadzba10;
	private float zapornyZakladSadzba10;
	private float zaporneDPHSadzba10;
	
	private float zapornyObratSadzba20;
	private float zapornyZakladSadzba20;
	private float zaporneDPHSadzba20;
	
	private final float sumaZapornyObrat;
	private final float sumaObrat;
	private final float sumaZakladDane;
	private final float sumaDPH;
	
	private final SimpleStringProperty datumProperty  = new SimpleStringProperty();
	private final SimpleStringProperty celkovyObratProperty = new SimpleStringProperty();
	private final SimpleStringProperty obrat10Property  = new SimpleStringProperty();
	private final SimpleStringProperty obrat20Property  = new SimpleStringProperty();
	private final SimpleStringProperty zapornyObratProperty = new SimpleStringProperty();
	
	public DenneZavierky(List<DennaZavierka> denneZavierky) {
		this.setDennaZavierky(denneZavierky);
		
		this.datumZavierok = denneZavierky.get(0).getCasZavierky();
		
		for (DennaZavierka dennaZavierka : denneZavierky) {
			obratSadzba10 += dennaZavierka.getObratSadzba10();
			zakladSadzba10 += dennaZavierka.getZakladSadzba10();
			dphSadzba10 += dennaZavierka.getDphSadzba10();
			
			obratSadzba20 += dennaZavierka.getObratSadzba20();
			zakladSadzba20 += dennaZavierka.getZakladSadzba20();
			dphSadzba20 += dennaZavierka.getDphSadzba20();
			
			zapornyObratSadzba10 += dennaZavierka.getZapornyObratSadzba10();
			zapornyZakladSadzba10 += dennaZavierka.getZapornyZakladSadzba10();
			zaporneDPHSadzba10 += dennaZavierka.getZaporneDPHSadzba10();
			
			zapornyObratSadzba20 += dennaZavierka.getZapornyObratSadzba20();
			zapornyZakladSadzba20 += dennaZavierka.getZapornyZakladSadzba20();
			zaporneDPHSadzba20 += dennaZavierka.getZaporneDPHSadzba20();

		}
		
		sumaZapornyObrat = zapornyObratSadzba10 + zapornyObratSadzba20;
		sumaObrat = obratSadzba10 + obratSadzba20 - zapornyObratSadzba10 - zapornyObratSadzba20;
		sumaZakladDane = zakladSadzba10 + zakladSadzba20 - zapornyZakladSadzba10 - zapornyZakladSadzba20;
		sumaDPH = dphSadzba10 + dphSadzba20 - zaporneDPHSadzba10 - zaporneDPHSadzba20;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		this.setDatumProperty(dateFormat.format(this.datumZavierok));
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
	

	public Date getDatumZavierok() {
		return datumZavierok;
	}


	public List<DennaZavierka> getDenneZavierky() {
		return denneZavierky;
	}
	
	public void setDennaZavierky(List<DennaZavierka> dennaZavierky) {
		this.denneZavierky = dennaZavierky;
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
	
	public SimpleStringProperty datumProperty() {
		return datumProperty;
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
