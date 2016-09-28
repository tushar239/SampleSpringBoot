package spring.boot.rest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tushar Chokshi @ 10/14/15.
 */
@EnableAutoConfiguration // (exclude={DataSourceAutoConfiguration.class})
//@EnableConfigurationProperties // works only with YML properties
//@ConfigurationProperties(prefix="my.")// works only with YML properties
@Configuration
@ComponentScan
public class ApplicationConfiguration {
}
