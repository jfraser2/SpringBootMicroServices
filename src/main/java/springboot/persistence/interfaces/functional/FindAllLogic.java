package springboot.persistence.interfaces.functional;

import java.util.List;

import javax.persistence.EntityManager;

public interface FindAllLogic<EntityType> {
	
	List<EntityType> findAll(EntityManager entityManager, String entityName);
}
