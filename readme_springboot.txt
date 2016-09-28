 http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#getting-started-maven-installation

    1) @EnableAutoConfiguration
    ---------------------------
    http://spring.io/guides/gs/convert-jar-to-war-maven/

    @EnableAutoConfiguration basically reads all dependencies that you have added in pom.xml. If you have added spring-boot-starter-web plugin in pom.xml, then
    spring will automatically add web mvc related AutoConfiguration class (WebMvcAutoConfigurationAdapter) in configurations hierarchy for that application. It will also configure Tomcat by default.

    Similarly, if you have spring-boot-starter-data-elasticsearch plugin in pom.xml, spring will automatically add ElasticsearchAutoConfiguration in configurations hierarchy for that application.

    The @EnableAutoConfiguration annotation switches on reasonable default behaviors based on the content of your classpath.
    For example, because the application depends on the embeddable version of Tomcat (tomcat-embed-core.jar), a Tomcat server is set up and configured with reasonable defaults on your behalf.
    If Jetty (spring-boot-starter-jetty) is added in pom.xml, default servlet container becomes Jetty.
    And because the application also depends on Spring MVC (spring-webmvc.jar), a Spring MVC DispatcherServlet is configured and registered for you

    Spring 3.1 has replaced web.xml with programmatic approach of using subclass of WebApplicationInitializer, in its onStartUp() method, you need to register all spring xmls(or configuration classes) that you want to load at server startup. You also need to register DispatcherServlet, ContextLoaderListener etc.
    If you are using non-spring-boot application, you need to write a subclass of WebApplicationInitializer to make Spring MVC work,
    e.g. https://samerabdelkafi.wordpress.com/2014/08/03/spring-mvc-full-java-based-config/

    But Spring Boot does all for your behind the scene. When you use @EnableAutoConfiguration and add a maven dependency of spring-boot-starter-web, spring boot will register DispatcherServlet in WebApplicationInitializer(web.xml) for you on server start up.
    When you use SpringApplication.run(<<configuration classes>>), spring boot will register all these configuration classes at server start up, just like you register in web.xml/WebApplicationInitializer.

    Moreover, in non-spring-boot spring mvc, WebMvcConfigurerAdapter has to be used with @EnableWebMvc to configure DispatcherServlet properties (like ViewResolver etc).
    Many of these properties can be defined in application.properties with Spring Boot (e.g. spring.view.prefix=/WEB-INF/jsp/, spring.view.suffix=.jsp)
    List of all properties that can defined in application.properties -
    http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html

    http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#getting-started-first-application-auto-configuration

    @EnableAutoConfiguration annotation tells Spring Boot to “guess” how you will want to configure Spring, based on the jar dependencies that you have added.
    Since spring-boot-starter-web added Tomcat and Spring MVC, the auto-configuration will assume that you are developing a web application and setup Spring accordingly. You don't have to add spring MVC related configuration in web.xml or anywhere else. It will be taken care by spring.
    You should only ever add one @EnableAutoConfiguration annotation.

    We generally recommend that you add this annotation to your primary @Configuration class.

    If you want to exclude any auto-configuration, you can use @EnableAutoConfiguration(exclude="DataSourceAutoConfiguration")

    @SpringBootApplication -

    http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-using-springbootapplication-annotation.html
    @SpringBootApplication is a convenience annotation that adds all of the following:

    - @Configuration tags the class as a source of bean definitions for the application context.
    - @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
    - Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
    - @ComponentScan tells Spring to look for other components, configurations, and services in the the hello package, allowing it to find the HelloController.


    2) SpringApplication
    --------------------
    http://spring.io/guides/gs/convert-jar-to-war-maven/

    SpringApplication.run(...) returns a specific type of ApplicationContext (e.g. EmbeddedWebApplicationContext etc.) based on your classpath (jars added in pom.xml)
    Here spring mvc jar is added using spring-boot-starter-web in pom.xml. So, it will return AnnotationConfigEmbeddedWebApplicationContext.
    run(...) is passed Application.class, it means it will read annotation metadata from Application.class and add its beans in application context.
    Along with configured beans from Application.class, it will also add other beans based on @EnableAutoConfiguration.

    @EnableAutoConfiguration annotation will see your classpath. It has spring-mvc related jar, so it will create beans related to spring mvc and SpringApplication will register those beans also application context.
    Likewise, tomcat/jetty related beans etc. This annotation also configures many beans like datasource, jdbcTemplate (if spring-jdbc jar is found in classpath) from application.properties.
    It also read embedded server's (tomcat/jetty) properties from application.properties, if provided.


    It can be used to bootstrap and launch a Spring application from a Java main method.
    By default class will perform the following steps to bootstrap your application:

    - Create an appropriate ApplicationContext instance (depending on your classpath)
    - Register a CommandLinePropertySource to expose command line arguments as Spring properties
    - Refresh the application context, loading all singleton beans
    - Trigger any CommandLineRunner beans

    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-spring-application.html

    - The SpringApplication class provides a convenient way to bootstrap a Spring application that will be started from a main()method.

    - Banner - The SpringApplication.setBanner(...)method can be used if you want to generate a banner programmatically.
    springApplication.setBannerMode(Banner.Mode.OFF);

    - The SpringApplicationBuilder
        allows you to chain together multiple method calls, and includes parent and child methods that allow you to create a hierarchy.

    new SpringApplicationBuilder()
        .bannerMode(Banner.Mode.OFF)
         .sources(Parent.class)
         .child(Application.class)
         .run(args);


    - A SpringApplication will attempt to create the right type of ApplicationContext on your behalf.
    By default,
    AnnotationConfigApplicationContext
    or
    AnnotationConfigEmbeddedWebApplicationContext
    will be used, depending on whether you are developing a web application or not.

    - CommandLine args
    You can pass commandline arguments to SpringApplication using SpringApplication.run(configuration class, args)
    You can access these args in any other bean by injecting ApplicationArguments.
    ApplicationArguments provides raw args, option args(--foo=hifoo), non-option args(foo=hifoo)
    Option args start with -- and it is optional for them to have values.
    Non-Option args need to have value. They don't start with --.

    @Component
    class MyBean {

        @Autowired
        ApplicationArguments args;

        ...
        void method() {
            args.getNonOptionArgs
            if(args.containsOption("debug")) ...
        }
    }

    You can add command line args to PropertySource also
    http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/core/env/SimpleCommandLinePropertySource.html

    PropertySource ps = new SimpleCommandLinePropertySource(args);


    http://spring.io/guides/gs/convert-jar-to-war-maven/

    SpringApplication.run(...) returns a specific type of ApplicationContext (e.g. EmbeddedWebApplicationContext etc.) based on your classpath (jars added in pom.xml)
    Here spring mvc jar is added using spring-boot-starter-web in pom.xml. So, it will return AnnotationConfigEmbeddedWebApplicationContext.
    run(...) is passed Application.class, it means it will read annotation metadata from Application.class and add its beans in application context.
    Along with configured beans from Application.class, it will also add other beans based on @EnableAutoConfiguration.

    @EnableAutoConfiguration annotation will see your classpath. It has spring-mvc related jar, so it will create beans related to spring mvc and SpringApplication will register those beans also application context.
    Likewise, tomcat/jetty related beans etc. This annotation also configures many beans like datasource, jdbcTemplate (if spring-jdbc jar is found in classpath) from application.properties.
    It also read embedded server's (tomcat/jetty) properties from application.properties, if provided.


    It can be used to bootstrap and launch a Spring application from a Java main method.
    By default class will perform the following steps to bootstrap your application:

    - Create an appropriate ApplicationContext instance (depending on your classpath)
    - Register a CommandLinePropertySource to expose command line arguments as Spring properties
    - Refresh the application context, loading all singleton beans
    - Trigger any CommandLineRunner beans

    Events
    ------
    Spring provides a few ApplicationContext related events like
     ContextClosedEvent, ContextRefreshedEvent, ContextStartedEvent, ContextStoppedEvent

    Spring-Boot provides a lot more events and related listeners related to SpringApplication in following order and they can be added in SpringApplication.addListeners(...)or SpringApplicationBuilder.listeners(...)

    1. An ApplicationStartedEvent is sent at the start of a run, but before any processing except the registration of listeners and initializers.
    2. An ApplicationEnvironmentPreparedEvent is sent when the Environment to be used in the context is known, but before the context is created.
    3. An ApplicationPreparedEvent is sent just before the refresh is started, but after bean definitions have been loaded.
    4. An ApplicationReadyEvent is sent after the refresh and any related callbacks have been processed to indicate the application is ready to service requests.
    5. An ApplicationFailedEvent is sent if there is an exception on startup.


    ApplicationRunner or CommandLineRunner
    --------------------------------------
    If you need to run some specific code once the SpringApplication has started, you can implement the ApplicationRunner or CommandLineRunner interfaces.

    class Application implements CommandLineRunner {
        public void run(String... args) {
            // Do something...
        }
    }

    ExitCodeGenerator
    -----------------
    beans may implement the org.springframework.boot.ExitCodeGenerator interface if they wish to return a specific exit code when the application ends.

    PropertySources and Environment
    -------------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

    Spring Boot uses a very particular PropertySource order.

    PropertySources:
        Order of properties in PropertySources

        [Command line arguments,
        Properties from SPRING_APPLICATION_JSON
        servletConfigInitParams,
        servletContextInitParams,
        systemProperties,
        systemEnvironment,
        random,
        applicationConfig: [classpath:/application-{profile}.properties],
        applicationConfig: [classpath:/application.properties],
        <<reverse order of @PropertySources declared with @Configuration on the class>>

    Properties from SPRING_APPLICATION_JSON:
        you can send json with properties as args
        ‐Dspring.application.json='{"foo":"bar"}
        ‐‐spring.application.json='{"foo":"bar"}'

    CommandLine Args:
        By default SpringApplication will convert any command line option arguments (starting with ‘--’, e.g. ‐‐server.port=9000) to a propertyand add it to the Spring Environment

    Random value properties:

        The RandomValuePropertySourceis useful for injecting random values

        my.secret=${random.value}
        my.number=${random.int}
        my.bignumber=${random.long}
        my.number.less.than.ten=${random.int(10)}
        my.number.in.range=${random.int[1024,65536]}


    application.properties :

        http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

        SpringApplication will consider application.properties as default @PropertySource and PropertyPlaceholderConfigurer's propertysource for returned application context.

        SpringApplication will load properties from application.properties files in the following locations and add them to the Spring Environment:

            - A /config subdir of the current directory.
            - The current directory
            - A classpath /config package
            - The classpath root

        If you don’t like application.properties as the configuration file name you can switch to another by specifying a 'spring.config.name' environment property.

        Property values can be injected directly into your beans using the
        - @Value annotation
        - accessed via Spring’s Environment abstraction or bound to structured objects.

        By default SpringApplication will convert any command line option arguments (starting with ‘--’, e.g. --server.port=9000) to a property and add it to the Spring Environment. As mentioned above, command line properties always take precedence over other property sources.
        If you don’t want command line properties to be added to the Environment you can disable them using SpringApplication.setAddCommandLineProperties(false).

        If you don’t like application.properties as the configuration file name you can switch to another by specifying a spring.config.name environment property. You can also refer to an explicit location using the spring.config.location environment property (comma-separated list of directory locations, or file paths).
        $ java -jar myproject.jar --spring.config.name=myproject
        or
        $ java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties

    Profile specific application.properties :

        In addition to application.properties files, profile specific properties can also be defined using the naming convention application-{profile}.properties.
        Here, I have created application-MiralProfile.properties also.
        Whenever this class is run with vm(system) arguments '-Dspring.profiles.active="MiralProfile"' or program argument '-Dspring.profiles.active="MiralProfile"', it will load application-MiralProfile.properties in spring's Environment.

        "mvn -Dspring.profiles.active=MiralProfile spring-boot:run" --- it will take application-MiralProfile.properties
        "mvn -Dspring.profiles.active=production spring-boot:run" --- it will take application.properties

        You can set profiles programmatically also, e.g. SpringApplication.setAdditionalProfiles(…​). It is also possible to activate profiles using Spring’s ConfigurableEnvironment interface.

    Placeholders in properties:
        app.name=MyApp
        app.description=${app.name} is a Spring Boot application

        You can also defined property place holder in properties file. It will also be replaced by property in the Environment.

    @ConfigurationProperties:
        http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
        http://www.java-allandsundry.com/2015/07/spring-boot-configurationproperties.html
        You can have this annotation on top of your bean class and bean class variables will automatically be injected values from mentioned properties

        Relaxed binding:
            @ConfigurationProperties(prefix='person") will inject all properties starting with person.*** to class variables.
            e.g. person.firstName property will be injected in firstName variable of a class.


        The @EnableConfigurationProperties annotation is automatically applied to your project so that any beans annotated with @ConfigurationProperties will be configured from the Environment properties.


        @Configuration
        @EnableConfigurationProperties(ConnectionSettings.class)
        public class MyConfiguration{
        }

        @EnableConfigurationProperties(ConnectionSettings.class) will consider ConnectionSettings class a properties holder class. It will inject Environment properties in this class. It's an alternative of @Value usage.


    Customizing servlet container's(server) properties
    --------------------------------------------------
    Common servlet container settings can be configured using Spring Environment properties. Usually you would define the properties in your application.properties file.
        Common server settings include:
            server.port — The listen port for incoming HTTP requests.
            server.address — The interface address to bind to.
            server.sessionTimeout — A session timeout.

        See the ServerProperties class for a complete list.

    You can do it programmatically also using EmbeddedServletContainerCustomizer.

    Customizing servlet container's(server) properties
    --------------------------------------------------
    Common servlet container settings can be configured using Spring Environment properties. Usually you would define the properties in your application.properties file.
        Common server settings include:
            server.port — The listen port for incoming HTTP requests.
            server.address — The interface address to bind to.
            server.sessionTimeout — A session timeout.

        See the ServerProperties class for a complete list.

    You can do it programmatically also using EmbeddedServletContainerCustomizer.

    3) Starting the project(Use of spring-boot-maven-plugin):
       ------------------------------------------------------
    http://spring.io/guides/gs/convert-jar-to-war-maven/

    The Spring Boot Maven plugin (spring-boot-maven-plugin) provides many convenient features:

    - It collects all the jars on the classpath and builds a single, runnable "über-jar", which makes it more convenient to execute and transport your service.
    - It searches for the 'public static void main()' method to flag as a runnable class.
    - It provides a built-in dependency resolver that sets the version number to match Spring Boot dependencies. You can override any version you wish, but it will default to Boot’s chosen set of versions.


    You can ofcourse run this class as java application from eclipse, but you can also do the same using spring-boot-maven-plugin : 'mvn spring-boot:run'.
    spring-boot will try find out single class in the project with main method and run that class. If more than one classes with main methods found, it will throw an error and server won't start. You can add <start-class> property in pom.xml to tell Spring Boot explicitly which class's main method to be used.


    Plugin documentation : http://docs.spring.io/spring-boot/docs/1.2.0.BUILD-SNAPSHOT/maven-plugin/index.html

    'spring-boot-maven-plugin' has a goal 'run'. this plugin is by default available in 'spring-boot-starter-parent' module. By default it is bound to phase 'repackage'.
    This plugin also repackages the project. You can see pom.xml of this project as well.

    It has two goals (http://docs.spring.io/spring-boot/docs/1.2.0.BUILD-SNAPSHOT/maven-plugin/)
    spring-boot:run --- runs your Spring Boot application.
    spring-boot:repackage --- Repackages existing JAR and WAR archives so that they can be executed from the command line using java -jar. (??? - "mvn package" anyway creates jar/war. why "mvn package spring-boot:repackage" is required???)

    4) Starter POMs
       ------------
    All spring boot starter dependencies are mentioned at https://github.com/spring-projects/spring-boot/tree/master/spring-boot-starters

    http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
    Spring Boot provides a number of “Starter POMs” that make easy to add jars to your classpath. Our sample application has already used
    Our application should use spring-boot-starter-parent in the parent section of the POM.
    Other “Starter POMs” simply provide dependencies that you are likely to need when developing a specific type of application.

    e.g. spring-boot-starter-web
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    If you run mvn dependency:tree again, you will see that there are now a number of additional dependencies, including the Tomcat web server and Spring Boot itself.

    @EnableAutoConfiguration will detect what dependencies are added. It uses many AutoConfiguration classes to add auto configured beans to application context.
    e.g.
    1) WebMvcAutoConfiguration
        If you have spring-boot-starter-web as dependency that adds DispatcherServlet etc webmvc related classes, then only this WebMvcAutoConfiguration bean will be added to application context by @EnableAutoConfiguration
        it does default configuration of WebMvc related beans.
        It will be used only if a bean of WebMvcConfigurationSupport is missing and Servlet, DispatcherServlet, WebMvcConfigurationAdapter etc are classes are added in classpath.
    2) DataSourceAutoConfiguration
        If you have spring-boot-starter-jdbc as dependency that adds DataSource etc classes to classpath, then only DataSourceAutoConfiguration bean will be added to application context.
        If it detects that you have your own DataSource bean, then it will not use its EmbeddedDataSourceConfiguration that uses
        It uses DataSourceProperties class that reads properties from your Environment with prefixed 'spring.datasource'.
    3) DataSourceTransactionManagerAutoConfiguration
    4) JmxAutoConfiguration
    5) EndpointMBeanExportAutoConfiguration


    @SpringBootApplication = @EnableAutoConfiguration + @Configuration + @ComponentScan + @EnableWebMvc
    ----------------------
    is a convenience annotation that adds all of the following:

    -   @Configuration tags the class as a source of bean definitions for the application context.
    -   @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
    -   Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
    -   @ComponentScan tells Spring to look for other components, configurations, and services in the the hello package, allowing it to find the HelloController.

    SpringBootServletInitializer - Special WebApplicationInitializer for Spring Boot application
    ----------------------------
    SpringBootServletInitializer is a WebApplicationInitializer that should be used when you want to deploy spring boot application using war deployment
    it means that pom.xml will have packaging=war.
    SpringBootServletInitializer is a predefined WebApplicationInitializer created for spring boot application. It automatically detects Servlets(ServletRegistrationBean) and Filters(FilterRegistrationBean) from the contexts(Configurations) and register them.

    Normal customized WebApplicationInitializer will not work with spring boot.
    http://www.java-allandsundry.com/2014/09/spring-webapplicationinitializer-and.html
    You need to use SpringBootServletInitializer because SpringBootServletInitializer builds SpringApplication which is a required element for spring boot.

    Setting Profiles in Spring-Boot
    -------------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html

    You can set active profiles in spring-boot using 'spring.profiles.active' as a command line arg/property inside properties file.

    Sometimes it is useful to have profile-specific properties that add to the active profiles rather than replace them.
    spring.profiles.include  property can be used to unconditionally add active profiles.

    Spring WebMvc with Spring-Boot
    ------------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html

    Spring Boot application extends SpringBootServletInitializer, which configures default WebApplicationInitializer.


    @EnableWebMvc provides default ViewResolvers, MessageConverters etc using DelegatingWebMvcConfiguration, which are required by Spring MVC. You can override/add more by adding customized WebMvcConfigurerAdapter.

    In Spring-Boot,
                    @EnableAutoConfiguration will add DispatcherServletAutoConfiguration, WebMvcAutoConfiguration etc Configuration classes if spring-boot-starter-web dependency is added in classpath.
                    WebMvcAutoConfiguration provides its own @EnableWebMvc and WebMvcConfigurationAdapter, so you don't need to add your own.
                    If you want to keep Spring Boot MVC features, and you just want to add additional MVC configuration (interceptors, formatters, view controllers etc.) you can add your own @Bean of type WebMvcConfigurerAdapter, but without @EnableWebMvc.

                    if you provide your own @EnableWebMvc, then WebMvcAutoConfiguration will not be used.

    If you want to take complete control of Spring MVC, you can add your own @Configuration annotated with @EnableWebMvc.


    Static Content:  (like css, js, html files etc)
        By default Spring Boot will serve static content from a directory called /static(or /publicor /resourcesor /META‐INF/resources) in the classpath or from the root of the ServletContext. It uses the ResourceHttpRequestHandlerfrom Spring MVC so you can modify that behavior by adding your own WebMvcConfigurerAdapterand overriding the addResourceHandlersmethod.


    Template Engines:
        Spring Boot includes auto-configuration support for the following templating engines:
            FreeMarker
            Groovy
            Thymeleaf
            Velocity
            Mustache
        your templates will be picked up automatically from src/main/resources/templates

    JAX-RS and Jersey:
        Read 'http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html'

    Embedded servlet container support:
        Spring Boot includes support for embedded Tomcat, Jetty, and Undertow servers.
        By default the embedded server will listen for HTTP requests on port 8080.
        Most developers will simply use the appropriate ‘Starter POM’ to obtain a fully configured instance

        Customizing embedded servlet containers:
        You can customize servlet container settings in properties file (normally application.properties)
        To change default server port 8080, use server.port, context-root to server.address
        There are many other properties like server.session.timeout etc.
        You can do the same thing programmatically also using implementsEmbeddedServletContainerCustomizer.



    Servlets, Filters, and listeners:
        By default, if the context contains only a single Servlet it will be mapped to /. In the case of multiple Servlet beans the bean name will be used as a path prefix. Filters will map to /*.
        If convention-based mapping is not flexible enough you can use the ServletRegistrationBean, FilterRegistrationBean and ServletListenerRegistrationBean classes for complete control.
        So, to add more servlets, filters, servlet listeners, you simply create a bean of type ServletRegistrationBean, FilterRegistrationBean and ServletListenerRegistrationBean.

    Logging with Spring-Boot
    ------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html

    Messaging using Spring-Boot
    ---------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-messaging.html

    Caching using Spring-Boot
    -------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html

    Working with SQL DBs using Spring-Data and Spring-Boot
    ------------------------------------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html

    Working with NoSql DBs using Spring-Data and Spring-Boot
    --------------------------------------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-nosql.html

    Security with Spring-Boot
    -------------------------
    http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-security.html
