package springboot.persistence.interfaces.functional;

import java.util.List;

import javax.persistence.EntityManager;

public interface FindByExampleLogic<EntityType> {
	
	List<EntityType> findByExample(EntityManager entityManager, EntityType anEntity, String entityName);
}
