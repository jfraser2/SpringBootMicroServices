package springboot.autowire.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import springboot.persistence.JpaExampleQueryParam;

public class JpaExampleQueryParamsContainer {
	List<JpaExampleQueryParam> exampleQueryParamList = null;
	
	public JpaExampleQueryParamsContainer()
	{
		this.exampleQueryParamList = new ArrayList<JpaExampleQueryParam>();
	}
	
	// Since it is AutoWired clear the array before you use it
	public void clearParamList()
	{
		// clear the List
		this.exampleQueryParamList.clear();
	}
	
	public List<JpaExampleQueryParam> getExampleParamList() {
		return this.exampleQueryParamList;
	}

	public int size()
	{
		return this.exampleQueryParamList.size();
	}
	
	public void addToList(String paramName, Object paramValue, StringBuilderContainer aContainer)
	{
		if (null != paramName && paramName.length() > 0 &&
			null != paramValue && null != aContainer)
		{
			JpaExampleQueryParam anExampleParam = new JpaExampleQueryParam(paramName, paramValue, aContainer);
			this.exampleQueryParamList.add(anExampleParam);
		}
		
	}
	
    @PreDestroy
    public void onDestroy() {
    	// give Memory Back to the JVM, when the Request is over
    	clearParamList();    	
    	this.exampleQueryParamList = null;
        System.out.println("JpaExampleQueryParams Container is destroyed!!!");
    }    

}
