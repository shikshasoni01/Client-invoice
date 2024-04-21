package com.management.clientinvoice.controller;

import com.management.clientinvoice.constant.UrlConstant;
import com.management.clientinvoice.constant.WebConstants;
import com.management.clientinvoice.dto.OtpRequestDTO;
import com.management.clientinvoice.dto.UserDTO;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.requestWrapper.*;
import com.management.clientinvoice.service.ICommonService;
import com.management.clientinvoice.service.IOtpManagerService;
import com.management.clientinvoice.service.IPasswordManagerService;
import com.management.clientinvoice.util.ResponseFormatter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = UrlConstant.ADMIN_PASSWORD_CONTROLLER_PREFIX)
public class AdminPasswordManagerController {

    private static final Logger LOGGER = Logger.getLogger(AdminPasswordManagerController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private IPasswordManagerService passwordManagerService;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private IOtpManagerService otpManagerService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "deviceId", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceType", value = "deviceType", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "appVersion", value = "appVersion", required = true, dataType = "string", paramType = "header")
    })
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> changePassword(@Valid @RequestBody ChangePasswordRequestWrapper changePassword, @RequestHeader(value = "Accept-Language") String acceptLanguage
    ) throws Exception {

        LOGGER.info("change password Start");

        passwordManagerService.changePassword(changePassword, acceptLanguage);
        String message = commonService.getMessage("passwordChangedSuccessfully");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

        LOGGER.info("change password end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/reset/password", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> forgotPassword(@Valid @RequestBody ForgotPasswordRequestWrapper forgotPasswordRequest,
                                                     @RequestHeader(value = "Accept-Language") String acceptLanguage
    ) throws Exception {

        LOGGER.info("forgot password Start");

        passwordManagerService.forgotPassword(forgotPasswordRequest, acceptLanguage);
        String message = commonService.getMessage("forgotPasswordSuccess");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, null, true);

        LOGGER.info("forgot password end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/otp/requestOtp", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> requestOtp(
            @Valid @RequestBody RequestOtpRequestWrapper requestOtpRequestWrapper,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @RequestHeader(value = "deviceId") String deviceId,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "appVersion") String appVersion,
            @RequestHeader(value = "deviceName") String deviceName
    ) throws Exception {

        LOGGER.info("otp send Start");

        OtpRequestDTO otpRequestDTO = passwordManagerService.requestOtp(requestOtpRequestWrapper, acceptLanguage);
        String message = commonService.getMessage("otpSend");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, otpRequestDTO, true);

        LOGGER.info("otp send end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/otp/resendOtp", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> resendOtp(
            @Valid @RequestBody RequestOtpRequestWrapper requestOtpRequestWrapper,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @RequestHeader(value = "deviceId") String deviceId,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "appVersion") String appVersion,
            @RequestHeader(value = "deviceName") String deviceName
    ) throws Exception {

        LOGGER.info("resend otp Start");

        OtpRequestDTO otpRequestDTO = passwordManagerService.resendOtp(requestOtpRequestWrapper, acceptLanguage);
        String message = commonService.getMessage("otpResend");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, otpRequestDTO, true);

        LOGGER.info("resend otp end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/otp/verify", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> otpVerify(
            @Valid @RequestBody OtpVerifyRequesWrapper otpVerifyRequesWrapper,
            @RequestHeader(value = "Accept-Language") String acceptLanguage,
            @RequestHeader(value = "deviceId") String deviceId,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "appVersion") String appVersion,
            @RequestHeader(value = "deviceName") String deviceName
    ) throws Exception {

        LOGGER.info("otp verify Start");

        CommonRequestHeaders commonRequestHeaders = commonService.getCommonRequestHeaders();
        UserDTO loginDTO = passwordManagerService.otpVerify(otpVerifyRequesWrapper, acceptLanguage, commonRequestHeaders);

        if (loginDTO.isValid()) {
            String message = commonService.getMessage("otpVerified");
            JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, loginDTO, true);

            LOGGER.info("otp verify end");

            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            String message = commonService.getMessage("otpNotVerified");
            JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, message, loginDTO, true);

            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JSONObject> processValidationError(HttpServletRequest req, MethodArgumentNotValidException ex
    ) throws InvoiceManagementException {

        BindingResult result = ex.getBindingResult();
        String localizedErrorMessage = "";
        String lang = req.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);

        if (lang == null || lang.isEmpty()) {
            lang = "en";
        }

        if (!result.getAllErrors().isEmpty()) {
            localizedErrorMessage = result.getAllErrors().get(0).getDefaultMessage();
        }

        localizedErrorMessage = commonService.getMessage(localizedErrorMessage);
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, localizedErrorMessage);

        LOGGER.error(WebConstants.KEY_STATUS_ERROR + " 400 " + localizedErrorMessage);

        return new ResponseEntity<JSONObject>(data, HttpStatus.BAD_REQUEST);
    }
}

