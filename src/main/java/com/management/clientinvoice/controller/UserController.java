package com.biz4solutions.clientinvoice.controller;


import com.biz4solutions.clientinvoice.constant.UrlConstant;
import com.biz4solutions.clientinvoice.constant.WebConstants;
import com.biz4solutions.clientinvoice.dto.UserDTO;
import com.biz4solutions.clientinvoice.dto.UserDetailsDTO;
import com.biz4solutions.clientinvoice.dto.UserProfileImagesDTO;
import com.biz4solutions.clientinvoice.exception.InvoiceManagementException;
import com.biz4solutions.clientinvoice.requestWrapper.AddUserRequestWrapper;
import com.biz4solutions.clientinvoice.requestWrapper.UserPictureRequestWrapper;
import com.biz4solutions.clientinvoice.service.ICommonService;
import com.biz4solutions.clientinvoice.service.IUserService;
import com.biz4solutions.clientinvoice.util.ResponseFormatter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = UrlConstant.USER_CONTROLLER_PREFIX)
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ICommonService commonService;


    @RequestMapping(value = UrlConstant.CREATE_OPERATION, method = RequestMethod.POST)

    public ResponseEntity<JSONObject> createUser(@RequestBody AddUserRequestWrapper request,
                                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                     @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
    ) throws Exception {

        LOGGER.info("create user Start");

        String userId = userService.createUser(request, acceptLanguage, false);
        String message = messageSource.getMessage("userCreatedSuccessfully", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, userId);

        LOGGER.info("create user end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = UrlConstant.UPDATE_OPERATION, method = RequestMethod.PUT)
    public ResponseEntity<JSONObject> updateUserProfile(@Valid @RequestBody AddUserRequestWrapper updateUserProfileRequestWrapper,
                                                            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                            @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
    ) throws Exception {

        LOGGER.info("update user profile Start");

        UserDetailsDTO userDetailsDTO = userService.updateUserProfile(updateUserProfileRequestWrapper, acceptLanguage);
        String message = messageSource.getMessage("updateProfileSuccess", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, userDetailsDTO);

        LOGGER.info("update user profile end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

//    @RequestMapping(value = UrlConstant.LOGOUT_OPERATION, method = RequestMethod.GET)
//      public ResponseEntity<JSONObject> logoutUser(HttpServletRequest request,
//                                                       @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
//                                                       @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
//    ) throws InvoiceManagementException {
//
//        LOGGER.info("logout user Start");
//
//        userService.logout(acceptLanguage);
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null) {
//            String tokenValue = authHeader.replace("Bearer", "").trim();
//            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
//            tokenStore.removeAccessToken(accessToken);
//        }
//
//        String message = messageSource.getMessage("logoutSuccess", null, new Locale(acceptLanguage));
//        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);
//
//        LOGGER.info("logout user end");
//
//        return new ResponseEntity<>(data, HttpStatus.OK);
//    }

    @RequestMapping(value = UrlConstant.ACTIVATE_USER, method = RequestMethod.GET)
    public ResponseEntity<JSONObject> activateUser(@RequestParam("userId") String userId,
                                                       @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                       @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
    ) throws InvoiceManagementException {

        LOGGER.info("active user Start");

        userService.activateUser(userId, acceptLanguage);
        String message = messageSource.getMessage("userActivatedSuccessfully", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message);

        LOGGER.info("active user end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @RequestMapping(value = UrlConstant.VIEW_USER_PROFILE, method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "deviceId", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceType", value = "deviceType", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "appVersion", value = "appVersion", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<JSONObject> getUserProfile(@RequestParam Long userId,
                                                            @RequestHeader(value = "Accept-Language") String acceptLanguage
    ) throws InvoiceManagementException {

        UserDTO userDetails = userService.getUserProfile(userId, acceptLanguage);
        String message = commonService.getMessageFromDatabase("userDetailsRetrievedSuccessfully");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, userDetails);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @RequestMapping(value = UrlConstant.ADD_USER_PROFILE_IMAGE, method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "deviceId", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceType", value = "deviceType", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "appVersion", value = "appVersion", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<JSONObject> addUserProfileImages(@Valid @RequestBody UserPictureRequestWrapper pictureRequestWrapper,
                                                                @RequestHeader(value = "Accept-Language") String acceptLanguage
    ) throws Exception {

        LOGGER.info("add user profile image Start");

        String userId = userService.addUserImage(pictureRequestWrapper, acceptLanguage);
        String message = commonService.getMessage("updateProfileSuccess");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, userId);

        LOGGER.info("add user profile image end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = UrlConstant.DELETE_USER_PROFILE_IMAGE, method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "deviceId", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceType", value = "deviceType", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "appVersion", value = "appVersion", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<JSONObject> deleteUserProfileImages(@Valid @RequestBody UserPictureRequestWrapper pictureRequestWrapper,
                                                                  @RequestHeader(value = "Accept-Language") String acceptLanguage
    ) throws Exception {

        LOGGER.info("delete user profile image Start");

        String userId = userService.deleteUserImage(pictureRequestWrapper, acceptLanguage);
        String message = commonService.getMessage("updateProfileSuccess");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, userId);

        LOGGER.info("delete user profile image end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = UrlConstant.GET_USER_PROFILE_IMAGES, method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "deviceId", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceType", value = "deviceType", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "appVersion", value = "appVersion", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<JSONObject> getUserProfileImages(@RequestHeader(value = "Accept-Language") String acceptLanguage
    ) throws Exception {

        LOGGER.info("get user profile image start");

        UserProfileImagesDTO profileImagesDTO = userService.getUserImages(acceptLanguage);
        String message = commonService.getMessage("updateProfileSuccess");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, profileImagesDTO);

        LOGGER.info("get user profile image end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = UrlConstant.SET_USER_DEFAULT_IMAGE, method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceId", value = "deviceId", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "deviceType", value = "deviceType", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "appVersion", value = "appVersion", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<JSONObject> setdefaultImage(@Valid @RequestBody UserPictureRequestWrapper pictureRequestWrapper,
                                                         @RequestHeader(value = "Accept-Language") String acceptLanguage
    ) throws Exception {

        LOGGER.info("set default image start");

        String userId = userService.setDefaultImage(pictureRequestWrapper, acceptLanguage);
        String message = commonService.getMessage("updateProfileSuccess");
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, userId);

        LOGGER.info("set default image end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getAllUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
                                                     @RequestHeader(value = WebConstants.HEADER_KEY_ACCEPT_LANGUAGE) String acceptLanguage
    ) throws InvoiceManagementException {

        LOGGER.info("get all User Start");

        List<UserDetailsDTO> userDetailsDTOS = userService.getAllUser();
        String message = messageSource.getMessage("listRetrievedSuccessfully", null, new Locale(acceptLanguage));
        JSONObject data = ResponseFormatter.formatter(WebConstants.KEY_STATUS_SUCCESS, 200, message, userDetailsDTOS);

        LOGGER.info("get all user end");

        return new ResponseEntity<>(data, HttpStatus.OK);
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
//        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
//    }

}