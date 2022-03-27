package springboot.persistence.interfaces.functional;

import javax.persistence.EntityManager;

public interface RemoveByReferenceLogic<EntityType, PrimaryKeyType> {
	
	void removeByReference(EntityManager entityManager, Class<EntityType> entityClass, PrimaryKeyType id);
}
