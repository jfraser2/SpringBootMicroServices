package springboot.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import springboot.autowire.helpers.JpaExampleQueryParamsContainer;
import springboot.autowire.helpers.JpaQueryParamsContainer;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.persistence.interfaces.functional.DeleteLogic;
import springboot.persistence.interfaces.functional.FindAllLogic;
import springboot.persistence.interfaces.functional.FindByExampleLogic;
import springboot.persistence.interfaces.functional.FindByNamedQueryLogic;
import springboot.persistence.interfaces.functional.FindByPkLogic;
import springboot.persistence.interfaces.functional.FindByQueryLogic;
import springboot.persistence.interfaces.functional.GenerateParamListLogic;
import springboot.persistence.interfaces.functional.MergeLogic;
import springboot.persistence.interfaces.functional.PersistLogic;
import springboot.persistence.interfaces.functional.RemoveByReferenceLogic;
import springboot.persistence.interfaces.functional.RemoveLogic;
import springboot.persistence.interfaces.functional.UpdateLogic;
import springboot.utils.reflection.ReflectionDefaultMethods;

@Service
@Scope("prototype")
public class JpaDefaultMethods<EntityType, PrimaryKeyType> {
	
	@Autowired
	@Qualifier("requestStringBuilderContainer")
	private StringBuilderContainer requestStringBuilderContainer;
	
	@Autowired
	@Qualifier("requestJpaQueryParamsContainer")
	private JpaQueryParamsContainer requestJpaQueryParamsContainer;
	
	@Autowired
	@Qualifier("requestJpaExampleQueryParamsContainer")
	private JpaExampleQueryParamsContainer requestJpaExampleQueryParamsContainer;
	
	private final PersistLogic defaultPersist = (entityManager, theRecord) ->
	{
		System.out.println("entityManager is: " + entityManager.toString());
		entityManager.persist(theRecord);
	};
	
	@SuppressWarnings(value="unchecked")
	private final FindByNamedQueryLogic<EntityType> defaultFindByNamedQuery = (entityManager, aNamedQuery, paramListContainer) ->
	{
		List<EntityType> retVar = null;
		  
		EntityManager em = entityManager;
		try {
			Query q = null;
			if (null != aNamedQuery)
			{
				q = em.createNamedQuery(aNamedQuery);
				List<JpaQueryParam> paramList = paramListContainer.getParamList();
				if (null != paramList && !paramList.isEmpty())
				{
					for (JpaQueryParam aParam : paramList)
					{
						q.setParameter(aParam.getParamName(),
							aParam.getParamValue()); 
					}
						  
				}
				retVar = (List<EntityType>) q.getResultList();
			}
		}
		catch (Exception e)
		{
//			log.debug("Query Name is: *" + aNamedQuery + "*" + e.getMessage());
			retVar = null;
		}
		
		return retVar;
	};
	
	@SuppressWarnings(value="unchecked")
	private final FindByQueryLogic<EntityType> defaultFindByQuery = (entityManager,  aQueryStr, paramListContainer) ->
	{
		List<EntityType> retVar = null;
		  
		try {
			Query q = null;
			if (null != aQueryStr)
			{
				try {
					q = entityManager.createQuery(aQueryStr);
				}
				catch (Exception e)
				{
					System.out.println("Don't like your query!!!");
				}
				List<JpaQueryParam> paramList = paramListContainer.getParamList();
				System.out.println("Now the list size is: " + paramList.size());
				if (null != paramList && paramList.size() > 0)
				{
					System.out.println("!!!processing query params");
					for (JpaQueryParam aParam : paramList)
					{
						q.setParameter(aParam.getParamName(),
							aParam.getParamValue()); 
					}
						  
				}
				retVar = (List<EntityType>) q.getResultList();
				System.out.println("After fetch arrayList size Is: " + retVar.size());
			}
		}
		catch (Exception e)
		{
//			  log.debug("Query String: *" + aQueryStr + "*" + e.getMessage());
			System.out.println("Query String: *" + aQueryStr + "* " + e.toString());  
			retVar = null;
		}
		
		return retVar;
	};
	
	private final FindByPkLogic<EntityType, PrimaryKeyType> defaultFindByPk = (entityManager,  entityClass, id) ->
	{
		Class<EntityType> theClass = entityClass;
		EntityType retVar = entityManager.find(theClass, id);
		
	    return retVar;
	};
	
