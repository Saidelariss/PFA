package webservices.webhook;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import webservices.nats.NatsPublisher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SlackService {
    private final RestTemplate restTemplate ;
    @Value(value = "${hook.slack.url}")
    private String url ;

  //  private final NatsPublisher nats = new NatsPublisher("nats://localhost:4222");;

    public SlackService(RestTemplate restTemplate)  {
        this.restTemplate = restTemplate;
    }

    public boolean sendMessageToSlack(String message){

        Map<String , String> messageBuilder = new HashMap<String , String>();
        HttpHeaders headers =  new HttpHeaders();
        // set the headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        messageBuilder.put("text" , message) ;
        HttpEntity<Map<String , String >> request = new HttpEntity<>(messageBuilder , headers);
        ResponseEntity response = restTemplate.postForEntity(url , request , String.class );
        if (response.getStatusCode().is2xxSuccessful()){
//            nats.sendNatsMessage("nats.notification.webhook", "message send successfully !!");
            return true;
        }
        else {
//            nats.sendNatsMessage("nats.notification.webhook", "message failed to send !!");
            return false;
        }
    }
}
