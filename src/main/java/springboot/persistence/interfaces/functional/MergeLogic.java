package springboot.persistence.interfaces.functional;

import javax.persistence.EntityManager;

public interface MergeLogic<EntityType> {
	
	EntityType merge(EntityManager entityManager, EntityType entityRec);	
}
