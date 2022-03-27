package springboot.tokens.jwt;

//import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
//import java.util.Formatter;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import springboot.autowire.helpers.StringBuilderContainer;
import springboot.entities.UserTokens;
import springboot.persistence.JpaOperations;

@Component
@Scope("prototype")
public class JwtToken
{
	private static final String HASH_ALGORITHM = "HMACSHA256";
//	private static final String HASH_ALGORITHM = "HS256";

//	private static final String charsetCP1252 = "Cp1252";
//	private static final String charsetUTF8 = "UTF-8";
	
	@Autowired
	JpaOperations<UserTokens, Long> jpa;

	@Autowired
	@Qualifier("requestStringBuilderContainer")
	private StringBuilderContainer requestStringBuilderContainer;
	
	public JwtToken()
	{
		
	}
	
	public void validate(final String aSecretKey, final String aJwtToken, final String userAppName, final Long userId)
		throws ValidationException
	{
		if (null != aSecretKey && null != aJwtToken && null != userAppName && null != userId)
		{
			Map<String, String> requestTokenMap;
			
			try {
				requestTokenMap = splitToken(aJwtToken);
				
				JsonNode tokenHeader = base64DecodeTokenHeader(requestTokenMap);
				JsonNode tokenPayload = base64DecodeTokenPayload(requestTokenMap);
				
				boolean sigOk = checkTokenOk(aSecretKey, tokenPayload, requestTokenMap, tokenHeader, userAppName, userId);
				if (!sigOk)
				{
					throw new ValidationException("Invalid token Signature");
				}
			}
			catch(ValidationException ve)
			{
				throw ve;
			}
			finally
			{
				requestTokenMap = null;
			}
		}
		else
		{
			throw new ValidationException("Missing Validation Parameters");
		}
	}

	public void validate(final String aSecretKey, final HttpServletRequest aRequest, final String userAppName, final Long userId)
		throws ValidationException
	{
		if (null != aSecretKey && null != aRequest && null != userAppName && null != userId)
		{
			String aJwtToken = null;
			Map<String, String> requestTokenMap;
			
			try {
				aJwtToken = aRequest.getHeader("Authorization");
				requestTokenMap = splitToken(aJwtToken);
				
				JsonNode tokenHeader = base64DecodeTokenHeader(requestTokenMap);
				JsonNode tokenPayload = base64DecodeTokenPayload(requestTokenMap);
				boolean sigOk = checkTokenOk(aSecretKey, tokenPayload, requestTokenMap, tokenHeader, userAppName, userId);
				
				if (!sigOk)
				{
					throw new ValidationException("invalid token Signature");
				}
			}
			catch(ValidationException ve)
			{
				throw ve;
			}
			finally
			{
				requestTokenMap = null;
			}
		}
		else
		{
			throw new ValidationException("Missing Validation Parameters");
		}
	}
	
	private Map<String, String> splitToken(final String aJwtToken)
		throws ValidationException
	{
		Map<String, String> retVar = null;
		if (null != aJwtToken)
		{
			String[] tokenArray = aJwtToken.split("\\.", -1);
			if (tokenArray.length == 3)
			{
				retVar = new HashMap<String, String>();
				retVar.put("tokenHeader", tokenArray[0]);
				retVar.put("tokenPayload", tokenArray[1]);
				retVar.put("tokenSignature", tokenArray[2]);
			}
			else
			{
				throw new ValidationException("invalid JWT token format");
			}
			
		}
		else
		{
			throw new ValidationException("request JWT token not available");
		}
		
		return retVar;
	}

/*	
	private String toHexString(final byte[] bytes)
	{  
		StringBuilder sb = new StringBuilder(bytes.length * 2);

	    Formatter formatter = new Formatter(sb);  
	    for (byte b : bytes)
	    {  
	        formatter.format("%02x", b);  
	    }  
	    formatter.close();

		String tempVar = sb.toString();
		sb = null;
		
	    return tempVar;  
	}
*/
	
