package com.management.clientinvoice.util;

import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.SignupRequestWrapper;
import lombok.NoArgsConstructor;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Pattern;


@Component
public class ValidationUtil {

    @Autowired
    private MessageSource messageSource;

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "/^\\s*(([^<>()[\\]\\\\.,;:\\s@\\']+(\\.[^<>()[\\]\\\\.,;:\\s@\\']+)*)|(\\'.+\\'))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\\s*$/)";
        if (TextUtils.isEmpty(email) || email.length() > 50) {
            return false;
        }
        return validatePattern(ePattern, email);
    }

    public static boolean validatePattern(String ePattern, String filedToValidate) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(filedToValidate);
        return m.matches();
    }

    public void validatePassword(String password, String acceptLanguage) {
        if (TextUtils.isEmpty(password) || password.length() > 50) {
            throw new InvoiceManagementException(
                    messageSource.getMessage("passwordError", null, new Locale(acceptLanguage)), 400);
        }

        Pattern pattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[#?!@$%^&*])[A-Za-z\\d#?!@$%^&*].{7,24}$");
        if (!pattern.matcher(password).matches()) {
            throw new InvoiceManagementException(
                    messageSource.getMessage("passwordError", null, new Locale(acceptLanguage)), 400);
        }
    }

    public void validateUserDetails(SignupRequestWrapper request, String acceptLanguage) {
        validatePassword(request.getAuthPassword(), acceptLanguage);

    }
}
