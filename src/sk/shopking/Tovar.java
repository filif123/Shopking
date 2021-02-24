package sk.shopking;


import java.text.DecimalFormat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 * Trieda Tovar určuje každý výrobok predávajuci v obchode.
 * @author Filip
 *
 */
public class Tovar {
	
	protected long tovarEAN;
	protected String tovarName;
	protected Category tovarCategory;
	protected JednotkaType tovarJednotka;
	protected float tovarJednotkovaCena;
	protected DPHType tovarDPH;
	protected int tovarPLU;
	
	
	protected final SimpleStringProperty name  = new SimpleStringProperty();
	protected final SimpleStringProperty category = new SimpleStringProperty();
	protected final SimpleLongProperty ean = new SimpleLongProperty();
	protected final SimpleIntegerProperty plu = new SimpleIntegerProperty();
	protected final SimpleStringProperty jednotka = new SimpleStringProperty();
	protected final SimpleStringProperty jednotkovaCena = new SimpleStringProperty();
	protected final SimpleStringProperty dph = new SimpleStringProperty();
	protected final SimpleStringProperty akcia = new SimpleStringProperty();
	
	public Tovar(int tovarPLU,String tovarName,Category tovarCategory, JednotkaType jednotka, long tovarEAN, float tovarCena,DPHType dph){
		
		this.setPLUProperty(tovarPLU);
		this.setNameProperty(tovarName);
		this.setCategoryProperty(tovarCategory.toString());
		this.setEANProperty(tovarEAN);
		this.setJednotkaProperty(jednotka);
		this.setJednotkovaCenaProperty(new DecimalFormat("0.00").format(tovarCena));
		this.setDPHProperty(dph);
		
		this.setTovarPLU(tovarPLU);
		this.setTovarName(tovarName);
		this.setTovarCategory(tovarCategory);
		this.setTovarEAN(tovarEAN);
		this.setTovarJednotkovaCena(tovarCena);
		this.setTovarDPH(dph);
		this.setTovarJednotka(jednotka);
	}
	
	public String getTovarName() {
		return tovarName;
	}

	public void setTovarName(String tovarName) {
		this.tovarName = tovarName;
	}
	
	public Category getTovarCategory() {
		return tovarCategory;
	}

	public void setTovarCategory(Category tovarCategory) {
		this.tovarCategory = tovarCategory;
	}

	public float getTovarJednotkovaCena() {
		return tovarJednotkovaCena;
	}

	public void setTovarJednotkovaCena(float tovarCena) {
		this.tovarJednotkovaCena = tovarCena;
	}

	public long getTovarEAN() {
		return tovarEAN;
	}

	public void setTovarEAN(long tovarCode) {
		this.tovarEAN = tovarCode;
	}

	public DPHType getTovarDPH() {
		return tovarDPH;
	}

	public void setTovarDPH(DPHType tovarDPH) {
		this.tovarDPH = tovarDPH;
	}

	public JednotkaType getTovarJednotka() {
		return tovarJednotka;
	}

	public void setTovarJednotka(JednotkaType jednotka) {
		this.tovarJednotka = jednotka;
	}
	
	public int getTovarPLU() {
		return tovarPLU;
	}

	public void setTovarPLU(int plu) {
		this.tovarPLU = plu;
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}

	public SimpleStringProperty categoryProperty() {
		return category;
	}
	
	public SimpleIntegerProperty pluProperty() {
		return plu;
	}
	
	public SimpleLongProperty eanProperty() {
		return ean;
	}

	public SimpleStringProperty jednotkaProperty() {
		return jednotka;
	}
	
	public SimpleStringProperty jednotkovaCenaProperty() {
		return jednotkovaCena;
	}
	
	public SimpleStringProperty dphProperty() {
		return dph;
	}
	
	public SimpleStringProperty akciaProperty() {
		return akcia;
	}
	
	public final String getNameProperty() {
        return nameProperty().get();
    }
	
	public final String getCategoryProperty() {
        return categoryProperty().get();
    }
	
	public final int getPLUProperty() {
        return pluProperty().get();
    }
	
	public final long getEANProperty() {
        return eanProperty().get();
    }
	
	public final String getJednotkaProperty() {
        return jednotkaProperty().get();
    }
	
	public final String getJednotkovaCenaProperty() {
        return jednotkovaCenaProperty().get();
    }
	
	public final String getDPHProperty() {
        return dphProperty().get();
    }
	
	public final String getAkciaProperty() {
        return akciaProperty().get();
    }
	
	public final void setNameProperty(String name) {
		nameProperty().set(name);
    }
	
	public final void setCategoryProperty(String category) {
		categoryProperty().set(category);
    }
	
	public final void setPLUProperty(int plu) {
		pluProperty().set(plu);
    }
	
	public final void setEANProperty(long barcode) {
		eanProperty().set(barcode);
    }
	
	public final void setJednotkaProperty(JednotkaType jednotka) {
		if (jednotka.equals(JednotkaType.KS)) {
			jednotkaProperty().set("ks");
		}
		else if (jednotka.equals(JednotkaType.G)) {
			jednotkaProperty().set("g");
		}
		else if (jednotka.equals(JednotkaType.KG)) {
			jednotkaProperty().set("kg");
		}
    }
	
	public final void setJednotkovaCenaProperty(String cena) {
		jednotkovaCenaProperty().set(cena);
    }
	
	public final void setDPHProperty(DPHType dph) {
		if (dph.equals(DPHType.DPH_10)) {
			dphProperty().set("10");
		}
		else if (dph.equals(DPHType.DPH_20)) {
			dphProperty().set("20");
		}
    }
	
	public final void setAkciaProperty(String akcia) {
		akciaProperty().set(akcia);
    }


}
