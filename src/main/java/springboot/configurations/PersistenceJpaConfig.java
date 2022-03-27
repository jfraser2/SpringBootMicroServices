package springboot.configurations;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import springboot.configurations.server.ConfigServerReader;

/* https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa */

@Configuration
public class PersistenceJpaConfig
{
	@Autowired
	private Environment env;

	@Bean(name="entityManagerFactory1")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
	    LocalContainerEntityManagerFactoryBean emfb 
	        = new LocalContainerEntityManagerFactoryBean();
	    emfb.setDataSource(dataSource());
	    String[] scanPackages = {"springboot.dto.response", "springboot.entities"};
	    emfb.setPackagesToScan(scanPackages);
	 
	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setShowSql(true);
	    vendorAdapter.setGenerateDdl(true);
	    vendorAdapter.setPrepareConnection(false);
	    emfb.setJpaVendorAdapter(vendorAdapter);
	    emfb.setJpaProperties(additionalProperties());
	    emfb.setPersistenceUnitName("RegistrationMicroServicePU");
	    
	    return emfb;
	}	
	
	@Bean(name="pooledDataSource")
	public DataSource dataSource(){
    	String appName = ConfigServerReader.getAppName();
    	
		DataSource aDataSource = ConfigServerReader.buildPooledDataSource(appName, env);
		System.out.println("in PersistenceJpaConfig AppName: " + appName + " env is: " + env.getActiveProfiles()[0]);
    	
	    return aDataSource;
	}

	@Bean(name="transactionManager1")
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf)
	{
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf);
	    
	    System.out.println(" in transactionManager Object is: " + emf.toString());
	 
	    return transactionManager;
	}
	
	@Bean	
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
	{
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	
	private Properties additionalProperties()
	{
	    Properties properties = new Properties();
//	    properties.setProperty("hibernate.ddl-auto", "create-drop");
	    properties.setProperty("hibernate.ddl-auto", "update");
	    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
	    properties.setProperty("hibernate.current_session_context_class", "thread");
	    properties.setProperty("hibernate.format_sql", "true");
	    properties.setProperty("hibernate.connection.release_mode", "after_transaction");
	    
	        
	    return properties;
	}
	
}
