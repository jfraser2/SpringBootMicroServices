package springboot.persistence.interfaces.functional;

import javax.persistence.EntityManager;

public interface DeleteLogic<EntityType, PrimaryKeyType> {

	void delete(EntityManager entityManager, Class<EntityType> entityClass, PrimaryKeyType id);	
}
