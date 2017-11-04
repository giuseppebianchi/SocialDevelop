package it.socialdevelop.mailer;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Hello World Group
 */
public class Mailer {

    String to;
    String ObjectMessage;
    String text;
    String email = "email";
    String password = "password";

    public Mailer(String to, String ObjectMessage, String text) {
        this.to = to;
        this.ObjectMessage = ObjectMessage;
        this.text = text;
    }

    public void sendEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Mailer.this.email, Mailer.this.password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.email));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(this.to));
            message.setSubject(this.ObjectMessage);
            message.setText(this.text);

            Transport.send(message);
            System.out.println("email successfully sent..");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
