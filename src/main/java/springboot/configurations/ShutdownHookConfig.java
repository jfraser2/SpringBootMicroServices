package springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springboot.shutdown.TerminateBean;

@Configuration
public class ShutdownHookConfig {
	
    @Bean
    public TerminateBean getTerminateBean() {
        return new TerminateBean();
    }
}
