package webservices.sms;


import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webservices.nats.NatsPublisher;

@Service
public class SmsVonage {
    @Autowired
    private VonageClient vonageClient ;
    @Autowired
    private NatsPublisher nats ;

    public boolean SendSMS(MessageSMS sms){
        TextMessage message = new TextMessage(
                sms.getFrom() ,
                sms.getTo(),
                sms.getMsgBody()
        );

        SmsSubmissionResponse smsSubResp =
                vonageClient.getSmsClient().submitMessage(message);

        if (smsSubResp.getMessages().get(0).getStatus() == MessageStatus.OK) {
            nats.sendNatsMessage("nats.notification.sms", "message send successfully !!");
            return true;
        } else {
            nats.sendNatsMessage("nats.notification.sms", "message failed to send");
            return false ;
        }
    }
}

