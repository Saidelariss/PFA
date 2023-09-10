package webservices.whatsapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SendingMessage {

    public String callAPIWhatsapp(String token, String destinataire, String body) throws IOException {
        String apiUrl = "https://api.ultramsg.com/instance56360/messages/chat";
        String urlWithParams = apiUrl + "?token=" + token + "&to=" + destinataire + "&body=" + body;

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(urlWithParams);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        return EntityUtils.toString(httpResponse.getEntity());
    }
}
