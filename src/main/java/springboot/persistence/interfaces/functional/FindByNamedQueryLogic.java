package springboot.persistence.interfaces.functional;

import java.util.List;
import javax.persistence.EntityManager;
import springboot.autowire.helpers.JpaQueryParamsContainer;

public interface FindByNamedQueryLogic<EntityType> {
	
	List<EntityType> findByNamedQuery(EntityManager entityManager, String aNamedQuery, JpaQueryParamsContainer paramListContainer);
}
