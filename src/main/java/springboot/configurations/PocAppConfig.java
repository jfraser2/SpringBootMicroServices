package springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import springboot.autowire.helpers.JpaExampleQueryParamsContainer;
import springboot.autowire.helpers.JpaQueryParamsContainer;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.StringContainer;
import springboot.autowire.helpers.ValidationErrorContainer;

@Configuration
public class PocAppConfig {
	
	/* By default the bean name matches the method Name */
	@Bean(name="requestValidationErrorsContainer")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ValidationErrorContainer requestValidationErrorsContainer() {
		// each request has a ValidationErrorList
		return new ValidationErrorContainer();
	}
	
	/* Every request has its very own StringBuilder. Within the request,
	 * the same StringBuilder is used over and over. */
	/* Good use of Memory */
	@Bean(name="requestStringBuilderContainer")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public StringBuilderContainer requestStringBuilderContainer() {
		// each request has a StringBuilder
		return new StringBuilderContainer();
	}
	
	/* By default the bean name matches the method Name */
	@Bean(name="requestJpaQueryParamsContainer")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JpaQueryParamsContainer requestJpaQueryParamsContainer() {
		// each request has a JpaQueryParamsList
		return new JpaQueryParamsContainer();
	}
	
	/* By default the bean name matches the method Name */
	@Bean(name="requestJpaExampleQueryParamsContainer")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JpaExampleQueryParamsContainer requestJpaExampleQueryParamsContainer() {
		// each request has a JpaExampleQueryParamsList
		return new JpaExampleQueryParamsContainer();
	}
	
	/* By default the bean name matches the method Name */
	@Bean(name="requestStringContainer")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public StringContainer requestStringContainer() {
		// each request has a StringList
		return new StringContainer();
	}
	
}
