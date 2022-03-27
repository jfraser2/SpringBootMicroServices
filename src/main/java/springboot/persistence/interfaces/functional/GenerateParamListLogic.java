package springboot.persistence.interfaces.functional;

import springboot.autowire.helpers.JpaExampleQueryParamsContainer;

public interface GenerateParamListLogic {
	
	void generateParamList(Class<?> origClassType, String origFieldName, Object origClassObject, JpaExampleQueryParamsContainer aParamsContainer, boolean hasJoinParent);
}
