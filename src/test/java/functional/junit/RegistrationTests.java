package functional.junit;

import org.junit.Test;

import base.junit.JunitBase;
import response.dto.ResponseData;
import springboot.tokens.jwt.JwtToken;

public class RegistrationTests
	extends JunitBase
{

	@Test
	public void testPassRegistrationValidation()
	{
		String registrationUser = "{" +
				  " \"firstName\": \"Bob\"," +
				  " \"lastName\": \"Jones\"," +
				  " \"email\": \"jfraser2@yahoo.com\"," +
				  " \"userName\": \"Huserkguy135\"," +
				  " \"password\": \"yyy\"" +
				"}";
		
		JwtToken aJwtToken = new JwtToken();
		String aJwtTokenStr = aJwtToken.buildJwtToken(defaultTokenHeader, defaultTokenPayLoadFuture, defaultSecretKey);
		
		// /rest/api is from file application.properties
		ResponseData response = postJsonRequest(defaultServerAndPort, 
			"rest/api/Registration/register", registrationUser, aJwtTokenStr);
		
		aJwtToken = null;
		
		defaultHttpResponse(response);
		
	}
	
	@Test
	public void testFailRegistrationValidation()
	{
		String registrationUser = "{" +
				  " \"firstName\": \"Bob\"," +
				  " \"lastName\": \"Jones\"" +
				"}";
		
		JwtToken aJwtToken = new JwtToken();
		String aJwtTokenStr = aJwtToken.buildJwtToken(defaultTokenHeader, defaultTokenPayLoadFuture, defaultSecretKey);
		
		// /rest/api is from file application.properties
		ResponseData response = postJsonRequest(defaultServerAndPort, 
			"rest/api/Registration/register", registrationUser, aJwtTokenStr);
		
		aJwtToken = null;
		
		defaultHttpResponse(response);
		
	}
	
}
