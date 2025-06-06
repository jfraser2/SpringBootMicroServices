package springboot.controllers.rest;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import springboot.annotations.roles.user.AllowedUserRoles;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.configurations.server.ConfigServerReader;
import springboot.dto.request.AdminUser;
import springboot.dto.request.RegistrationInfo;
import springboot.dto.response.AdminOperation;
import springboot.dto.response.UserRegistration;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.security.SecurityDefaultMethods;
import springboot.security.SecurityValidation;
import springboot.services.interfaces.Registration;
import springboot.services.interfaces.RequestValidation;

@RestController
@RequestMapping(path="/rest/api/Registration")
public class RegistrationController
	extends ControllerBase
{
	@Autowired
	private Registration registrationService;
	
	@Autowired
	private SecurityValidation securityValidation;
	
	@Autowired
	private RequestValidation<RegistrationInfo> registrationValidation;
	
	@Autowired
	private RequestValidation<AdminUser> adminValidation;

	@Autowired
	@Qualifier("requestValidationErrorsContainer")
	private ValidationErrorContainer requestValidationErrorsContainer;
	
	@Autowired
	@Qualifier("requestStringBuilderContainer")
	private StringBuilderContainer requestStringBuilderContainer;
	
	@AllowedUserRoles(values={SecurityDefaultMethods.ALLOW_ANY_USER})
	@RequestMapping(method = {RequestMethod.POST},
			path = "/register",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> register(@RequestBody RegistrationInfo user, HttpServletRequest request)
		throws RequestValidationException, IllegalArgumentException, AccessDeniedException
	{
		
		registrationValidation.validateRequest(user, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
		Method registerMethod = getMethodOfClass(this.getClass(), "register");
		boolean userOK = securityValidation.validateUserRoles(registerMethod, user.getRegAppName(), null, null);
		if (!userOK)
		{
			throw new AccessDeniedException("User does not have a proper Role");
		}
			
		boolean usernameAlreadyExists = registrationService.checkUserExists(user);
		if(usernameAlreadyExists) {
			throw new IllegalArgumentException("You have already Registered for this Application.");
		}

		UserRegistration userRegistrationEntity = new UserRegistration(user);
		registrationService.persistData(userRegistrationEntity);
		
		String jsonString = goodResponse(userRegistrationEntity, requestStringBuilderContainer);
		userRegistrationEntity = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
	@AllowedUserRoles(values={SecurityDefaultMethods.ADMIN})
	@RequestMapping(method = {RequestMethod.POST},
			path = "/resetHashMap",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ApiImplicitParam(
			name = "Authorization",
			value = "JWT Authorization token", 
            required = true,
            dataType = "string",
            paramType = "header"
	)
	public ResponseEntity<Object> resetHashMap(@RequestBody AdminUser user, HttpServletRequest request)
		throws RequestValidationException, IllegalArgumentException, AccessDeniedException
	{
		
		adminValidation.validateRequest(user, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
		Method resetMethod = getMethodOfClass(this.getClass(), "resetHashMap");
		boolean userOK = securityValidation.validateUserRoles(resetMethod, user.getAppName(), user.getId(), null);
		if (!userOK)
		{
			throw new AccessDeniedException("User does not have a proper Role");
		}
		
		try {
			securityValidation.validateJwtToken(request, user.getAppName(), user.getId(), null);
		}
		catch (ValidationException ve)
		{
			throw new AccessDeniedException(ve.getMessage());
		}
		
		ConfigServerReader.clearHashMap();
		
		AdminOperation theResponse = new AdminOperation("HashMap Reset!!");
		String jsonString = goodResponse(theResponse, requestStringBuilderContainer);
		theResponse = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
}
