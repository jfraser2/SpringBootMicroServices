package springboot.security.interfaces.functional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

public interface ValidateJwtTokenLogic {
	
	void validateJwtToken(HttpServletRequest aRequest, String userAppName, Long userId)
		throws ValidationException;
}
