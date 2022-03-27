/**
 * Copyright: NAIC 2010
 *
 * Author: JFraser
 *
 * Creation Date: Oct 4, 2010
 *
 * Modification History
 *  mm/dd/yyyy  user     Created for xxxxxxx.
 */
 
package springboot.persistence;

/**
 * Description.
 * The purpose of the Class is too pass query params
 * to a Controller Base class method
 * @author JFraser
 *
 */

public class JpaQueryParam {
	
	private String paramName;
	private Object paramValue;
	
	public JpaQueryParam ()
	{
		
	}
	
	public JpaQueryParam (String aParamName, Object aParamValue)
	{
		this.paramName = aParamName;
		this.paramValue = aParamValue;
	}
	
	public String getParamName() {
		return this.paramName;
	}
	
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	public Object getParamValue() {
		return this.paramValue;
	}
	
	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}
	
}
