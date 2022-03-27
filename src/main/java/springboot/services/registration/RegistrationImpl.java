package springboot.services.registration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.dto.request.RegistrationInfo;
import springboot.dto.response.UserRegistration;
import springboot.persistence.JpaOperations;
import springboot.services.interfaces.Registration;

@Service
public class RegistrationImpl
	implements Registration
{
	@Autowired
	private JpaOperations<UserRegistration, Long> jpa;

	public boolean checkUserExists(RegistrationInfo user)
	{
		boolean retVar = false;
		
		UserRegistration userRegistration = new UserRegistration();
		userRegistration.setEmail(user.getEmail());
		userRegistration.setRegAppName(user.getRegAppName());
		List<UserRegistration> theList = jpa.findByExample(userRegistration, "RegistrationEntity", null);
		
		if (null != theList && theList.size() > 0)
		{
			retVar = true;
		}
		userRegistration = null;
		theList.clear();
		theList = null;
		
		return retVar;
	}

	public void persistData(UserRegistration userRegistrationEntity)
	{
		jpa.persist(userRegistrationEntity, null);
		System.out.println("the Id is now: " + userRegistrationEntity.getId());
	}
	
}
