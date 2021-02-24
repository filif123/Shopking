/**
 * 
 */
package sk.shopking;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Trieda Category určuje kategóriu tovaru, do ktorej výrobok patrí.
 * @author Filip
 *
 */
public class Category {
	private int categoryID;
	private String categoryName;
	private boolean categoryPristupnePreMladistvych;
	
	private final SimpleStringProperty name  = new SimpleStringProperty();
	private final SimpleBooleanProperty preMladistvych = new SimpleBooleanProperty();
	
	public Category(int id,String categoryName, boolean pristupnePreMladistvych){
		this.setNameProperty(categoryName);
		this.setPreMladistvychProperty(pristupnePreMladistvych);
		
		this.setCategoryID(id);
		this.setCategoryName(categoryName);
		this.setCategoryPristupnePreMladistvych(pristupnePreMladistvych);
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public boolean getCategoryPristupnePreMladistvych() {
		return categoryPristupnePreMladistvych;
	}

	public void setCategoryPristupnePreMladistvych(boolean categoryPristupnePreMladistvych) {
		this.categoryPristupnePreMladistvych = categoryPristupnePreMladistvych;
	}
	
	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	@Override
	public String toString() {
		return categoryName;
	}
	
	public SimpleStringProperty nameProperty() {
		return name;
	}

	public SimpleBooleanProperty preMladistvychProperty() {
		return preMladistvych;
	}
	
	public final String getNameProperty() {
        return nameProperty().get();
    }
	
	public final boolean getPreMladistvychProperty() {
        return preMladistvychProperty().get();
    }
	
	public final void setNameProperty(String name) {
		nameProperty().set(name);
    }
	
	public final void setPreMladistvychProperty(boolean preMladistvych) {
		preMladistvychProperty().set(preMladistvych);
    }
}
