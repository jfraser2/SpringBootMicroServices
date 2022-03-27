package nonFunctional.junit;

import org.junit.Test;

import base.junit.JunitBase;
import springboot.tokens.jwt.JwtToken;

public class BuildJwtToken
	extends JunitBase
{

	@Test
	public void buildExpiredToken()
	{
//		"exp": 1492702201
		
		// Made from Class JwtToken - solves the CharacterSet problem Cp1252 vs. UTF-8
		// The Secret to making the JUnit produce a UTF-8 token is to set the Windows Environment Variable JAVA_TOOL_OPTIONS to value -Dfile.encoding=UTF-8
		
		JwtToken aJwtToken = new JwtToken();
		
		String requestTokenStr = aJwtToken.buildJwtToken(defaultTokenHeader, defaultTokenPayLoadPast, defaultSecretKey);
		System.out.println("Expired Token is: " + requestTokenStr);
		
		aJwtToken = null;
	}
	
	@Test
	public void buildSuccessfulToken()
	{
//		"exp": 1692702201
		
		// Made from Class JwtToken - solves the CharacterSet problem Cp1252 vs. UTF-8
		// The Secret to making the JUnit produce a UTF-8 token is to set the Windows Environment Variable JAVA_TOOL_OPTIONS to value -Dfile.encoding=UTF-8
		
		JwtToken aJwtToken = new JwtToken();
		
		String requestTokenStr = aJwtToken.buildJwtToken(defaultTokenHeader, defaultTokenPayLoadFuture, defaultSecretKey);
		System.out.println("Successful Token is: " + requestTokenStr);
		
		aJwtToken = null;
	}
	
}
