package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;


@RestController
@RequestMapping("/lifechamps")
public class LifechampsController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private Environment environment;
    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping(path = "/healthcheck", method = RequestMethod.GET)
    public ResponseEntity healthCheck() {
        return new ResponseEntity<>("{ \"status\": \"ok\"}", HttpStatus.OK);
    }

    @RequestMapping(path = "/ehr", method = RequestMethod.POST)
    public ResponseEntity getEHRRecord(@RequestBody HashMap<String, Object> payload, @RequestHeader("Authorization") String token) {
        JSONObject jsonObject = new JSONObject(payload);
        kafkaTemplate.send(Objects.requireNonNull(environment.getProperty("ehr_topic_raw")), jsonObject.toString());
        return new ResponseEntity<>(HttpStatus.OK);

    }

    private boolean validateToken(String token) {
//        TODO: add some logic here that validates if the user is eligible to push ehr data
        return true;
    }


}
