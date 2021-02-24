/**
 * 
 */
package sk.shopking;


import java.text.DecimalFormat;

import javafx.beans.property.SimpleStringProperty;
/**
 * Trieda Tovar určuje každý výrobok predávajuci v obchode.
 * @author Filip
 *
 */
public class Tovar {
	
	private long tovarEAN;
	private String tovarName;
	private Category tovarCategory;
	private JednotkaType tovarJednotka;
	private float tovarJednotkovaCena;
	private DPHType tovarDPH;
	private int tovarPLU;
	private Akcia tovarAkcia;
	
	
	private final SimpleStringProperty name  = new SimpleStringProperty();
	private final SimpleStringProperty category = new SimpleStringProperty();
	private final SimpleStringProperty ean = new SimpleStringProperty();
	private final SimpleStringProperty plu = new SimpleStringProperty();
	private final SimpleStringProperty jednotka = new SimpleStringProperty();
	private final SimpleStringProperty jednotkovaCena = new SimpleStringProperty();
	private final SimpleStringProperty dph = new SimpleStringProperty();
	private final SimpleStringProperty akcia = new SimpleStringProperty();
	
	public Tovar(int tovarPLU,String tovarName,Category tovarCategory, JednotkaType jednotka, long tovarEAN, float tovarCena,DPHType dph){
		
		this.setPLUProperty("" + tovarPLU);
		this.setNameProperty(tovarName);
		this.setCategoryProperty(tovarCategory.toString());
		this.setEANProperty("" + tovarEAN);
		this.setJednotkaProperty(jednotka);
		this.setJednotkovaCenaProperty("" + new DecimalFormat("#.##").format(tovarCena));
		this.setDPHProperty(dph);
		
		this.setTovarPLU(tovarPLU);
		this.setTovarName(tovarName);
		this.setTovarCategory(tovarCategory);
		this.setTovarEAN(tovarEAN);
		this.setTovarJednotkovaCena(tovarCena);
		this.setTovarDPH(dph);
		this.setTovarJednotka(jednotka);
	}

	public Tovar(int tovarPLU,String tovarName,Category tovarCategory, JednotkaType jednotka, long tovarEAN, float tovarCena,DPHType dph,int plu){
		
		this.setNameProperty(tovarName);
		this.setCategoryProperty(tovarCategory.toString());
		this.setEANProperty("" + tovarEAN);
		this.setJednotkaProperty(jednotka);
		this.setJednotkovaCenaProperty("" + new DecimalFormat("#.##").format(tovarCena));
		this.setPLUProperty("" + plu);
		this.setDPHProperty(dph);
		
		this.setTovarPLU(tovarPLU);
		this.setTovarName(tovarName);
		this.setTovarCategory(tovarCategory);
		this.setTovarEAN(tovarEAN);
		this.setTovarJednotkovaCena(tovarCena);
		this.setTovarDPH(dph);
		this.setTovarJednotka(jednotka);
		this.setTovarPLU(plu);
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
	
	public Akcia getTovarAkcia() {
		return tovarAkcia;
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
	
	public void setTovarAkcia(Akcia akcia) {
		this.tovarAkcia = akcia;
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}

	public SimpleStringProperty categoryProperty() {
		return category;
	}
	
	public SimpleStringProperty pluProperty() {
		return plu;
	}
	
	public SimpleStringProperty eanProperty() {
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
	
	public final String getPLUProperty() {
        return pluProperty().get();
    }
	
	public final String getEANProperty() {
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
	
	public final void setPLUProperty(String plu) {
		pluProperty().set(plu);
    }
	
	public final void setEANProperty(String barcode) {
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
		dphProperty().set(akcia);
    }


}
