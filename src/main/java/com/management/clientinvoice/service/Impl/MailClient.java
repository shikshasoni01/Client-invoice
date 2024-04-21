package com.management.clientinvoice.service.Impl;


import com.management.clientinvoice.dto.MailDTO;
import com.management.clientinvoice.enumerator.MailType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailClient {

    private static final Logger logger = Logger.getLogger(MailClient.class);

    private final JavaMailSender mailSender;

    private final MailContentBuilder mailContentBuilder;

    @Value("${mail_from}")
    private String mailFrom;

    @Value("${accounts_team_mail")
    private String accountsTeamMail;

    @Autowired
    public MailClient(JavaMailSender mailSender, MailContentBuilder mailContentBuilder) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    @Async
    public void prepareAndSend(String recipient, String message, boolean isHtml, String subject) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, isHtml);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.info("Error in prepareAndSend " + e.getMessage());
        }
    }

    @Async
    public void prepareAndSend(MailDTO mail, MailType emailTemplateType) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setSubject(mail.getMailSubject());
        mimeMessageHelper.setFrom(mail.getMailFrom());
        mimeMessageHelper.setTo(mail.getMailToo());
        mimeMessageHelper.setCc(mail.getMailCc());
        String fileName = mail.getFileName()+".pdf";
        mimeMessageHelper.addAttachment(fileName,mail.getFileAttachment());

        String message = mailContentBuilder.build(mail.getModel(), emailTemplateType.getMailType());
        mimeMessageHelper.setText(message, true);

        mailSender.send(mimeMessageHelper.getMimeMessage());
    }


    @Async
    public void prepareAndSendB(MailDTO mail, MailType emailTemplateType) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setSubject(mail.getMailSubject());
        mimeMessageHelper.setFrom(mail.getMailFrom());
        mimeMessageHelper.setTo(mail.getMailToo());
        mimeMessageHelper.setCc(mail.getMailCc());
//        mimeMessageHelper.addAttachment("draft.pdf",mail.getFileAttachment());

        String message = mailContentBuilder.build(mail.getModel(), emailTemplateType.getMailType());
        mimeMessageHelper.setText(message, true);

        mailSender.send(mimeMessageHelper.getMimeMessage());
    }



}

