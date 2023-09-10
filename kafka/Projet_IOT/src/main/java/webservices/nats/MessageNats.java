package webservices.nats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageNats {
    private String recipient; // Destinataire du message
    private String content;   // Contenu du message
    private boolean sentSuccessfully; // Indique si le message a été envoyé avec succès

    public boolean getSentSuccessfully(){
        return sentSuccessfully;
    }


}
