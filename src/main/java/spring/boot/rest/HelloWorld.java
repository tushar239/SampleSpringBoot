package spring.boot.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // same as @Component for normal spring bean
@RequestMapping("/hello")
public class HelloWorld {
    private static Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    @Value("${my.name}")
    private String name;
    
    @Value("${my.description}")
    private String description;

    // Run main method using tool (eclipse/intellij). Based on pom.xml dependencies, tomcat or jetty will be started

    // If you are using default profile (application.properties), then use
    // http://localhost:8080/hello/home

    // You can use 'application-MiralProfile.properties', if you are using 'MiralProfile'. It can be used by passing '-Dspring.profiles.active=MiralProfile' while running main method
    // or "mvn -Dspring.profiles.active=MiralProfile spring-boot:run"
    // http://localhost:8084/hello/home

    @RequestMapping("/home")
    public String home() {
        logger.info("Inside HelloWorld's home()");
        return "Hello World! "+name+ ". "+description;
    }
    public static void main(String[] args) throws Exception {
        // run method actually creates an instance of AnnotationConfigEmbeddedWebApplicationContext
        // and r    egisters sources (here just HelloWorld.class) as beans in the context.
        // It is same as mentioning 'ContextConfigLocations' in web.xml with spring xml files.
        SpringApplication.run(HelloWorld.class, args);
    }
}
