package com.management.clientinvoice.exception;

import com.management.clientinvoice.constant.WebConstants;
import com.management.clientinvoice.util.ResponseFormatter;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.util.Locale;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {


    private final static Logger logger = Logger.getLogger(ExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<JSONObject> handleMyOwnException(ConstraintViolationException err) throws JSONException, InvoiceManagementException {
        String msg = "";
        for (ConstraintViolation<?> objectError : err.getConstraintViolations()) {
            msg = objectError.getMessage();
            break;
        }
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, msg);
        logger.error(WebConstants.KEY_STATUS_ERROR + " 400 " + msg);
        return new ResponseEntity<JSONObject>(data, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<JSONObject> handleMyOwnException(HttpServletRequest req, DataIntegrityViolationException err) throws JSONException, InvoiceManagementException {
        String lang = req.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);
        if (lang == null || lang.isEmpty()) {
            lang = "en";
        }
        String msg = err.getMostSpecificCause().getMessage();
        if (msg.contains("user") && msg.contains("unique")) {
            msg = messageSource.getMessage("userNameAlreadyExist", null, new Locale(lang));
        } else if (msg.contains("email") && msg.contains("unique")) {
            msg = messageSource.getMessage("emailAlreadyExist", null, new Locale(lang));
        } else if (msg.contains("contact") && msg.contains("unique")) {
            msg = messageSource.getMessage("phoneAlreadyExist", null, new Locale(lang));
        }else if (msg.contains("clientId") && msg.contains("unique")) {
            msg = messageSource.getMessage("clientIdAlreadyExist", null, new Locale(lang));
        }  else {
            msg = "Oops! Something went wrong. Please try again later.";
        }
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, msg);
        logger.error(WebConstants.KEY_STATUS_ERROR + " 400 " + msg);
        return new ResponseEntity<JSONObject>(data, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<JSONObject> handleMyOwnException(TransactionSystemException e) throws JSONException, InvoiceManagementException {
        String msg = "";
        if (e.getCause().getCause() != null && e.getCause().getCause().getMessage().contains("constraint")) {
            ConstraintViolationException err = (ConstraintViolationException) e.getCause().getCause();
            for (ConstraintViolation<?> objectError : err.getConstraintViolations()) {
                msg = objectError.getMessage();
                break;
            }
        }
        e.getCause();
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, msg);
        logger.error(WebConstants.KEY_STATUS_ERROR + " 400 " + msg);
        return new ResponseEntity<JSONObject>(data, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<JSONObject> handleMyOwnException(AccessDeniedException e) throws JSONException, InvoiceManagementException {
        e.printStackTrace();
        String msg = "Access denied. You do not have permission to perform this action or access this resource.";
        int code = 403;
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, code, msg);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    // added by sagar to handle exception

    @org.springframework.web.bind.annotation.ExceptionHandler( org.springframework.mail.MailException.class)
    public ResponseEntity<JSONObject> handleMyOwnException1( org.springframework.mail.MailException e) throws JSONException, InvoiceManagementException {
        String msg = "";
        int code = 500;

            msg = "email did not send to account and manager"; //exception
            code = 200;

        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, code, msg);
        logger.error(WebConstants.KEY_STATUS_ERROR + " " + code + " " + msg);
        e.printStackTrace();
        return new ResponseEntity<JSONObject>(data, HttpStatus.OK);
    }



    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<JSONObject> handleMyOwnException(Exception e) throws JSONException, InvoiceManagementException {
        String msg = "";
        int code = 500;
        if (e.getClass().getName().equals("com.stripe.exception.APIException") || e.getClass().getName().equals("com.stripe.exception.InvalidRequestException") || e.getClass().getName().equals("com.stripe.exception.APIConnectionException") || e.getClass().getName().equals("com.stripe.exception.CardException") || e.getClass().getName().equals("com.stripe.exception.AuthenticationException")) {
            msg = e.getMessage();
        } else {
            msg = "Oops! Something went wrong. Please try again later."; //exception
            code = 500;
        }
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, code, msg);
        logger.error(WebConstants.KEY_STATUS_ERROR + " " + code + " " + msg);
        e.printStackTrace();
        return new ResponseEntity<JSONObject>(data, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ParseException.class)
    public ResponseEntity<JSONObject> handleMyOwnException(ParseException e) throws JSONException, InvoiceManagementException {
        String msg = "";
        int code = 500;
        if (e.getClass().getName().equals("java.text.ParseException")) {
            msg = e.getMessage();
        } else {
            msg = "Oops! Something went wrong. Please try again later."; //exception
            code = 500;
        }
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, code, msg);
        logger.error(WebConstants.KEY_STATUS_ERROR + " " + code + " " + msg);
        return new ResponseEntity<JSONObject>(data, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvoiceManagementException.class)
    public ResponseEntity<JSONObject> handleMyOwnException(InvoiceManagementException e) throws JSONException, InvoiceManagementException {
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, e.getErrCode(), e.getMessage(), 0);
        logger.error(WebConstants.KEY_STATUS_ERROR + " " + e.getErrCode() + " " + e.getMessage());
        if (e.getMessage().equals("You are logged in from another device. You are logged out from this device.")) {
            return new ResponseEntity<>(data, HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);

        }
    }
}
