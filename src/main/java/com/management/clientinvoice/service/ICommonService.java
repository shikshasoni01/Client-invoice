package com.biz4solutions.clientinvoice.service;



import com.biz4solutions.clientinvoice.domain.UserIdentity;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.CommonRequestHeaders;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;

@Service
public interface ICommonService {

    String getMessage(final String msgConstant, final String lang);

    String getMessage(final String msgConstant);

    String getMessage(final String msgConstant, final Object[] params, final String lang);

    String getMessage(final String msgConstant, final Object[] params);

    String getMessage(final String msgConstant, final Object[] params, boolean parseParam);

    ExecutorService getExecutorService();

    String getLanguageCode();

    String getLanguageCode(HttpServletRequest req);

    UserIdentity getLoggedInUserIdentity() throws InvoiceManagementException;

    UserIdentity getLoggedInUserIdentity(String acceptLanguage) throws InvoiceManagementException;

    UserIdentity getLoggedInAdmin(String acceptLanguage) throws InvoiceManagementException;

    boolean isLoggedInUser(UserIdentity userIdentity);

    String getDeviceType();

    String getIsWeb();

    CommonRequestHeaders getCommonRequestHeaders();

    String getMessageFromDatabase(String messageKey);

    String getMessageFromDatabase(String messageKey, final Object[] params);

    String getModifiedMessage(String body, String invoiceId);


}
