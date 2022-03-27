package springboot.utils.reflection.interfaces.functional;

import java.lang.reflect.Field;

public interface CheckForAnnotationLogic {
	
	boolean checkForAnnotation(Field aField, String annotationName);	
}
