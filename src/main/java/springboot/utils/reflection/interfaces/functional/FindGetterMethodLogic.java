package springboot.utils.reflection.interfaces.functional;

import java.lang.reflect.Method;

public interface FindGetterMethodLogic {

	Method findGetterMethod(String fieldName, Method[] methodList);	
}
