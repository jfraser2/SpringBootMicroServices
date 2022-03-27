package springboot.security.interfaces.functional;

import java.util.List;

public interface LoadUserRolesLogic {

	void loadUserRoles(List<String> userRoles, String appName, Long userId);	
}
