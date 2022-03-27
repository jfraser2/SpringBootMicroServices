package springboot.persistence.interfaces.functional;

import javax.persistence.EntityManager;

public interface FindByPkLogic<EntityType, PrimaryKeyType> {

	 EntityType findByPk(EntityManager entityManager, Class<EntityType> entityClass, PrimaryKeyType id);	
}
