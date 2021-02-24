package sk.shopking;

import java.text.DecimalFormat;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author filip
 *
 */
public class Predajnost {
	
	private Tovar predavajuciTovar;
	private float predajnost;
	
	private final SimpleStringProperty nazovProperty  = new SimpleStringProperty();
	private final SimpleStringProperty cenaProperty  = new SimpleStringProperty();
	private final SimpleStringProperty predajnostProperty  = new SimpleStringProperty();
	private final SimpleStringProperty jednotkaProperty  = new SimpleStringProperty();
	
	public Predajnost(Tovar tovar, float predajnost) {
		this.setPredavajuciTovar(tovar);
		this.setPredajnost(predajnost);
		
		this.setNazovProperty(tovar.getTovarName());
		this.setCenaProperty(new DecimalFormat("#.##").format(tovar.getTovarJednotkovaCena()));
		this.setPredajnostProperty(new DecimalFormat("#.##").format(predajnost));
		this.setJednotkaProperty(tovar.getJednotkaProperty());
	}

	public Tovar getPredavajuciTovar() {
		return predavajuciTovar;
	}

	public void setPredavajuciTovar(Tovar predavajuciTovar) {
		this.predavajuciTovar = predavajuciTovar;
	}

	public float getPredajnost() {
		return predajnost;
	}

	public void setPredajnost(float predajnost) {
		this.predajnost = predajnost;
	}
	
	public SimpleStringProperty nazovProperty() {
		return nazovProperty;
	}
	
	public SimpleStringProperty cenaProperty() {
		return cenaProperty;
	}
	
	public SimpleStringProperty predajnostProperty() {
		return predajnostProperty;
	}
	
	public SimpleStringProperty jednotkaProperty() {
		return jednotkaProperty;
	}
	
	public final String getNazovProperty() {
        return nazovProperty().get();
    }
	
	public final String getCenaProperty() {
        return cenaProperty().get();
    }
	
	public final String getPredajnostProperty() {
        return predajnostProperty().get();
    }
	
	public final String getJednotkaProperty() {
        return jednotkaProperty().get();
    }
	
	public final void setNazovProperty(String name) {
		nazovProperty().set(name);
    }
	
	public final void setCenaProperty(String cena) {
		cenaProperty().set(cena);
    }
	
	public final void setPredajnostProperty(String predajnost) {
		predajnostProperty().set(predajnost);
    }
	
	public final void setJednotkaProperty(String jednotka) {
		jednotkaProperty().set(jednotka);
    }
}
