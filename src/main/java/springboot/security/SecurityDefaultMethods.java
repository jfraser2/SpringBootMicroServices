package springboot.security;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import springboot.annotations.roles.user.AllowedUserRoles;
import springboot.autowire.helpers.StringContainer;
import springboot.configurations.server.ConfigServerReader;
import springboot.entities.App;
import springboot.entities.User;
import springboot.entities.UserAppRoles;
import springboot.persistence.JpaOperations;
import springboot.security.interfaces.functional.LoadUserRolesLogic;
import springboot.security.interfaces.functional.UserRolesOkLogic;
import springboot.security.interfaces.functional.ValidateJwtTokenLogic;
import springboot.security.interfaces.functional.ValidateUserRolesLogic;
import springboot.tokens.jwt.JwtToken;

@Service
public class SecurityDefaultMethods
{
	
	public static final String ALLOW_ANY_USER = "Any";
	public static final String ADMIN = "ADMIN";
	private static final String JWT_COOKIE_NAME = "JwtToken";
	
	@Autowired
	JpaOperations<App, Long> jpa;
	
	@Autowired
	JpaOperations<UserAppRoles, Long> jpa2;
	
	@Autowired
	JwtToken aJwtToken;
	
	@Autowired
	@Qualifier("requestStringContainer")
	StringContainer requestStringContainer;
	
	private final LoadUserRolesLogic defaultLoadUserRoles = (userRoles, appName, userId) ->
	{
		if (null != userRoles && null != userId &&
				null != appName && appName.length() > 0)
		{
			
//				UserAppRoles has a three-part logical unique key(user_ID, app_ID, role_ID)
			
			User aUser = new User();
			aUser.setId(userId);
			
			App anApp = new App(); // this Entity has a unique constraint on appName
			anApp.setAppName(appName);
			
			List<App> readAppList = jpa.findByExample(anApp, "AppEntity", null);
			if (null != readAppList && readAppList.size() > 0)
			{
				App readApp = readAppList.get(0);
				readApp.setAppName(null);
				System.out.println("ReadApp Id is: " + readApp.getId());
				
				UserAppRoles userAppRoles = new UserAppRoles();
				userAppRoles.setUser(aUser);
				userAppRoles.setApp(readApp);
				userAppRoles.setRole(null);
				
				List<UserAppRoles> userAppRoleList = jpa2.findByExample(userAppRoles, "UserAppRolesEntity", null);
				if (null != userAppRoleList && userAppRoleList.size() > 0)
				{
					for(UserAppRoles aUserAppRole : userAppRoleList)
					{
						System.out.println("added the role: " + aUserAppRole.getRole().getRoleName());
						userRoles.add(aUserAppRole.getRole().getRoleName());
					}
				}
				
				userAppRoles = null;
				readApp = null;
				readAppList.clear();
				readAppList = null;
				userAppRoleList.clear();
				userAppRoleList = null;
			}
			
			aUser = null;
			anApp = null;
		}
		
		return;
	};
	
	private final UserRolesOkLogic defaultUserRolesOk = (methodRoles, userRoles) ->
	{
		boolean retVar = false;
		
		if (null != methodRoles && null != userRoles)
		{
			String aMethodRole;
			for (int i = 0; i < methodRoles.length; i++)
			{
				aMethodRole = methodRoles[i];
				if (aMethodRole.equalsIgnoreCase(ALLOW_ANY_USER))
				{
					retVar = true;
					break;
				}
				else
				{
					if (userRoles.contains(aMethodRole))
					{
						retVar = true;
						break;
					}
				}
			} // end the for Loop
		}
		
		return retVar;
	};
	
	private final ValidateUserRolesLogic defaultValidateUserRoles = (methodToTest, appName, userId) ->
	{
		boolean retVar = false;
		
		if (null != methodToTest && null != appName &&
			appName.length() > 0)
		{
			// Since it is Autowired clear the stringList before you use it
			requestStringContainer.clearStringList();
			List<String> userRoles = requestStringContainer.getStringList();
			
			defaultLoadUserRoles.loadUserRoles(userRoles, appName, userId);
			
		    if (methodToTest.isAnnotationPresent(AllowedUserRoles.class))
		    {
		    	AllowedUserRoles theAnnotation = methodToTest.getAnnotation(AllowedUserRoles.class);
		    	if (null != theAnnotation)
		    	{
		    		String [] valuesToTest = theAnnotation.values();
		    		boolean userOK = defaultUserRolesOk.userRolesOk(valuesToTest, userRoles);
		    		if (userOK)
		    		{
		    			retVar = true;
		    		}
		    	}
		    }
		}
		
		return retVar;
	};
	
	private final ValidateJwtTokenLogic defaultValidateJwtToken = (aRequest, userAppName, userId) ->
	{
		String aToken = null;
		
		// Check the Header First
		aToken = aRequest.getHeader("Authorization");
		
		// Check the Cookie for JWT
		if (null == aToken)
		{
			Cookie[] theCookies = aRequest.getCookies();
			if (null != theCookies)
			{
				for(Cookie aCookie : theCookies)
				{
					if (JWT_COOKIE_NAME.equalsIgnoreCase(aCookie.getName()))
					{
						aToken = aCookie.getValue();
						break;
					}
				} // end the for Loop
			}
		}
		
		
		if (null != aToken)
		{
			try {
				String appName = ConfigServerReader.getAppName();
				String secretKey = ConfigServerReader.getSuperSecretKey(appName);
				System.out.println("Secret Key is: " + secretKey);
				aJwtToken.validate(secretKey, aToken, userAppName, userId);
			}
			catch (ValidationException ve)
			{
				throw ve;
			}
		}
		
		return;
	};
	
	public SecurityDefaultMethods()
	{
	
	}
	
	public ValidateUserRolesLogic getDefaultValidateUserRoles()
	{
		return this.defaultValidateUserRoles;
	}
	
	public ValidateJwtTokenLogic getDefaultValidateJwtToken()
	{
		return this.defaultValidateJwtToken;
	}

}
