package springboot.utils.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import springboot.utils.reflection.interfaces.functional.CheckForAnnotationLogic;
import springboot.utils.reflection.interfaces.functional.ExecuteGetterMethodLogic;
import springboot.utils.reflection.interfaces.functional.FindGetterMethodLogic;
import springboot.utils.reflection.interfaces.functional.IsPrimitiveLogic;
import springboot.utils.string.StringDefaultMethods;
import springboot.utils.string.interfaces.functional.ToUpperFirstCharLogic;

public class ReflectionDefaultMethods {
	
	public static final CheckForAnnotationLogic checkForAnnotationFunc = (aField, annotationName) ->
	{
		boolean retVar = false;
		
		Annotation [] annotationList = aField.getAnnotations();
		
	    if (null != annotationList && annotationList.length > 0)
	    {
	    	Class<? extends Annotation> annotationType = null;
	    	String currName = null;
	    	
	    	for(Annotation theAnnotation : annotationList)
	    	{
	    		annotationType = theAnnotation.annotationType();	    		
	    		currName = annotationType.getSimpleName();
	    		System.out.println("AnnotationName is: " + currName);
	    		if (currName.equalsIgnoreCase(annotationName))
	    		{
	    			retVar = true;
	    			break;
	    		}
	    	} // end the for loop
	    }

		return retVar;
	};

	public static final ExecuteGetterMethodLogic executeGetterMethodFunc = (getterMethod, classInstance) ->
	{
		Object retVar = null;
		
		try {
			retVar = getterMethod.invoke(classInstance, (Object[])null);
		}
		catch (Exception e)
		{
			retVar = null;
		}
		
		return retVar;
	};
	
	public static final FindGetterMethodLogic findGetterMethodFunc = (fieldName, methodList) ->
	{
		Method retVar = null;
		ToUpperFirstCharLogic toUpperFunc = StringDefaultMethods.toUpperFirstCharFunc;
		
		String searchText = "get" + toUpperFunc.toUpperFirstChar(fieldName);
		for(Method aMethod : methodList)
		{
			if (searchText.equals(aMethod.getName()))
			{
				retVar = aMethod;
				System.out.println("!!!found a getter method!!! for fieldName: " + fieldName);
				break;
			}
		}
		
		return retVar;
	};
	
	public static final IsPrimitiveLogic isPrimitiveFunc = (fieldType) ->
	{
		boolean retVar = false;
		String simpleClassName = fieldType.getSimpleName(); // no path included
		
		if (simpleClassName.equalsIgnoreCase("String"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("long"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("Integer"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("boolean"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("byte"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("char"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("short"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("int"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("float"))
		{
			retVar = true;
		}
		else if (simpleClassName.equalsIgnoreCase("double"))
		{
			retVar = true;
		}
		
		return retVar;
	};
	
}
