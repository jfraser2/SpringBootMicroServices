package springboot.configurations.server;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.core.env.Environment;

/**
 * 
 * Read Configurations from a Vault or a Configuration Server.
 * The idea is to be able to change configurations without 
 * restarting the service.
 *
 */

public class ConfigServerReader {
	
	/* For Decrypting all Stored Decryption Keys */
	/* for example what if you wanted to changed the encryption key */
	/* for PII(Personally Identifiable Information) data. To do that, you */
	/* would have to keep track of the key used for every record, stored with */
	/* with PII data. Or you would have decrypt and re-encrypt every record in the  */
	/* database with PII data. The super secret key would be used to encrypt the */
	/* decryption keys. Now the database Hacker would also have to hack your Config Server */
	/* or vault which should be on an intranet(not public facing) Server. A tall order indeed. */
	/* if the super secret key needs to change, you would have to re-encrypt all the */
	/* saved decryption keys(I am assuming they are stored in a database table for explanation sake */
	/* OK the vault should be behind the Configuration Server */
	private static final String SUPER_SECRET_KEY = "SuperSecretKey::1";
	public static final String APP_NAME_PROPERTY = "appName";
	
	private static final ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>();
	
	/**
	 * 
	 * A MicroService can be used by many applications
	 * 
	 * @param appName
	 * @param aKey
	 * @param anEnvironment
	 * @return
	 */
	public static String getConfigSetting(String appName, String aKey, Environment anEnvironment)
	{
		String retVar = "";
		
		if (null != aKey && aKey.length() > 0
			&& null != appName && appName.length() > 0
			&& null != anEnvironment
			&& anEnvironment.getActiveProfiles().length > 0)
		{
			String envName =  anEnvironment.getActiveProfiles()[0].toUpperCase();
			String realKey = appName + "::" + aKey + "-" + envName;
			// check the cache then call config Server;
			// check the cache then call config Server;
			String cacheTest = concurrentHashMap.get(realKey);
			if (null != cacheTest && cacheTest.length() > 0)
			{
				retVar = cacheTest;
			}
			else
			{
				// hit the config Server using the Rest Template, put the response in the cache
				String configServerResponse = "myKey"; // for Starter project only
				retVar = configServerResponse;
				concurrentHashMap.put(realKey, configServerResponse);
			}
		}
		
		return retVar;
	}
	
	public static String getConfigSettingNoCache(String appName, String aKey, Environment anEnvironment)
	{
		String retVar = "";
		
		if (null != aKey && aKey.length() > 0
			&& null != appName && appName.length() > 0
			&& null != anEnvironment
			&& anEnvironment.getActiveProfiles().length > 0)
		{
			String envName =  anEnvironment.getActiveProfiles()[0].toUpperCase();
			String realKey = appName + "::" + aKey + "-" + envName;
			// hit the config Server using the Rest Template
			String configServerResponse = "myKey"; // for Starter project only
			retVar = configServerResponse;
		}
		
		return retVar;
	}
	
	/**
	 * 
	 * A MicroService can be used by many applications
	 * 
	 * @param appName
	 * @return
	 */
	public static String getSuperSecretKey(String appName)
	{
		String retVar = "";
		
		if (null != appName && appName.length() > 0)
		{
			String realKey = appName + "::" + SUPER_SECRET_KEY;
			// check the cache then call config Server;
			String cacheTest = concurrentHashMap.get(realKey);
			if (null != cacheTest && cacheTest.length() > 0)
			{
				retVar = cacheTest;
			}
			else
			{
				// hit the config Server using the Rest Template, put the response in the cache
				String configServerResponse = "myKey"; // for Starter project only
				retVar = configServerResponse;
				concurrentHashMap.put(realKey, configServerResponse);
			}
		}
		
		return retVar;
	}
	
	/**
	 * Used to Refresh the Map if the environment changes Dynamically for example
	 */
	public static void clearHashMap()
	{
		concurrentHashMap.clear();
		return;
	}
	
	public static DataSource buildPooledDataSource(String appName, Environment anEnvironment)
	{
//		String envName =  anEnvironment.getActiveProfiles()[0].toUpperCase();
//		String connectString = getConfigSettingNoCache(appName, "DBCONN", anEnvironment);
//		String userName = getConfigSettingNoCache(appName, "DBCONN-UserName", anEnvironment);
//		String password = getConfigSettingNoCache(appName, "DBCONN-Password", anEnvironment);
//		String driverClass = getConfigSettingNoCache(appName, "DBCONN-DriverClass", anEnvironment);
		
		/* cheating for now in memory database */
		
		String connectString = "jdbc:h2:mem:testdb";
		String userName = "SA"; // also try SA, with no password, initial h2
		String password = ""; // initial h2
		String driverClass = "org.h2.Driver";
		
		// https://tomcat.apache.org/tomcat-9.0-doc/jdbc-pool.html#Plain_Ol'_Java

		PoolProperties p = new PoolProperties();
		
		p.setDefaultAutoCommit(false);
		p.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		
        p.setUrl(connectString);
        p.setUsername(userName);
        p.setPassword(password);
        p.setDriverClassName(driverClass);
        
        p.setTestWhileIdle(false);
        p.setValidationQuery("SELECT 1");
        
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(20);
        p.setMaxIdle(20);
        p.setInitialSize(5);
        p.setMaxWait(30000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(1);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
		 
		DataSource aDataSource = null;
		
		try {
			aDataSource = new DataSource(p);
		}
		catch (Exception e)
		{
			System.out.println("Create Pool Failed!!!");
			aDataSource = null;
		}
		
		return aDataSource;
		
	}
	
	public static void closeAllDbConnections(DataSource aPooledDataSource)
	{
		if (null != aPooledDataSource)
		{
			aPooledDataSource.close(true);
			System.out.println("removed all the connections");
		}
		
		return;
	}
	
	public static String getAppName()
	{
		String retVar = System.getProperty(APP_NAME_PROPERTY);
		
		return retVar;
	}
	
}
