package springboot.security;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.security.interfaces.functional.ValidateJwtTokenLogic;
import springboot.security.interfaces.functional.ValidateUserRolesLogic;

@Service
public class SecurityValidation
{
	@Autowired
	SecurityDefaultMethods securityDefaultMethods;

	public boolean validateUserRoles(Method methodToTest, String appName, Long userId, ValidateUserRolesLogic overRide)
	{
		boolean retVar = false;
		
		ValidateUserRolesLogic methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = securityDefaultMethods.getDefaultValidateUserRoles();
		}
		
		retVar = methodToExecute.validateUserRoles(methodToTest, appName, userId);
		
		return retVar;
	}
	
	public void validateJwtToken(HttpServletRequest aRequest, String userAppName, Long userId, ValidateJwtTokenLogic overRide)
		throws ValidationException
	{
		
		ValidateJwtTokenLogic methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = securityDefaultMethods.getDefaultValidateJwtToken();
		}
		
		methodToExecute.validateJwtToken(aRequest, userAppName, userId);
		
		return;
	}

}
