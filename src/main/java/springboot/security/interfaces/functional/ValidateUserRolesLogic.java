package springboot.security.interfaces.functional;

import java.lang.reflect.Method;

public interface ValidateUserRolesLogic {
	
	boolean validateUserRoles(Method methodToTest, String appName, Long userId);
}
