package ehtp.mostafa.servicesmsvonage.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import ehtp.mostafa.servicesmsvonage.domain.MessageSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class SendingSmsVonage {
    public static void main(String[] args) {
        VonageClient client = VonageClient.builder()
                .apiKey("aff3bb96")
                .apiSecret("xrKx4gi0xM4TQCZd")
                .build();

        TextMessage message = new TextMessage("Vonage APIs",
                "212706163014",
                "A text message sent using the Vonage SMS API"
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
}

