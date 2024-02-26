package webservices.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSMS {
    private String from ;
    private String to ;
    private String msgBody ;
}