	public String createSignatureHS256(final String sigData, final String aSecretKey)
			throws SignatureException
	{
		String retVar = null;
		Key sk = null;
		
		 try {
			sk = new SecretKeySpec(aSecretKey.getBytes(), HASH_ALGORITHM);
			String algorithOfficalName = sk.getAlgorithm();
			Mac mac = Mac.getInstance(algorithOfficalName);
//			Mac mac = Mac.getInstance(HASH_ALGORITHM);
			mac.init(sk);
			final byte[] hmac = mac.doFinal(sigData.getBytes());
			retVar = new String(hmac);	
		}
//		catch (UnsupportedEncodingException uee)
//		{
//			throw new SignatureException(
//				    "error building signature, sigData is not UTF-8 "
//				    + HASH_ALGORITHM);
//		}
		catch (NoSuchAlgorithmException e1) {
			  // throw an exception or pick a different encryption method
			throw new SignatureException(
			    "error building signature, no such algorithm in device "
			    + HASH_ALGORITHM);
		}
		catch (InvalidKeyException e) {
			throw new SignatureException(
			  "error building signature, invalid key " + HASH_ALGORITHM);
		}
		catch(IllegalStateException ise)
		{
			throw new SignatureException(
					  "Mac was not in the proper State. Algorithm is:  " + HASH_ALGORITHM);
		}
		finally
		{
			sk = null;
		}
		 
		return retVar;
		
	}
	
	private boolean checkTokenOk(final String aSecretKey, final JsonNode tokenPayload, final Map<String, String> requestTokenMap,
		final JsonNode tokenHeader, final String userAppName, final Long userId)
		throws ValidationException
	{
		boolean retVar = false;
		
		String tokenHeaderStr = null;
		String tokenPayloadStr = null;
		String tokenSignature = null;
		
		if (null != requestTokenMap)
		{
			tokenHeaderStr = requestTokenMap.get("tokenHeader");
			tokenPayloadStr = requestTokenMap.get("tokenPayload");
			tokenSignature = requestTokenMap.get("tokenSignature");
		}
		
		if (null != aSecretKey && null != tokenSignature && null != tokenPayload)
		{
			UserTokens aUserToken = null;
			
			StringBuilderContainer aContainer = requestStringBuilderContainer;
			aContainer.clearStringBuffer();
			StringBuilder hashStr = aContainer.getStringBuilder();
			
			hashStr.append(tokenHeaderStr);
			hashStr.append(".");
			hashStr.append(tokenPayloadStr);
			
			String hashStrTest = tokenHeaderStr + "." + tokenPayloadStr;
			
			String signatureReHash = null;
			Long referenceId = null;
			
			try {
				referenceId = JsonHelper.getLongValue(tokenPayload, "refId");
				if (null != referenceId)
				{
					aUserToken = jpa.findByPk(UserTokens.class, referenceId, null);
					if (null != aUserToken)
					{
						if (null != userId && userId.equals(aUserToken.getUser().getId())
							&& null != userAppName && userAppName.equalsIgnoreCase(aUserToken.getApp().getAppName())
							&& aUserToken.getActive().booleanValue())
						{
							String tempReHash = createSignatureHS256(hashStrTest, aSecretKey);
							signatureReHash = base64EncodeTokenSignature(tempReHash, tokenHeader);
						}
					}
				}
			}
			catch (SignatureException se)
			{
				signatureReHash = null;
				aUserToken = null;
			}
			catch (JSONException je)
			{
				signatureReHash = null;
				aUserToken = null;
			}
			catch (ValidationException ve)
			{
				signatureReHash = null;
				aUserToken = null;
			}
			
			if (null != signatureReHash && signatureReHash.equals(tokenSignature))
			{
				// the Token is good so far
				try {
					boolean tempRetVar = checkExpirationDate(tokenPayload, aUserToken);
					if (tempRetVar)
					{
						retVar = true;
					}
				}
				catch (ValidationException ve)
				{
					throw ve;
				}
				finally
				{
					signatureReHash = null;
					aUserToken = null;
				}
			}
			else
			{
				signatureReHash = null;				
				aUserToken = null;
			}
		}
		
		return retVar;
	}
	
	public boolean checkExpirationDate(final JsonNode tokenPayload, UserTokens aUserToken)
		throws ValidationException
	{
		boolean retVar = false;
		
		Date expirationDate = null;
		Date now = new Date();
		
		try {
			expirationDate = JsonHelper.getJWTDateValue(tokenPayload, "exp");
			int expireTest = now.compareTo(expirationDate);
			if (expireTest <= 0)
			{
				retVar = true;
			}
			else
			{
				if (null != aUserToken)
				{
					aUserToken.setActive(Boolean.FALSE);
					jpa.update(aUserToken, null);
					throw new ValidationException("Your Token is Expired!!!");
				}
				else
				{
					throw new ValidationException("Internal System Error. No User Token Record!!!");
				}
			}
		}
		catch (JSONException je)
		{
			throw new ValidationException(je.getCause());
		}
		
		return retVar;
	}
	
