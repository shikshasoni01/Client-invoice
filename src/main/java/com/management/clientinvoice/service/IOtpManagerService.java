package com.biz4solutions.clientinvoice.service;

import com.biz4solutions.clientinvoice.dto.OtpRequestDTO;
import com.biz4solutions.clientinvoice.dto.UserDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.CommonRequestHeaders;
import com.biz4solutions.clientinvoice.requestWrapper.OtpVerifyRequesWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.RequestOtpRequestWrapper;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface IOtpManagerService {

    OtpRequestDTO requestOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException;

    OtpRequestDTO resendOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException;

    UserDTO otpVerify(OtpVerifyRequesWrapper otpVerifyRequesWrapper, String acceptLanguage, CommonRequestHeaders commonRequestHeaders) throws InvoiceManagementException;
}
