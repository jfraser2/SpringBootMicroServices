package springboot.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import springboot.autowire.helpers.JpaQueryParamsContainer;
import springboot.persistence.interfaces.functional.DeleteLogic;
import springboot.persistence.interfaces.functional.FindAllLogic;
import springboot.persistence.interfaces.functional.FindByExampleLogic;
import springboot.persistence.interfaces.functional.FindByNamedQueryLogic;
import springboot.persistence.interfaces.functional.FindByPkLogic;
import springboot.persistence.interfaces.functional.FindByQueryLogic;
import springboot.persistence.interfaces.functional.PersistLogic;
import springboot.persistence.interfaces.functional.UpdateLogic;

@Service
@Scope("prototype")
public class JpaOperations<EntityType, PrimaryKeyType>
{
	@PersistenceContext(unitName = "RegistrationMicroServicePU")	
	private EntityManager entityManager;
	
	@Autowired
	private JpaDefaultMethods<EntityType, PrimaryKeyType> jpaDefaults;
	
	public JpaOperations()
	{
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void persist(EntityType passedObject, PersistLogic persistOverride)
	{
		PersistLogic methodToExecute = null;
		if (null != persistOverride)
		{
			methodToExecute = persistOverride;
		}
		else
		{
			methodToExecute = jpaDefaults.getPersistDefault();
		}
//		System.out.println("entityManager is: " + this.entityManager.toString());
//		this.entityManager.persist(passedObject);
		methodToExecute.persist(this.entityManager, passedObject);
		
		return;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<EntityType> findByNamedQuery(String aNamedQuery, JpaQueryParamsContainer paramListContainer, FindByNamedQueryLogic<EntityType> overRide)
	{
		FindByNamedQueryLogic<EntityType> methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = jpaDefaults.getFindByNamedQueryDefault();
		}
		
		List<EntityType> retVar = null;
		  
		retVar = methodToExecute.findByNamedQuery(this.entityManager, aNamedQuery, paramListContainer);
		return retVar;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<EntityType> findByQuery(String aQueryStr, JpaQueryParamsContainer paramListContainer, FindByQueryLogic<EntityType> overRide)
	{
		FindByQueryLogic<EntityType> methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = jpaDefaults.getFindByQueryDefault();
		}
		
		List<EntityType> retVar = null;
		  
		retVar = methodToExecute.findByQuery(this.entityManager, aQueryStr, paramListContainer);
		
		return retVar;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public EntityType findByPk(Class<EntityType> entityClass, PrimaryKeyType id, FindByPkLogic<EntityType, PrimaryKeyType> overRide)
	{
		FindByPkLogic<EntityType, PrimaryKeyType> methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = jpaDefaults.getFindByPkDefault();
		}
		
		EntityType retVar = methodToExecute.findByPk(this.entityManager, entityClass, id);
		
	    return retVar;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<EntityType> findByExample(EntityType anEntity, String entityName, FindByExampleLogic<EntityType> overRide)
	{
		List<EntityType> retVar = null;
		
		FindByExampleLogic<EntityType> methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = jpaDefaults.getFindByExampleDefault();
		}
		
		retVar = methodToExecute.findByExample(this.entityManager, anEntity, entityName);
		
	    return retVar;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<EntityType> findAll(String entityName, FindAllLogic<EntityType> overRide)
	{
		List<EntityType> retVar = null;
		
		FindAllLogic<EntityType> methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = jpaDefaults.getFindAllDefault();
		}
		
		retVar = methodToExecute.findAll(this.entityManager, entityName);
		
		return retVar;
	}

	/**
	 * This Method takes a java object that is already in a JPA
	 * managed state(i.e. already read from the database) and tries
	 * to update the database row. It throws a NonexistentEntityyException
	 * if the record no longer exists.
	 * 
	 * @param  entityRec - a Java Class Instance.
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(EntityType entityRec, UpdateLogic<EntityType> overRide)
	{
		UpdateLogic<EntityType> methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = jpaDefaults.getUpdateDefault();
		}
		
		methodToExecute.update(this.entityManager, entityRec);
	}
	
	/**
	 * This Method takes a primary key and tries to delete the
	 * corresponding database row. It throws a NonexistentEntityyException
	 * if the record no longer exists.
	 * 
	 * @param  entityClass - a Java Class.
	 * @param  id - primary key of the record.
	 * 
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Class<EntityType> entityClass, PrimaryKeyType id,  DeleteLogic<EntityType, PrimaryKeyType> overRide)
	{
		DeleteLogic<EntityType, PrimaryKeyType> methodToExecute = null;		
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = jpaDefaults.getDeleteDefault();
		}
		
		methodToExecute.delete(this.entityManager, entityClass, id);
	}
	
}
