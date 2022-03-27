package springboot.persistence.interfaces.functional;

import javax.persistence.EntityManager;

public interface UpdateLogic<EntityType> {
	
	void update(EntityManager entityManager, EntityType entityRec);
}
