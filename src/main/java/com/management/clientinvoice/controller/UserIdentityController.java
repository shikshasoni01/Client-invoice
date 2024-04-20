package com.biz4solutions.clientinvoice.controller;

import com.biz4solutions.clientinvoice.constant.WebConstants;
import com.biz4solutions.clientinvoice.dto.UserDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.SignupBaseRequestWrapper;
import com.biz4solutions.clientinvoice.service.ICommonService;
import com.biz4solutions.clientinvoice.service.IUserIdentityService;
import com.biz4solutions.clientinvoice.service.IUserService;
import com.biz4solutions.clientinvoice.service.Impl.UserIdentityValidator;
import com.biz4solutions.clientinvoice.util.ResponseFormatter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserIdentityController {

    private static final Logger LOGGER = Logger.getLogger(UserIdentityController.class);

    @Autowired
    private IUserIdentityService  userIdentityService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private IUserService userService;


    @Autowired
    private UserIdentityValidator userIdentityValidator;


    /**
     * complete code for signup
     * @param acceptLanguage
     * @param deviceId
     * @param deviceType
     * @param appVersion
     * @param deviceName
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> signup(@RequestHeader(value = "Accept-Language") String acceptLanguage,
                                                 @RequestHeader(value = "deviceId") String deviceId,
                                                 @RequestHeader(value = "deviceType") String deviceType,
                                                 @RequestHeader(value = "appVersion") String appVersion,
                                                 @RequestHeader(value = "deviceName") String deviceName,
                                                 @Valid @RequestBody SignupBaseRequestWrapper request
    ) throws Exception {

        String message = "";

        LOGGER.info("signup Start");

        UserDTO userDTO = userIdentityService.signup(request, acceptLanguage);
        message = messageSource.getMessage("signupSuccess", null, new Locale(acceptLanguage));;
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, userDTO);
        //data.put("token", userDTO.getToken());

        LOGGER.info("signup end");

        return new ResponseEntity<>(data, HttpStatus.OK);

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    /**
     *  logout flow
     * @param request
     * @param acceptLanguage
     * @return
     * @throws InvoiceManagementException
     */

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public ResponseEntity<JSONObject> logout(HttpServletRequest request,
                                                  @RequestHeader(value = "Accept-Language") String acceptLanguage,
                                                  @RequestHeader(value = "deviceId") String deviceId,
                                                  @RequestHeader(value = "deviceType") String deviceType,
                                                  @RequestHeader(value = "appVersion") String appVersion,
                                                  @RequestHeader(value = "deviceName") String deviceName,
                                                  @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
    ) throws InvoiceManagementException {

        LOGGER.info("logout start");

        userService.logout(acceptLanguage);
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);

        }

        String message = commonService.getMessage("logoutSuccess");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

        LOGGER.info("logout end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }



    /**
     * verify email method for signup
     * @param code
     * @return
     */

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userIdentityService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<JSONObject> processValidationError(HttpServletRequest req, MethodArgumentNotValidException ex
//    ) throws  InvoiceManagementException {
//
//        BindingResult result = ex.getBindingResult();
//        String localizedErrorMessage = "";
//        String lang = req.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);
//
//        if (lang == null || lang.isEmpty()) {
//            lang = "en";
//        }
//
//        if (!result.getAllErrors().isEmpty()) {
//            localizedErrorMessage = result.getAllErrors().get(0).getDefaultMessage();
//
//        }
//
//        localizedErrorMessage = messageSource.getMessage(localizedErrorMessage, null, new Locale(lang));
//        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_ERROR, 500, localizedErrorMessage);
//
//        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//    }


}

