package webservices.email;

import ethp.mostafa.serviceemailspringboot.entities.Message;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import webservices.nats.NatsPublisher;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailServiceImpl implements EmailService {
    @Autowired
    private Configuration configuration ;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value(value = "${spring.mail.username}")
    private String sender ;

//    @Autowired
//    private NatsPublisher nats ;

    @Override
    public void sendSimpleMail(String recipient,String msg) throws Exception {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            // set the infos of email
            mailMessage.setFrom(sender);
            mailMessage.setTo(recipient);
            mailMessage.setText(msg);
            mailMessage.setSubject("Alerte");
           // mailMessage.setSubject(details.getSubject());
            // send the email
            javaMailSender.send(mailMessage);
           // nats.sendNatsMessage("nats.notification.mail", "message send successfully");
        /*}
        catch (Exception e) {
          //  nats.sendNatsMessage("nats.notification.mail", "message failed to send");
            System.out.println(e.getMessage());
        }

         */
    }

    @Override
    public String sendMailWithAttachment(Message details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper ;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage , true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getTo());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());


            // add attachement

            FileSystemResource file = new FileSystemResource(
                    new File(details.getAttachment())
            );



            mimeMessageHelper.addAttachment(file.getFilename() , file);

            // send the message  :
            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully...";

        } catch (MessagingException e) {
            return "Error while Sending Mail";
        }


    }

    @Override
    public String sendMailTemplate(Message details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper ;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage , true) ;
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getTo());
            mimeMessageHelper.setText(getEmailContent(details) , true);
            mimeMessageHelper.setSubject(details.getSubject());
            javaMailSender.send(mimeMessage);

            return "Mail Sent Successfully...";

        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
    String getEmailContent(Message details) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("details", details);
        configuration.getTemplate("email-template.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
