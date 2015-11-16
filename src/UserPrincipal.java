package src;

import java.security.Principal;

public class UserPrincipal implements Principal {

	String name;
	
	public UserPrincipal(String _name) {
		name = _name;
	}
	public String getName() {
		return name;
		
	}
}