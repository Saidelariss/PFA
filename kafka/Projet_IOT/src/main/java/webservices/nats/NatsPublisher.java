package webservices.nats;

import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
public class NatsPublisher {
    private final Connection natsConnection;

    public NatsPublisher(String natsUrl) throws IOException, InterruptedException {
        natsConnection = Nats.connect(natsUrl);
    }


    public void publishMessage(String subject, String message) {
        natsConnection.publish(subject, message.getBytes());
    }

    public void sendNatsMessage(String subject , String message )  {
        natsConnection.publish(subject , message.getBytes());
    }


    public void closeConnection() throws InterruptedException {
        natsConnection.close();
    }
}


