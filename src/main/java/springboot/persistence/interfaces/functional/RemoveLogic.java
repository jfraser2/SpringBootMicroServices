package springboot.persistence.interfaces.functional;

import javax.persistence.EntityManager;

public interface RemoveLogic {
	
	void remove(EntityManager entityManager, Object entityRec);	
}
