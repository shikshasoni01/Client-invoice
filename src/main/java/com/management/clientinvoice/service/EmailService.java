package com.management.clientinvoice.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;

@Service
public interface EmailService {

    void sendInvoiceCreateMailPending(String email, String fullName, String projectName, String clientName, String invoiceId) throws MessagingException;

    void sendUpdateInvoiceNumberCreateMail(String email,File file, String projectName, String userName, String oldInvoiceNumber,String updatedInvoiceNumber) throws MessagingException;

    void sendInvoiceCreateMailBilledToClient(String email,String fullName, String projectName, String clientName, String invoiceId, String paymentStatus) throws MessagingException;

    void sendInvoiceCreateMailPartiallyOrFullyPaid( String email,String fullName, String projectName, String clientName, String invoiceId, String paymentStatus) throws MessagingException;

    void sendInvoiceCreateMailVoidStatus(String email,String fullName, String projectName, String clientName, String invoiceId, String paymentStatus) throws MessagingException;

    void sendReadyToBilledTOClientInvoice(String email, File file, String fullName, String invoiceId, String projectName, String clientName, String paymentStatus) throws MessagingException, FileNotFoundException;
}
