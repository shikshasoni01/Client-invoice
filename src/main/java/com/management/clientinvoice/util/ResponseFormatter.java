package com.biz4solutions.clientinvoice.util;



import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.CountryRequestWrapper;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class ResponseFormatter extends ResponseEntityExceptionHandler {
    private ResponseFormatter(){}

    public static JSONObject formatter(String status, int code, String msg, Object data) throws InvoiceManagementException {
//		CommonUtil.checkMultipleDeviceLogin();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", msg);
        jsonObject.put("data", data);
        jsonObject.put("status", status);
        jsonObject.put("code", code);
        return jsonObject;
    }

    public static JSONObject formatter(String status, int code, String msg) throws InvoiceManagementException {
//		CommonUtil.checkMultipleDeviceLogin();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", msg);
        jsonObject.put("data", new JSONObject());
        jsonObject.put("status", status);
        jsonObject.put("code", code);
        return jsonObject;
    }

    public static JSONObject formatter(String status, int code, String msg, int forMultipleDeviceLogin) throws InvoiceManagementException {
        // Only forMultipleDeviceLogin
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", msg);
        jsonObject.put("data", new JSONObject());
        jsonObject.put("status", status);
        jsonObject.put("code", code);

        return jsonObject;
    }

    public static JSONObject errorHandler(String status, int code, BindingResult result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", result.getAllErrors().get(0).getDefaultMessage());
        jsonObject.put("data",new JSONObject());
        jsonObject.put("status", status);
        jsonObject.put("code", code);
        return jsonObject;
    }

    public static JSONObject formatter(String status, int code, String msg, Object data, boolean isCallFromNotAuthenticated) {
        //Handling for not authenticated apis
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", msg);
        jsonObject.put("data", data);
        jsonObject.put("status", status);
        jsonObject.put("code", code);
        return jsonObject;
    }




}

