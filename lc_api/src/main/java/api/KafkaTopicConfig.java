package api;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
//@EnableAutoConfiguration
@PropertySource(value = {"classpath:/kafka.properties"})
public class KafkaTopicConfig {

    @Autowired
    private Environment environment;

    //    Initialize the topics
    @Bean
    public NewTopic topicEHR(){
        return new NewTopic(environment.getProperty("ehr_topic_raw"),1,(short) 1);
    }



}