	private final GenerateParamListLogic defaultGenerateParamList = (origClassType, origFieldName, origClassObject, aParamsContainer, hasJoinParent) ->
	{
		StringBuilderContainer aContainer = requestStringBuilderContainer;
		Field[] theFields = origClassType.getDeclaredFields();
		Method[] theMethods = origClassType.getMethods();
		
		Object theValue = null;
		String fieldName = null;
		Class<?> fieldType = null;
		Object nestedClassObject = null;
		
		boolean isPrimitiveType = false;
		for(Field aField : theFields)
		{
			fieldName = aField.getName();
			fieldType = aField.getType();
			isPrimitiveType = ReflectionDefaultMethods.isPrimitiveFunc.isPrimitive(fieldType);
			theValue = null;
			nestedClassObject = null;
			try {
				aField.setAccessible(true);
				if (isPrimitiveType)
				{
					theValue = aField.get(origClassObject);
				}
				else
				{
					Method getterMethod = ReflectionDefaultMethods.findGetterMethodFunc.findGetterMethod(fieldName, theMethods);
					nestedClassObject = ReflectionDefaultMethods.executeGetterMethodFunc.execute(getterMethod, origClassObject);
				}
			}
			catch (Exception e)
			{
				theValue = null;
			}
			
			if (null != theValue || null != nestedClassObject)
			{
				System.out.println("the field got in: " + fieldName);
				if (isPrimitiveType)
				{
					System.out.println("the Value should be: " + theValue);
					if (null != origFieldName)
					{
						String separatorChar = ".";
						System.out.println("adding to the Array: " + origFieldName + separatorChar + fieldName + " the Value: " + theValue);
						aParamsContainer.addToList(origFieldName + separatorChar + fieldName, theValue, aContainer);
					}
					else
					{
						aParamsContainer.addToList(fieldName, theValue, aContainer);
					}
				}
				else
				{
					boolean joinCheck = ReflectionDefaultMethods.checkForAnnotationFunc.checkForAnnotation(aField, "ManyToOne");
					if (null != origFieldName)
					{
						this.defaultGenerateParamList.generateParamList(fieldType, origFieldName + "." + fieldName, nestedClassObject, aParamsContainer, joinCheck);
					}
					else
					{
						this.defaultGenerateParamList.generateParamList(fieldType, fieldName, nestedClassObject, aParamsContainer, joinCheck);
					}
				}
			}
			
		} // end the for loop
		
		return;
	};
	
	private final FindByExampleLogic<EntityType> defaultFindByExample = (entityManager,  anEntity, entityName) ->
	{
		List<EntityType> retVar = null;
		
		Class<? extends Object> theClass = null;
		
		if (null != anEntity)
		{
			theClass = anEntity.getClass();
		}
		
		String defaultEntityName = null;
		if (null != entityName && entityName.length() > 0)
		{
			defaultEntityName = entityName;
		}
		else if (null != theClass)
		{
			defaultEntityName = theClass.getSimpleName(); // no path included
		}
		
		if (null != anEntity && null != defaultEntityName)
		{
			// Since it is Autowired clear the paramList before you use it
			requestJpaExampleQueryParamsContainer.clearParamList();			
			defaultGenerateParamList.generateParamList(theClass, null, anEntity, requestJpaExampleQueryParamsContainer, false);
			
			String entityAlias = "data";
			
			// Since it is Autowired clear the buffer before you use it
			requestStringBuilderContainer.clearStringBuffer();			
			StringBuilder aBuilder = requestStringBuilderContainer.getStringBuilder();
			
			aBuilder.append("SELECT ");
			aBuilder.append(entityAlias);
			aBuilder.append(" FROM ");
			aBuilder.append(defaultEntityName);
			aBuilder.append(" ");
			aBuilder.append(entityAlias);
	
			// Since it is Autowired clear the Param List before you use it
			requestJpaQueryParamsContainer.clearParamList();
			List<JpaExampleQueryParam> params = requestJpaExampleQueryParamsContainer.getExampleParamList();
			if (null != params && params.size() > 0)
			{
				aBuilder.append(" WHERE ");
				
				JpaExampleQueryParam aParam = params.get(0);
				aBuilder.append(entityAlias);
				aBuilder.append(".");
				aBuilder.append(aParam.getParamName());
				aBuilder.append(" = :");
				aBuilder.append(aParam.getCorrectedParamName());
				
				requestJpaQueryParamsContainer.addToList(aParam); 
				
				for (int i = 1; i < params.size(); i++)
				{
					aParam = params.get(i);
					aBuilder.append(" AND ");
					aBuilder.append(entityAlias);
					aBuilder.append(".");
					aBuilder.append(aParam.getParamName());
					aBuilder.append(" = :");
					aBuilder.append(aParam.getCorrectedParamName());
					
					requestJpaQueryParamsContainer.addToList(aParam); 
				}
				
			}
			
			System.out.println("generated Query is: " + aBuilder.toString() + " paramList size is: " + requestJpaQueryParamsContainer.size());
//			for(JpaQueryParam aRec : realList)
//			{
//				System.out.println("realList field: " + aRec.getParamName() + " Value: " + aRec.getParamValue());
//			}
			retVar = defaultFindByQuery.findByQuery(entityManager, aBuilder.toString(), requestJpaQueryParamsContainer);
		} // end initial null checks
		
	    return retVar;
	};
	
