package com.management.clientinvoice.service;

import com.management.clientinvoice.dto.OtpRequestDTO;
import com.management.clientinvoice.dto.UserDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.CommonRequestHeaders;
import com.management.clientinvoice.requestWrapper.OtpVerifyRequesWrapper;
import com.management.clientinvoice.requestWrapper.RequestOtpRequestWrapper;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface IOtpManagerService {

    OtpRequestDTO requestOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException;

    OtpRequestDTO resendOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException;

    UserDTO otpVerify(OtpVerifyRequesWrapper otpVerifyRequesWrapper, String acceptLanguage, CommonRequestHeaders commonRequestHeaders) throws InvoiceManagementException;
}
