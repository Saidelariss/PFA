package webservices.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import webservices.email.EmailServiceImpl;
import webservices.nats.MessageNats;
import webservices.nats.NatsPublisher;
import webservices.telegram.SimpleBot;
import webservices.webhook.SlackService;
import webservices.whatsapp.SendingMessage;

import java.io.IOException;

@Component
public class KafkaConsumer {
    @Autowired
    private SimpleBot simpleBot;
    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private SendingMessage sendingMessageWhatsapp;
    private String token = "wh24votbcjgqjgnn";
    private final NatsPublisher natsPublisher;
    @Autowired
    private SlackService slackService;

    public KafkaConsumer() throws IOException, InterruptedException {
        this.natsPublisher = new NatsPublisher("nats://localhost:4222");
    }

    @KafkaListener(topics = "TopicWebHook", groupId = "my-consumer-group")
    public void consume(String message) throws Exception {
        System.out.println("message reçu : "+message);
        boolean b = slackService.sendMessageToSlack(message);
        MessageNats messageNats = new MessageNats("slack",message,b);
        if(b){
            sendSuccessConfirmation(messageNats);
        }else{
            sendErrorConfirmation(messageNats);
        }

    }

    @KafkaListener(topics = "TopicTelegram", groupId = "my-consumer-group")
    public void consumeMessageFromTopicTelegram(ConsumerRecord<String, String> record) throws Exception{
        String key = record.key();
        String value = record.value();
        MessageNats message = createMessage(key, value);
        try {

            System.out.println("Received message telegram {   message: " + value + ",   chatId: " + key + "    }");
            simpleBot.sendMessage(key, value);
            sendSuccessConfirmation(message);

        } catch (Exception e) {
            sendErrorConfirmation(message);

        }
    }

    @KafkaListener(topics = "TopicWhatsapp", groupId = "my-consumer-group")
    public void consumeMessageFromTopicWhatsapp(ConsumerRecord<String, String> record) throws Exception {
        String key = record.key();
        String value = record.value();

        MessageNats message = createMessage(key,value);
        boolean b = sendingMessageWhatsapp.callAPIWhatsapp(token, key, value);
        System.out.println("Received message whatsapp {   message: " + value + ",   Numéro de Téléphone : " + key + "    }");
        if(b){
            sendingMessageWhatsapp.callAPIWhatsapp(token, key, value);
            sendSuccessConfirmation(message);


        } else  {
            sendErrorConfirmation(message);
        }

    }

    @KafkaListener(topics = "TopicEmail", groupId = "my-consumer-group")
    public void consumeMessageFromTopicEmail(ConsumerRecord<String, String> record) throws Exception{
        String key = record.key();
        String value = record.value();
        MessageNats message = createMessage(key, value);
        try {

            System.out.println("Received message telegram {   message: " + value + ",   email: " + key + "    }");
            emailService.sendSimpleMail(key,value);
            sendSuccessConfirmation(message);

        } catch (Exception e) {
            sendErrorConfirmation(message);
            e.printStackTrace();


        }
    }

    private MessageNats createMessage(String recipient, String content) {
        MessageNats message = new MessageNats();
        message.setRecipient(recipient);
        message.setContent(content);
        return message;
    }

    private void sendSuccessConfirmation(MessageNats message) throws Exception {
        message.setSentSuccessfully(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(message);
        natsPublisher.publishMessage("messageConfirmation", json);
    }

    private void sendErrorConfirmation(MessageNats message) throws Exception {
        message.setSentSuccessfully(false);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(message);
        natsPublisher.publishMessage("messageConfirmation", json);
    }









}