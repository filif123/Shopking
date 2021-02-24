/**
 * 
 */
package sk.shopking;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Pomocou tejto triedy sa zobrazujú aktuálne informácie o pokladniciach v obchodnej prevádzke.
 * @author Filip
 *
 */
public class Pokladnica {
	
	private int idPokladnice;
	private String dkpPokladnice;
	private boolean isOpen;
	private UserPokladnik pokladnikUser;
	
	private final SimpleStringProperty id  = new SimpleStringProperty();
	private final SimpleStringProperty dkp = new SimpleStringProperty();
	private final SimpleStringProperty pripojena = new SimpleStringProperty();
	private final SimpleBooleanProperty open = new SimpleBooleanProperty();
	private final SimpleStringProperty pokladnik = new SimpleStringProperty();
	private final SimpleStringProperty suma = new SimpleStringProperty();
	
	public Pokladnica(int idPokladnice, String dkpPokladnice, boolean isOpen, UserPokladnik pokladnik){
		this.idPokladnice = idPokladnice;
		this.isOpen = isOpen;
		this.dkpPokladnice = dkpPokladnice;
		this.pokladnikUser = pokladnik;
		
		this.setDkpProperty(dkpPokladnice);
		this.setIsOpenProperty(isOpen);
		this.setIdProperty("" + idPokladnice);
		if (isOpen) {
			this.setPripojenaProperty("ip");
			this.setPokladnikProperty(pokladnik.getUserMeno() + " " + pokladnik.getUserPriezvisko());
			this.setSumaProperty("" + pokladnik.getSumaPokladnica());
		}
		else {
			this.setPripojenaProperty("-");
			this.setPokladnikProperty("-");
			this.setSumaProperty("-");
		}
		
	}
	
	public int getIdPokladnice() {
		return idPokladnice;
	}

	public void setIdPokladnice(int idPokladnice) {
		this.idPokladnice = idPokladnice;
	}

	public String getDkpPokladnice() {
		return dkpPokladnice;
	}

	public void setDkpPokladnice(String dkpPokladnice) {
		this.dkpPokladnice = dkpPokladnice;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public UserPokladnik getPokladnikUser() {
		return pokladnikUser;
	}

	public void setPokladnikUser(UserPokladnik pokladnikUser) {
		this.pokladnikUser = pokladnikUser;
	}

	public SimpleStringProperty idProperty() {
		return id;
	}

	public SimpleStringProperty dkpProperty() {
		return dkp;
	}
	
	public SimpleStringProperty pripojenaProperty() {
		return pripojena;
	}
	
	public SimpleBooleanProperty openProperty() {
		return open;
	}
	
	public SimpleStringProperty pokladnikProperty() {
		return pokladnik;
	}
	
	public SimpleStringProperty sumaProperty() {
		return suma;
	}
	
	public final String getIdProperty() {
        return idProperty().get();
    }
	
	public final String getDkpProperty() {
        return dkpProperty().get();
    }
	
	public final String getPripojenaProperty() {
        return pripojenaProperty().get();
    }
	
	public final boolean getIsOpenProperty() {
        return openProperty().get();
    }
	
	public final String getPokladnikProperty() {
        return pokladnikProperty().get();
    }
	
	public final String getSumaProperty() {
        return sumaProperty().get();
    }
	
	public final void setIdProperty(String id) {
		idProperty().set(id);
    }
	
	public final void setDkpProperty(String dkp) {
		dkpProperty().set(dkp);
    }
	
	public final void setPripojenaProperty(String pripojena) {
		pripojenaProperty().set(pripojena);
    }
	
	public final void setIsOpenProperty(boolean isOpen) {
		openProperty().set(isOpen);
    }
	
	public final void setPokladnikProperty(String pokladnik) {
		pokladnikProperty().set(pokladnik);
    }
	
	public final void setSumaProperty(String suma) {
		sumaProperty().set(suma);
    }
	
	@Override
	public String toString() {
		return "Pokladnica " + idPokladnice;
		
	}
}
