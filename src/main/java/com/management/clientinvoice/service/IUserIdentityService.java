package com.management.clientinvoice.service;


import com.management.clientinvoice.dto.UserDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.SignupBaseRequestWrapper;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Service
public interface IUserIdentityService {

    UserDTO signup(@Valid SignupBaseRequestWrapper request, String acceptLanguage) throws InvoiceManagementException, MessagingException, javax.mail.MessagingException, UnsupportedEncodingException;

     boolean verify(String verificationCode);
}
