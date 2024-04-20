package com.biz4solutions.clientinvoice.service.Impl;

import com.biz4solutions.clientinvoice.dto.MailDTO;
import com.biz4solutions.clientinvoice.enumerator.MailType;
import com.biz4solutions.clientinvoice.service.EmailService;
import com.biz4solutions.clientinvoice.service.ICommonService;
import com.google.api.client.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${mail_from}")
    private String mailFrom;


    @Autowired
    private ICommonService commonService;

    @Autowired
    private MailClient mailClient;

    @Value("${accounts_team_mail}")
    private String accountsTeamMail;

    @Override
    public void sendInvoiceCreateMailPending(String email, String fullName, String projectName, String clientName, String invoiceId) throws MessagingException {
        String subject = commonService.getMessageFromDatabase("invoiceMailSubject", new Object[]{projectName,invoiceId});
        String body = commonService.getMessageFromDatabase("invoiceMailBody", new Object[]{invoiceId, clientName, projectName});
        String modifiedBody = commonService.getModifiedMessage(body, invoiceId);
        System.out.println("///////////////////////"+email);
        commonMailFormat(email,fullName,projectName,invoiceId, clientName, subject, modifiedBody);
    }

    @Override
    public void sendUpdateInvoiceNumberCreateMail(String email,File file, String projectName, String userName, String oldInvoiceNumber,String updatedInvoiceNumber) throws MessagingException {
        String subject = commonService.getMessageFromDatabase("InvoiceNumberUpdateMailSubject", new Object[]{projectName,updatedInvoiceNumber});
        String body = commonService.getMessageFromDatabase("invoiceMailBody", new Object[]{oldInvoiceNumber,updatedInvoiceNumber, userName, projectName});
        String modifiedBody = commonService.getModifiedMessage(body,updatedInvoiceNumber );
        System.out.println("///////////////////////"+email);
        commonMailFormat(email,file,userName,projectName,oldInvoiceNumber,updatedInvoiceNumber,subject, modifiedBody);
    }

    @Override
    public void sendInvoiceCreateMailBilledToClient(String email,String fullName, String projectName, String clientName, String invoiceId,String paymentStatus) throws MessagingException {
        String subject = commonService.getMessageFromDatabase("invoiceMailSubjectBilledToClient", new Object[]{projectName,invoiceId});
        String body = commonService.getMessageFromDatabase("invoiceMailBodyBilledToClient", new Object[]{invoiceId, projectName, clientName});
        String modifiedBody = commonService.getModifiedMessage(body, invoiceId);
        billedToClientTemplate(email,fullName,projectName, clientName, invoiceId, paymentStatus, subject, modifiedBody);
    }

    @Override
    public void sendInvoiceCreateMailPartiallyOrFullyPaid(String paymentStatus, String email,String fullName, String projectName, String clientName, String invoiceId) throws MessagingException {
        String subject = commonService.getMessageFromDatabase("invoiceMailSubjectFullyOrPartially", new Object[]{projectName,invoiceId});
        String body = commonService.getMessageFromDatabase("invoiceMailBodyFullyOrPartially", new Object[]{invoiceId, clientName, projectName, paymentStatus});
        String modifiedBody = commonService.getModifiedMessage(body, invoiceId);
        billedToClientTemplate(email,fullName, projectName, clientName, invoiceId, paymentStatus, subject, modifiedBody);
    }

    @Override
    public void sendInvoiceCreateMailVoidStatus(String email,String fullName, String projectName, String clientName, String invoiceId, String paymentStatus) throws MessagingException {
        String subject = commonService.getMessageFromDatabase("invoiceMailSubjectVoid", new Object[]{projectName,invoiceId});
        String body = commonService.getMessageFromDatabase("invoiceMailBodyVoid", new Object[]{invoiceId,clientName, projectName});
        String modifiedBody = commonService.getModifiedMessage(body, invoiceId);
        billedToClientTemplate(email,fullName, projectName, clientName, invoiceId, paymentStatus, subject, modifiedBody);
    }

    @Override
    public void sendReadyToBilledTOClientInvoice(String email, File file, String fullName, String invoiceId, String projectName, String clientName,String paymentStatus) throws MessagingException, FileNotFoundException {
        String subject = commonService.getMessageFromDatabase("invoiceMailSubjectReadyToBillToClient", new Object[]{projectName,invoiceId});
        String body = commonService.getMessageFromDatabase("invoiceMailBodyReadyToBillToClient", new Object[]{invoiceId,clientName, projectName});
        String modifiedBody = commonService.getModifiedMessage(body, invoiceId);
        billedToClientTemplateB(email, file, fullName, invoiceId, projectName, clientName, paymentStatus, subject, modifiedBody);
    }


    private void commonMailFormat(String email,String fullName, String projectName, String invoiceId, String clientName, String subject, String body) throws MessagingException {
        MailDTO mail = new MailDTO();
        mail.setMailSubject(subject);

        //replace to too accounts team email id
        mail.setMailToo(accountsTeamMail);

        mail.setMailCc(email);
        mail.setMailFrom(mailFrom);
        Map<String, Object> model = new HashMap<>();
        model.put("name", fullName);
        model.put("invoiceId",invoiceId);
        model.put("clientName",clientName);
        model.put("projectName",projectName);
        mail.setModel(model);

        mailClient.prepareAndSendB(mail, MailType.COMMON_MAIL_TEMPLATE);
    }
    private void commonMailFormat(String email,File file,String userName, String projectName,String oldInvoiceNumber, String updateInvoiceNumber, String subject, String body) throws MessagingException {
        MailDTO mail = new MailDTO();
        mail.setMailSubject(subject);

        //replace to too accounts team email id
        mail.setMailToo(accountsTeamMail);

        mail.setMailCc(email);
        mail.setMailFrom(mailFrom);
        Map<String, Object> model = new HashMap<>();
        model.put("userName", userName);
        model.put("oldInvoiceNumber",oldInvoiceNumber);
        model.put("updatedInvoiceNumber",updateInvoiceNumber);
        model.put("projectName",projectName);
        mail.setFileAttachment(file);
        mail.setFileName(updateInvoiceNumber);
        mail.setModel(model);

        mailClient.prepareAndSend(mail, MailType.INVOICE_NUMBER_UPDATE_MAIL_TEMPLATE);
    }

    private void billedToClientTemplate(String email,String fullName, String projectName, String clientName,String invoiceId, String paymentStatus, String subject, String body) throws MessagingException {
        MailDTO mail = new MailDTO();
        mail.setMailSubject(subject);
        mail.setMailToo(email);
        mail.setMailFrom(mailFrom);
        //replace cc too accounts team email id
        mail.setMailCc(accountsTeamMail);
        Map<String, Object> model = new HashMap<>();
        model.put("name", fullName);
        model.put("name", fullName);
        model.put("invoiceId",invoiceId);
        model.put("clientName",clientName);
        model.put("projectName",projectName);
        model.put("paymentStatus",paymentStatus);
        mail.setModel(model);

        if(paymentStatus.equals("1")) {
            mailClient.prepareAndSendB(mail, MailType.BILLED_TO_CLIENT_TEMPLATE);
        }else if(paymentStatus.equals("full") || paymentStatus.equals("partial")){
            mailClient.prepareAndSendB(mail, MailType.FULL_PAYMENT_TEMPLATE);
        }else if(paymentStatus.equals("5")){
            mailClient.prepareAndSendB(mail, MailType.VOID_TEMPLATE);

        }
    }

    private void billedToClientTemplateB(String email,File file, String fullName, String invoiceId, String projectName, String clientName,String paymentStatus, String subject, String body) throws MessagingException, FileNotFoundException {
        MailDTO mail = new MailDTO();
        mail.setMailSubject(subject);
        mail.setMailToo(email);
        mail.setMailFrom(mailFrom);
        mail.setFileAttachment(file);
        mail.setFileName(invoiceId);
        //replace cc too accounts team email id
        mail.setMailCc(accountsTeamMail);
        Map<String, Object> model = new HashMap<>();
        model.put("name", fullName);
        model.put("invoiceId",invoiceId);
        model.put("clientName",clientName);
        model.put("projectName",projectName);
        model.put("paymentStatus",paymentStatus);
        mail.setModel(model);

        mailClient.prepareAndSend(mail, MailType.READY_TO_BILL_TEMPLATE);
    }
}
