package springboot.security.interfaces.functional;

import java.util.List;

public interface UserRolesOkLogic {
	
	boolean userRolesOk(String [] methodRoles, List<String> userRoles);
}
