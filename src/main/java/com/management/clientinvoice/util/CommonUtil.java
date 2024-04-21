package com.management.clientinvoice.util;

import com.management.clientinvoice.config.SpringContext;
import com.management.clientinvoice.domain.UserIdentity;
import com.management.clientinvoice.domain.UserProfile;
import com.management.clientinvoice.dto.Pair;
import com.management.clientinvoice.exception.InvoiceManagementException;
import com.management.clientinvoice.repository.UserIdentityRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CommonUtil {

    static final long ONE_MINUTE_IN_MILLIS = 60000;

    @Autowired
    private static HttpServletRequest httpServletRequest;

    private CommonUtil() {
    }

    public static String getUniqueToken() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public static String getUniqueOtp() {
        Random random = new SecureRandom();
        String id = String.format("%06d", random.nextInt(1000000));
        if (id.length() != 6) {
            getUniqueOtp();
        }
        return id;
    }

    public static List<Pair<String, Object>> extractPairsFromJSONObject(JSONObject jsonObject) {
        List<Pair<String, Object>> pair = new ArrayList<>();
        jsonObject.keys().forEachRemaining(key -> {
            Object value = jsonObject.get(key);
            pair.add(new Pair<>(key, value, true));
        });
        return pair;
    }

    public static String convertToJsonString(List<Pair<String, Object>> pairs) {
        StringBuilder sb = new StringBuilder();
        if (null != pairs && pairs.size() > 0) {
            sb.append("{");
            int len = pairs.size();
            for (Pair<String, Object> pair : pairs) {
                sb.append(pair.getKey()).append(":").append(pair.getValue());
                if (--len > 0) {
                    sb.append(",");
                }
            }
            sb.append("}");
        }
        return sb.toString();
    }


    public static Date getTimeAfterMinutes(int minutes) {
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return new Date(t + (minutes * ONE_MINUTE_IN_MILLIS));
    }



    public static final UUID getUUIDFromString(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id.trim());
        } catch (Exception e) {
            return null;
        }
        return uuid;
    }

//    public static Date getTimeAfterMinutes(int minutes) {
//        Calendar date = Calendar.getInstance();
//        long t = date.getTimeInMillis();
//        return new Date(t + (minutes * ONE_MINUTE_IN_MILLIS));
//    }
//
//    public static long convertDollarsToCents(Double dollars) {
//        if (null == dollars || 0D >= dollars) {
//            throw new IllegalArgumentException("Amount should be greater than 0");
//        }
//        //return dollars.longValue() * 100L;
//        return (BigDecimal.valueOf(dollars).multiply(WebConstant.bigDecimalValueOfHundred)).longValue();
//    }

    public static UUID getUUIDFromStringIfValidElseNull(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id.trim());
        } catch (Exception e) {
            return null;
        }
        return uuid;
    }

    public static List<UUID> getUUIDListFromStringList(List<String> stringList) {
        List<UUID> uuids = new ArrayList<>();
        if (stringList != null && !stringList.isEmpty()) {
            for (String id : stringList) {
                uuids.add(getUUIDFromStringIfValidElseNull(id));
            }
        }
        return uuids;
    }

//    public static void sendMailUsingCommonTemplate(String email, String receiverFullName,
//                                                   String subject, String message, String senderName) {
//        MailDTO mail = new MailDTO();
//        String[] toEmail = new String[1];
//        toEmail[0] = email;
//
//        mail.setMailTo(toEmail);
//        mail.setMailSubject(subject);
//        Map<String, Object> model = new HashMap<>();
//        model.put("receiverName", receiverFullName);
//        model.put("message", message);
//        model.put("senderName", senderName);
//        mail.setModel(model);
//        SpringContext.getBean(MailClient.class).prepareAndSend(mail, MailType.COMMON_MAIL_TEMPLATE);
//    }

    public static String getFullNameFromUserIdentity(UserIdentity userIdentity) {

        StringBuilder userName = new StringBuilder();
        if (null != userIdentity) {

            if (!TextUtils.isEmpty(userIdentity.getFirstName())) {
                userName.append(userIdentity.getFirstName());
            }
            if (!TextUtils.isEmpty(userIdentity.getLastName())) {
                userName.append(" " + userIdentity.getLastName());
            }
        }
        return userName.toString();
    }

    public static <T> Object invokeMethod(String methodName, T type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        type.getClass().getMethod(methodName).setAccessible(true);
        return type.getClass().getMethod(methodName).invoke(type);
    }

    public static int getAgeFromBirthDate(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        Period period;
        try {
            period = Period.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    LocalDate.now());
        } catch (Exception e) {
            period = Period.between(LocalDate.parse(birthDate.toString()),
                    LocalDate.now());
        }
        return period.getYears();
    }

    public static String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return RandomStringUtils.random(len, chars);
    }

    public static boolean listContainsIgnoreCase(List<String> list, String value) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        for (String listValue : list) {
            if (listValue.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public static Date getDateMinusYears(Integer years) {
        years *= -1;

        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, years);
        return now.getTime();
    }

    public static Date getDateFromDaysProvided(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static Date getDayEndTime(Date date) {
        LocalDateTime ldt = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX);
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        return out;
    }

    public static UserProfile getUserProfileOfLoggedInUser() throws InvoiceManagementException {
        // Used for OAuth2
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            // throw new VideoCallException("authenticationFailed", true, 401);
            throw new InvoiceManagementException();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails.getUsername() != null) {
            UserIdentity userIdentity = SpringContext.getBean(UserIdentityRepository.class).findOneByUniqueId(userDetails.getUsername());
            if (userIdentity != null) {
                return userIdentity.getUserProfile();
            }
        }
        return null;
    }

    public static boolean validateHeaderForValidLoginDevice(UserIdentity userIdentity, String deviceType, String deviceId, String deviceName) {

        return (deviceId.equals(userIdentity.getLastLoginDeviceId())
                && deviceType.equals(userIdentity.getLastLoginDeviceType())
                && deviceName.equals(userIdentity.getLastLoginDeviceToken()));

    }

    public static UUID getUUIDIfValidElseNull(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id.trim());
        } catch (Exception e) {
            return null;
        }
        return uuid;
    }

