package com.biz4solutions.clientinvoice.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {
    private void sendmail(String emailTo) throws MessagingException, IOException {
        Properties props = System.getProperties();
        // props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.smtp.host", "smtp.ionos.com");
         props.put("mail.smtp.port", "587");

    /*    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("tutorialspoint@gmail.com", "<your password>");
            }
        });
*/
     //   System.out.println(mail.sm);
        Session session = Session.getDefaultInstance(props);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(props.getProperty("spring_mail_username"), false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
        msg.setSubject("Please verify your email");
        msg.setContent("Verify your email", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        String contentMessage = "Click on the link to activate your account \n http://";
        messageBodyPart.setContent(contentMessage, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        //   MimeBodyPart attachPart = new MimeBodyPart();

        // attachPart.attachFile("/var/tmp/image19.png");
        //  multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }
}