	private final FindAllLogic<EntityType> defaultFindAll = (entityManager, entityName) ->
	{
		List<EntityType> retVar = null;
		
		if (null != entityName && entityName.length() > 0)
		{
			// Since it is Autowired clear the buffer before you use it
			requestStringBuilderContainer.clearStringBuffer();
			
			String entityAlias = "data";
			StringBuilder aBuilder = requestStringBuilderContainer.getStringBuilder();
			
			aBuilder.append("SELECT ");
			aBuilder.append(entityAlias);
			aBuilder.append(" FROM ");
			aBuilder.append(entityName);
			aBuilder.append(" ");
			aBuilder.append(entityAlias);
			
			retVar = defaultFindByQuery.findByQuery(entityManager, aBuilder.toString(), null);
		}
		return retVar;
	};
	
	/**
	 * This Method takes a java object in a JPA managed state
	 * and tries to perform a database update. The passed entity
	 * represents an updated record that was read from the 
	 * database. 
	 * 
	 * @param  entityRec - a Java Class Instance.
	 * 
	 * @return The updated Record.
	 * @throws IllegalArgumentException, IllegalStateException, TransactionRequiredException 
	 * 
	 */
	private final MergeLogic<EntityType> defaultMerge = (entityManager, entityRec) ->
	{
		EntityType retVar = null;
		  
	    retVar = entityManager.merge(entityRec);
	      
	    return retVar; 
	};
	
	private final UpdateLogic<EntityType> defaultUpdate = (entityManager,  entityRec) ->
	{
	    defaultMerge.merge(entityManager, entityRec);
	};

	/**
	 * This Method takes a JPA managed java object and removes
	 * the corresponding database row. It is used during row deletes.
	 * 
	 * @param  entityRec - a Java Class Instance.
	 * @throws IllegalArgumentException, IllegalStateException, TransactionRequiredException
	 * 
	 */
	private final RemoveLogic defaultRemove = (entityManager, entityRec) ->
	{
		entityManager.remove(entityRec); 
	};
	
	private final RemoveByReferenceLogic<EntityType, PrimaryKeyType> defaultRemoveByReference = (entityManager, entityClass, id) ->
	{
		Class<EntityType> theClass = entityClass;
		EntityType anEntityRec;
			      
		anEntityRec = entityManager.getReference(theClass, id);
		defaultRemove.remove(entityManager, anEntityRec);
		
	};
	
	private final DeleteLogic<EntityType, PrimaryKeyType> defaultDelete = (entityManager,  entityClass, id) ->
	{
		defaultRemoveByReference.removeByReference(entityManager, entityClass, id);
	};
	
	public JpaDefaultMethods()
	{
		
	}
	
	public PersistLogic getPersistDefault()
	{
		return defaultPersist;
	}

	public FindByNamedQueryLogic<EntityType> getFindByNamedQueryDefault()
	{
		return defaultFindByNamedQuery;
	}
	
	public FindByQueryLogic<EntityType> getFindByQueryDefault()
	{
		return defaultFindByQuery;
	}
	
	public FindByPkLogic<EntityType, PrimaryKeyType> getFindByPkDefault()
	{
		return defaultFindByPk;
	}

	public FindByExampleLogic<EntityType> getFindByExampleDefault()
	{
		return defaultFindByExample;
	}
	
	public FindAllLogic<EntityType> getFindAllDefault()
	{
		return defaultFindAll;
	}
	
	public UpdateLogic<EntityType> getUpdateDefault()
	{
		return defaultUpdate;
	}
	
	public DeleteLogic<EntityType, PrimaryKeyType> getDeleteDefault()
	{
		return defaultDelete;
	}
	
}
