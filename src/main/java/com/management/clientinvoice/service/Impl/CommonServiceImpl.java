package com.management.clientinvoice.service.Impl;


import com.management.clientinvoice.constant.WebConstants;
import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.UserIdentityRepository;
import com.management.clientinvoice.requestWrapper.CommonRequestHeaders;
import com.management.clientinvoice.service.ICommonService;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class CommonServiceImpl implements ICommonService {

  //  private static final Logger LOGGER = Logger.getLogger(com.biz4solutions.clientinvoice.serviceImpl.CommonServiceImpl.class);
//

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserIdentityRepository userIdentityRepository;


    @Override
    public String getMessageFromDatabase(String messageKey) {
        String acceptLanguage = getLanguageCode(httpServletRequest);

        String message = null;


        if (StringUtils.isEmpty(message)) {
            try {
                //message = getMessage(messageKey);
                message = messageSource.getMessage(messageKey, null, new Locale("en"));
            } catch (Exception e) {
                message = messageSource.getMessage("commonErrorMessage", null, new Locale(acceptLanguage));
            }
        }
        return message;
    }

    @Override
    public String getMessageFromDatabase(String messageKey, Object[] params) {
        String acceptLanguage = getLanguageCode(httpServletRequest);

        String message = null;

        if (StringUtils.isEmpty(message)) {
            try {
                message = messageSource.getMessage(messageKey, params,  new Locale("en"));
            } catch (Exception e) {
                message = messageSource.getMessage("commonErrorMessage", null, new Locale(acceptLanguage));
            }
        }
        return message;
    }




    @Override
    public String getMessage(String msgConstant, String lang) {
        return null;
    }

    @Override
    public String getMessage(final String msgConstant) {
        return getMessage(msgConstant, null, getLanguageCode(httpServletRequest));
    }

    @Override
    public String getMessage(String msgConstant, Object[] params, String lang) {
        return null;
    }

    @Override
    public String getMessage(final String msgConstant, final Object[] params) {
        return getMessage(msgConstant, params, getLanguageCode(httpServletRequest));
    }

    @Override
    public String getMessage(final String msgConstant, final Object[] params, boolean parseParam) {

        if (parseParam) {
            for (Object ob : params) {
                if (null != ob) {
                    ob = getMessage(ob.toString());
                }
            }
        }

//		return getMessage(msgConstant, params, httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE));
        return getMessage(msgConstant, params, "en");
    }

    @Override
    public ExecutorService getExecutorService() {
        ExecutorService service = Executors.newFixedThreadPool(50);
        return service;
    }

    @Override
    public String getLanguageCode() {
        return null;
    }

    @Override
    public String getLanguageCode(HttpServletRequest req) {
        return null;
    }

    @Override
    public UserIdentity getLoggedInUserIdentity() throws InvoiceManagementException {
        return getLoggedInUserIdentity(httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE));
    }

    @Override
    public UserIdentity getLoggedInUserIdentity(String acceptLanguage) throws InvoiceManagementException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (TextUtils.isEmpty(acceptLanguage)) {
            acceptLanguage = httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);
        }
        if (authentication == null) {
            throw new InvoiceManagementException(messageSource.getMessage("authenticationFailed", null, new Locale(acceptLanguage)), 401);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails.getUsername() != null) {
            UserIdentity loggedInUser = userIdentityRepository.findTop1ByEmailIgnoreCase(userDetails.getUsername());
          //  UserIdentity loggedInUser = userIdentityRepository.findOneByUniqueId(userDetails.getUsername());
            if (null != loggedInUser && loggedInUser.getIsBlocked() != null && loggedInUser.getIsBlocked()) {
                throw new InvoiceManagementException(getMessage("blockedAccount"), 406);
            }
            return loggedInUser;
        }
        return null;
    }

    @Override
    public UserIdentity getLoggedInAdmin(String acceptLanguage) throws InvoiceManagementException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (TextUtils.isEmpty(acceptLanguage)) {
            acceptLanguage = httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);
        }
        if (authentication == null) {
            throw new InvoiceManagementException(messageSource.getMessage("authenticationFailed", null, new Locale(acceptLanguage)), 401);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails.getUsername() != null) {
            UserIdentity loggedInUser = userIdentityRepository.findTop1ByEmailIgnoreCase(userDetails.getUsername());
            // UserIdentity loggedInUser = userIdentityRepository.findOneByUniqueId(userDetails.getUsername());
            if (null != loggedInUser && loggedInUser.getIsBlocked() != null && loggedInUser.getIsBlocked()) {
                throw new InvoiceManagementException(getMessage("blockedAccount"), 406);
            }
            return loggedInUser;
        } else {
            throw new InvoiceManagementException(messageSource.getMessage("authenticationFailed", null, new Locale(acceptLanguage)), 401);
        }
    }

    @Override
    public boolean isLoggedInUser(UserIdentity userIdentity) {
        try {
            UserIdentity loggedInUser = getLoggedInUserIdentity(getLanguageCode());

            if (loggedInUser != null && userIdentity != null) {
                if (userIdentity.getId().toString().equalsIgnoreCase(loggedInUser.getId().toString())) {
                    return true;
                }
            }
        } catch (InvoiceManagementException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getDeviceType() {
        return httpServletRequest.getHeader(WebConstants.HEADER_KEY_DEVICE_TYPE);
    }

    @Override
    public String getIsWeb() {
        String isWeb = httpServletRequest.getHeader(WebConstants.HEADER_KEY_IS_WEB);
        if (TextUtils.isEmpty(isWeb)) {
            return "false";
        }
        return isWeb;
    }

    public CommonRequestHeaders getCommonRequestHeaders() {
        CommonRequestHeaders commonRequestHeaders = new CommonRequestHeaders();
        commonRequestHeaders.setAppVersion(httpServletRequest.getHeader(WebConstants.HEADER_KEY_APP_VERSION));
        commonRequestHeaders.setDeviceId(httpServletRequest.getHeader(WebConstants.HEADER_KEY_DEVICE_ID));
       // commonRequestHeaders.setDeviceName(httpServletRequest.getHeader(WebConstants.HEADER_KEY_DEVICE_NAME));
        commonRequestHeaders.setDeviceType(httpServletRequest.getHeader(WebConstants.HEADER_KEY_DEVICE_TYPE));
        commonRequestHeaders.setLanguage(httpServletRequest.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE));

        return commonRequestHeaders;
    }


    @Override
    public String getModifiedMessage(String body, String jobId) {
        if (body.contains("{0}")) {
            return body.replace("{0}", jobId);
        }
        return body;
    }

}