	public JsonNode base64DecodeTokenHeader(final Map<String, String> requestTokenMap)
		throws ValidationException
	{
		JsonNode retVar = null;
		String tokenHeader = null;
		
		if (null != requestTokenMap)
		{
			tokenHeader = requestTokenMap.get("tokenHeader");
		}
		
		if (null != tokenHeader)
		{
			String jsonString = null;
			byte[] tempArray = null;
			
			Decoder base64Decoder = Base64.getUrlDecoder();
//			Decoder base64Decoder = Base64.getDecoder();
			tempArray = base64Decoder.decode(tokenHeader);
			
			if (null != tempArray && tempArray.length > 0)
			{
				jsonString = new String(tempArray);
				try {
					retVar =  JsonHelper.createJSONObject(jsonString);
				}
				catch (JSONException e)
				{
					retVar = null;
					throw new ValidationException("invalid JWT token Header json string");
				}
			}
			else
			{
				throw new ValidationException("Could Not Decode JWT token Header");
			}
		}
		else
		{
			throw new ValidationException("JWT token Header not available");
		}
		
		return retVar;
	}

	public JsonNode base64DecodeTokenPayload(final Map<String, String> requestTokenMap)
		throws ValidationException
	{
		JsonNode retVar = null;
		String tokenPayload = null;
		
		if (null != requestTokenMap)
		{
			tokenPayload = requestTokenMap.get("tokenPayload");
		}
		
		if (null != tokenPayload)
		{
			String jsonString = null;
			byte[] tempArray = null;
			
			Decoder base64Decoder = Base64.getUrlDecoder();
//			Decoder base64Decoder = Base64.getDecoder();
			tempArray =  base64Decoder.decode(tokenPayload);
			
			if (null != tempArray && tempArray.length > 0)
			{
				jsonString = new String(tempArray);
				try {
					retVar =  JsonHelper.createJSONObject(jsonString);
				}
				catch (JSONException e)
				{
					retVar = null;
					throw new ValidationException("invalid JWT token Payload json string");
				}
			}
			else
			{
				throw new ValidationException("Could Not Decode JWT token Payload");
			}
		}
		else
		{
			throw new ValidationException("JWT token Payload not available");
		}
		
		return retVar;
	}
	
/*	
	private String stripTrailingEncoderPadding(final String checkString)
	{
		String retVar = null;
		
		if (null != checkString)
		{
			char testChar;
			int i = checkString.length() - 1;
			
			for(;  i > -1; i--)
			{
				testChar = checkString.charAt(i);
				if (testChar != '=')
				{
					break;
				}
			}
			i++;
			
			retVar = checkString.substring(0, i);
		}
		
		return retVar;
	}
*/	
	public String base64EncodeString(final String input)
	{
		String retVar = null;
		
		if (null != input)
		{
			byte[] tempArray = null;
			Encoder base64Encoder = Base64.getUrlEncoder();
//			Encoder base64Encoder = Base64.getEncoder();
			Encoder noPaddingEncoder = base64Encoder.withoutPadding();
			tempArray = noPaddingEncoder.encode(input.getBytes());
			
			if (null != tempArray && tempArray.length > 0)
			{
//				String tempString = new String(tempArray).trim();
//				retVar = stripTrailingEncoderPadding(tempString);
				retVar = new String(tempArray).trim();
			}
			
		}
		
		return retVar;

	}
	
	public String base64EncodeTokenSignature(final String signatureReHash, final JsonNode tokenHeader)
		throws ValidationException
	{
		String retVar = null;
		if (null != signatureReHash)
		{
			String encoding = base64EncodeString(signatureReHash);
			
			if (null != encoding)
			{
				retVar = encoding;
			}
			else
			{
				throw new ValidationException("Could Not Encode JWT token Signature");
			}
		}
		else
		{
			throw new ValidationException("Signature ReHash not available");
		}
		
		return retVar;
	}
	
	public String buildJwtToken(String tokenHeaderJson, String tokenPayLoadJson, String secretKey)
	{
		String aToken = null;
		
		try {
			String encodedHeader = base64EncodeString(tokenHeaderJson);
			String encodedPayLoad = base64EncodeString(tokenPayLoadJson);
			
			String buildSignature = encodedHeader + "." + encodedPayLoad;
			String signatureHash = createSignatureHS256(buildSignature, secretKey);
			String encodedSignature = base64EncodeString(signatureHash);
			
			aToken = encodedHeader + "." + encodedPayLoad + "." + encodedSignature;
			
		}
		catch (SignatureException se)
		{
			aToken = null;
		}

		return aToken;
	}
		
}
