package com.management.clientinvoice.service;

import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.dto.OtpRequestDTO;
import com.management.clientinvoice.dto.UserDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.*;
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

