package springboot.utils.reflection.interfaces.functional;

import java.lang.reflect.Method;

public interface ExecuteGetterMethodLogic {
	
	Object execute(Method getterMethod, Object classInstance);
}
