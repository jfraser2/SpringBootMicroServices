package springboot.persistence.interfaces.functional;

import javax.persistence.EntityManager;

public interface PersistLogic {
	
	void persist(EntityManager em, Object dataRec);
}
