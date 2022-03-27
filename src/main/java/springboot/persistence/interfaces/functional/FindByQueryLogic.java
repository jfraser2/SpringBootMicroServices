package springboot.persistence.interfaces.functional;

import java.util.List;
import javax.persistence.EntityManager;
import springboot.autowire.helpers.JpaQueryParamsContainer;

public interface FindByQueryLogic<EntityType> {
	
	List<EntityType> findByQuery(EntityManager entityManager, String aQueryStr, JpaQueryParamsContainer paramListContainer);
}
