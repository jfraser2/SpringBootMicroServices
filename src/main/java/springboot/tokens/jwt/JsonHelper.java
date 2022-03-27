package springboot.tokens.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.enums.MapperEnum;

public class JsonHelper {
	
	public static JsonNode createJSONObject(String jsonString)
		throws JSONException
	{
		JsonNode retVar = null;

		if (null != jsonString)
		{
			try {
				ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();				
				retVar = mapper.readTree(jsonString);
			}
			catch(JsonParseException ex)
			{
				throw new JSONException(ex.getMessage());
			}
			catch(IOException ioe)
			{
				throw new JSONException(ioe.getMessage());
			}
		}
		else
		{
			throw new JSONException("No String to process");
		}
		
		return retVar;
	}
	
	public static String getStrValue(JsonNode anObject, String key)
		throws JSONException
	{
		String retVar = null;
		
		if (null != key && null != anObject)
		{
			JsonNode fieldNode = anObject.get(key);
			if (null != fieldNode)
			{
				retVar = fieldNode.textValue();
			}
			else
			{
				throw new JSONException("Field Not Found");
			}
		}
		else
		{
			throw new JSONException("Missing JSONObject or key");
		}
		
		return retVar;
	}

	public static Integer getIntValue(JsonNode anObject, String key)
		throws JSONException
	{
		Integer retVar = null;
		
		if (null != key && null != anObject)
		{
			JsonNode fieldNode = anObject.get(key);
			if (null != fieldNode)
			{
				int tempInt = fieldNode.intValue();
				retVar = new Integer(tempInt);
			}
			else
			{
				throw new JSONException("Field Not Found");
			}
		}
		else
		{
			throw new JSONException("Missing JSONObject or key");
		}
		
		return retVar;
	}
	
	public static Iterator<JsonNode> getArrayValues(JsonNode anObject, String key)
		throws JSONException
	{
		Iterator<JsonNode> retVar = null;
		
		if (null != key && null != anObject)
		{
			JsonNode fieldNode = anObject.get(key);
			if (null != fieldNode)
			{
				retVar = fieldNode.elements();
			}
			else
			{
				throw new JSONException("Field Not Found");
			}
		}
		else
		{
			throw new JSONException("Missing JSONObject or key");
		}
		
		return retVar;
	}
	
	public static Date getJWTDateValue(JsonNode anObject, String key)
		throws JSONException
	{
		Date retVar = null;
		
		if (null != key && null != anObject)
		{
			JsonNode fieldNode = anObject.get(key);
			if (null != fieldNode)
			{
				long tempLong = fieldNode.longValue();
				retVar = new Date(tempLong * 1000L);
			}
			else
			{
				throw new JSONException("Field Not Found");
			}
		}
		else
		{
			throw new JSONException("Missing JSONObject or key");
		}
		
		return retVar;
	}
	
	public static Boolean getBooleanValue(JsonNode anObject, String key)
		throws JSONException
	{
		Boolean retVar = null;
		
		if (null != key && null != anObject)
		{
			JsonNode fieldNode = anObject.get(key);
			if (null != fieldNode)
			{
				boolean tempBoolean = fieldNode.booleanValue();
				retVar = new Boolean(tempBoolean);
			}
			else
			{
				throw new JSONException("Field Not Found");
			}
		}
		else
		{
			throw new JSONException("Missing JSONObject or key");
		}
		
		return retVar;
	}
	
	public static Long getLongValue(JsonNode anObject, String key)
		throws JSONException
	{
		Long retVar = null;
		
		if (null != key && null != anObject)
		{
			JsonNode fieldNode = anObject.get(key);
			if (null != fieldNode)
			{
				long tempLong = fieldNode.longValue();
				retVar = new Long(tempLong);
			}
			else
			{
				throw new JSONException("Field Not Found");
			}
		}
		else
		{
			throw new JSONException("Missing JSONObject or key");
		}
		
		return retVar;
	}
	
	public static Double getDoubleValue(JsonNode anObject, String key)
		throws JSONException
	{
		Double retVar = null;
		
		if (null != key && null != anObject)
		{
			JsonNode fieldNode = anObject.get(key);
			if (null != fieldNode)
			{
				double tempDouble = fieldNode.doubleValue();
				retVar = new Double(tempDouble);
			}
			else
			{
				throw new JSONException("Field Not Found");
			}
		}
		else
		{
			throw new JSONException("Missing JSONObject or key");
		}
		
		return retVar;
	}
	
}
