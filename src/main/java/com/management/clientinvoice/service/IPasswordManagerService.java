package com.biz4solutions.clientinvoice.service;

import com.biz4solutions.clientinvoice.domain.UserIdentity;
import com.biz4solutions.clientinvoice.dto.OtpRequestDTO;
import com.biz4solutions.clientinvoice.dto.UserDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.*;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@Service
public interface IPasswordManagerService {

    void changePassword(ChangePasswordRequestWrapper changePasswordRequestWrapper, String lang) throws InvoiceManagementException;

    boolean forgotPassword(ForgotPasswordRequestWrapper forgotPaaswordRequest, String lang) throws NoSuchMessageException, InvoiceManagementException;

    void resetPassword(ResetPasswordRequestWrapper resetPassword, String acceptLanguage) throws Exception;

    OtpRequestDTO requestOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException;

    OtpRequestDTO resendOtp(RequestOtpRequestWrapper requestOtpRequestWrapper, String acceptLanguage) throws NoSuchMessageException, InvoiceManagementException, MessagingException;

    UserDTO otpVerify(OtpVerifyRequesWrapper otpVerifyRequesWrapper, String acceptLanguage, CommonRequestHeaders commonRequestHeaders) throws InvoiceManagementException;


    @Transactional
    void deleteOtpTransactionsAndOtp(UserIdentity userIdentity) throws InvoiceManagementException;
}

