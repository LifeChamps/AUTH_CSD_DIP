package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

//@EnableScheduling
//@EnableAutoConfiguration
//@ComponentScan("java")
@SpringBootApplication
public class ApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(ApiApplication.class); // load application properties from resources
    }

    /**
     * The application entry point
     * @param args Application arguments
     */
    public static void main(String[] args)
    {
        SpringApplication.run(ApiApplication.class, args);
    }
}