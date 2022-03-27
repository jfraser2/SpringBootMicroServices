package springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import springboot.configurations.server.ConfigServerReader;
import springboot.entities.App;
import springboot.entities.Role;
import springboot.entities.User;
import springboot.entities.UserAppRoles;
import springboot.entities.UserTokens;
import springboot.persistence.JpaOperations;

@Component
public class AppInitListener
	implements ApplicationListener<ContextRefreshedEvent>	
{
	@Autowired
	private Environment env;
	
	@Autowired
	JpaOperations<User, Long> jpa;
	
	@Autowired
	JpaOperations<App, Long> jpa2;
	
	@Autowired
	JpaOperations<Role, Long> jpa3;
	
	@Autowired
	JpaOperations<UserAppRoles, Long> jpa4;
	
	@Autowired // good Token and Expired Token
	JpaOperations<UserTokens, Long> jpa5;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event)
	{
		
		String envValue = env.getActiveProfiles()[0];
		System.out.println("!!! Boot Environment is: " + envValue);
		
		String appName = ConfigServerReader.getAppName();
		System.out.println("!!! Application Name is: " + appName);
		
		System.out.println("in Application Init Listener ");
        // read the database and get everything you can think of into memcached
		
		
		/* for the Starter Project only */
		
		User aUser = new User();
		aUser.setEmail("joseph.fraser@contractor.hallmark.com");
		aUser.setPassword("a");
		aUser.setLastName("b");
		aUser.setFirstName("c");
		aUser.setUserName("d");
		aUser.setActive(true);
		jpa.persist(aUser, null);
		System.out.println("User Id is: " + aUser.getId());
		
		App anApp = new App();
		anApp.setAppName("Starter");
		jpa2.persist(anApp, null);
		System.out.println("App Id is: " + anApp.getId());
		
		Role aRole = new Role();
		aRole.setRoleName("ADMIN");
		jpa3.persist(aRole, null);
		System.out.println("Role Id is: " + aRole.getId());
		
		UserAppRoles userAppRoles = new UserAppRoles();
		userAppRoles.setUser(aUser);
		userAppRoles.setApp(anApp);
		userAppRoles.setRole(aRole);
		jpa4.persist(userAppRoles, null);
		System.out.println("UserAppRoles Id is: " + userAppRoles.getId());
		
		// Good Token
		UserTokens userTokens = new UserTokens();
		userTokens.setUser(aUser);
		userTokens.setApp(anApp);
		userTokens.setActive(true);
		jpa5.persist(userTokens, null); 
		System.out.println("UserTokens Good Id is: " + userTokens.getId());

		// Expired Token
		UserTokens userTokensExpired = new UserTokens();
		userTokensExpired.setUser(aUser);
		userTokensExpired.setApp(anApp);
		userTokensExpired.setActive(true);
		jpa5.persist(userTokensExpired, null); 
		System.out.println("UserTokens Expired Id is: " + userTokensExpired.getId());
		
		userTokensExpired = null;
		userTokens = null;
		userAppRoles = null;
		aRole = null;
		anApp = null;
		aUser = null;
		
	}
	
}
