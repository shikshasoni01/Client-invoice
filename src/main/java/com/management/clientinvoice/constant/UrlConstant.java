package com.management.clientinvoice.constant;


public class UrlConstant {

    public static final String SPONSOR_PERMISSION_CONTROLLER_PREFIX = "/api/v1/sponsor/permission";
    public static final String ADD_OPERATION = "/add";
    public static final String UPDATE_OPERATION = "/update";

    public static final String LIST_OPERATION = "/list";

    public static final String DELETE_OPERATION = "/delete";

    public static final String GET_OPERATION = "/get";

    public static final String DETAILS_OPERATION = "/details";
    public static final String CREATE_OPERATION = "/create";

    public static final String FIREBASE_CONTROLLER_PREFIX = "/api/v1/firebase";
    public static final String USER_CONTROLLER_PREFIX = "/api/v1/user";

    public static final String PUSH_NOTIFICATION_CONTROLLER_PREFIX = "/api/v1/users/notification";

    public static final String SAVE_FIREBASE_TOKEN = "/token/save";
    public static final String LOGOUT_OPERATION = "/logout";

    public static final String DEMO_MESSAGE = "/fcm/demoMessage";

    public static final String DEMO_CANCEL_MESSAGE = "/fcm/cancel/demoMessage";
    public static final String ACTIVATE_USER = "/activate";

    public static final String VIEW_MY_PROFILE = "/view/myProfile";
    public static final String VIEW_USER_PROFILE = "/view/userProfile";

    public static final String UPDATE_NOTIFICATION_SETTING = "/setting/notification/update";

    //path to save the files to download the zip or a file........
    public static final String MAIN_DIRECTORY_PATH = "temp/";
    public static final String ADD_USER_PROFILE_IMAGE = "/add/userProfileImage";
    public static final String DELETE_USER_PROFILE_IMAGE = "/delete/userProfileImage";
    public static final String GET_USER_PROFILE_IMAGES = "/get/userProfileImages";
    public static final String SET_USER_DEFAULT_IMAGE = "/update/setDefaultImage";

    public static final String USER_LIST_INFO = "/list/info";

    public static final String USER_SUBSCRIPTION_CONTROLLER_PREFIX = "/api/v1/user/subscription";

    public static final String ADD_PLAN = "/plan/add";

    public static final String AUTORENEW_PLAN = "/plan/autorenew/{id}/{isAutorenew}";

    public static final String MY_PLAN = "/myCurrentPlan";

    public static final String OTHER_PLANS = "/other/plans";
    public static final String ADMIN_PASSWORD_CONTROLLER_PREFIX ="/api/v1/admin" ;
}


