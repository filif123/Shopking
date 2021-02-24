package sk.shopking;

/**
 * @author Filip
 *
 */
public class UserAdministrator extends User{
	
	public UserAdministrator(int id,String meno, String priezvisko, String nick, int hash) {
		super(id,meno, priezvisko, nick, hash);

	}

	@Override
	public void setUsertypeProperty() {
		usertypeProperty().set("Administrátor");
	}

	@Override
	public void setUsertype() {
		super.userType = UserType.ADMIN;
		
	}

}
