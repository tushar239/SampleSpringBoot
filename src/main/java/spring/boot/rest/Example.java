package spring.boot.rest;

import org.springframework.boot.SpringApplication;

public class Example /*extends SpringBootServletInitializer*/ {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationConfiguration.class, args);
    }

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.sources(ApplicationConfiguration.class);
        return application;
    }*/
}
