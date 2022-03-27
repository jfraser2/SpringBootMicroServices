package springboot.autowire.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import springboot.persistence.JpaExampleQueryParam;
import springboot.persistence.JpaQueryParam;

public class JpaQueryParamsContainer {

	List<JpaQueryParam> queryParamList = null;
	
	public JpaQueryParamsContainer()
	{
		this.queryParamList = new ArrayList<JpaQueryParam>();
	}
	
	// Since it is AutoWired clear the array before you use it
	public void clearParamList()
	{
		// clear the List
		this.queryParamList.clear();
	}
	
	public List<JpaQueryParam> getParamList() {
		return this.queryParamList;
	}

	public int size()
	{
		return this.queryParamList.size();
	}
	
	public void addToList(String paramName, Object paramValue)
	{
		if (null != paramName && paramName.length() > 0 && null != paramValue)
		{
			JpaQueryParam aParam = new JpaQueryParam(paramName, paramValue);
			this.queryParamList.add(aParam);
		}
		
	}
	
	public void addToList(JpaExampleQueryParam anExampleParam)
	{
		if (null != anExampleParam)
		{
			addToList(anExampleParam.getCorrectedParamName(), anExampleParam.getParamValue());
		}
	}
	
    @PreDestroy
    public void onDestroy() {
    	// give Memory Back to the JVM, when the Request is over
    	clearParamList();    	
    	this.queryParamList = null;
        System.out.println("JpaQueryParams Container is destroyed!!!");
    }    

}
