package springboot.shutdown;

import javax.annotation.PreDestroy;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import springboot.configurations.server.ConfigServerReader;

public class TerminateBean
{
	@Autowired
	@Qualifier("pooledDataSource")
	private DataSource dataSource;
	
	public TerminateBean()
	{
	}
    
    @PreDestroy
    public void onDestroy() throws Exception {
    	ConfigServerReader.closeAllDbConnections(dataSource);
        System.out.println("Spring Container is destroyed! System Shutting Down");
    }    

}
