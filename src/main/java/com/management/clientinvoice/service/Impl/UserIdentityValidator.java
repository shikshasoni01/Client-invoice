package com.management.clientinvoice.service.Impl;

import com.management.clientinvoice.constant.WebConstants;
import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.UserIdentityRepository;
import com.management.clientinvoice.repository.UserProfileRepository;
import com.management.clientinvoice.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class UserIdentityValidator {


    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ICommonService commonService;

    public void checkForUniqueEmail(String email) throws InvoiceManagementException{
        UserIdentity userIdentity = userIdentityRepository.findOneByEmailIgnoreCase(email);
        if (null != userIdentity) {
            throw new InvoiceManagementException(messageSource.getMessage("emailAlreadyExist", null, new Locale(request.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE))), 400);
        }
    }

    public void checkForUniqueContactNo(String contactNo) throws InvoiceManagementException {
        UserIdentity userIdentity = userIdentityRepository.findOneByContactNo(contactNo);
        if (null != userIdentity) {
            throw new InvoiceManagementException(messageSource.getMessage("phoneAlreadyExist", null, new Locale(request.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE))), 400);
        }
    }
}

