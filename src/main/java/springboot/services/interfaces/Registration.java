package springboot.services.interfaces;

import springboot.dto.request.RegistrationInfo;
import springboot.dto.response.UserRegistration;

public interface Registration {
	
	public boolean checkUserExists(RegistrationInfo user);
	public void persistData(UserRegistration userRegistrationEntity);
}
