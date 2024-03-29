package webservices.email;



import webservices.email.entities.Message;

public interface EmailService {
    // To send a simple email
    void sendSimpleMail(String recipient,String msg) throws Exception;

    // To send an email with attachment
    String sendMailWithAttachment(Message details);

    String sendMailTemplate(Message details) ;
}