//    public static org.locationtech.jts.geom.Point convertLongitudeLatitudeToCoordinates(Double longitude, Double latitude) {
//        /*Point coordinate = null;
//
//        if ((null != longitude && null != latitude) && (longitude >= -180 && longitude <= 180) && (latitude >= -90 && latitude <= 90)) {
//            coordinate = Geometries.mkPoint(new G2D(longitude, latitude), CoordinateReferenceSystems.WGS84);
//        }
//
//        return coordinate;*/
//
//        if (latitude != null && longitude != null) {
//            GeometryFactory geometryFactory = new GeometryFactory();
//            Coordinate coordinate = new Coordinate(latitude, longitude);
//            org.locationtech.jts.geom.Point point = geometryFactory.createPoint(coordinate);
//            point.setSRID(4326);
//
//            return point;
//        }
//        return null;
//    }

//    public static boolean checkConnectedAccountType(String connectedAccountType) {
//        return Arrays.asList(getConnectedAccountTypes()).contains(connectedAccountType);
//    }

//    public static String[] getConnectedAccountTypes() {
//        return new String[]{ConnectedAccountType.INDIVIDUAL.getConnectedAccountType(), ConnectedAccountType.COMPANY.getConnectedAccountType()};
//    }
//
//    public static ActionUserType getActionUserType(String actionBy) throws InvalidArgumentException {
//        if (ActionUserType.POSTER.getActionUserType().equalsIgnoreCase(actionBy)) {
//            return ActionUserType.POSTER;
//        } else if (ActionUserType.TASKER.getActionUserType().equalsIgnoreCase(actionBy)) {
//            return ActionUserType.TASKER;
//        } else if(ActionUserType.ADMIN.getActionUserType().equalsIgnoreCase(actionBy)){
//            return ActionUserType.ADMIN;
//        } else {
//            throw new IllegalArgumentException();
//        }
//    }

    public static String getINSQLStringWithDelim(List<UUID> list, boolean isString) {
        StringBuilder result = new StringBuilder();
        String delimit;
        for (UUID id : list) {
            delimit = ", ";
            if (TextUtils.isEmpty(result)) {
                delimit = "";
            }
            if (!TextUtils.isEmpty(id.toString())) {
                if (isString) {
                    result.append(delimit).append("'").append(id).append("'");
                } else {
                    result.append(delimit).append(id);
                }
            }
        }
        return result.toString();
    }

    public static String getINSQLStringWithDelim(List<String> list) {
        StringBuilder result = new StringBuilder();
        String delimit;
        for (String id : list) {
            delimit = ", ";
            if (TextUtils.isEmpty(result)) {
                delimit = "";
            }
            if (!TextUtils.isEmpty(id)) {
                result.append(delimit).append("'").append(id).append("'");
            }
        }
        return result.toString();
    }


//    /**
//     * This method returns payout status list for incomplete payouts.
//     * @return
//     */
//    public static List<JobPaymentStatus> getPayoutIncompleteStatusList() {
//
//        JobPaymentStatus[] payoutStatusList = {
//                JobPaymentStatus.NOT_INITIATED,
//                JobPaymentStatus.INITIATED,
//                JobPaymentStatus.FAILED };
//
//        return Arrays.asList(payoutStatusList);
//    }

//
//    public static List<String> getStripeEventsList() {
//
//        String[] stripeEventsList = {
//                GiggiConstant.chargeCaptureFailure, GiggiConstant.transferFailure, GiggiConstant.payoutFailure,
//                GiggiConstant.refundFailed
//        };
//
//        return Arrays.asList(stripeEventsList);
//    }


    public static HashMap<String, String> getDynamicDataForPaymentEmails(String subjectKey, String bodyKey, String username, String jobId, String reason) {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("subjectKey", subjectKey);
        hashMap.put("bodyKey", bodyKey);
        hashMap.put("username", username);
        hashMap.put("jobId", jobId);
        hashMap.put("reason", reason);

        return hashMap;
    }

}

