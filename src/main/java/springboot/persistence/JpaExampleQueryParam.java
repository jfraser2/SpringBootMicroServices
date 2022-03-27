package springboot.persistence;

import springboot.autowire.helpers.StringBuilderContainer;
import springboot.utils.string.StringDefaultMethods;
import springboot.utils.string.interfaces.functional.ToUpperFirstCharLogic;

public class JpaExampleQueryParam {
	private String paramName;
	private Object paramValue;
	private String correctedParamName;
	
	public JpaExampleQueryParam ()
	{
	}
	
	public JpaExampleQueryParam (String aParamName, Object aParamValue, StringBuilderContainer aContainer)
	{
		this.paramName = aParamName;
		this.paramValue = aParamValue;
		correctParamName(aContainer);
	}
	
	public String getParamName() {
		return this.paramName;
	}
	
	public void setParamName(String paramName, StringBuilderContainer aContainer) {
		this.paramName = paramName;
		correctParamName(aContainer);
	}
	
	public Object getParamValue() {
		return this.paramValue;
	}
	
	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}

	public String getCorrectedParamName() {
		return correctedParamName;
	}

	public void setCorrectedParamName(String correctedParamName) {
		this.correctedParamName = correctedParamName;
	}
	
/*	
	private String underScoreSubToken(String aToken, StringBuilderContainer aContainer)
	{
		String retVar = null;
		
		if (null != aToken)
		{
			
			String [] theTokens = aToken.split("_");
			if (null != theTokens && theTokens.length > 1)
			{
				if (null != aContainer)
				{
					StringBuilder aBuilder = aContainer.getStringBuilder();
				
					aBuilder.append(theTokens[0]);
					for (int i = 1; i < theTokens.length; i++)
					{
						aBuilder.append(toUpperFirstChar(theTokens[i]));
					}
					retVar = aBuilder.toString();
				}
			}
			else
			{
				retVar = aToken;
			}
		}
		
		return retVar;
	}
*/	
	private void correctParamName(StringBuilderContainer aContainer)
	{
		if (null != this.paramName)
		{
			ToUpperFirstCharLogic toUpperFunc = StringDefaultMethods.toUpperFirstCharFunc;
			
			String [] theTokens = this.paramName.split("\\.");
			if (null != theTokens && theTokens.length > 1)
			{
				if (null != aContainer)
				{
					// Since it is Autowired clear the buffer before you use it
					aContainer.clearStringBuffer();
					
					StringBuilder aBuilder = aContainer.getStringBuilder();
					
					aBuilder.append(theTokens[0]);
					for (int i = 1; i < theTokens.length; i++)
					{
						aBuilder.append(toUpperFunc.toUpperFirstChar(theTokens[i]));
					}
					this.correctedParamName = aBuilder.toString();
				}
			}
			else
			{
				this.correctedParamName = this.paramName;
			}
		}
		else
		{
			this.correctedParamName = null;
		}
		
	}
	
	
}
