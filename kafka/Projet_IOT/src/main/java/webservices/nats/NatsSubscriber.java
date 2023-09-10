package webservices.nats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import org.springframework.stereotype.Component;

@Component
public class NatsSubscriber {

    private final Connection natsConnection;

    public NatsSubscriber(Connection natsConnection) {
        this.natsConnection = natsConnection;
        subscribeToMessageConfirmation();
    }

    private void subscribeToMessageConfirmation() {
        Dispatcher dispatcher = natsConnection.createDispatcher((msg) -> {
            String confirmationMessage = new String(msg.getData());
            ObjectMapper objectMapper = new ObjectMapper();
            MessageNats message = new MessageNats();
            try {
                message = objectMapper.readValue(confirmationMessage, MessageNats.class);

                //traitement
                System.out.println("Message de confirmation reçu de NATS : " +"{ destinataire : "+ message.getRecipient()+",   message : "+ message.getContent()+",  envoi avec succès : "+message.getSentSuccessfully()+"  }");
            } catch (JsonProcessingException e) {
                //throw new RuntimeException(e);
                System.out.println("Message de confirmation reçu de NATS : " +"{ destinataire : "+ message.getRecipient()+",   message : "+ message.getContent()+",  envoi avec succès : "+message.getSentSuccessfully()+"  }");

            }

        });

        dispatcher.subscribe("messageConfirmation");
    }
}
