package springboot.utils.string;

import springboot.utils.string.interfaces.functional.ToUpperFirstCharLogic;

public class StringDefaultMethods {
	
	public static final ToUpperFirstCharLogic toUpperFirstCharFunc = (aString) ->
	{
		String retVar = null;
		if (null != aString && aString.length() > 0)
		{
			String backEnd = aString.substring(1);
			String firstString = aString.substring(0, 1);
			retVar = firstString.toUpperCase() + backEnd;
		}
		else
		{
			retVar = aString;
		}
		
		return retVar;
	};

}
